package controler.user.favouriteBook;

import Service.AddressService;
import Service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebServlet(name = "addFavouriteBook", value = "/addFavouriteBook")
public class AddFavouriteBook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Chưa đăng nhập\"}");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        try {
            int bookId = Integer.parseInt(request.getParameter("id"));

            BookService bookService = new BookService();
            bookService.insertFavoriteBook(bookId, userId);

            response.getWriter().write("{\"success\": true, \"active\": true}");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"Thêm yêu thích thất bại\"}");
        }
    }
}



