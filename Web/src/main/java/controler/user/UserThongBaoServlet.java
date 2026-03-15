package controler.user;

import Service.NotificationService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Notification;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserThongBaoServlet", value = "/thong-bao")
public class UserThongBaoServlet extends HttpServlet {
    private final NotificationService service = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

//        service.readAll(user.getId());
//        req.getSession().setAttribute("numNotiFy", 0);

        List<Notification> list =
                service.getUserNotifications(user.getId());

        int count = service.countNotification(user.getId());

        req.setAttribute("notifications", list);

        req.getRequestDispatcher("/user/user-thongbao.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}