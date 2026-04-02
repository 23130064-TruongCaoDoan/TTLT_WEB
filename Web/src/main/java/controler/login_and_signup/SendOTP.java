package controler.login_and_signup;

import Service.UserService;
import Util.EmailSender;
import Util.Token8;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

@WebServlet(name = "send_otp", value = "/send_otp")
public class SendOTP extends HttpServlet {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String email=request.getParameter("emailMK").toString().trim();
        UserService userService = new UserService();
        User user=userService.findUser(email);
        if(user!=null){
            if(email.isEmpty()){
                out.print("{\"successSend\":\"error\",\"message\":\"Email không tồn tại\"}");
                return;
            }
            if (!Pattern.matches(EMAIL_REGEX, email)) {
                out.print("{\"successSend\":\"error\",\"message\":\"Email không đúng định dạng\"}");
                return;
            }
            Token8 token = new Token8();
            EmailSender emailSender = new EmailSender();
            String verifyCode = token.generateToken8();
            session.setAttribute("verifyCode", verifyCode);
            session.setAttribute("otpTime", System.currentTimeMillis());
            emailSender.sendVerificationEmail(email, "Mã xác thực tài khoản", user.getName(), verifyCode,"Mã xác thực:","Cảm ơn bạn đã đăng ký");
            out.print("{\"successSend\":\"success\",\"message\":\"Gửi thành công\"}");
        }else{
            out.print("{\"successSend\":\"error\",\"message\":\"Email không tồn tại\"}");
        }
    }
}