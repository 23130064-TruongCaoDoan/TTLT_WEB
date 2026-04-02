package controler.login_and_signup;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

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
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (session == null) {
            out.print("{\"successVerify\":\"error\",\"message\":\"Phiên làm việc hết hạn\"}");
            return;
        }

        String otpInput = request.getParameter("otp");
        String otpSession = (String) session.getAttribute("verifyCode");
        Long otpTime = (Long) session.getAttribute("otpTime");

        if (otpSession == null || otpTime == null) {
            out.print("{\"successVerify\":\"error\",\"message\":\"OTP không tồn tại\"}");
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime - otpTime > 120000) {
            session.removeAttribute("verifyCode");
            session.removeAttribute("otpTime");
            out.print("{\"successVerify\":\"error\",\"message\":\"OTP đã hết hạn\"}");
            return;
        }

        if (otpSession.equals(otpInput)) {
            session.removeAttribute("verifyCode");
            session.removeAttribute("otpTime");
            out.print("{\"successVerify\":\"success\",\"message\":\"Xác thực thành công\"}");
        } else {
            out.print("{\"successVerify\":\"error\",\"message\":\"OTP không đúng\"}");
        }
    }
}