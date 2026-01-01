package movies.userservice.serviceimpl.RoleServiceImpl;

import lombok.RequiredArgsConstructor;
import movies.userservice.dtos.AssignRoleRequest.AssignRoleRequest;
import movies.userservice.dtos.RoleDTO.RoleDTO;
import movies.userservice.dtos.UserRoleDTO.UserRoleDTO;
import movies.userservice.entity.Permission.Permission;
import movies.userservice.entity.Role.Role;
import movies.userservice.entity.RolePermission.RolePermission;
import movies.userservice.entity.User.User;
import movies.userservice.entity.UserRole.UserRole;
import movies.userservice.exception.DuplicateRecordException;
import movies.userservice.exception.ResourceNotFoundException;
import movies.userservice.repository.PermissionRepository.PermissionRepository;
import movies.userservice.repository.RolePermissionRepository.RolePermissionRepository;
import movies.userservice.repository.RoleRepository.RoleRepository;
import movies.userservice.repository.UserRepository.UserRepository;
import movies.userservice.repository.UserRoleRepository.UserRoleRepository;
import movies.userservice.security.SecurityUtils;
import movies.userservice.service.RoleService.RoleService;
import movies.userservice.serviceimpl.PermissionCheckService.PermissionCheckService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final PermissionRepository permRepo;
    private final RolePermissionRepository rolePermRepo;
    private final UserRepository userRepo;
    private final UserRoleRepository userRoleRepo;
    private final PermissionCheckService permissionCheckService;

    @Override
    public RoleDTO createRole(RoleDTO dto) {
        permissionCheckService.requirePermission("ROLE_MANAGE");

        if(roleRepo.findByCode(dto.getCode()).isPresent()) {
            throw new DuplicateRecordException("Role already exists: " + dto.getCode());
        }

        Role role = Role.builder()
                .code(dto.getCode())
                .description(dto.getDescription())
                .systemRole(dto.isSystemRole())
                .active(true)
                .build();

        roleRepo.save(role);
        attachPermissions(role, dto.getPermissions());
        return toDTO(role);
    }

    @Override
    public RoleDTO updateRole(String roleCode, RoleDTO dto) {
        permissionCheckService.requirePermission("ROLE_MANAGE");
        Role role = getRole(roleCode);

        // Don't allow changing system flags of existing roles to prevent breaking logic
        if (role.isSystemRole() != dto.isSystemRole()) {
            throw new IllegalArgumentException("Cannot change systemRole status of an existing role");
        }

        role.setDescription(dto.getDescription());
        rolePermRepo.deleteByRole(role); // Clear old perms
        attachPermissions(role, dto.getPermissions()); // Add new ones

        return toDTO(role);
    }

    @Override
    public void disableRole(String roleCode) {
        permissionCheckService.requirePermission("ROLE_MANAGE");
        Role role = getRole(roleCode);
        if (role.isSystemRole()) {
            throw new IllegalArgumentException("Cannot disable core System Roles");
        }
        role.setActive(false);
    }

    @Override
    public void assignRoleToUser(String userCode, AssignRoleRequest req) {
        // 1. Permission Check
        permissionCheckService.requirePermission("ROLE_MANAGE");

        User targetUser = getUser(userCode);
        Role targetRole = getRole(req.getRoleCode());

        // 2. Security Checks
        preventSelfEscalation(targetUser);
        validateRoleHierarchy(targetRole); // <--- NEW CHECK

        // 3. Scope Validation
        if (targetRole.isSystemRole() && req.getScopeRefCode() != null) {
            throw new IllegalArgumentException("System Roles (like Admin) cannot be limited to a Theatre. Scope must be null.");
        }
        if (!targetRole.isSystemRole() && req.getScopeRefCode() == null) {
            throw new IllegalArgumentException("Business Roles (Staff, Manager) must be assigned to a specific Theatre (scopeRefCode is required).");
        }

        // 4. Duplicate Check
        if (userRoleRepo.existsByUserAndRoleAndScopeRefCode(
                targetUser, targetRole, req.getScopeRefCode())) {
            throw new DuplicateRecordException("User already has this role for this scope");
        }

        // 5. Assign
        userRoleRepo.save(UserRole.builder()
                .user(targetUser)
                .role(targetRole)
                .scopeRefCode(req.getScopeRefCode())
                .assignedAt(Instant.now())
                .build());
    }

    @Override
    public void revokeRoleFromUser(String userCode, AssignRoleRequest req) {
        permissionCheckService.requirePermission("ROLE_MANAGE");
        User user = getUser(userCode);
        preventSelfEscalation(user);

        // Find specific assignment to delete
        UserRole assignment = userRoleRepo.findByUserAndRoleCodeAndScopeRefCode(
                        user, req.getRoleCode(), req.getScopeRefCode())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role Not Found For User",
                                "",
                                ""
                        )
                );

        userRoleRepo.delete(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserRoleDTO> getUserRoles(String userCode) {
        User user = getUser(userCode);
        return userRoleRepo.findByUser(user).stream()
                .map(this::toUserRoleDTO)
                .collect(Collectors.toSet());
    }

    // ---------------- Helpers ----------------

    private void validateRoleHierarchy(Role targetRole) {
        // Simple Logic: Only allow assigning roles that are NOT Platform Admin
        // Unless the actor IS a Platform Admin.

        // Ideally you check the Actor's roles here.
        // For MVP: If the target role is ROLE_PLATFORM_ADMIN, block it
        // unless we verify the actor is also one.

        if ("ROLE_PLATFORM_ADMIN".equals(targetRole.getCode())) {
            // Fetch current user
            String currentKeycloakId = SecurityUtils.getKeycloakUserId();
            User actor = userRepo.findByKeycloakId(currentKeycloakId).orElseThrow();

            boolean isActorAdmin = userRoleRepo.findByUser(actor).stream()
                    .anyMatch(ur -> "ROLE_PLATFORM_ADMIN".equals(ur.getRole().getCode()));

            if (!isActorAdmin) {
                throw new AccessDeniedException("Only Platform Admins can assign Admin roles.");
            }
        }
    }

    private void preventSelfEscalation(User targetUser) {
        String keycloakId = SecurityUtils.getKeycloakUserId();
        if (keycloakId == null) throw new AccessDeniedException("Unauthenticated");

        if (keycloakId.equals(targetUser.getKeycloakId())) {
            throw new AccessDeniedException("You cannot modify your own roles");
        }
    }

    private void attachPermissions(Role role, Set<String> permCodes) {
        if (permCodes == null) return;
        for (String code : permCodes) {
            Permission p = permRepo.findByCode(code)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission", "code", code));
            rolePermRepo.save(new RolePermission(null, role, p));
        }
    }

    private RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setCode(role.getCode());
        dto.setDescription(role.getDescription());
        dto.setSystemRole(role.isSystemRole());
        dto.setActive(role.isActive());
        dto.setPermissions(
                role.getPermissions().stream()
                        .map(rp -> rp.getPermission().getCode())
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    private UserRoleDTO toUserRoleDTO(UserRole ur) {
        UserRoleDTO dto = new UserRoleDTO();
        dto.setRoleCode(ur.getRole().getCode());
        dto.setScopeRefCode(ur.getScopeRefCode());
        dto.setPermissions(
                ur.getRole().getPermissions().stream()
                        .map(rp -> rp.getPermission().getCode())
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    private User getUser(String code) {
        return userRepo.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("User", "code", code));
    }

    private Role getRole(String code) {
        return roleRepo.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "code", code));
    }
}