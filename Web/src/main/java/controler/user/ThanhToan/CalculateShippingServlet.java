package controler.user.ThanhToan;

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

        JSONObject result = null;
        try {

            int addressId = Integer.parseInt(request.getParameter("addressId"));

            AddressService addressService = new AddressService();
            Address address = addressService.getAddressById(addressId);

            if (address == null) {
                response.getWriter().write("{\"error\":\"Address not found\"}");
                return;
            }


            int toProvinceId = GHNApiUtil.getProvinceIdByName(address.getCity());
            int toDistrictId = GHNApiUtil.getDistrictIdByName(
                    address.getDistricts(), toProvinceId);
            String toWardCode = GHNApiUtil.getWardCodeByName(
                    address.getWard(), toDistrictId);


            JSONArray services = GHNApiUtil.getAvailableServices(toDistrictId);

            JSONArray resultServices = new JSONArray();

            for (int i = 0; i < services.length(); i++) {

                JSONObject service = services.getJSONObject(i);

                int serviceId = service.getInt("service_id");
                String serviceName = service.getString("short_name");

//                int fee = GHNApiUtil.calculateShippingFee(
//                        toDistrictId,
//                        toWardCode,
//                        1000,
//                        serviceId
//                );

                long leadtime = GHNApiUtil.getLeadTime(
                        toDistrictId,
                        toWardCode,
                        serviceId
                );

                JSONObject item = new JSONObject();
                item.put("service_id", serviceId);
                item.put("service_name", serviceName);
//                item.put("fee", fee);
                item.put("leadtime", leadtime);

                resultServices.put(item);
            }

            result = new JSONObject();
            result.put("services", resultServices);
//            System.out.println("Province: " + toProvinceId);
//            System.out.println("District: " + toDistrictId);
//            System.out.println("Ward: " + toWardCode);
//            System.out.println("Services: " + services.toString());

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"Shipping error\"}");
        }
    }
}