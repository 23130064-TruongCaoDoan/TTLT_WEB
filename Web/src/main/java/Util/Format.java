package Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Format {
    public static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
    public static List<String> requestColumns = List.of(
            "Mã sản phẩm", "Tiêu đề", "Giá", "Giá nhập", "Thể loại",
            "Độ tuổi", "Ảnh bìa", "Mô tả", "Nhà xuất bản", "Nhà cung cấp", "Năm xuất bản",
            "Trọng lượng(g)", "Kích thước", "Loại bìa","Ảnh chi tiết", "Tên tác giá", "Ngày sinh tác giả", "Bút danh"
    );
    public static String normalizeString(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

}
