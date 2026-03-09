package controler.user.Cart;

import Cart.Cart;
import Service.BookService;
import Service.EventService;
import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.User;
import model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShoppingCart", value = "/ShoppingCart")
public class ShoppingCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();

        VoucherService voucherService = new VoucherService();
        HttpSession session = request.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        User user = (User) session.getAttribute("user");

        int userId = user == null ? 0 : user.getId();
        double cartTotal = cart == null ? 0 : cart.getTotalBill();


        Voucher applied = (Voucher) session.getAttribute("appliedDiscountVoucher");

        if (applied != null) {
            boolean valid = voucherService.isVoucherValid( cart,applied);
            if (!valid) {
                session.removeAttribute("appliedDiscountVoucher");
                applied = null;
                Integer numApplyVoucher= (Integer) session.getAttribute("numApplyVoucher");
                numApplyVoucher--;
                session.setAttribute("numApplyVoucher", numApplyVoucher);
            }
        }


        double finalTotal = cartTotal;

        if (applied != null && "discount".equals(applied.getType())) {
            double value = applied.getValuee();

            if (value > 0) {
                if (value < 1) {
                    finalTotal -= cartTotal * value;
                } else {
                    finalTotal -= value;
                }
            }
            if (finalTotal < 0) finalTotal = 0;
        }

        request.setAttribute("finalTotal", finalTotal);


        List<Voucher> listVoucherDiscount = voucherService.listVoucherDiscountUser(userId);
        List<Voucher> listVoucherShip = voucherService.listVoucherShipUser(userId);

        listVoucherDiscount = voucherService.filterVoucherValid(cart, cartTotal, listVoucherDiscount);
        listVoucherShip = voucherService.filterVoucherValid(cart, cartTotal, listVoucherShip);

        request.setAttribute("listVoucherDiscount", listVoucherDiscount);
        request.setAttribute("listVoucherShip", listVoucherShip);

        request.getRequestDispatcher("user/shoppingCart.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}