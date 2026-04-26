package controler.user.ThongBao;

import Service.NotificationService;
import dao.NotificationDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "read-all-notifications", value = "/read-all-notifications")
public class ReadAllNotificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            NotificationService notificationService = new NotificationService();
            notificationService.readAll(user.getId());
        }
        Integer count = (Integer) request.getSession().getAttribute("numNotiFy");

        if (count != null && count > 0) {
            request.getSession().setAttribute("numNotiFy", count - 1);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}