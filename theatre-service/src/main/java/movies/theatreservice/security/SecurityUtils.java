package movies.theatreservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Now 'Jwt' refers to the token object, which HAS the getSubject() method
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }

        throw new IllegalStateException("User is not authenticated or token is invalid");
    }
}