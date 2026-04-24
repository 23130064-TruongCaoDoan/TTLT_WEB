package controler.admin.adminOrder;

import Service.BookService;
import Service.NotificationService;
import Service.OrderService;
import Service.ReturnRequestService;
import dao.ReturnRequestDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.OrderItemsView;
import model.ReturnRequest;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProcessReturnServlet", value = "/process-return")
public class ProcessReturnServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String action = request.getParameter("action");
        String rejectReason = request.getParameter("rejectReason");

        ReturnRequestService returnService = new ReturnRequestService();
        ReturnRequest returnReq = returnService.getById(requestId);

        if (returnReq == null || !returnReq.getStatus().equals("PENDING")) {
            response.getWriter().write("{\"success\":false,\"message\":\"Yêu cầu không tồn tại hoặc đã xử lý.\"}");
            return;
        }

        OrderService orderService = new OrderService();
        BookService bookService = new BookService();
        NotificationService notiService = new NotificationService();

        if (action.equalsIgnoreCase("APPROVE")) {
            returnService.updateStatus(requestId, "APPROVED", null);

            orderService.updateOrderStatus(returnReq.getOrderId(), "REFUNDED");

            List<OrderItemsView> items = orderService.getOrderItemsByOrderId(returnReq.getOrderId());
            for (OrderItemsView item : items) {
                bookService.updateQuantityOrderCancelled(item.getBookId(), item.getQuantity());
            }

            notiService.sendNoti(returnReq.getUserId(), "Trả hàng thành công", "Yêu cầu trả hàng cho đơn #" + returnReq.getOrderId() + " đã được duyệt. Tiền sẽ được hoàn lại sớm nhất!");

            response.getWriter().write("{\"success\":true,\"message\":\"Đã duyệt trả hàng & hoàn kho.\"}");

        } else if (action.equalsIgnoreCase("REJECT")) {
            returnService.updateStatus(requestId, "REJECTED", rejectReason);

            notiService.sendNoti(returnReq.getUserId(), "Trả hàng thất bại", "Yêu cầu trả hàng đơn #" + returnReq.getOrderId() + " bị từ chối. Lý do: " + rejectReason);

            response.getWriter().write("{\"success\":true,\"message\":\"Đã từ chối yêu cầu trả hàng.\"}");
        }
    }
}