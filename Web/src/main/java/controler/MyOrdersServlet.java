package controler;

import DTO.MyOrderDTO;
import Service.OrderService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MyOrdersServlet", value = "/my-orders")
public class MyOrdersServlet extends HttpServlet {
    private final OrderService service = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        List<MyOrderDTO> orders = service.getMyOrders(user.getId());
        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/user/user-myOrders.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}