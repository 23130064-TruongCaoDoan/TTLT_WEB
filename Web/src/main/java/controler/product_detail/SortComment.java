package controler.product_detail;

import Service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CommentView;

import java.io.IOException;
import java.util.List;

@WebServlet(name="sortComment" ,value="/sortComment")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class SortComment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommentService commentService = new CommentService();
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int rating = Integer.parseInt(request.getParameter("rating"));

        List<CommentView> comments;

        if (rating == 0) {
            comments = commentService.getCommentView(bookId);
        } else {
            comments = commentService.getCommentByRating(bookId, rating);
        }

        request.setAttribute("commentViewList", comments);

        request.getRequestDispatcher("user/commentList.jsp").forward(request, response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
