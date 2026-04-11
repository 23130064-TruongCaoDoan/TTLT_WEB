package controler.user.ThanhToan;

import Cart.Cart;
import Cart.CartItem;
import Service.BookService;
import Service.OrderService;
import Service.UserService;
import Util.Token8;
import Util.VNPayUtils;
import jakarta.servlet.*;
        import jakarta.servlet.http.*;
        import jakarta.servlet.annotation.*;
        import model.Book;
import model.User;
import model.Voucher;

import java.io.IOException;
import java.util.Iterator;

@WebServlet(name = "CreateOrder", value = "/CreateOrder")
public class CreateOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserService userService = new UserService();

        int addressId = 0;
        String shipType = null;
        double shipFee = 0;
        double finalTotal = 0;
        String note = null;
        String deliveryRange = null;
        boolean usePoint = false;

        User user = (User) session.getAttribute("user");
        String mode = request.getParameter("mode");
        String paymentMethod = request.getParameter("payment");
        String flagVNPay = request.getParameter("fromVNPay");
        String addressIdStr = request.getParameter("addressId");


        if (flagVNPay != null) {
            mode = (String) session.getAttribute("pendingMode");
            paymentMethod = (String) session.getAttribute("pendingPayment");
        }
        Cart cart;
        if ("buynow".equals(mode)) {
            cart = (Cart) session.getAttribute("buyNowCart");
        } else {
            cart = (Cart) session.getAttribute("cart");
        }

        if (flagVNPay == null) {
            if (user == null || cart == null || cart.getItems().isEmpty()) {
                response.sendRedirect("cart.jsp");
                return;
            }

            if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn địa chỉ giao hàng");
                request.getRequestDispatcher("ThanhToan").forward(request, response);
                return;
            }
             addressId = Integer.parseInt(addressIdStr);
             shipType = request.getParameter("shipType");
             usePoint = "1".equals(request.getParameter("usePoint"));
             note = request.getParameter("orderNote");
             deliveryRange = request.getParameter("deliveryRange");
        }




        Voucher dis = (Voucher) session.getAttribute("appliedDiscountVoucher");
        Voucher ship = (Voucher) session.getAttribute("appliedShipVoucher");

        int disid = dis == null ? 0 : dis.getId();
        int shipid = ship == null ? 0 : ship.getId();

        int userId = user.getId();
        int pointUsed =user.getPoint();


        if (usePoint && pointUsed >=100) {
            user.setPoint(user.getPoint() - pointUsed);
            userService.updateDiem(userId, pointUsed);
        }

        //Xu ly VNPay

        if (flagVNPay != null) {

            addressId = (int) session.getAttribute("pendingAddressId");
            shipType = (String) session.getAttribute("pendingShipType");
            shipFee = (double) session.getAttribute("pendingShipFee");
            finalTotal = (double) session.getAttribute("pendingFinalTotal");
            note = (String) session.getAttribute("pendingNote");
            deliveryRange = (String) session.getAttribute("pendingDeliveryRange");

        }
        if ("vnpay".equals(paymentMethod) && flagVNPay == null) {
            session.setAttribute("pendingMode", mode);
            session.setAttribute("pendingAddressId", addressId);
            session.setAttribute("pendingShipType", shipType);
            session.setAttribute("pendingShipFee", shipFee);
            session.setAttribute("pendingFinalTotal", finalTotal);
            session.setAttribute("pendingNote", note);
            session.setAttribute("pendingDeliveryRange", deliveryRange);
            session.setAttribute("pendingPayment", paymentMethod);

            Token8 token = new Token8();
            long amount = (long) finalTotal;
            String paymentUrl = VNPayUtils.createPaymentUrl(token.generateToken8(), amount);
            response.sendRedirect(paymentUrl);
            return;
        }
        if (flagVNPay != null) {
            String code = request.getParameter("vnp_ResponseCode");

            if (!"00".equals(code)) {
                String msg = "Thanh Toán không thành công";
                request.setAttribute("error", msg);
                request.getRequestDispatcher("/ThanhToan").forward(request, response);
                return;
            }
        }
        String paymentStatus = paymentMethod.equalsIgnoreCase("cod") ? "NOPAID" : "PAID";

        OrderService orderService = new OrderService();
        boolean check = orderService.addOrder(userId, finalTotal, note,paymentMethod,paymentStatus,disid, shipid, addressId, shipType, shipFee, deliveryRange, cart);

        session.removeAttribute("appliedDiscountVoucher");
        session.removeAttribute("appliedShipVoucher");

        BookService bookService = new BookService();

        if (check) {
            userService.tichDiem(userId, finalTotal);
            user.setPoint(user.getPoint() + (int) (finalTotal * 0.05));
            session.setAttribute("user", user);
            if ("buynow".equals(mode)) {
                Cart buyNowCart = (Cart) session.getAttribute("buyNowCart");
                session.removeAttribute("buyNowCart");

                Cart mainCart = (Cart) session.getAttribute("cart");
                if (mainCart == null) {
                    response.sendRedirect("my-orders");
                    return;
                }

                Iterator<CartItem> iterator = mainCart.getItems().iterator();

                while (iterator.hasNext()) {
                    CartItem item = iterator.next();
                    int bookId = item.getBook().getId();

                    int stock = bookService.getStockByBookId(bookId);

                    if (stock <= 0) {
                        iterator.remove();
                        continue;
                    }

                    if (item.getQuantity() > stock) {
                        item.setQuantity(stock);
                    }
                }
            } else {
                session.removeAttribute("cart");
            }

            session.removeAttribute("pendingMode");
            session.removeAttribute("pendingAddressId" );
            session.removeAttribute("pendingShipType");
            session.removeAttribute("pendingShipFee");
            session.removeAttribute("pendingFinalTotal");
            session.removeAttribute("pendingNote");
            session.removeAttribute("pendingDeliveryRange");
            session.removeAttribute("pendingPayment");
            session.removeAttribute("pendingUsePoint");
            session.removeAttribute("pendingPointUsed");
            response.sendRedirect("my-orders");
        } else {
            response.sendRedirect("ThanhToan");
        }
    }
}