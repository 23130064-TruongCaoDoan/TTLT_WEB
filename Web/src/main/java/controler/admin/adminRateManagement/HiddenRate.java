package controler.admin.adminRateManagement;

import Service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet(name="hidden",value="/hidden")
public class HiddenRate extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommentService commentService = new CommentService();

        int id = Integer.parseInt(request.getParameter("id"));
        commentService.setActive(id);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
