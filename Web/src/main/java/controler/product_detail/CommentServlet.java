package controler.product_detail;

import DTO.MyOrderDTO;
import DTO.OrderDetailDTO;
import DTO.OrderItemDTO;
import Service.CommentService;
import Service.OrderService;
import Service.UploadService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Book;
import model.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet (name="comment" ,value="/comment")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class CommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        OrderService  orderService = new OrderService();
        OrderDetailDTO order = orderService.getOrderDetail(orderId);
        List<OrderItemDTO> items = order.getItems();
        List<MyOrderDTO> orders = orderService.getMyOrders(user.getId());
        if (order.getOrder().isReviewed()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Đơn hàng này đã được đánh giá");
            return;
        }

        int rating = Integer.parseInt(request.getParameter("rating"));
        String content = request.getParameter("content");
        Part imagePart = request.getPart("image");

        UploadService uploadService = new UploadService();
        String imageUrl = uploadService.upload(imagePart, "comments");
        CommentService commentService = new CommentService();
        int bookId;
        for (OrderItemDTO item : items) {
            bookId = item.getBookId();
            commentService.insertComment(user.getId(), bookId, rating, content, imageUrl);
        }
        orderService.setReviewed(orderId);



        request.setAttribute("message", "Đánh giá thành công");

        request.setAttribute("orders", orders);
        response.setStatus(HttpServletResponse.SC_OK);

    }

}
