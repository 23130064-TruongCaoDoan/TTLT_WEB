package controler.user.ThanhToan;

import Cart.Cart;
import Service.BookService;
import Service.OrderService;
import Service.UserService;
import Util.Token8;
import Util.VNPayUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Voucher;

import java.io.IOException;

@WebServlet(name="vnpayPayment", value = "/vnpayPayment")
public class VNPayPayment extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String code = request.getParameter("vnp_ResponseCode");

        System.out.println("VNPay response: " + code);

        if (!"00".equals(code)) {
            request.setAttribute("error", "Thanh toán không thành công");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }

        Integer addressId = (Integer) session.getAttribute("pendingAddressId");
        String shipName = (String) session.getAttribute("pendingShipName");
        Double shipFee = (Double) session.getAttribute("pendingShipFee");
        Double finalTotal = (Double) session.getAttribute("pendingFinalTotal");
        String note = (String) session.getAttribute("pendingNote");
        String deliveryRange = (String) session.getAttribute("pendingDeliveryRange");
        String mode = (String) session.getAttribute("pendingMode");

        Integer productVoucherId = (Integer) session.getAttribute("pendingProductVoucherId");
        Integer shipVoucherId = (Integer) session.getAttribute("pendingShipVoucherId");

        Boolean usePoint = (Boolean) session.getAttribute("pendingUsePoint");
        Integer pointUsed = (Integer) session.getAttribute("pendingPointUsed");

        if (addressId == null || finalTotal == null) {
            response.sendRedirect("cart");
            return;
        }

        Cart cart = "buynow".equals(mode)
                ? (Cart) session.getAttribute("buyNowCart")
                : (Cart) session.getAttribute("cart");

        OrderService orderService = new OrderService();
        UserService userService = new UserService();

        boolean ok = orderService.addOrder(
                user.getId(),
                finalTotal,
                note,
                "vnpay",
                "PAID",
                productVoucherId != null ? productVoucherId : 0,
                shipVoucherId != null ? shipVoucherId : 0,
                addressId,
                shipName,
                shipFee,
                deliveryRange,
                cart
        );

        if (!ok) {
            request.setAttribute("error", "Tạo đơn hàng thất bại");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }

        if (usePoint) {
            user.setPoint(user.getPoint() - pointUsed);
            userService.updateDiem(user.getId(), pointUsed);
        }

        user.setPoint(user.getPoint() + (int) (finalTotal * 0.05));
        session.setAttribute("user", user);

        session.removeAttribute("cart");
        session.removeAttribute("buyNowCart");
        session.removeAttribute("pendingMode");
        session.removeAttribute("pendingAddressId");
        session.removeAttribute("pendingShipType");
        session.removeAttribute("pendingShipName");
        session.removeAttribute("pendingShipFee");
        session.removeAttribute("pendingFinalTotal");
        session.removeAttribute("pendingNote");
        session.removeAttribute("pendingDeliveryRange");
        session.removeAttribute("pendingPayment");
        session.removeAttribute("pendingProductVoucherId");
        session.removeAttribute("pendingShipVoucherId");
        session.removeAttribute("pendingUsePoint");
        session.removeAttribute("pendingPointUsed");

        response.sendRedirect("my-orders");
    }

}
