package controler.admin.adminOrder;

import Service.BookService;
import Service.NotificationService;
import Service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
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

        try {
            Order order = orderService.getOrderDetail(orderId).getOrder();
            if (order != null) {
                int customerId = order.getUserId();

                String statusVN = "";
                switch (orderStatus.toUpperCase()) {
                    case "PENDING": statusVN = "Chờ xử lý"; break;
                    case "PROCESSING": statusVN = "Đang chuẩn bị hàng"; break;
                    case "SHIPPING": statusVN = "Đang giao hàng"; break;
                    case "COMPLETED": statusVN = "Giao hàng thành công"; break;
                    case "REFUNDED": statusVN = "Đã hoàn tiền"; break;
                    case "CANCELLED": statusVN = "Đã bị hủy"; break;
                    default: statusVN = orderStatus;
                }

                String title = "Cập nhật đơn hàng";
                String content = "Đơn hàng #" + orderId + " của bạn đã được chuyển sang trạng thái: " + statusVN;

                NotificationService notiService = new NotificationService();
                notiService.sendNoti(customerId, title, content);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi gửi thông báo cập nhật đơn hàng: " + e.getMessage());
        }

        request.setAttribute("logSuccess", true);
        String q = request.getParameter("q");

        String sortDate = request.getParameter("sortDate");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String statusFilter = request.getParameter("statusFilter");

        if (fromDate != null && fromDate.trim().isEmpty()) fromDate = null;
        if (toDate != null && toDate.trim().isEmpty()) toDate = null;
        List<OrderView> list = orderService.searchOrder(q, sortDate, fromDate, toDate, statusFilter);

        Map<String, List<String>> transitions = Map.of(
                "PENDING", List.of("PROCESSING", "CANCELLED"),
                "PROCESSING", List.of("SHIPPING", "CANCELLED"),
                "SHIPPING", List.of("COMPLETED","CANCELLED"),
                "COMPLETED", List.of("REFUNDED"),
                "REFUNDED", List.of(),
                "CANCELLED", List.of()
        );

        request.setAttribute("orders", list);
        request.setAttribute("transitions", transitions);

        request.getRequestDispatcher("admin/orderTable.jsp").forward(request, response);
    }

}
