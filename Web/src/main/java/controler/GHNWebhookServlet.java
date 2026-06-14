package controler;

import dao.ShippingDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/webhook/ghn")
public class GHNWebhookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonPayload = sb.toString();

        try {
            String trackingCode = extractValueFromJson(jsonPayload, "OrderCode");
            String status = extractValueFromJson(jsonPayload, "Status");

            if (trackingCode != null && status != null) {
                String myStatus = mapGHNStatus(status);
                ShippingDao shippingDao = new ShippingDao();
                shippingDao.updateShippingStatusByTrackingCode(trackingCode, myStatus);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"code\": 200, \"message\": \"Success\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String extractValueFromJson(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex != -1) {
            startIndex += searchKey.length();
            int endIndex = json.indexOf("\"", startIndex);
            return json.substring(startIndex, endIndex);
        }
        return null;
    }

    private String mapGHNStatus(String ghnStatus) {
        switch (ghnStatus) {
            case "ready_to_pick":
                return "PENDING";
            case "picking":
                return "PROCESSING";
            case "delivering":
                return "SHIPPING";
            case "delivered":
                return "COMPLETED";
            case "returned":
                return "REFUNDED";
            case "cancel":
                return "CANCELLED";
            default:
                return "PROCESSING";
        }
    }
}
