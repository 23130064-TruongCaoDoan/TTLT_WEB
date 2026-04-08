package controler.admin.CustomerManage;

import Service.UserService;
import Util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AddUserAdminServlet", value = "/admin-add-user")
public class AddUserAdminServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int role = Integer.parseInt(request.getParameter("role"));
        int status = Integer.parseInt(request.getParameter("status"));

        if (userService.checkExit(email)) {
            response.sendRedirect(request.getContextPath() + "/user-manage?error=email_exists");
            return;
        }

        if (!userService.isValidPassword(password)) {
            response.sendRedirect(request.getContextPath() + "/user-manage?error=invalid_password");
            return;
        }

        PasswordUtil passwordUtil = new PasswordUtil();
        String hashPassword = passwordUtil.hashPassword(password);

        userService.addUserByAdmin(name, email, hashPassword, role, status);

        response.sendRedirect(request.getContextPath() + "/user-manage?success=add_user");
    }
}
