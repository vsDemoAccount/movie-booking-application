package movies.userservice.security;


public class SecurityContext {

    private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

    public static void setCurrentUser(String userCode) {
        CURRENT_USER.set(userCode);
    }

    public static String getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
