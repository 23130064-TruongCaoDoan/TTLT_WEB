package controler.admin.adminEvent;

import Service.EventService;
import dao.AdminLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebServlet(name = "deleteEvent", value = "/deleteEvent")
public class deleteEvent extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id")==null?"0":request.getParameter("id"));
        EventService  eventService= new EventService();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        boolean success= eventService.deleteEvent(id);
        String json;
        if (success) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                String eventName = request.getParameter("eventName");
                String content = "Bạn đã xóa sự kiện: " + (eventName != null ? eventName : "ID " + id);
                AdminLogDAO logDAO = new AdminLogDAO();
                logDAO.insertLog(user.getId(), "XÓA", content);
            }

            json = "{\"success\":true,\"message\":\"Xóa event thành công\"}";
        } else {
            json = "{\"success\":false,\"message\":\"Xóa event thất bại\"}";
        }
        response.getWriter().write(json);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}