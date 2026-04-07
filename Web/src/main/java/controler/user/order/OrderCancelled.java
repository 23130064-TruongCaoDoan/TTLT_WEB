package controler.user.order;

import DTO.OrderItemDTO;
import Service.BookService;
import Service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.OrderItemsView;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "cancell-order", value = "/cancell-order")
public class OrderCancelled extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        int orderId = Integer.parseInt(request.getParameter("id"));
        OrderService orderService = new OrderService();
        BookService bookService = new BookService();
        Order order = orderService.getOrderDetail(orderId).getOrder();
        if(order.getUserId() != user.getId()){
            response.sendError(403);
            return;
        }
        List<OrderItemsView> listItem = orderService.getOrderItemsByOrderId(orderId);
        if (order.getStatus().equalsIgnoreCase("pending") || order.getStatus().equalsIgnoreCase("processing")) {
            orderService.updateOrderStatus(orderId,"CANCELLED");
            if(!order.getPaymentMethod().equalsIgnoreCase("COD")) {
                orderService.updatePaymentStatus(orderId,"REFUNDED");
            }
            for (OrderItemsView item : listItem){
                bookService.updateQuantityOrderCancelled(item.getBookId(), item.getQuantity());
            }

        }
    }
}
