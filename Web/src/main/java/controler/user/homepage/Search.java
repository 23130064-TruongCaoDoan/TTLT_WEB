package controler.user.homepage;

import Service.BookService;
import Service.EventService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "search", value = "/search")
public class Search extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();

        String search = request.getParameter("bSearch");

        if (search == null || search.trim().isEmpty()) {
            response.sendRedirect("home");
            return;
        }
        search = search.trim();
        if (search == null || search.equals("")) {
            response.sendRedirect("home");
            return;
        } else {
            BookService bookService = new BookService();
            int page = 1;
            int pageSize = 28;

            String p = request.getParameter("page");
            if (p != null) {
                page = Integer.parseInt(p);
            }
            int totalBooks = bookService.countBooksBySearch(search);
            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
            if (totalPages == 0) {
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }
            int offset = (page - 1) * pageSize;

            request.setAttribute("categories", bookService.getAllCategories());
            request.setAttribute("authors", bookService.getAllAuthors());
            request.setAttribute("publishers", bookService.getAllPublishers());
            request.setAttribute("years", bookService.getAllYears());

            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages",totalPages);
            List<Book> bookList = bookService.findListBook(search, pageSize, offset);
            request.setAttribute("bookList", bookList);
            request.setAttribute("search", search);

            request.setAttribute("mode", "search");
            request.getRequestDispatcher("user/dsSanPham.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}