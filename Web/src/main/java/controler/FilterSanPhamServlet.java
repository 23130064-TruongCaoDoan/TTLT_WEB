package controler;

import Service.BookService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "FilterSanPhamServlet", value = "/filter")
public class FilterSanPhamServlet extends HttpServlet {
    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 28;
        String p = request.getParameter("page");
        if (p != null && !p.isBlank()) page = Integer.parseInt(p);

        int ageFrom = 0;
        int ageTo = 100;
        String af = request.getParameter("ageFrom");
        String at = request.getParameter("ageTo");
        if (af != null && !af.isBlank()) ageFrom = Integer.parseInt(af);
        if (at != null && !at.isBlank()) ageTo = Integer.parseInt(at);

        String category = request.getParameter("category");
        if (category != null && category.isBlank()) category = null;

        int totalBooks = bookService.countBooksByCategoryAndAge(
                category, ageFrom, ageTo
        );

        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;

        int offset = (page - 1) * pageSize;

        List<Book> bookList = bookService.getBooksByCategoryAndAge(
                category, ageFrom, ageTo, pageSize, offset
        );

        String qs = request.getQueryString();
        if (qs != null) {
            qs = qs.replaceAll("(^|&)page=\\d+", "");
            qs = qs.replaceAll("^&+", "");
            qs = qs.replaceAll("&+$", "");
        }

        request.setAttribute("bookList", bookList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("qs", qs);

        request.setAttribute("category", category);
        request.setAttribute("ageFrom", ageFrom);
        request.setAttribute("ageTo", ageTo);
        request.setAttribute("mode", "filter");

        request.getRequestDispatcher("/user/dsSanPham.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}