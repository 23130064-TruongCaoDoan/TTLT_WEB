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
            "Mã sản phẩm", "Tiêu đề", "Giá", "Giá nhập", "Số lượng", "Thể loại",
            "Độ tuổi", "Ảnh bìa", "Mô tả", "Nhà xuất bản", "Nhà cung cấp", "Năm xuất bản",
            "Trọng lượng(g)", "Kích thước", "Loại bìa","Ảnh chi tiết", "Tên tác giả", "Ngày sinh tác giả", "Bút danh"
    );
    public static String normalizeString(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
    public static String getCellString(Row row, Map<String, Integer> headerMap, String col) {
        Cell cell = row.getCell(headerMap.get(normalizeString(col)));
        if (cell == null) return "";

        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
    public static int getCellInt(Row row, Map<String, Integer> headerMap, String col) {
        Cell cell = row.getCell(headerMap.get(normalizeString(col)));
        if (cell == null) return 0;

        return (int) cell.getNumericCellValue();
    }
    public static double getCellDouble(Row row, Map<String, Integer> headerMap, String col) {
        Cell cell = row.getCell(headerMap.get(normalizeString(col)));
        if (cell == null) return 0;

        return cell.getNumericCellValue();
    }

    public static List<String> validateBookRow(Row row, Map<String, Integer> headerMap) {

        List<String> errors = new ArrayList<>();

        String bookCode = getCellString(row, headerMap, "mã sản phẩm");
        if (bookCode == null || bookCode.isBlank()) {
            errors.add("Mã sản phẩm bị trống");
        }

        String title = getCellString(row, headerMap, "tiêu đề");
        if (title == null || title.isBlank()) {
            errors.add("Tiêu đề bị trống");
        }

        Integer price = getCellInt(row, headerMap, "giá");
        if (price == null || price <= 0) {
            errors.add("Giá phải > 0");
        }

        Integer priceImport = getCellInt(row, headerMap, "giá nhập");
        if (priceImport == null || priceImport <= 0) {
            errors.add("Giá nhập phải > 0");
        }
        Integer quantity = getCellInt(row, headerMap, "số lượng");
        if (quantity == null || priceImport <= 0) {
            errors.add("Số lượng phải > 0");
        }

        String type = getCellString(row, headerMap, "thể loại");
        if (type == null || type.isBlank()) {
            errors.add("Thể loại bị trống");
        }

        Integer age = getCellInt(row, headerMap, "độ tuổi");
        if (age == null || age < 0) {
            errors.add("Độ tuổi không hợp lệ");
        }

        String description = getCellString(row, headerMap, "mô tả");
        if (description == null || description.isBlank()) {
            errors.add("Mô tả bị trống");
        }

        String publisher = getCellString(row, headerMap, "nhà xuất bản");
        if (publisher == null || publisher.isBlank()) {
            errors.add("Nhà xuất bản bị trống");
        }

        String provider = getCellString(row, headerMap, "nhà cung cấp");
        if (provider == null || provider.isBlank()) {
            errors.add("Nhà cung cấp bị trống");
        }

        Integer publishedDate = getCellInt(row, headerMap, "năm xuất bản");
        if (publishedDate == null || publishedDate < 1990) {
            errors.add("Năm xuất bản không hợp lệ");
        }

        Double weight = getCellDouble(row, headerMap, "trọng lượng(g)");
        if (weight == null || weight <= 0) {
            errors.add("Trọng lượng phải > 0");
        }

        String size = getCellString(row, headerMap, "kích thước");
        if (size == null || size.isBlank()) {
            errors.add("Kích thước bị trống");
        }

        String format = getCellString(row, headerMap, "loại bìa");
        if (format == null || format.isBlank()) {
            errors.add("Loại bìa bị trống");
        }

        String coverImg = getCellString(row, headerMap, "ảnh bìa");
        if (coverImg == null || coverImg.isBlank()) {
            errors.add("Ảnh bìa bị trống");
        }
        String authorName = getCellString(row, headerMap, "tên tác giả");
        if (authorName == null || authorName.isBlank()) {
            errors.add("Tên tác giả bị trống");
        }

        String birthday = getCellString(row, headerMap, "ngày sinh tác giả");
        if (birthday == null || birthday.isBlank()) {
            errors.add("Ngày sinh tác giả bị trống");
        }

        return errors;
    }


}
