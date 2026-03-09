package controler.user.profile;

import Service.UserService;
import Util.EmailSender;
import Util.Token8;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
@WebServlet(name="changeEmail", value = "/changeEmail")
public class changeEmail extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user==null){
            response.sendRedirect("login");
            return;
        }
        UserService userService = new UserService();
        String email = request.getParameter("email");
        String name = user.getName();
        EmailSender emailSender = new EmailSender();
        Token8 token = new Token8();

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("type", "danger");
            request.setAttribute("error", "Email không được để trống");
        } else if (userService.checkExit(email)) {
            request.setAttribute("type", "danger");
            request.setAttribute("error", "Email đã tồn tại");
        } else {
            String verifyCode = token.generateToken8();
            session.setAttribute("verifyCode", verifyCode);
            session.setAttribute("email", email);


            emailSender.sendVerificationEmail(
                    email,
                    "Mã xác thực thay đổi email",
                    name,
                    verifyCode,
                    "Mã xác thực:",
                    "Cảm ơn bạn đã đăng ký"
            );
            request.setAttribute("newEmail", email);
            request.setAttribute("showOTP", true);
        }
        request.getRequestDispatcher("user/user-hoSoCaNhan.jsp")
                .forward(request, response);
    }
}
