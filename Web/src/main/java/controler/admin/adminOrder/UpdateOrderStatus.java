package controler.admin.adminOrder;

import Service.BookService;
import Service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.OrderView;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@WebServlet(name = "UpdateOrderStatus", value = "/UpdateOrderStatus")
public class UpdateOrderStatus extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User)session.getAttribute("user");

        if(!user.isRole()){
            response.sendRedirect("login");
            return;
        }

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String orderStatus = request.getParameter("orderStatus");

        OrderService orderService = new OrderService();
        orderService.updateOrderStatus(orderId, orderStatus);

        String q = request.getParameter("q");

        String sortDate = request.getParameter("sortDate");
        List<OrderView> list=orderService.searchOrder(q,sortDate);

        Map<String, List<String>> transitions = Map.of(
                "PENDING", List.of("PROCESSING", "CANCELLED"),
                "PROCESSING", List.of("SHIPPING", "CANCELLED"),
                "SHIPPING", List.of("COMPLETED"),
                "COMPLETED", List.of("REFUNDED"),
                "REFUNDED", List.of(),
                "CANCELLED", List.of()
        );

        request.setAttribute("orders", list);
        request.setAttribute("transitions", transitions);

        request.getRequestDispatcher("admin/orderTable.jsp").forward(request, response);
    }

}
