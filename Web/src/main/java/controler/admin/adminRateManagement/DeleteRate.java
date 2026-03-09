package controler.admin.adminRateManagement;

import Service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="deleteRate", value = "/deleteRate")
public class DeleteRate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CommentService commentService = new CommentService();
        int id = Integer.parseInt(request.getParameter("id"));
        commentService.deleteRate(id);

        response.setStatus(HttpServletResponse.SC_OK);
    }

}
