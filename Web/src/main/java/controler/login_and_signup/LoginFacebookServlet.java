package controler.login_and_signup;

import Service.UserService;
import Util.FacebookOAuthUltis;
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
        HttpSession session = request.getSession();
        try {
            String token= FacebookOAuthUltis.getToken(code);
            FacebookUser userFace= FacebookOAuthUltis.getUserInfo(token);
            User user;
            if(userFace!=null){
                if(userService.checkExit(userFace.getEmail())){
                    user=userService.findUser(userFace.getEmail());
                    session.setAttribute("user",user);
                }
                else{
                    userService.addUser(userFace.getName(),userFace.getEmail());
                    user=userService.findUser(userFace.getEmail());
                    session.setAttribute("user",user);
                }

                response.sendRedirect("home");
                return;
            }

            response.sendRedirect("login");
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}