package controler.user.favouriteBook;

import Service.BookService;
import Service.EventService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Book;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "favoriteBook", value = "/favoriteBook")
public class UserFavouriteBook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        BookService bookService = new BookService();
        List<Book> favoriteBookList = bookService.getFavouriteBook(userId);

        request.setAttribute("favoriteBookList", favoriteBookList);
        request.getRequestDispatcher("user/user-spYeuThich.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
