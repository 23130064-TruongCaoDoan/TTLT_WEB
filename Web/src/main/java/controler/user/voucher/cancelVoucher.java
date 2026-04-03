package controler.user.voucher;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Voucher;

import java.io.IOException;

@WebServlet(name = "cancelVoucher", value = "/cancelVoucher")
public class cancelVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        String voucherIdStr = request.getParameter("voucherId");
        Integer numApplyVoucher = (Integer) session.getAttribute("numApplyVoucher");
        if (numApplyVoucher == null) numApplyVoucher = 0;
        boolean success = false;

        if (voucherIdStr != null) {
            int voucherId = Integer.parseInt(voucherIdStr);

            Voucher discount = (Voucher) session.getAttribute("appliedDiscountVoucher");
            Voucher ship = (Voucher) session.getAttribute("appliedShipVoucher");

            if (discount != null && discount.getId() == voucherId) {
                session.removeAttribute("appliedDiscountVoucher");
                numApplyVoucher--;
                success = true;
            }

            if (ship != null && ship.getId() == voucherId) {
                session.removeAttribute("appliedShipVoucher");
                numApplyVoucher--;
                success = true;
            }
        }
        session.setAttribute("numApplyVoucher", numApplyVoucher);

        response.getWriter().write(
                "{ \"success\": " + success +", \"num\": " + numApplyVoucher + " }"
        );
    }
}