package controler.user.ThanhToan;

import Cart.Cart;
import Service.AddressService;
import Util.GHNApiUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "calculateShipping", value = "/calculateShipping")
public class CalculateShippingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            int addressId = Integer.parseInt(request.getParameter("addressId"));
            AddressService addressService = new AddressService();
            Address address = addressService.getAddressById(addressId);

            if (address == null) {
                response.getWriter().write("{\"error\":\"Address not found\"}");
                return;
            }

            int toProvinceId = GHNApiUtil.getProvinceIdByName(address.getCity());
            int toDistrictId = GHNApiUtil.getDistrictIdByName(address.getDistricts(), toProvinceId);
            String toWardCode = GHNApiUtil.getWardCodeByName(address.getWard(), toDistrictId);

            JSONArray services = GHNApiUtil.getAvailableServices(toDistrictId);

            String mode = request.getParameter("mode");

            Cart cart;
            if ("buynow".equals(mode)) {
                cart = (Cart) request.getSession().getAttribute("buyNowCart");
            } else {
                cart = (Cart) request.getSession().getAttribute("cart");
            }


            JSONArray resultServices = new JSONArray();

            int weight = cart.getTotalWeight();

            for (int i = 0; i < services.length(); i++) {

                JSONObject service = services.getJSONObject(i);
                int serviceId = service.getInt("service_id");

                try {
                    int fee = GHNApiUtil.calculateShippingFee(toDistrictId,toWardCode,weight,serviceId);
                    long leadtime = GHNApiUtil.getLeadTime(toDistrictId,toWardCode,serviceId);
                    JSONObject item = new JSONObject();
                    item.put("service_id", serviceId);
                    item.put("fee", fee);
                    item.put("leadtime", leadtime);
                    int serviceTypeId = service.optInt("service_type_id", 2);
                    if (serviceTypeId == 2) {
                        item.put("service_name", "Nhanh");
                        item.put("type", "EXPRESS");
                    } else {
                        item.put("service_name", "Tiêu chuẩn");
                        item.put("type", "STANDARD");
                    }

                    resultServices.put(item);

                } catch (Exception e) {
                    System.out.println("SKIP serviceId = " + serviceId + " | reason: " + e.getMessage());
                }
            }
            JSONObject result = new JSONObject();
            result.put("services", resultServices);

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"Shipping error\"}");
        }
    }

}