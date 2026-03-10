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

        User user = (User) session.getAttribute("user");
        String mode = request.getParameter("mode");
        String paymentMethod = request.getParameter("payment");
        String flagVNPay = request.getParameter("fromVNPay");

        Cart cart;
        if ("buynow".equals(mode)) {
            cart = (Cart) session.getAttribute("buyNowCart");
        } else {
            cart = (Cart) session.getAttribute("cart");
        }

        if (user == null || cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect("cart.jsp");
            return;
        }

        String addressIdStr = request.getParameter("addressId");

        if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn địa chỉ giao hàng");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }
        int addressId = Integer.parseInt(addressIdStr);
        String shipType = request.getParameter("shipType");
        boolean usePoint = "1".equals(request.getParameter("usePoint"));
        String note = request.getParameter("orderNote");

        double shipFee = Double.parseDouble(request.getParameter("shipFee"));
        String pointUsedStr = request.getParameter("pointUsed");
        int pointUsed = (int) Double.parseDouble(pointUsedStr);
        double finalTotal = Double.parseDouble(request.getParameter("finalTotal"));
        String deliveryRange = request.getParameter("deliveryRange");

        Voucher dis = (Voucher) session.getAttribute("appliedDiscountVoucher");
        Voucher ship = (Voucher) session.getAttribute("appliedShipVoucher");

        int disid = dis == null ? 0 : dis.getId();
        int shipid = ship == null ? 0 : ship.getId();

        int userId = user.getId();


        if (usePoint && pointUsed > 0) {
            user.setPoint(user.getPoint() - pointUsed);
            userService.updateDiem(userId, pointUsed);
        }

        //Xu ly VNPay

        if (flagVNPay != null) {
            String code = request.getParameter("vnp_ResponseCode");
            if (!"00".equals(code)) {
                String msg = "Thanh Toán không thành công";
                request.setAttribute("error", msg);
                request.getRequestDispatcher("/ThanhToan").forward(request, response);
                return;
            }
        }
        if ("vnpay".equals(paymentMethod) && flagVNPay == null) {
            Token8  token = new Token8();
            long amount = (long) finalTotal;
            String url = VNPayUtils.createPaymentUrl(token.generateToken8(), amount);
            response.sendRedirect(url);
            return;
        }


        OrderService orderService = new OrderService();
        boolean check = orderService.addOrder(userId, finalTotal, note,paymentMethod,disid, shipid, addressId, shipType, shipFee, deliveryRange, cart);

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


            response.sendRedirect("my-orders");
        } else {
            response.sendRedirect("ThanhToan");
        }
    }
}