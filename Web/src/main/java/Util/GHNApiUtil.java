package Util;

import Service.AddressService;
import model.Address;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class GHNApiUtil {

    private static final String BASE_URL ="https://online-gateway.ghn.vn/shiip/public-api";

    private static String TOKEN;
    private static int SHOP_ID;
    private static int FROM_PROVINCE_ID;
    private static int FROM_DISTRICT_ID;
    private static String FROM_WARD_CODE;

    static {
        try {
            Properties prop = new Properties();
            InputStream input = GHNApiUtil.class.getClassLoader()
                    .getResourceAsStream("GHN.properties");

            InputStreamReader reader = new InputStreamReader(input, "UTF-8");
            prop.load(reader);

            TOKEN = prop.getProperty("ghn.token");
            SHOP_ID = Integer.parseInt(prop.getProperty("ghn.shop.id"));
            FROM_PROVINCE_ID = Integer.parseInt(prop.getProperty("ghn.from.province.id"));
            FROM_DISTRICT_ID = Integer.parseInt(prop.getProperty("ghn.from.district.id"));
            FROM_WARD_CODE = prop.getProperty("ghn.from.ward.code");


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
            if (normalize(province.getString("ProvinceName"))
                    .equals(normalize(provinceName))) {
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
            if (normalize(district.getString("DistrictName"))
                    .equals(normalize(districtName))) {
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

            if (normalize(ward.getString("WardName"))
                    .equals(normalize(wardName))) {
                return ward.getString("WardCode");
            }
        }
        throw new Exception("Ward not found");
    }

    public static int getServiceId(int toDistrictId) throws Exception {

        JSONArray services = getAvailableServices(toDistrictId);

        for (int i = 0; i < services.length(); i++) {
            JSONObject s = services.getJSONObject(i);
            int serviceType = s.optInt("service_type_id");
            if (serviceType == 2) {
                return s.getInt("service_id");
            }
        }

        if (services.length() > 0) {
            return services.getJSONObject(0).getInt("service_id");
        }

        throw new Exception("No service available");
    }

    public static int calculateShippingFee(int toDistrictId,String toWardCode,int weight,int serviceId) throws Exception {

        JSONObject body = new JSONObject();
        body.put("shop_id", SHOP_ID);
        body.put("service_id", serviceId);
        body.put("from_district_id", FROM_DISTRICT_ID);
        body.put("to_district_id", toDistrictId);
        body.put("to_ward_code", toWardCode);
        body.put("height", 10);
        body.put("length", 20);
        body.put("width", 20);
        body.put("weight", weight);

        JSONObject response =sendPostRequest("/v2/shipping-order/fee", body);

        return response.getJSONObject("data").getInt("total");
    }
    public static long getLeadTime(int toDistrictId,String toWardCode,int serviceId) throws Exception {
        JSONObject body = new JSONObject();
        body.put("from_district_id", FROM_DISTRICT_ID);
        body.put("from_ward_code", FROM_WARD_CODE);
        body.put("to_district_id", toDistrictId);
        body.put("to_ward_code", toWardCode);
        body.put("service_id", serviceId);

        JSONObject response =
                sendPostRequest("/v2/shipping-order/leadtime", body);

        return response.getJSONObject("data").getLong("leadtime");
    }

    public static JSONArray getAvailableServices(int toDistrictId) throws Exception {

        JSONObject body = new JSONObject();
        body.put("shop_id", SHOP_ID);
        body.put("from_district", FROM_DISTRICT_ID);
        body.put("to_district", toDistrictId);

        JSONObject response = sendPostRequest("/v2/shipping-order/available-services", body);

        return response.getJSONArray("data");
    }
    public static String normalize(String s) {
        if (s == null) return "";

        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replace("thanh pho", "")
                .replace("tp", "")
                .replace("tinh", "")
                .replace("quan", "")
                .replace("huyen", "")
                .replace("phuong", "")
                .replace("xa", "")
                .replaceAll("\\s+", " ")
                .trim();
    }


    public static void main(String[] args) throws Exception {
//        int provinceId = GHNApiUtil.getProvinceIdByName("Long An");
//
//        int districtId = GHNApiUtil.getDistrictIdByName(
//                "Cần giuộc",
//                provinceId
//        );
//
//        String wardCode = GHNApiUtil.getWardCodeByName(
//                "Mỹ Lộc",
//                districtId
//        );
//
//        System.out.println(
//                GHNApiUtil.getLeadTime(
//                        districtId,
//                        wardCode,
//                        53322
//                )
//        );
//        System.out.println(GHNApiUtil.getProvinceIdByName("thành phố Hồ chí minh"));
//        System.out.println(GHNApiUtil.getDistrictIdByName("thành phố thủ đức",202));
//        System.out.println(GHNApiUtil.getWardCodeByName("phường tăng nhơn phú a",3695));
//        System.out.println(GHNApiUtil.getServiceId(1540));
//
//        System.out.println(getLeadTime(1907,"491307",53321));
//
////        JSONArray services = GHNApiUtil.getAvailableServices(toDistrictId);
////
////        for (int i = 0; i < services.length(); i++) {
////            System.out.println(services.getJSONObject(i).toString(2));
////        }
//
//        System.out.println(GHNApiUtil.calculateShippingFee(
//                1540,
//                "440505",
//                10000000,
//                53321
//        ));
//
//        AddressService addressService = new AddressService();
//        Address address = addressService.getAddressById(16);
//        System.out.println(address.toString());
//        System.out.println(address.getCity());
//        System.out.println(address.getDistricts());
//        System.out.println(address.getWard());
//
//        int toProvinceId = GHNApiUtil.getProvinceIdByName("tỉnh long an");
//        int toDistrictId = GHNApiUtil.getDistrictIdByName("huyện cần giuộc",toProvinceId);
//        String toWardCode = GHNApiUtil.getWardCodeByName("xã mỹ lộc", toDistrictId);
//
//
//        System.out.println(toProvinceId);
//        System.out.println(toDistrictId);
//        System.out.println(toWardCode);
//
//        System.out.println(calculateShippingFee(toDistrictId,toWardCode,300,53321));
    }
}