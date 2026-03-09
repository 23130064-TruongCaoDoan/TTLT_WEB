package controler.login_and_signup;

import Service.UserService;
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
        request.getRequestDispatcher("user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("user");
        String password = request.getParameter("password");
        UserService userService = new UserService();
        User user = userService.findUser(username);
       if(user!=null&&userService.checkPass(user, password)&& user.isStatus()==true){
           HttpSession oldSession = request.getSession(false);
           if (oldSession != null) {
               oldSession.invalidate();
           }
           HttpSession session = request.getSession();
           session.setAttribute("user",user);
           if(userService.checkRole(user)){
               response.sendRedirect("ThongKe");
           }
           else {
               response.sendRedirect("home");
           }
       }
       else{
           request.setAttribute("username",username);
           request.setAttribute("password",password);
           request.setAttribute("error","Vui lòng kiểm tra lại tên đăng nhập hoặc mật khẩu");
           request.getRequestDispatcher("user/login.jsp").forward(request, response);
       }
    }
}