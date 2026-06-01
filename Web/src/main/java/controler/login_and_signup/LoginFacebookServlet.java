package controler.login_and_signup;

import Service.CartSerive;
import Service.UserService;
import Util.FacebookOAuthUltis;
import Util.RememberMeUtil;
import cart.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.FacebookUser;
import model.User;

import java.io.IOException;

@WebServlet(name = "LoginFacebookServlet", value = "/login-facebook")
public class LoginFacebookServlet extends HttpServlet {
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
            String token = FacebookOAuthUltis.getToken(code);
            FacebookUser userFace = FacebookOAuthUltis.getUserInfo(token);
            User user;
            if (userFace != null) {
                HttpSession session = request.getSession();
                if (userService.checkExit(userFace.getEmail())) {
                    user = userService.findUser(userFace.getEmail());
                    session.setAttribute("user", user);
                } else {
                    userService.addUser(userFace.getName(), userFace.getEmail());
                    user = userService.findUser(userFace.getEmail());
                    session.setAttribute("user", user);
                }
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
                session.setAttribute("cart", cartModel);
                response.sendRedirect("home");
                return;
            }

            response.sendRedirect("login");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}