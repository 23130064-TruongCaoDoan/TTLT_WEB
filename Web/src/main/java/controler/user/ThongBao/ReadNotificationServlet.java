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

        boolean isNewRead = service.markReadById(id);

        HttpSession session = req.getSession();
        Integer count = (Integer) session.getAttribute("numNotiFy");

        if (isNewRead && count != null && count > 0) {
            count = count - 1;
            session.setAttribute("numNotiFy", count);
        }

        resp.getWriter().write(count != null ? String.valueOf(count) : "0");
    }
}