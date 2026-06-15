package controler.admin.ImportManage;

import Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Author;
import model.Book;
import model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Util.Format.*;
import static Util.RolesGroup.IMPORT_ROLE;
import static org.apache.poi.ss.usermodel.TableStyleType.headerRow;
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
@WebServlet(name="UploadFileExcel", value="/UploadFileExcel")
public class ImportFileBook extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        UserService userService = new UserService();
        int role = userService.checkRole(user);
        if (!IMPORT_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }
        List<Book> listBooks = new ArrayList<>();
        List<List<String>> allDetailImages = new ArrayList<>();
        try{
            Part filePart = request.getPart("fileBooks");
            InputStream inputStream = filePart.getInputStream();

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;

            Map<String, Integer> headerMap = new HashMap<>();
            Row headerRow = sheet.getRow(1);

            if (headerRow == null) {
                request.setAttribute("errorMessage", "File Excel không có header");
                request.getRequestDispatcher(request.getContextPath()+"/product-manage.jsp").forward(request, response);
                return;
            }

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) continue;

                String cellValue;
                switch (cell.getCellType()) {
                    case STRING:
                        cellValue = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        cellValue = String.valueOf((long) cell.getNumericCellValue());
                        break;
                    case BLANK:
                        continue;
                    default:
                        cellValue = cell.toString();
                }
                headerMap.put(normalizeString(cellValue), i);
            }

            List<String> missingColumns = new ArrayList<>();

            for (String col : requestColumns) {
                if (!headerMap.containsKey(normalizeString(col))) {
                    missingColumns.add(col);
                }
                if (!missingColumns.isEmpty()) {
                    System.out.println("Thiếu cột: " + String.join(", ", missingColumns));
                    request.getRequestDispatcher("admin/ManageProduct.jsp").forward(request, response);
                    return;
                }
            }

            List<String> errors = new ArrayList<>();
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                errors = validateBookRow(row, headerMap);
                if (!errors.isEmpty()) {
                    System.out.println("errorMessage"+ String.join(", ", errors));
                    request.getRequestDispatcher("admin/ManageProduct.jsp").forward(request, response);
                    return;
                }
            }


            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Book book = new Book();
                book.setBookCode(getCellString(row, headerMap, "mã sản phẩm"));
                book.setTitle(getCellString(row, headerMap, "tiêu đề"));
                book.setPrice(getCellInt(row, headerMap, "giá"));
                book.setPriceImport(getCellInt(row, headerMap, "giá nhập"));

                book.setType(getCellString(row, headerMap, "thể loại"));
                book.setAge(getCellInt(row, headerMap, "độ tuổi"));
                book.setDescription(getCellString(row, headerMap, "mô tả"));

                book.setPublisher(getCellString(row, headerMap, "nhà xuất bản"));
                book.setProvider(getCellString(row, headerMap, "nhà cung cấp"));
                book.setPublishedDate(getCellInt(row, headerMap, "năm xuất bản"));

                book.setWeight(getCellDouble(row, headerMap, "trọng lượng(g)"));
                book.setBookSize(getCellString(row, headerMap, "kích thước"));
                book.setFormat(getCellString(row, headerMap, "loại bìa"));
                System.out.println(book);

                book.setCoverImgUrl(getCellString(row, headerMap, "ảnh bìa"));
                listBooks.add(book);
                String[] splitImage = getCellString(row, headerMap, "ảnh chi tiết").split(",");
                List<String> imageDetail = new ArrayList<>();


                Author author = new Author();
                author.setName(getCellString(row, headerMap, "tên tác giả"));
                author.setBirthday(getCellString(row, headerMap, "ngày sinh tác giả"));
                author.setPenName(getCellString(row, headerMap, "bút danh"));

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
