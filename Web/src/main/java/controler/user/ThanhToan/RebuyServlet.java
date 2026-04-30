package controler.user.ThanhToan;

import Cart.Cart;
import DTO.OrderItemDTO;
import Service.AddressService;
import Service.BookService;
import Service.OrderService;
import Service.VoucherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name="rebuy", value = "/rebuy")
public class RebuyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        int OrderId = Integer.parseInt(request.getParameter("OrderId"));
        OrderService orderService = new OrderService();
        List<OrderItemsView> list = orderService.getOrderItemsByOrderId(OrderId);

        BookService bookService = new BookService();

        Cart rebuyCart = new Cart();
        for (OrderItemsView orderItemsView : list) {
            Book book = bookService.getBooksById(orderItemsView.getBookId());
            rebuyCart.addItem(book, orderItemsView.getQuantity());
        }

        session.setAttribute("rebuyCart", rebuyCart);

        response.sendRedirect("ThanhToan?mode=rebuy");
    }
}
