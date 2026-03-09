package controler;

import Service.UserService;
import Util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "Verify", value = "/Verify")
public class Verify extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("message", "Phiên xác thực đã hết hạn. Vui lòng đăng ký lại.");
            request.setAttribute("type", "danger");
            request.getRequestDispatcher("user/errol.jsp").forward(request, response);
            return;
        }
        String verifyCode = (String) session.getAttribute("verifyCode");
        String email = (String) session.getAttribute("email");
        String fullname = (String) session.getAttribute("fullname");
        String password = (String) session.getAttribute("password");
        String otp =request.getParameter("otp");
        UserService userService = new UserService();
        PasswordUtil passwordUtil = new PasswordUtil();
        if (verifyCode == null || email == null || fullname == null || password == null) {
            request.setAttribute("message", "Phiên xác thực không hợp lệ. Vui lòng đăng ký lại.");
            request.setAttribute("type", "danger");
            request.getRequestDispatcher("user/errol.jsp").forward(request, response);
            return;
        }

        if(otp.isEmpty()){
            request.setAttribute("showOTP", true);
            request.setAttribute("error", "Vui lòng nhập mã xác thực!");
            request.getRequestDispatcher("user/errol.jsp").forward(request, response);
            return;
        }
        long now = System.currentTimeMillis();
        long otpTime = (long) session.getAttribute("otpTime");

        if (now - otpTime > 2 * 60 * 1000) {
            session.removeAttribute("verifyCode");
            session.removeAttribute("otpTime");

            request.setAttribute("showOTP", true);
            request.setAttribute("error", "Mã xác thực đã hết hạn!");
            request.getRequestDispatcher("user/errol.jsp").forward(request, response);
            return;
        }
        if (otp.equals(verifyCode)) {
            userService.addUser(fullname, email, passwordUtil.hashPassword(password));
            session.removeAttribute("verifyCode");
            session.removeAttribute("otpTime");
            session.removeAttribute("email");
            session.removeAttribute("fullname");
            session.removeAttribute("password");
            request.setAttribute("message","Đăng kí thành công");
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
        } else {
            request.setAttribute("showOTP", true);
            request.setAttribute("error", "Mã xác thực không đúng!");
            request.getRequestDispatcher("user/errol.jsp").forward(request, response);
        }
    }
}