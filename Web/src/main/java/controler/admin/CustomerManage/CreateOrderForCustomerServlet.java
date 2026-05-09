package controler.admin.CustomerManage;

import Cart.CartItem;
import Service.BookService;
import Service.OrderService;
import Util.GHNApiUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreateOrderForCustomerServlet", value = "/CreateOrderForCustomerServlet")
public class CreateOrderForCustomerServlet extends HttpServlet {
    BookService bookService = new BookService();
    OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        String userId=request.getParameter("userId");

        Integer uid = Integer.parseInt(userId);
        String receiverName = request.getParameter("receiverName");
        String receiverPhone = request.getParameter("receiverPhone");
        String province = request.getParameter("province");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String specificAddress =request.getParameter("specificAddress");
        String service_id = request.getParameter("shippingServiceId");



        String productString = request.getParameter("products");

        if (receiverName == null || receiverName.isEmpty()
                || receiverPhone == null || receiverPhone.isEmpty()
                || province == null || province.isEmpty()
                || district == null || district.isEmpty()
                || ward == null || ward.isEmpty()
                || productString == null || productString.isEmpty()
                ||specificAddress == null || specificAddress.isEmpty()
                || service_id == null || service_id.isEmpty()
                ) {

            response.getWriter().write("{\"success\":false,\"message\": \"thiếu thông tin bắt buộc\"}");
            return;
        }

        if (receiverName == null || receiverName.trim().isEmpty()
                || !receiverName.matches("^[\\p{L} ]{2,50}$")) {

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Tên không hợp lệ\"}"
            );
            return;
        }
        if (receiverPhone == null || !receiverPhone.matches("^(03|05|07|08|09)[0-9]{8}$")) {

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Số điện thoại không hợp lệ\"}"
            );
            return;
        }

        Map<Integer, Integer> productMap = new HashMap<>();

        if (productString != null && !productString.isEmpty()) {
            String[] items = productString.split(",");

            for (String item : items) {
                String[] parts = item.split(":");

                int id = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                productMap.put(id, quantity);
            }
        }


        int serviceId = Integer.parseInt(service_id);

        double shipFee = 0;
        String deliveryRange = null;

        int toProvinceId = -1;
        int toDistrictId = -1;
        String toWardCode = "";
        try {
            toProvinceId = GHNApiUtil.getProvinceIdByName(province);
            toDistrictId = GHNApiUtil.getDistrictIdByName(district, toProvinceId);
            toWardCode = GHNApiUtil.getWardCodeByName(ward, toDistrictId);

            shipFee = GHNApiUtil.calculateShippingFee(
                    toDistrictId,
                    toWardCode,
                    3000,
                    serviceId
            );

            long leadTime = GHNApiUtil.getLeadTime(toDistrictId, toWardCode, serviceId);

            java.util.Date date = new java.util.Date(leadTime * 1000);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

            deliveryRange = sdf.format(date);

        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\": \"Lỗi tính phí vận chuyển\"}");
            return;
        }

        String shipName = getShipName(serviceId, toDistrictId);

        double cartTotal = 0;
        for (Map.Entry<Integer, Integer> entry : productMap.entrySet()) {
            Integer id = entry.getKey();
            Integer quantity = entry.getValue();
            Book book = bookService.getBooksById(id);

            if (book.getPriceDiscounted() > 0) {
                cartTotal += book.getPriceDiscounted() * quantity;
            } else {
                cartTotal += book.getPrice() * quantity;
            }

        }
        double finalTotal =cartTotal+shipFee;

        boolean ok= orderService.addOrder(
                uid,
                finalTotal,
                "NOPAID",
                receiverName,
                receiverPhone,
                province,
                district,
                ward,
                specificAddress,
                shipName,
                shipFee,
                deliveryRange,
                productMap
        );
        if (ok) {
            response.getWriter().write("{\"success\":true,\"message\": \"Tạo đơn thành công\"}");
            return;
        }
        else{
            response.getWriter().write("{\"success\":false,\"message\": \"thất bại\"}");
            return;
        }





    }
}