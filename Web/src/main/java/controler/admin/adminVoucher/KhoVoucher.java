package controler.admin.adminVoucher;

import Service.UserService;
import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "KhoVoucher", value = "/KhoVoucher")
public class KhoVoucher extends HttpServlet {
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
        VoucherService voucherService = new VoucherService();
        List<Voucher> listVoucher = voucherService.getListVoucher();

        request.setAttribute("listVoucher", listVoucher);
        request.getRequestDispatcher("admin/khoVoucher.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}