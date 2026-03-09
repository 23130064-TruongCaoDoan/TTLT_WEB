package controler.admin.adminOrder;

import Service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.OrderItemsView;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetOrderItemsServlet", value = "/GetOrderItemsServlet")
public class GetOrderItemsServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        String orderIdRaw = request.getParameter("orderId");
        System.out.println("DEBUG orderId = " + orderIdRaw);

        if (orderIdRaw == null || orderIdRaw.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("[]");
            return;
        }

        int orderId = Integer.parseInt(orderIdRaw);

        List<OrderItemsView> items = orderService.getOrderItemsByOrderId(orderId);

        new ObjectMapper().writeValue(response.getWriter(), items);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}