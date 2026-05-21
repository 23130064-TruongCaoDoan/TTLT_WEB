package controler.admin.adminLog;

import dao.AdminLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

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

        User currentUser = (User) session.getAttribute("user");

        AdminLogDAO logDAO = new AdminLogDAO();
        request.setAttribute("histories", logDAO.getHistoryByUserId(currentUser.getId()));
        request.getRequestDispatcher("/admin/myHistory.jsp").forward(request, response);
    }
}
