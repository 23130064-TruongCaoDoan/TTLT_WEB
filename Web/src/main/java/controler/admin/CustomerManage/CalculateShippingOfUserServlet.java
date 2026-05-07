package controler.admin.CustomerManage;

import Cart.Cart;
import Util.GHNApiUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "calculateShippingFromForm", value = "/calculateShippingFromForm")
public class CalculateShippingOfUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            String province = request.getParameter("province");
            String district = request.getParameter("district");
            String ward = request.getParameter("ward");

            if (province == null || district == null || ward == null) {
                response.getWriter().write("{\"error\":\"Missing address info\"}");
                return;
            }

            int toProvinceId = GHNApiUtil.getProvinceIdByName(province);
            int toDistrictId = GHNApiUtil.getDistrictIdByName(district, toProvinceId);
            String toWardCode = GHNApiUtil.getWardCodeByName(ward, toDistrictId);

            JSONArray services = GHNApiUtil.getAvailableServices(toDistrictId);
            int weight = 300;


            JSONArray resultServices = new JSONArray();

            for (int i = 0; i < services.length(); i++) {

                JSONObject service = services.getJSONObject(i);
                int serviceId = service.getInt("service_id");

                try {
                    int fee = GHNApiUtil.calculateShippingFee(
                            toDistrictId,
                            toWardCode,
                            weight,
                            serviceId
                    );

                    long leadtime = GHNApiUtil.getLeadTime(
                            toDistrictId,
                            toWardCode,
                            serviceId
                    );

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
                    System.out.println("SKIP serviceId = " + serviceId + " | " + e.getMessage());
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