package controler.user.order;

import DTO.OrderDetailDTO;
import DTO.OrderItemDTO;
import Service.CommentService;
import Service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        int orderId = Integer.parseInt(request.getParameter("id"));
        OrderDetailDTO dto = orderService.getOrderDetail(orderId);
        System.out.println(dto);
        System.out.println(dto.getItems());

        CommentService commentService = new CommentService();
        Set<Integer> bookReviewList = commentService.getReviewedBookIds(user.getId(), orderId);
        System.out.println(bookReviewList);
        for (OrderItemDTO orderItemDTO : dto.getItems()){
            boolean isReviewed = bookReviewList.contains(orderItemDTO.getBookId());
            orderItemDTO.setReviewed(isReviewed);
        }

        LocalDate orderDate = LocalDateTime.parse(dto.getOrder().getOrderDate(), formatterDate).toLocalDate();

        LocalDate dateCreateFunc = LocalDate.of(2026, 3, 31);

        boolean isNewOrder = !orderDate.isBefore(dateCreateFunc);

        request.setAttribute("isNewOrder", isNewOrder);
        request.setAttribute("dto", dto);

        request.getRequestDispatcher("/user/user-order-detail.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}