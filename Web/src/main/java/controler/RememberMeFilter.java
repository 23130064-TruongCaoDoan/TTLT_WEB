package controler;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import Service.NotificationService;
import dao.AuthTokenDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import model.AuthToken;
import model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@WebFilter("/*")
public class RememberMeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {

                for (Cookie cookie : cookies) {
                    if (!"remember_me".equals(cookie.getName())) {
                        continue;
                    }
                    String value = cookie.getValue();
                    if (value == null || !value.contains(":")) {
                        continue;
                    }
                    String[] parts = value.split(":");
                    if (parts.length != 2) {
                        continue;
                    }
                    String selector = parts[0];
                    String validator = parts[1];
                    Optional<AuthToken> opt = new AuthTokenDao().findBySelector(selector);
                    if (opt.isEmpty()) {
                        continue;
                    }
                    AuthToken token = opt.get();
                    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
                        new AuthTokenDao().deleteBySelector(selector);
                        cookie.setMaxAge(0);
                        cookie.setPath(req.getContextPath());
                        resp.addCookie(cookie);
                        continue;
                    }
                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        String validatorHash = Base64.getUrlEncoder().withoutPadding().encodeToString(digest.digest(validator.getBytes(StandardCharsets.UTF_8)));
                        if (!validatorHash.equals(token.getValidatorHash())) {
                            continue;
                        }
                        User user = new UserDao().findUserById(token.getUserId());
                        if (user == null) {
                            continue;
                        }
                        session = req.getSession();
                        session.setAttribute("user", user);
                        NotificationService notificationService = new NotificationService();
                        int count = notificationService.countNotification((user.getId()));
                        session.setAttribute("numNotiFy", count);
                        cookie.setMaxAge(30 * 24 * 60 * 60);
                        cookie.setPath(req.getContextPath());
                        resp.addCookie(cookie);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
