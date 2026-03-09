package controler.Cart;

import Cart.Cart;
import Service.BookService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;

import java.io.IOException;

@WebServlet(name = "updateItem", value = "/updateItem")
public class updateItem extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        int id = Integer.parseInt(request.getParameter("id") == null ? "0" : request.getParameter("id"));
        int quantity = Integer.parseInt(request.getParameter("quantity") == null ? "0" : request.getParameter("quantity"));

        if (cart != null) {
            if (id != 0) {
                if (quantity > 0) {

                    BookService bookService = new BookService();
                    Book book = bookService.getBooksById(id);

                    if (book == null || quantity > book.getStock()) {
                        response.getWriter().print(
                                "{\"success\":false,\"total\":" + cart.getTotalQuantity() + "}"
                        );
                        return;
                    }

                    cart.updateItem(id, quantity);

                } else {
                    cart.removeItem(id);
                }
            } else {
                cart.removeAllItems();
            }
        }

        response.getWriter().print(
                "{\"success\":true,\"total\":" + (cart != null ? cart.getTotalQuantity() : 0) + "}"
        );
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}