package movies.theatreservice.utils;

import java.security.SecureRandom;

public class CodeGeneratorUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random alphanumeric string with a specific prefix.
     * Example: generate("Cit", 10) -> "Cit-Xy9zAb12Wq"
     * * @param prefix The prefix for the entity (e.g., "Cit", "Scr")
     * @param length The length of the random part
     * @return The formatted unique code
     */
    public static String generate(String prefix, int length) {
        StringBuilder sb = new StringBuilder(length + prefix.length() + 1);

        // Add prefix and separator
        sb.append(prefix).append("-");

        // Generate random alphanumeric characters
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }
}

