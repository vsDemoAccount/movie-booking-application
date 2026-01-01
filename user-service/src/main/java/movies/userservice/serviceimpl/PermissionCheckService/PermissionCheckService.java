package movies.userservice.serviceimpl.PermissionCheckService;


import lombok.RequiredArgsConstructor;
import movies.userservice.entity.User.User;
import movies.userservice.repository.UserRoleRepository.UserRoleRepository;
import movies.userservice.serviceimpl.UserProvisioningService.UserProvisioningService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;


@Service
@RequiredArgsConstructor
public class PermissionCheckService {

    private final UserProvisioningService provisioningService;
    private final UserRoleRepository userRoleRepository;

    public void requirePermission(String permissionCode) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User actor = provisioningService.getOrCreate(jwt);

        boolean allowed = userRoleRepository.findByUser(actor).stream()
                .flatMap(ur -> ur.getRole().getPermissions().stream())
                .anyMatch(rp -> rp.getPermission().getCode().equals(permissionCode));

        if (!allowed) {
            throw new AccessDeniedException("Missing permission: " + permissionCode);
        }
    }
}
