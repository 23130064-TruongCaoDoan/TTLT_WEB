package controler.login_and_signup;

import Service.UserService;
import Util.GoogleOAuthUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GoogleUser;
import model.User;

import java.io.IOException;

@WebServlet(name = "login-google", value = "/login-google")

public class LoginGoogleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        UserService userService = new UserService();
        try {
            String idToken = GoogleOAuthUtils.getToken(code);
            GoogleUser userLogin = GoogleOAuthUtils.getUserInfo(idToken);
            User user;
            if(userService.checkExit(userLogin.getEmail())) {
                user = userService.findUser(userLogin.getEmail());
                request.getSession().setAttribute("user", user);
            } else {
                userService.addUser(userLogin.getName(), userLogin.getEmail());
                request.getSession().setAttribute("user", userLogin);
            }

            System.out.println(userLogin.getName());
            response.sendRedirect("home");
            return;
        }catch (Exception e){
            e.printStackTrace();
        }
        request.getRequestDispatcher("user/login.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
