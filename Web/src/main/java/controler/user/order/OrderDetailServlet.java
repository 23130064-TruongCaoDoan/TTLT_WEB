package controler.user.order;

import DTO.OrderDetailDTO;
import DTO.OrderItemDTO;
import Service.CommentService;
import Service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.CommentView;
import model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Util.Format.formatter;
import static Util.Format.formatterDate;

@WebServlet(name = "OrderDetailServlet", value = "/my-order")
public class OrderDetailServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String orderIdEncoded  = request.getParameter("id");
        int orderId = Integer.parseInt(new String(Base64.getUrlDecoder().decode(orderIdEncoded)));
        OrderDetailDTO dto = orderService.getOrderDetail(orderId);
        if(dto.getOrder().getUserId() != user.getId()){
            response.sendError(403);
            return;
        }
        CommentService commentService = new CommentService();

        Set<Integer> bookReviewList = commentService.getReviewedBookIds(user.getId(), orderId);
        for (OrderItemDTO orderItemDTO : dto.getItems()){
            boolean isReviewed = bookReviewList.contains(orderItemDTO.getBookId());
            orderItemDTO.setReviewed(isReviewed);
            orderItemDTO.setCommentView(commentService.getCommentByOrder(orderItemDTO.getBookId(),orderId,user.getId()));
        }

        LocalDate orderDate = LocalDateTime.parse(dto.getOrder().getOrderDate(), formatter).toLocalDate();
        LocalDate dateCreateFunc = LocalDate.of(2026, 3, 31);
        boolean isNewOrder = !orderDate.isBefore(dateCreateFunc);
        request.setAttribute("isNewOrder", isNewOrder);
        request.setAttribute("dto", dto);
        request.setAttribute("isCompleted", "COMPLETED".equalsIgnoreCase(dto.getOrder().getStatus()));

        request.getRequestDispatcher("/user/user-order-detail.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}