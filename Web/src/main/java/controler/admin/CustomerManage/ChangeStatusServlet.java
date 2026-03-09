package controler.admin.CustomerManage;

import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "ChangeStatusServlet", value = "/change-status")
public class ChangeStatusServlet extends HttpServlet {
    private UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        int userId = Integer.parseInt(request.getParameter("userId"));
        String statusParam = request.getParameter("status");
        boolean newStatus = "1".equals(statusParam);
        if (currentUser.getId() == userId && !newStatus) {
            response.sendRedirect("user-manage?error=self_downgrade");
            return;
        }

        userService.updateStatus(userId, newStatus);

        response.sendRedirect("user-manage");
    }
}