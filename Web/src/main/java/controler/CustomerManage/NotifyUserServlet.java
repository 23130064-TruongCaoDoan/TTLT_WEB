package controler.CustomerManage;

import Service.NotificationService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Notification;

import java.io.IOException;

@WebServlet(name = "NotifyUserServlet", value = "/notify-user")
public class NotifyUserServlet extends HttpServlet {
    private NotificationService ns = new NotificationService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String[] userCodes = request.getParameterValues("userIds");

        ns.sendToUsers(title, content, userCodes);
        response.sendRedirect("user-manage?success=notify");

    }
}