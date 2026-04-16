package controler.user.ThanhToan;

import Cart.Cart;
import Cart.CartItem;
import Service.AddressService;
import Service.BookService;
import Service.OrderService;
import Service.UserService;
import Util.GHNApiUtil;
import Util.Token8;
import Util.VNPayUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;
import model.Voucher;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "CreateOrder", value = "/CreateOrder")
public class CreateOrder extends HttpServlet {

    private String getShipName(int serviceId, int toDistrictId) {
        try {
            JSONArray services = GHNApiUtil.getAvailableServices(toDistrictId);

            for (int i = 0; i < services.length(); i++) {
                JSONObject s = services.getJSONObject(i);

                if (s.getInt("service_id") == serviceId) {

                    int type = s.optInt("service_type_id");

                    if (type == 1) return "STANDARD";
                    if (type == 2) return "EXPRESS";

                    return s.optString("short_name", "UNKNOWN");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "UNKNOWN";
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String mode = request.getParameter("mode");
        String paymentMethod = request.getParameter("payment");


        Cart cart = "buynow".equals(mode)
                ? (Cart) session.getAttribute("buyNowCart")
                : (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            request.setAttribute("error", "Giỏ hàng trống");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }


        String addressIdStr = request.getParameter("addressId");

        if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn địa chỉ giao hàng");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }

        int addressId = Integer.parseInt(addressIdStr);

        AddressService addressService = new AddressService();
        Address address = addressService.getAddressById(addressId);


        String shipTypeStr = request.getParameter("shipType");

        if (shipTypeStr == null || shipTypeStr.isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn phương thức vận chuyển");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }

        int serviceId = Integer.parseInt(shipTypeStr);

        double shipFee = 0;
        String deliveryRange = null;

        int toProvinceId=-1;
        int toDistrictId=-1;
        String toWardCode="";
        try {
             toProvinceId = GHNApiUtil.getProvinceIdByName(address.getCity());
             toDistrictId = GHNApiUtil.getDistrictIdByName(address.getDistricts(), toProvinceId);
             toWardCode = GHNApiUtil.getWardCodeByName(address.getWard(), toDistrictId);

            shipFee = GHNApiUtil.calculateShippingFee(
                    toDistrictId,
                    toWardCode,
                    500,
                    serviceId
            );

            long leadTime = GHNApiUtil.getLeadTime(toDistrictId, toWardCode, serviceId);

            java.util.Date date = new java.util.Date(leadTime * 1000);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

            deliveryRange = sdf.format(date);

        } catch (Exception e) {
            request.setAttribute("error", "Không thể tính phí vận chuyển");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }

        String shipName = getShipName(serviceId, toDistrictId);

        boolean usePoint = "1".equals(request.getParameter("usePoint"));
        int pointUsed = 0;

        if (usePoint) {
            if (user.getPoint() < 100) {
                request.setAttribute("error", "Bạn cần tối thiểu 100 point");
                request.getRequestDispatcher("ThanhToan").forward(request, response);
                return;
            }

            pointUsed = Math.min(user.getPoint(), 1000);
        }

        double cartTotal = 0;
        for (CartItem item : cart.getItems()) {
            cartTotal += item.getPrice() * item.getQuantity();
        }


        Voucher productVoucher = (Voucher) session.getAttribute("appliedDiscountVoucher");
        Voucher shipVoucher = (Voucher) session.getAttribute("appliedShipVoucher");

        int productVoucherId = (productVoucher != null) ? productVoucher.getId() : 0;
        int shipVoucherId = (shipVoucher != null) ? shipVoucher.getId() : 0;


        double productDiscountMoney = (productVoucher != null) ? productVoucher.getValuee() : 0;


        double shipDiscountMoney = (shipVoucher != null) ? shipVoucher.getValuee() : 0;


        double realShipFee = shipFee - shipDiscountMoney;
        if (realShipFee < 0) realShipFee = 0;

        double finalTotal =
                cartTotal
                        + realShipFee
                        - productDiscountMoney
                        - pointUsed;

        if (finalTotal < 0) finalTotal = 0;


        if ("vnpay".equals(paymentMethod)) {
            session.setAttribute("pendingMode", mode);
            session.setAttribute("pendingAddressId", addressId);
            session.setAttribute("pendingShipType", serviceId);
            session.setAttribute("pendingShipName", shipName);
            session.setAttribute("pendingShipFee", realShipFee);
            session.setAttribute("pendingFinalTotal", finalTotal);
            session.setAttribute("pendingNote", request.getParameter("orderNote"));
            session.setAttribute("pendingDeliveryRange", deliveryRange);
            session.setAttribute("pendingPayment", paymentMethod);
            session.setAttribute("pendingProductVoucherId", productVoucherId);
            session.setAttribute("pendingShipVoucherId", shipVoucherId);
            session.setAttribute("pendingUsePoint", usePoint);
            session.setAttribute("pendingPointUsed", pointUsed);

            Token8 token = new Token8();

            String url = VNPayUtils.createPaymentUrl(token.generateToken8(), (long) finalTotal);

            response.sendRedirect(url);
            return;
        }

        OrderService orderService = new OrderService();

        String paymentStatus = "cod".equalsIgnoreCase(paymentMethod) ? "NOPAID" : "PAID";

        boolean ok = orderService.addOrder(
                user.getId(),
                finalTotal,
                request.getParameter("orderNote"),
                paymentMethod,
                paymentStatus,
                productVoucherId,
                shipVoucherId,
                addressId,
                shipName,
                realShipFee,
                deliveryRange,
                cart
        );

        if (!ok) {
            request.setAttribute("error", "Tạo đơn hàng thất bại");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }

        UserService userService = new UserService();

        if (usePoint) {
            user.setPoint(user.getPoint() - pointUsed);
            userService.updateDiem(user.getId(), pointUsed);
        }

        user.setPoint(user.getPoint() + (int) (finalTotal * 0.05));
        session.setAttribute("user", user);

        session.removeAttribute("cart");
        session.removeAttribute("buyNowCart");

        session.removeAttribute("appliedDiscountVoucher");
        session.removeAttribute("appliedShipVoucher");

        response.sendRedirect("my-orders");
    }
}