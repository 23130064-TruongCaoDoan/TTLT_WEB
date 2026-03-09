package controler;

import DTO.OrderDetailDTO;
import Service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "OrderDetailServlet", value = "/my-order")
public class OrderDetailServlet extends HttpServlet {
    private final OrderService service = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("id"));

        OrderDetailDTO dto = service.getOrderDetail(orderId);
        req.setAttribute("dto", dto);

        req.getRequestDispatcher("/user/user-order-detail.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}