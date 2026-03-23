package controler.user.homepage;

import Service.SearchHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebServlet("/search-history")
public class SearchHistoryServlet  extends HttpServlet {
    SearchHistoryService service = new SearchHistoryService();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        HttpSession session = req.getSession(false);

        if(session == null){
            resp.getWriter().write("[]");
            return;
        }

        User user = (User) session.getAttribute("user");

        if(user == null){
            resp.getWriter().write("[]");
            return;
        }

        var list = service.getHistory(user.getId());
        new ObjectMapper().writeValue(resp.getWriter(), list);
    }
}
