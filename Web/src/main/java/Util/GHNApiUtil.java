package Util;

import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class GHNApiUtil {

    private static final String BASE_URL =
            "https://online-gateway.ghn.vn/shiip/public-api";

    private static String TOKEN;
    private static int SHOP_ID;

    static {
        try {
            Properties prop = new Properties();
            InputStream input = GHNApiUtil.class
                    .getClassLoader()
                    .getResourceAsStream("GHN.properties");

            prop.load(input);

            TOKEN = prop.getProperty("ghn.token");
            SHOP_ID = Integer.parseInt(prop.getProperty("ghn.shop.id"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject sendPostRequest(String endpoint, JSONObject body) throws Exception {

        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Token", TOKEN);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes("UTF-8"));
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder responseStr = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            responseStr.append(line);
        }

        br.close();
        conn.disconnect();

        return new JSONObject(responseStr.toString());
    }

    public static int getProvinceIdByName(String provinceName) throws Exception {

        JSONObject body = new JSONObject();
        JSONObject response =sendPostRequest("/master-data/province", body);

        JSONArray provinces = response.getJSONArray("data");

        for (int i = 0; i < provinces.length(); i++) {
            JSONObject province = provinces.getJSONObject(i);
            if (province.getString("ProvinceName").equalsIgnoreCase(provinceName)) {
                return province.getInt("ProvinceID");
            }
        }

        throw new Exception("Province not found");
    }

    public static int getDistrictIdByName(String districtName, int provinceId) throws Exception {
        JSONObject body = new JSONObject();
        body.put("province_id", provinceId);

        JSONObject response =sendPostRequest("/master-data/district", body);
        JSONArray districts = response.getJSONArray("data");

        for (int i = 0; i < districts.length(); i++) {
            JSONObject district = districts.getJSONObject(i);
            if (district.getString("DistrictName").equalsIgnoreCase(districtName)) {
                return district.getInt("DistrictID");
            }
        }
        throw new Exception("District not found");
    }

    public static String getWardCodeByName(String wardName, int districtId) throws Exception {
        JSONObject body = new JSONObject();
        body.put("district_id", districtId);
        JSONObject response =sendPostRequest("/master-data/ward", body);

        JSONArray wards = response.getJSONArray("data");

        for (int i = 0; i < wards.length(); i++) {
            JSONObject ward = wards.getJSONObject(i);

            if (ward.getString("WardName").equalsIgnoreCase(wardName)) {
                return ward.getString("WardCode");
            }
        }
        throw new Exception("Ward not found");
    }

    public static int getServiceId(int toDistrictId) throws Exception {

        JSONObject body = new JSONObject();
        body.put("shop_id", SHOP_ID);
        body.put("to_district", toDistrictId);
        JSONObject response =sendPostRequest("/v2/shipping-order/available-services", body);
        JSONArray services = response.getJSONArray("data");
        if (services.length() == 0) {
            throw new Exception("No service available");
        }
        return services.getJSONObject(0).getInt("service_id");
    }

    public static int calculateShippingFee(int toDistrictId,String toWardCode,int weight,int serviceId) throws Exception {

        JSONObject body = new JSONObject();
        body.put("shop_id", SHOP_ID);
        body.put("service_id", serviceId);
        body.put("to_district_id", toDistrictId);
        body.put("to_ward_code", toWardCode);
        body.put("height", 10);
        body.put("length", 20);
        body.put("width", 20);
        body.put("weight", weight);

        JSONObject response =
                sendPostRequest("/v2/shipping-order/fee", body);

        return response.getJSONObject("data").getInt("total");
    }
}