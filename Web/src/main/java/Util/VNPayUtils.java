package Util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class VNPayUtils {

    public static String createPaymentUrl(String transId, long amount) {
        try {

            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", VNPayConfig.TMN_CODE);

            params.put("vnp_Amount", String.valueOf(amount * 100));
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", transId);

            params.put("vnp_OrderInfo", "Thanh toan don hang"
            );

            params.put("vnp_OrderType", "other");

            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", VNPayConfig.RETURN_URL+ "?fromVNPay=1&payment=vnpay");

            params.put("vnp_IpAddr", "127.0.0.1");

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

            params.put("vnp_CreateDate", formatter.format(cld.getTime()));

            List<String> fieldNames = new ArrayList<>(params.keySet());

            Collections.sort(fieldNames);

            StringBuilder query = new StringBuilder();
            StringBuilder hashData = new StringBuilder();


            for (String name : fieldNames) {
                String value = params.get(name);
                if (query.length() > 0) {
                    query.append("&");
                    hashData.append("&");
                }
                query.append(name).append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
                hashData.append(name).append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            }
            String secureHash = VNPayConfig.hmacSHA512(VNPayConfig.HASH_SECRET, hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);
            return VNPayConfig.URL + "?" + query.toString();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}