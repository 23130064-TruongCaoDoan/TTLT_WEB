package controler.user.ThanhToan;

import cart.Cart;
import Service.AddressService;
import Service.CartSerive;
import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;
import model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ThanhToan", value = "/ThanhToan")
public class ThanhToan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String mode = request.getParameter("mode");

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
        if(isDbCart){
            if(!cartSerive.checkCart(cartDb)){
                request.setAttribute("toastMessage", "Một số sản phẩm trong giỏ hàng không hợp lệ đã bị xóa");
                request.getRequestDispatcher("/home").forward(request, response);
                return;
            }
        }
        else {
            if (!cartSerive.checkCart(cartGuest)) {
                request.setAttribute("toastMessage", "Một số sản phẩm trong giỏ hàng không hợp lệ đã bị xóa");
                request.getRequestDispatcher("/home").forward(request, response);
                return;
            }
        }

        VoucherService voucherService = new VoucherService();


//        if (user == null) {
//            String currentUrl = request.getRequestURI();
//            String queryString = request.getQueryString();
//            if (queryString != null) {
//                currentUrl += "?" + queryString;
//            }
//            response.sendRedirect(request.getContextPath()
//                    + "/login?redirect=" + currentUrl);
//            return;
//        }

        boolean isEmpty = isDbCart
                ? (cartDb == null || cartDb.getItems().isEmpty())
                : (cartGuest == null || cartGuest.getItems().isEmpty());
        if (isEmpty) {
            response.sendRedirect("ShoppingCart");
            return;
        }


        AddressService addressService = new AddressService();
        List<Address> listAddress = addressService.getAddress(user.getId());

        String addressIdStr = request.getParameter("addressId");
        int selectedAddressId = 0;

        if (addressIdStr != null && !addressIdStr.trim().isEmpty()) {
            selectedAddressId = Integer.parseInt(addressIdStr);
        } else {
            boolean foundDefault = false;

            for (Address a : listAddress) {
                if (a.getIsDefault()) {
                    selectedAddressId = a.getId();
                    foundDefault = true;
                    break;
                }
            }
            if (!foundDefault && !listAddress.isEmpty()) {
                selectedAddressId = listAddress.get(0).getId();
            }
        }

        double totalBill = isDbCart ? cartDb.getTotalBill() : cartGuest.getTotalBill();

        Voucher voucherDis = (Voucher) session.getAttribute("appliedDiscountVoucher");
        Voucher voucherShip = (Voucher) session.getAttribute("appliedShipVoucher");
        if (isDbCart) {
            if (voucherDis != null) {
                boolean valid = voucherService.isVoucherValid(cartDb, voucherDis);
                if (!valid) {
                    session.removeAttribute("appliedDiscountVoucher");
                    voucherDis = null;
                    Integer numApplyVoucher = (Integer) session.getAttribute("numApplyVoucher");
                    numApplyVoucher--;
                    session.setAttribute("numApplyVoucher", numApplyVoucher);
                }
            }
        }

        if (voucherShip != null) {
            boolean valid = voucherService.isVoucherValid(cartDb, voucherShip);
            if (!valid) {
                session.removeAttribute("appliedShipVoucher");
                voucherShip = null;
                Integer numApplyVoucher = (Integer) session.getAttribute("numApplyVoucher");
                numApplyVoucher--;
                session.setAttribute("numApplyVoucher", numApplyVoucher);
            }
        }

        double discountMoney = 0;

        if (voucherDis != null && "discount".equals(voucherDis.getType())) {
            double value = voucherDis.getValuee();
            if (value < 1) discountMoney = totalBill * value;
            else discountMoney = value;
        }



        double finalTotal = totalBill  - discountMoney;
        if (finalTotal < 0) finalTotal = 0;
        if (isDbCart) {
            request.setAttribute("cart", cartDb);
        } else {
            request.setAttribute("cart", cartGuest);
        }
        request.setAttribute("totalBill", totalBill);
        request.setAttribute("discountMoney", discountMoney);
        request.setAttribute("finalTotal", finalTotal);
        request.setAttribute("listAddress", listAddress);
        request.setAttribute("selectedAddressId", selectedAddressId);

        int userId = user.getId();

        request.setAttribute("listVoucherDiscount", voucherService.filterVoucherValid(cartDb, totalBill, voucherService.listVoucherDiscountUser(userId)));

        request.setAttribute("listVoucherShip", voucherService.filterVoucherValid(cartDb, totalBill, voucherService.listVoucherShipUser(userId)));

        request.getRequestDispatcher("user/ThanhToan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}