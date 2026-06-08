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

import static Util.RolesGroup.SALES_ROLE;

@WebServlet(name = "OrderManagerServlet", value = "/OrderManagerServlet")
public class OrderManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login");
            return;
        }
        UserService userService = new UserService();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        int role = userService.checkRole(user);
        if (!SALES_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }

        OrderService orderService = new OrderService();

        String q = request.getParameter("q");

        String sortDate = request.getParameter("sortDate");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String statusFilter = request.getParameter("statusFilter");

        if (fromDate != null && fromDate.trim().isEmpty()) {
            fromDate = null;
        }
        if (toDate != null && toDate.trim().isEmpty()) {
            toDate = null;
        }
        int pageSize = 5;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isBlank()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage <= 0) {
                    currentPage = 1;
                }
            } catch (Exception e) {
                currentPage = 1;
            }
        }
        int totalOrders = orderService.countSearchOrder(q, fromDate, toDate, statusFilter);
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        int offset = (currentPage - 1) * pageSize;
        if (offset < 0) {
            offset = 0;
        }
        List<OrderView> list = orderService.searchOrderPaginated(q, sortDate, fromDate, toDate, statusFilter, pageSize, offset);

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
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("admin/quanlidonhang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}