package controler.admin.adminOrder;

import Service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateOrder", value = "/UpdateOrder")
public class UpdateOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String id = request.getParameter("id");
        int orderId = Integer.parseInt(id);
        String orderStatus = request.getParameter("status");
        String totalPrice = request.getParameter("total");
        Double orderTotalPrice = Double.parseDouble(totalPrice);

        OrderService orderService = new OrderService();
        boolean success=orderService.updateOrder(orderId,orderTotalPrice,orderStatus);
        if(success){
            response.getWriter().write(
                    "{\"success\":true}"
            );
        }
        else{
            response.getWriter().write(
                    "{\"success\":false}"
            );
        }

    }
}