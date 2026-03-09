package controler.login_and_signup;

import Util.Token8;
import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import Util.EmailSender;

import java.io.IOException;

@WebServlet(name = "dangki", value = "/dangki")
public class SignupServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("user/errol.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullname = request.getParameter("fullname") == null ? "" : request.getParameter("fullname");
        String email = request.getParameter("email") == null ? "" : request.getParameter("email");
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password") == null ? "" : request.getParameter("confirm-password");
        UserService userService = new UserService();
        EmailSender emailSender = new EmailSender();
        Token8 token = new Token8();
        HttpSession session = request.getSession();
        if (userService.checkExit(email)) {
            request.setAttribute("type", "danger");
            request.setAttribute("message", "Email đã tồn tại");
        } else if (!password.equals(confirmPassword)) {
            request.setAttribute("type", "danger");
            request.setAttribute("message", "Mật khẩu xác nhận không khớp");
        } else if (!userService.isValidPassword(password)) {
            request.setAttribute("type", "danger");
            request.setAttribute(
                    "message",
                    "Mật khẩu phải ít nhất 8 ký tự, có số và ký tự đặc biệt"
            );
        } else {
            String verifyCode = token.generateToken8();
            session.setAttribute("verifyCode", verifyCode);
            session.setAttribute("otpTime", System.currentTimeMillis());

            session.setAttribute("email", email);
            session.setAttribute("fullname", fullname);
            session.setAttribute("password", password);

            emailSender.sendVerificationEmail(
                    email,
                    "Mã xác thực tài khoản",
                    fullname,
                    verifyCode,
                    "Mã xác thực:",
                    "Cảm ơn bạn đã đăng ký"
            );
            request.setAttribute("showOTP", true);
        }
        request.getRequestDispatcher("user/errol.jsp").forward(request, response);

    }
}