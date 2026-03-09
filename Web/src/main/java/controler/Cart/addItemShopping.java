package controler.Cart;

import Cart.Cart;
import Service.BookService;
import Service.CommentService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.CommentView;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "addItemShopping", value = "/addItemShopping")
public class addItemShopping extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart == null){
            cart = new Cart();
        }
        BookService bookService = new BookService();
        Book book=bookService.getBooksById(bookId);
        User user = (User) session.getAttribute("user");
        if(book!=null){
            int currentQty = cart.getQuantityByBookId(bookId);

            if (currentQty + quantity > book.getStock()) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(
                        "{\"success\":false,\"total\":" + cart.getTotalQuantity() + "}"
                );
                return;
            }

            cart.addItem(book, quantity);
            session.setAttribute("cart",cart);
            if(user!=null){
                cart.updateUser(user);
            }
        }
//        CommentService commentService = new CommentService();
//        List<Book> bookListRe = bookService.getBookRecommendInDetail(book.getType());
//        List<CommentView> commentViewList = commentService.getCommentView(bookId);
//        Double averageRating = commentService.getAverageRating(bookId);
//
//        request.setAttribute("book", book);
//        request.setAttribute("bookListRe", bookListRe);
//        request.setAttribute("commentViewList", commentViewList);
//        request.setAttribute("averageRating", averageRating);
//        request.getRequestDispatcher("user/productDetail.jsp")
//                .forward(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(
                "{\"success\":true,\"total\":" + cart.getTotalQuantity() + "}"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}