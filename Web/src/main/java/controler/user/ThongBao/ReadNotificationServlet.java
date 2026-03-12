package controler.user.ThongBao;

import Service.NotificationService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/read-notification")
public class ReadNotificationServlet extends HttpServlet {

    private NotificationService service = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));

        service.markReadById(id);

        HttpSession session = req.getSession();
        Integer count = (Integer) session.getAttribute("numNotiFy");

        if (count != null && count > 0) {
            session.setAttribute("numNotiFy", count - 1);
        }

        resp.getWriter().write("ok");
    }
}