package controler.admin.adminLog;

import Service.UserService;
import dao.AdminLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

import static Util.RolesGroup.USER_MANAGER_ROLE;

@WebServlet(name = "myHistory", value = "/myHistory")
public class MyHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        UserService userService = new UserService();


        User currentUser = (User) session.getAttribute("user");
        int role = userService.checkRole(currentUser);
        if (currentUser == null || !USER_MANAGER_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }
        AdminLogDAO logDAO = new AdminLogDAO();
        logDAO.markAllAsRead(currentUser.getId());
        request.setAttribute("unreadLogCount", 0);
        request.setAttribute("histories", logDAO.getHistoryByUserId(currentUser.getId()));
        request.getRequestDispatcher("/admin/myHistory.jsp").forward(request, response);
    }
}
