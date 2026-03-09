package controler.user.profile;

import Service.UserService;
import Util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "Confirm", value = "/Confirm")
public class Confirm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String otp = request.getParameter("otp");
        String verifyCode = (String) session.getAttribute("verifyCode");
        String email = (String) session.getAttribute("email");

        if (otp == null || otp.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã OTP");
            request.setAttribute("showOTP", true);
            request.getRequestDispatcher("user/user-hoSoCaNhan.jsp").forward(request, response);
            return;
        }

        if (verifyCode == null || email == null) {
            request.setAttribute("error", "Phiên xác thực đã hết hạn");
            request.getRequestDispatcher("user/user-hoSoCaNhan.jsp").forward(request, response);
            return;
        }

        UserService userService = new UserService();
        if (userService.checkExit(email)) {
            request.setAttribute("error", "Email đã tồn tại");
            request.getRequestDispatcher("user/user-hoSoCaNhan.jsp").forward(request, response);
            return;
        }

        if (otp.trim().equals(verifyCode)) {
            System.out.println("ok");
            userService.updateEmail(user.getId(), email);
            user.setEmail(email);
            session.setAttribute("user", user);

            session.removeAttribute("verifyCode");
            session.removeAttribute("email");

            response.sendRedirect("SetUpAccount");
        } else {
            request.setAttribute("error", "Mã OTP không đúng");
            request.setAttribute("showOTP", true);
            request.getRequestDispatcher("user/user-hoSoCaNhan.jsp").forward(request, response);
        }
    }
}