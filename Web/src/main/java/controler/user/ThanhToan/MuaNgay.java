package controler.user.ThanhToan;

import Cart.Cart;
import Service.BookService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.User;

import java.io.IOException;

@WebServlet(name = "MuaNgay", value = "/MuaNgay")
public class MuaNgay extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        BookService bookService = new BookService();
        Book book = bookService.getBooksById(bookId);

        Cart buyNowCart = new Cart();
        buyNowCart.addItem(book, quantity);

        session.setAttribute("buyNowCart", buyNowCart);

        response.sendRedirect("ThanhToan?mode=buynow");
    }
}