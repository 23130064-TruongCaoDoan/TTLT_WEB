package Util;
import dao.AuthTokenDao;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.AuthToken;
import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class RememberMeUtil {
    public static void createRememberMe(User user, HttpServletRequest request, HttpServletResponse response) {
        try {
            SecureRandom random = new SecureRandom();

            byte[] selectorBytes = new byte[9];
            random.nextBytes(selectorBytes);
            String selector = Base64.getUrlEncoder().withoutPadding().encodeToString(selectorBytes);

            byte[] validatorBytes = new byte[33];
            random.nextBytes(validatorBytes);
            String validator = Base64.getUrlEncoder().withoutPadding().encodeToString(validatorBytes);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String validatorHash = Base64.getUrlEncoder().withoutPadding().encodeToString(digest.digest(validator.getBytes(StandardCharsets.UTF_8)));

            LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);

            AuthToken authToken = new AuthToken(user.getId(), selector, validatorHash, expiresAt);

            new AuthTokenDao().saveToken(authToken);

            Cookie cookie = new Cookie("remember_me", selector + ":" + validator);

            cookie.setMaxAge(30 * 24 * 60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
