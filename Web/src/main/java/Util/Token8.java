package Util;
import java.security.SecureRandom;

public class Token8 {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public String generateToken8() {
        StringBuilder token = new StringBuilder(16);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }
        return token.toString();
    }

}