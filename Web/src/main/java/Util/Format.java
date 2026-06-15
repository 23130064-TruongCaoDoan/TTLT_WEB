package Util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public static String getCellString(Row row, Map<String, Integer> headerMap, String col) {
        Cell cell = row.getCell(headerMap.get(col));
        if (cell == null) return "";

        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
    public static int getCellInt(Row row, Map<String, Integer> headerMap, String col) {
        Cell cell = row.getCell(headerMap.get(col));
        if (cell == null) return 0;

        return (int) cell.getNumericCellValue();
    }
    public static double getCellDouble(Row row, Map<String, Integer> headerMap, String col) {
        Cell cell = row.getCell(headerMap.get(col));
        if (cell == null) return 0;

        return cell.getNumericCellValue();
    }


}
