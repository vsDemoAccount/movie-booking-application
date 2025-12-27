package movies.userservice.security;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    public static String getKeycloakUserId() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getSubject(); // KEYCLOAK USER ID
        }

        return null;
    }

    public static String getEmail() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getClaimAsString("email");
        }
        return null;
    }
}
