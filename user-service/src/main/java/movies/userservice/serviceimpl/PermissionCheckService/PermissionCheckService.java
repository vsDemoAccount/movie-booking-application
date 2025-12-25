package movies.userservice.serviceimpl.PermissionCheckService;


import lombok.RequiredArgsConstructor;
import movies.userservice.entity.User.User;
import movies.userservice.repository.UserRepository.UserRepository;
import movies.userservice.repository.UserRoleRepository.UserRoleRepository;
import movies.userservice.security.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;


@Service
@RequiredArgsConstructor
public class PermissionCheckService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public void requirePermission(String permissionCode) throws AccessDeniedException {
        String actorCode = SecurityContext.getCurrentUser();

        if (actorCode == null) {
            throw new AccessDeniedException("Unauthenticated request");
        }

        User actor = userRepository.findByCode(actorCode)
                .orElseThrow(() -> new AccessDeniedException("Invalid user"));

        boolean hasPermission = userRoleRepository.findByUser(actor).stream()
                .flatMap(ur -> ur.getRole().getPermissions().stream())
                .anyMatch(rp -> rp.getPermission().getCode().equals(permissionCode));

        if (!hasPermission) {
            throw new AccessDeniedException("Missing permission: " + permissionCode);
        }
    }
}
