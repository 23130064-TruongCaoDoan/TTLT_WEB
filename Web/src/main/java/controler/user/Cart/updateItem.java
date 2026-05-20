package controler.user.Cart;

import Cart.Cart;
import Service.BookService;
import Service.CartSerive;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.User;

import java.io.IOException;

@WebServlet(name = "updateItem", value = "/updateItem")
public class updateItem extends HttpServlet {
    private CartSerive cartSerive;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            cartSerive = new CartSerive();

            int id = Integer.parseInt(request.getParameter("id") == null ? "0" : request.getParameter("id"));
            int quantity = Integer.parseInt(request.getParameter("quantity") == null ? "0" : request.getParameter("quantity"));

            if (user == null) {
                Cart cart = (Cart) session.getAttribute("cart");
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
            } else {
                model.Cart cart2 = cartSerive.getCart(user.getId());
                if (id != 0) {
                    if (quantity > 0) {

                        BookService bookService = new BookService();
                        Book book = bookService.getBooksById(id);

                        if (book == null || quantity > book.getStock()) {
                            response.getWriter().print(
                                    "{\"success\":false,\"total\":" + cart2.getTotalQuantity() + "}"
                            );
                            return;
                        }

                        cartSerive.updateItem(cart2.getId(), id, quantity);

                    } else {
                        cartSerive.removeItem(cart2.getId(), id);
                    }
                } else {
                    cartSerive.removeAllItems(cart2.getId());
                }
                cart2 = cartSerive.getCart(user.getId());
                session.setAttribute("cart", cart2);

                response.getWriter().print(
                        "{\"success\":true,\"total\":" + (cart2 != null ? cart2.getTotalQuantity() : 0) + "}"
                );
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