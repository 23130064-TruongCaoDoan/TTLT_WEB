package controler.login_and_signup;

import Service.CartSerive;
import dao.AuthTokenDao;
import model.AuthToken;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import jakarta.servlet.http.Cookie;

import java.util.Optional;

import dao.UserDao;
import Service.UserService;
import Service.NotificationService;
import Util.FacebookOAuthUltis;
import cart.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("home");
            return;
        }
        String redirect = request.getParameter("redirect");
        request.setAttribute("redirect", redirect);
        request.setAttribute("fbClientId", FacebookOAuthUltis.getAppId());

        request.getRequestDispatcher("user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("user");
        String password = request.getParameter("password");
        UserService userService = new UserService();
        User user = userService.findUser(username);

        if (user != null && userService.checkPass(user, password)) {
            HttpSession oldSession = request.getSession(false);
            Cart cart = null;

            if (oldSession != null) {
                cart = (Cart) oldSession.getAttribute("cart");
                oldSession.invalidate();
            }

            if (!user.isStatus()) {
                request.setAttribute("username", username);
                request.setAttribute("password", password);
                request.setAttribute("error", "Tài khoản bạn đã bị khóa");
                request.getRequestDispatcher("user/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("loginSuccess", "Đăng nhập thành công");

            String remember = request.getParameter("remember");
            if (remember != null && (remember.equalsIgnoreCase("on") || remember.equalsIgnoreCase("true"))) {
                SecureRandom random = new SecureRandom();
                byte[] selectorBytes = new byte[9];
                random.nextBytes(selectorBytes);
                String selector = Base64.getUrlEncoder().withoutPadding().encodeToString(selectorBytes);
                byte[] validatorBytes = new byte[33];
                random.nextBytes(validatorBytes);
                String validator = Base64.getUrlEncoder().withoutPadding().encodeToString(validatorBytes);
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                String validatorHash = Base64.getUrlEncoder().withoutPadding().encodeToString(digest.digest(validator.getBytes(StandardCharsets.UTF_8)));
                LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);
                AuthToken authToken = new AuthToken(user.getId(), selector, validatorHash, expiresAt);
                new AuthTokenDao().saveToken(authToken);
                String cookieValue = selector + ":" + validator;
                Cookie rememberCookie = new Cookie("remember_me", cookieValue);
                rememberCookie.setMaxAge(30 * 24 * 60 * 60);
                rememberCookie.setHttpOnly(true);
                rememberCookie.setPath(request.getContextPath());
                response.addCookie(rememberCookie);
            }

            NotificationService notificationService = new NotificationService();
            int count = notificationService.countNotification((user.getId()));
            session.setAttribute("numNotiFy", count);


            String redirect = request.getParameter("redirect");

            if (redirect != null && redirect.startsWith("/")) {
                response.sendRedirect(redirect);
                return;
            }
            int role = userService.checkRole(user);
            if (role!=0) {
                response.sendRedirect("ThongKe");
            } else {
                CartSerive cartSerive = new CartSerive();
                if (cart != null && cart.getItems() != null && cart.getItems().size() > 0) {
                    cartSerive.mergerCart(cart, user.getId());
                }
                model.Cart cartModel = cartSerive.getCart(user.getId());
                session.setAttribute("cart", cartModel);
                response.sendRedirect("home");

            }
        } else {
            request.setAttribute("username", username);
            request.setAttribute("password", password);
            request.setAttribute("error", "Vui lòng kiểm tra lại tên đăng nhập hoặc mật khẩu");
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
        }
    }
}