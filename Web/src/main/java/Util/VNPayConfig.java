package Util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Properties;

public class VNPayConfig {
    public static String TMN_CODE;
    public static String HASH_SECRET;
    public static String URL;

    public static String RETURN_URL = "http://localhost:8080/Web_war_exploded/vnpayPayment?fromVNPay=1&payment=vnpay";

        static {
            try {
                Properties prop = new Properties();

                prop.load(VNPayConfig.class.getClassLoader().getResourceAsStream("vnpay.properties"));
                TMN_CODE = prop.getProperty("vnpay.tmnCode");
                HASH_SECRET = prop.getProperty("vnpay.hashSecret");
                URL = prop.getProperty("vnpay.url");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public static String hmacSHA512(String key, String data) {

        try {

            Mac mac = Mac.getInstance("HmacSHA512");

            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            mac.init(secretKey);

            byte[] hash = mac.doFinal(data.getBytes());

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            return "";
        }

}
}
