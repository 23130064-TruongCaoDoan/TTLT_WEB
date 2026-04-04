package controler.user.danh_muc;

import Service.BookService;
import Service.EventService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "FilterSanPhamServlet", value = "/filter")
public class FilterSanPhamServlet extends HttpServlet {
    private final BookService bookService = new BookService();
    private final int PAGE_SIZE = 28;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();

        BookService bookService = new BookService();


        String keyword = request.getParameter("bSearch");
        int type = getInt(request, "type");
        int idEvent = getInt(request, "idEvent");

        String category = request.getParameter("category");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String age = request.getParameter("age");
        String maxPrice = request.getParameter("maxPrice");
        String year = request.getParameter("year");

        int page = getInt(request, "page");
        if (page <= 0) page = 1;


        int totalBooks = bookService.countBooksUniversal(keyword, type, idEvent,category, author, publisher,age, maxPrice, year);

        int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        int offset = (page - 1) * PAGE_SIZE;

        List<Book> bookList = bookService.getBooksUniversal(keyword, type, idEvent,category, author, publisher,age, maxPrice, year,PAGE_SIZE, offset);

        String searchTitle = "";
        String icon = "";
        String color = "";

        if (keyword != null && !keyword.isBlank()) {
            searchTitle = "Kết quả tìm kiếm: " + keyword;
            request.setAttribute("mode", "search");
        } else {
            switch (type) {
                case 1:
                    searchTitle = "Sách Đang Giảm Giá";
                    icon = "assets/img/icon/sale.png";
                    color = "#FF4C4C";
                    break;
                case 2:
                    searchTitle = "Góc Sách Mới";
                    break;
                case 3:
                    searchTitle = "Sách được yêu thích nhất";
                    break;
                case 4:
                    searchTitle = "Sách theo sự kiện";
                    break;
                default:
                    searchTitle = "Sản phẩm";
            }
            request.setAttribute("mode", "filter");
        }

        request.setAttribute("bookList", bookList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("search", searchTitle);
        request.setAttribute("icon", icon);
        request.setAttribute("color", color);

        request.setAttribute("type", type);
        request.setAttribute("idEvent", idEvent);
        request.setAttribute("bSearch", keyword);

        request.getRequestDispatcher("user/dsSanPham.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private int getInt(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return (value == null || value.isBlank()) ? 0 : Integer.parseInt(value);
    }

}