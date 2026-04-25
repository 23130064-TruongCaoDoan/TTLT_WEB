package controler.user.order;

import Service.OrderService;
import Service.ReturnRequestService;
import Service.UploadService; // Thêm import này
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Order;
import model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "RequestReturnServlet", value = "/request-return")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 10 * 1024 * 1024, maxRequestSize = 20 * 1024 * 1024)
public class RequestReturnServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng đăng nhập lại.\"}");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String reason = request.getParameter("reason");

            OrderService orderService = new OrderService();
            Order order = orderService.getOrderDetail(orderId).getOrder();

            if (order.getUserId() != user.getId() || !order.getStatus().equalsIgnoreCase("COMPLETED")) {
                response.getWriter().write("{\"success\":false,\"message\":\"Đơn hàng không hợp lệ.\"}");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            LocalDate orderDate = LocalDateTime.parse(order.getOrderDate(), formatter).toLocalDate();
            if (ChronoUnit.DAYS.between(orderDate, LocalDate.now()) > 7) {
                response.getWriter().write("{\"success\":false,\"message\":\"Đã quá 7 ngày kể từ khi nhận hàng, không thể trả hàng.\"}");
                return;
            }

            Part filePart = request.getPart("proofImage");
            if (filePart == null || filePart.getSize() == 0) {
                response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng chọn ảnh minh chứng.\"}");
                return;
            }

            UploadService uploadService = new UploadService();
            String dbImagePath = uploadService.upload(filePart, "returns");

            if (dbImagePath == null) {
                response.getWriter().write("{\"success\":false,\"message\":\"Lỗi tải ảnh lên server.\"}");
                return;
            }

            ReturnRequestService returnService = new ReturnRequestService();
            boolean success = returnService.insertRequest(orderId, user.getId(), reason, dbImagePath);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"Đã gửi yêu cầu trả hàng, vui lòng chờ duyệt.\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"Lỗi hệ thống khi lưu database.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Có lỗi xảy ra: " + e.getMessage() + "\"}");
        }
    }
}