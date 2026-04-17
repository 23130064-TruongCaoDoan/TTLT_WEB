package controler.admin.adminOrder;

import Service.OrderService;
import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.OrderView;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderManagerServlet", value = "/OrderManagerServlet")
public class OrderManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        UserService userService = new UserService();

        if (!userService.checkRole(user)) {
            response.sendRedirect("login");
            return;
        }
        OrderService orderService = new OrderService();

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

        request.setAttribute("transitions", transitions);
        request.setAttribute("orders", list);
        request.getRequestDispatcher("admin/quanlidonhang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}