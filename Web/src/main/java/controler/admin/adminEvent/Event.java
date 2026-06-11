package controler.admin.adminEvent;

import Service.BookService;
import Service.EventService;
import Service.UserService;
import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;

import static Util.RolesGroup.SALES_ROLE;

@WebServlet(name = "Event", value = "/Event")
public class Event extends HttpServlet {
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
        int role = userService.checkRole(user);
        if (!SALES_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }

        EventService eventService = new EventService();

        String q = request.getParameter("q");
        String sortDate = request.getParameter("sortDate");
        String sortActivity = request.getParameter("sortActivity");

        List<model.Event> listEvent = eventService.searchAndFilter(q, sortDate, sortActivity);

        BookService bookService = new BookService();
        VoucherService voucherService = new VoucherService();
        request.setAttribute("listAuthors", bookService.getAllAuthorsOb());
        request.setAttribute("listPublishers", bookService.getAllPublishers());
        request.setAttribute("listAges", bookService.getAllAges());
        request.setAttribute("listVoucher", voucherService.getListVoucher());
        request.setAttribute("listEvent", listEvent);
        request.getRequestDispatcher("admin/events.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}