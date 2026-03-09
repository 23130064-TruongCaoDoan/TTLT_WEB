package controler.user.voucher;

import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViVoucher", value = "/my-vouchers")
public class ViVoucher extends HttpServlet {
    private final VoucherService voucherService = new VoucherService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = user.getId();
        System.out.println("ViVoucher servlet called, userId = " + userId);
        List<Voucher> discountVouchers =
                voucherService.listVoucherDiscountUser(userId);

        List<Voucher> shipVouchers =
                voucherService.listVoucherShipUser(userId);

        System.out.println("Discount size = " + discountVouchers.size());
        System.out.println("Ship size = " + shipVouchers.size());
        request.setAttribute("discountVouchers", discountVouchers);
        request.setAttribute("shipVouchers", shipVouchers);

        request.getRequestDispatcher("/user/ViVoucher.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}