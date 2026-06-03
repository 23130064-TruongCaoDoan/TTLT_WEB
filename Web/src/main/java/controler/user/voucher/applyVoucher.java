package controler.user.voucher;

import Service.CartSerive;
import cart.Cart;
import Service.VoucherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
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
        String mode = request.getParameter("mode");
        User user = (User) session.getAttribute("user");
        Cart cartGuest = null;
        model.Cart cartDb = null;

        boolean isDbCart = false;
        CartSerive cartSerive = new CartSerive();

        if ("buynow".equals(mode)) {
            cartGuest = (Cart) request.getSession().getAttribute("buyNowCart");

        } else if ("rebuy".equals(mode)) {
            cartGuest = (Cart) request.getSession().getAttribute("rebuyCart");
        } else {
            if(user != null){
                cartDb = cartSerive.getCart(user.getId());
                session.setAttribute("cart", cartDb);
                isDbCart = true;
            }
            else {
                cartGuest = (Cart) request.getSession().getAttribute("cart");
            }
        }

        int voucherId = Integer.parseInt(request.getParameter("voucherId"));
        VoucherService voucherService = new VoucherService();
        Voucher voucher = voucherService.getById(voucherId);
        boolean valid=false;
        if(isDbCart){
             valid = voucherService.isVoucherValid(cartDb, voucher);
        }
        else{
            valid = voucherService.isVoucherValid(cartGuest, voucher);
        }

        Integer numApplyVoucher = (Integer) session.getAttribute("numApplyVoucher");
        if (numApplyVoucher == null) numApplyVoucher = 0;

        boolean success = false;
        String message = "";
        System.out.println("Voucher = " + voucher.getCode());
        System.out.println("Valid = " + valid);
        if (valid) {
            if ("discount".equals(voucher.getType())&& session.getAttribute("appliedDiscountVoucher") == null) {
                session.setAttribute("appliedDiscountVoucher", voucher);
                numApplyVoucher++;
                success = true;
                message = "Áp dụng voucher giảm giá thành công!";
            }

            if ("ship".equals(voucher.getType())&& session.getAttribute("appliedShipVoucher") == null) {
                session.setAttribute("appliedShipVoucher", voucher);
                session.setAttribute("shipDiscount", voucher.getValuee());
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
