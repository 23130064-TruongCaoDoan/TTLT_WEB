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

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        int voucherId = Integer.parseInt(request.getParameter("voucherId"));
        VoucherService voucherService = new VoucherService();
        Voucher voucher = voucherService.getById(voucherId);

        boolean valid = voucherService.isVoucherValid(cart, voucher);

        Integer numApplyVoucher = (Integer) session.getAttribute("numApplyVoucher");
        if (numApplyVoucher == null) numApplyVoucher = 0;

        boolean success = false;
        String message = "";

        if (valid) {
            if ("discount".equals(voucher.getType())&& session.getAttribute("appliedDiscountVoucher") == null) {
                session.setAttribute("appliedDiscountVoucher", voucher);
                numApplyVoucher++;
                success = true;
                message = "Áp dụng voucher giảm giá thành công!";
            }

            if ("ship".equals(voucher.getType())&& session.getAttribute("appliedShipVoucher") == null) {
                session.setAttribute("appliedShipVoucher", voucher);
                numApplyVoucher++;
                success = true;
                message = "Áp dụng voucher vận chuyển thành công!";
            }
        } else {
            message = "Voucher không hợp lệ!";
        }

        session.setAttribute("numApplyVoucher", numApplyVoucher);
        response.getWriter().write(
                "{ \"success\": " + success +", \"message\": \"" + message +"\", \"num\": " + numApplyVoucher + " }"
        );
    }

}
