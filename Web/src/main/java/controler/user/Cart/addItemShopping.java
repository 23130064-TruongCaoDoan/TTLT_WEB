package controler.user.Cart;

import Cart.Cart;
import Service.BookService;
import Service.CartSerive;
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
    private CartSerive cartSerive;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            cartSerive = new CartSerive();
            BookService bookService = new BookService();
            Book book = bookService.getBooksById(bookId);

            if (user == null) {
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                }
                if (book != null) {
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
                    session.setAttribute("cart", cart);
                }
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(
                        "{\"success\":true,\"total\":" + cart.getTotalQuantity() + "}"
                );
            } else {
                if (book != null) {

                    model.Cart cart2 = cartSerive.getCart(user.getId());
                    int currentQty = cart2.getQuantityByBookId(bookId);

                    if (currentQty + quantity > book.getStock()) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().print(
                                "{\"success\":false,\"total\":" + cart2.getTotalQuantity() + "}"
                        );
                        return;
                    }

                    cartSerive.addToCart(user.getId(), bookId, quantity);

                    cart2 = cartSerive.getCart(user.getId());
                    session.setAttribute("cart", cart2);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(
                            "{\"success\":true,\"total\":" + cart2.getTotalQuantity() + "}"
                    );
                }
            }
        }
       catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}