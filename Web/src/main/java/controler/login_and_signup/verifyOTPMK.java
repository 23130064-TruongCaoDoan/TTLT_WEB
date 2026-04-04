package controler.login_and_signup;

import Service.UserService;
import Util.EmailSender;
import Util.PasswordUtil;
import Util.Token8;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "verify_otp", value = "/verify_otp")
public class verifyOTPMK extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserService userService=new UserService();
        PasswordUtil passwordUtil=new PasswordUtil();
        Token8 token8=new Token8();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (session == null) {
            out.print("{\"successVerify\":\"error\",\"message\":\"Phiên làm việc hết hạn\"}");
            return;
        }

        String otpInput = request.getParameter("otp");
        String otpSession = (String) session.getAttribute("verifyOTP");
        Long otpTime = (Long) session.getAttribute("otpT");

        if (otpSession == null || otpTime == null) {
            out.print("{\"successVerify\":\"error\",\"message\":\"OTP không tồn tại\"}");
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime - otpTime > 120000) {
            session.removeAttribute("verifyOTP");
            session.removeAttribute("otpT");
            out.print("{\"successVerify\":\"error\",\"message\":\"OTP đã hết hạn\"}");
            return;
        }

        if (otpSession.equals(otpInput)) {
            String email=session.getAttribute("email").toString();
            session.removeAttribute("verifyCode");
            session.removeAttribute("otpT");
            User user =userService.findUser(email);
            EmailSender emailSender=new EmailSender();
            String pass=token8.generateToken8();
            userService.updatePass(email,passwordUtil.hashPassword(pass));
            session.removeAttribute("email");
            emailSender.sendVerificationEmail(email,"Mật khẩu khôi phục",user.getName(), pass, "Mật khẩu:","Vui lòng không cung cấp cho bất cứ ai");
            out.print("{\"successVerify\":\"success\",\"message\":\"Mật khẩu mới đã được gửi bên email của bạn\"}");
        } else {
            out.print("{\"successVerify\":\"error\",\"message\":\"OTP không đúng\"}");
        }
    }
}