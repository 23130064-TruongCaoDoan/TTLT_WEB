package controler.product_detail;

import Service.BookService;
import Service.CommentService;
import Service.EventService;
import dao.CommentDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.CommentView;
import model.RatingStartView;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "productDetail", value = "/productDetail")
public class ProductDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();


        int bookId = Integer.parseInt(request.getParameter("id"));
        String type = request.getParameter("type");
        BookService bookService = new BookService();

        Book book = bookService.getBooksById(bookId);
        CommentService commentService = new CommentService();

        List<Book> bookListRe = bookService.getBookRecommendInDetail(type);
        List<CommentView> commentViewList = commentService.getCommentView(bookId);
        Double averageRating = commentService.getAverageRating(bookId);
        List<RatingStartView> ratingList = commentService.getRatingStartView(bookId);
        List<String> listImg = bookService.getImgDetails(bookId);

        boolean isFavouriteBook = false;
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user != null) {
            isFavouriteBook = bookService.isFavouriteBook(bookId, user.getId());
        }
        request.setAttribute("book", book);
        request.setAttribute("bookListRe", bookListRe);
        request.setAttribute("commentViewList", commentViewList);
        request.setAttribute("averageRating", averageRating);
        request.setAttribute("ratingList", ratingList);
        request.setAttribute("isFavouriteBook", isFavouriteBook);
        request.setAttribute("listImg", listImg);
        request.getRequestDispatcher("user/productDetail.jsp").forward(request,response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}