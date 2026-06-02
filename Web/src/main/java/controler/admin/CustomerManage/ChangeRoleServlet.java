package controler.admin.CustomerManage;

import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

import static Util.RolesGroup.USER_MANAGER_ROLE;

@WebServlet(name = "ChangeRoleServlet", value = "/change-role")
public class ChangeRoleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private UserService userService = new UserService();

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
        String roleParam = request.getParameter("role");
        int newRole = Integer.parseInt(roleParam);

        if (currentUser.getId() == userId) {
            response.sendRedirect("user-manage?error=self_downgrade");
            return;
        }

        userService.updateRole(userId, newRole);
        request.setAttribute("logSuccess", true);

        response.sendRedirect("user-manage");
    }
}