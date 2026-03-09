package controler.user.danh_muc;

import Service.BookService;
import Service.EventService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Book;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "dsSanPham", value = "/dsSanPham")

public class DanhMucServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookService bookService = new BookService();
        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();
        String id = request.getParameter("idEvent");
        int idEvent;
        if(id==null||id.isEmpty() || id.isBlank()){
            idEvent = 0;
        }else{
            idEvent = Integer.parseInt(request.getParameter("idEvent"));
        }
        String title  = request.getParameter("title");

        int page = 1;
        int pageSize = 28;

        String p = request.getParameter("page");
        if (p != null && !p.trim().isEmpty()) {
            page = Integer.parseInt(p.trim());
        }
        int totalBooks;
        String typeParam = request.getParameter("type");
        int type = 0;
        if (typeParam != null && !typeParam.isBlank()) {
            type = Integer.parseInt(typeParam);
        }
        switch (type) {
            case 1:
                totalBooks = bookService.countBooksDiscounted();
                break;
            case 2:
                totalBooks = bookService.countBooksNew();
                break;
            case 3:
                totalBooks = bookService.countBookFavourite();
                break;
            case 4:
                totalBooks = bookService.countBooksByEvent(idEvent);
                break;
            default:
                totalBooks = bookService.countBooks();
        }

        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
        if (page > totalPages) {
            page = totalPages;
        }
        int offset = (page - 1) * pageSize;
        List<Book> bookList;
        String search="";
        String icon="";
        String color="";
        switch (type) {
            case 1:{
                bookList=bookService.getAllBooksDiscounted(pageSize, offset);
                search = "Sách Đang Giảm Giá";
                icon="assets/img/icon/sale.png";
                color="#FF4C4C";
                break; }
            case 2:{
                bookList=bookService.getAllBooksNew(pageSize, offset);
                search="Góc Sách Mới";
                icon="assets/img/icon/iconNew.png";
                break;
            }
            case 3:{
                bookList=bookService.getAllFavouriteBook(pageSize, offset);
                search="Sách được yêu thích nhất";
                break;
            }
            case 4:{
                bookList=bookService.getBookByEvent(pageSize, offset, idEvent);
                search="Sách " + title;
                break;
            }
            default:{
                bookList = bookService.getAllBooks(pageSize, offset);
                break;
            }
        }

        request.setAttribute("type", type);
        request.setAttribute("color", color);
        request.setAttribute("search", search);
        request.setAttribute("icon", icon);

        request.setAttribute("bookList", bookList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages",totalPages);


        request.setAttribute("mode", "category");

        request.getRequestDispatcher("user/dsSanPham.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
