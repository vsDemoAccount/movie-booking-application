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
import java.util.List;
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

        role.setDescription(dto.getDescription());
        rolePermRepo.deleteByRole(role);
        attachPermissions(role, dto.getPermissions());

        return toDTO(role);
    }

    @Override
    public void disableRole(String roleCode) {
        permissionCheckService.requirePermission("ROLE_MANAGE");
        Role role = getRole(roleCode);
        role.setActive(false);
    }

    @Override
    public void assignRoleToUser(String userCode, AssignRoleRequest req) {
        permissionCheckService.requirePermission("ROLE_MANAGE");
        User user = getUser(userCode);
        preventSelfEscalation(user);
        Role role = getRole(req.getRoleCode());

        if (role.isSystemRole() && req.getScopeRefCode() != null) {
            throw new IllegalArgumentException("System role cannot have scope");
        }

        if (userRoleRepo.existsByUserAndRoleAndScopeRefCode(
                user, role, req.getScopeRefCode())) {
            return;
        }

        userRoleRepo.save(UserRole.builder()
                .user(user)
                .role(role)
                .scopeRefCode(req.getScopeRefCode())
                .assignedAt(Instant.now())
                .build());
    }

    @Override
    public void revokeRoleFromUser(String userCode, AssignRoleRequest req) {
        permissionCheckService.requirePermission("ROLE_MANAGE");
        User user = getUser(userCode);
        preventSelfEscalation(user);
        userRoleRepo.deleteByUserAndRoleAndScopeRefCode(
                getUser(userCode), getRole(req.getRoleCode()), req.getScopeRefCode());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserRoleDTO> getUserRoles(String userCode) {
        User user = getUser(userCode);
        return userRoleRepo.findByUser(user).stream()
                .map(this::toUserRoleDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<String> getAllPermissions() {
        return permRepo.findAll().stream()
                .map(Permission::getCode)
                .toList();
    }

    // ---------------- helpers ----------------

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

    private void preventSelfEscalation(User targetUser) {
        String keycloakId = SecurityUtils.getKeycloakUserId();

        if (keycloakId == null) {
            throw new AccessDeniedException("Unauthenticated request");
        }

        if (keycloakId.equals(targetUser.getKeycloakId())) {
            throw new AccessDeniedException("You cannot modify your own roles");
        }
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
