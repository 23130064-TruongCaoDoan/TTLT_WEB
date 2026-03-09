package controler.user.voucher;

import Cart.Cart;
import Service.VoucherService;
import com.google.protobuf.Enum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Voucher;

import java.io.IOException;

@WebServlet("/applyVoucher")
public class applyVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        int voucherId = Integer.parseInt(request.getParameter("voucherId"));
        VoucherService voucherService = new VoucherService();
        Voucher voucher = voucherService.getById(voucherId);

        boolean valid = voucherService.isVoucherValid(cart, voucher);

        int page = Integer.parseInt(request.getParameter("page") == null ? "0" : request.getParameter("page"));
        Integer numApplyVoucher= (Integer) session.getAttribute("numApplyVoucher");
        if (numApplyVoucher == null) numApplyVoucher = 0;
        if (valid) {
            if (voucher.getType().equals("discount")&& session.getAttribute("appliedDiscountVoucher") == null) {
                session.setAttribute("appliedDiscountVoucher", voucher);
                numApplyVoucher++;
            }
            if (voucher.getType().equals("ship")&& session.getAttribute("appliedShipVoucher") == null) {
                session.setAttribute("appliedShipVoucher", voucher);
                numApplyVoucher++;
            }
        }
        session.setAttribute("numApplyVoucher", numApplyVoucher);
        if (page == 1) {
            response.sendRedirect("ShoppingCart");
        }
        if (page == 2) {
            response.sendRedirect("ThanhToan");
        }
    }

}
