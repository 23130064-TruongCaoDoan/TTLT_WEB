package Util;

import java.util.Map;

public class CheckNullParams {
    public static String getString(Map<String, String[]> params, String key, String defaultValue) {

        String[] arr = params.get(key);
        if (arr == null || arr.length == 0) {
            return defaultValue;
        }
        String value = arr[0];
        return value == null ? defaultValue : value.trim();
    }

    public static int getInt(Map<String, String[]> params, String key, int defaultValue) {

        String value = getString(params, key, "");
        if (value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double getDouble(Map<String, String[]> params, String key, double defaultValue) {
        String value = getString(params, key, "");
        if (value.isBlank()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
