package controler.user.order;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        String status = request.getParameter("status");
        if (status == null) {
            status = "ALL";
        }
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String sort = request.getParameter("sort");


        List<MyOrderDTO> orders = service.getMyOrders(user.getId(),status);
        request.setAttribute("orders", orders);
        if (sort == null) {
            request.getRequestDispatcher("/user/user-myOrders.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/user/orderList.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}