package controler.admin.CustomerManage;

import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

import static Util.RolesGroup.USER_MANAGER_ROLE;
import static Util.RolesGroup.isManager;

@WebServlet(name = "ChangeRoleServlet", value = "/change-role")
public class ChangeRoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        UserService userService = new UserService();
        int role = userService.checkRole(currentUser);

        if (!USER_MANAGER_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }

        int userId = Integer.parseInt(request.getParameter("userId"));
        User userChanged = userService.getUserById(userId);

        String roleParam = request.getParameter("role");
        int newRole = Integer.parseInt(roleParam);

        if (currentUser.getId() == userId) {
            response.sendRedirect("user-manage?error=self_downgrade");
            return;
        }

        if (isManager(currentUser.getRole()) && USER_MANAGER_ROLE.contains(userChanged.getRole())) {
            response.sendRedirect("user-manage?error=role_not_enough");
            return;
        }

        if (isManager(currentUser.getRole()) && USER_MANAGER_ROLE.contains(newRole)) {
            response.sendRedirect("user-manage?error=role_not_enough");
            return;
        }

        userService.updateRole(userId, newRole);

        response.sendRedirect("user-manage?success=true");
    }
}