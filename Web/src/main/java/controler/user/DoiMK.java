package controler.user;

import Service.UserService;
import Util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "DoiMK", value = "/DoiMK")
public class DoiMK extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        UserService userService = new UserService();

        if (userService.checkRole(user)) {
            response.sendRedirect("login");
            return;
        }
        request.getRequestDispatcher("user/user-changePassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        UserService userService = new UserService();

        HttpSession session = request.getSession(false);

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng đăng nhập\"}");
            return;
        }

        String oldPassword = request.getParameter("oldPass");
        String newPassword = request.getParameter("newPass");
        String confirmPassword = request.getParameter("confirmPass");

        if (oldPassword == null || oldPassword.isEmpty() ||
                newPassword == null || newPassword.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng nhập đầy đủ thông tin.\"}");
            return;
        }


        PasswordUtil passwordUtil=new PasswordUtil();

        if (!userService.checkPass(user, oldPassword)) {
            response.getWriter().write("{\"success\":false,\"message\":\"Mật khẩu hiện tại không đúng.\"}");
            return;
        }


        if (!userService.isValidPassword(newPassword)) {
            response.getWriter().write("{\"success\":false,\"message\":\"Mật khẩu mới phải ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.\"}");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            response.getWriter().write("{\"success\":false,\"message\":\"Mật khẩu xác nhận không trùng khớp.\"}");
            return;
        }

        String npassword=passwordUtil.hashPassword(newPassword);
        boolean changeSuccess = userService.changePassword(user.getId(), npassword);
        if (changeSuccess) {
            response.getWriter().write("{\"success\":true,\"message\":\"Đổi mật khẩu thành công!\"}");
        } else {
            response.getWriter().write("{\"success\":false,\"message\":\"Có lỗi xảy ra, vui lòng thử lại.\"}");
        }
    }
}