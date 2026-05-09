package controler.admin.CustomerManage;

import Service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateOrderServlet", value = "/UpdateOrderServlet")
public class UpdateOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderService orderService = new OrderService();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");
        String totalStr = request.getParameter("totalAmount");

        if (totalStr == null || !totalStr.matches("\\d+")) {
            response.getWriter().write("{\"success\":false,\"message\":\"Tổng tiền phải là số nguyên\"}");
            return;
        }

        double totalAmount = Double.parseDouble(totalStr);

        boolean ok= orderService.updateOrder(orderId,totalAmount,status);
        if (ok) {
            response.getWriter().write("{\"success\":true,\"message\":\"Thực hiện chỉnh sửa thành công\"}");
            return;
        }
        else{
            response.getWriter().write("{\"success\":false,\"message\":\"chỉnh sửa thất bại\"}");
            return;
        }


    }
}