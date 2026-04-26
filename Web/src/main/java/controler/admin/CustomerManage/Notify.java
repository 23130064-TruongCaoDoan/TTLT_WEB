package controler.admin.CustomerManage;

import Service.NotificationService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "Notify", value = "/Notify")
public class Notify extends HttpServlet {
    private NotificationService ns = new NotificationService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String userCodes = request.getParameter("userIds");
        int userId = Integer.parseInt(userCodes);

        ns.sendNoti(userId, title, content);
        response.getWriter().write("{\"success\" :true, \"message\":\"Đã gửi thông báo cho khách hàng\"}");
    }
}