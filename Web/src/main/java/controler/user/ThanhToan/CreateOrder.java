package controler.user.ThanhToan;

import Service.CartSerive;
import cart.Cart;
import cart.CartItem;
import Service.AddressService;
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

        boolean isEmpty = isDbCart
                ? (cartDb == null || cartDb.getItems().isEmpty())
                : (cartGuest == null || cartGuest.getItems().isEmpty());
        if (isEmpty) {
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

             if(isDbCart) {
                 shipFee = GHNApiUtil.calculateShippingFee(
                         toDistrictId,
                         toWardCode,
                         cartDb.getTotalWeight(),
                         serviceId
                 );
             }
             else{
                shipFee = GHNApiUtil.calculateShippingFee(
                        toDistrictId,
                        toWardCode,
                        cartGuest.getTotalWeight(),
                        serviceId
                );
            }

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
            if(isDbCart) {
                pointUsed = (int) Math.min(user.getPoint(), cartDb.getTotalBill());
            }
            else{
                pointUsed = (int) Math.min(user.getPoint(), cartGuest.getTotalBill());
            }
        }

        double cartTotal = 0;
        if(isDbCart) {
            for (model.CartItem item : cartDb.getItems()) {
                cartTotal += item.getPrice() * item.getQuantity();
            }
        }
        else{
            for (CartItem item : cartGuest.getItems()) {
                cartTotal += item.getPrice() * item.getQuantity();
            }
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

        boolean ok;
        if(isDbCart) {
            ok = orderService.addOrderdb(
                    user.getId(),
                    finalTotal,
                    request.getParameter("orderNote"),
                    paymentMethod,
                    paymentStatus,
                    productVoucherId,
                    shipVoucherId,
                    address,
                    shipName,
                    realShipFee,
                    deliveryRange,
                    cartDb
            );
        }
        else{
            ok = orderService.addOrder(
                    user.getId(),
                    finalTotal,
                    request.getParameter("orderNote"),
                    paymentMethod,
                    paymentStatus,
                    productVoucherId,
                    shipVoucherId,
                    address,
                    shipName,
                    realShipFee,
                    deliveryRange,
                    cartGuest
            );
        }


        if (!ok) {
            request.setAttribute("error", "Tạo đơn hàng thất bại");
            request.getRequestDispatcher("ThanhToan").forward(request, response);
            return;
        }

        UserService userService = new UserService();
        int currentPoint = user.getPoint();
        if (usePoint) {
            currentPoint -= pointUsed;
        }
        int rewardPoint = (int) (finalTotal * 0.05);
        currentPoint += rewardPoint;
        user.setPoint(currentPoint);
        session.setAttribute("user", user);
        userService.updateDiem(user.getId(), currentPoint);

        if(mode.isEmpty() ) {
            session.removeAttribute("cart");
        }
        session.removeAttribute("buyNowCart");
        session.removeAttribute("rebuyCart");


        session.removeAttribute("appliedDiscountVoucher");
        session.removeAttribute("appliedShipVoucher");
        session.setAttribute("numApplyVoucher", 0);
        response.sendRedirect("my-orders");
    }
}