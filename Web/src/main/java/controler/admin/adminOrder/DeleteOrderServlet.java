package controler.admin.adminOrder;

import Service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteOrderServlet", value = "/DeleteOrderServlet")
public class DeleteOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String idRaw = request.getParameter("id");

        try {
            OrderService orderService = new OrderService();
            int id = Integer.parseInt(idRaw);
            String userCodes = request.getParameter("userId");
            boolean success;

            if (userCodes.isEmpty()){
                 success = orderService.deleteOrder(id);
            }
            else{
                int userId = Integer.parseInt(userCodes);
                success= orderService.deleteOrderOfUser(userId,id);
            }

            if (success) {
                response.getWriter().write(
                        "{\"success\":true,\"message\":\"Xóa đơn hàng thành công\"}"
                );
            } else {
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Xóa đơn hàng thất bại\"}"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Lỗi server\"}"
            );
        }
    }
}