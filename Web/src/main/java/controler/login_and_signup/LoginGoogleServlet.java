package controler.login_and_signup;

import Service.CartSerive;
import Service.UserService;
import Util.GoogleOAuthUtils;
import Util.RememberMeUtil;
import cart.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GoogleUser;
import model.User;

import java.io.IOException;

@WebServlet(name = "login-google", value = "/login-google")

public class LoginGoogleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        UserService userService = new UserService();
        HttpSession oldSession = request.getSession(false);

        Cart cart = null;
        if (oldSession != null) {
            cart = (Cart) oldSession.getAttribute("cart");
            oldSession.invalidate();
        }
        try {
            String idToken = GoogleOAuthUtils.getToken(code);
            GoogleUser userLogin = GoogleOAuthUtils.getUserInfo(idToken);
            User user;
            if (userService.checkExit(userLogin.getEmail())) {
                user = userService.findUser(userLogin.getEmail());
            } else {
                userService.addUser(userLogin.getName(), userLogin.getEmail());
                user = userService.findUser(userLogin.getEmail());
            }
            request.getSession().setAttribute("user", user);
            String state = request.getParameter("state");
            if ("remember".equals(state)) {
                RememberMeUtil.createRememberMe(
                        user,
                        request,
                        response
                );
            }
            CartSerive cartSerive = new CartSerive();
            if (cart != null && cart.getItems() != null && cart.getItems().size() > 0) {
                cartSerive.mergerCart(cart, user.getId());
            }
            model.Cart cartModel = cartSerive.getCart(user.getId());

            request.getSession().setAttribute("cart", cartModel);
            response.sendRedirect("home");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("user/login.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
