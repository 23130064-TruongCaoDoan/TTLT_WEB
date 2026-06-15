package controler.admin.ImportManage;

import Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
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

import static Util.Format.normalizeString;
import static Util.Format.requestColumns;
import static Util.RolesGroup.IMPORT_ROLE;
import static org.apache.poi.ss.usermodel.TableStyleType.headerRow;

@WebServlet
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
        try{
            Part filePart = request.getPart("fileBooks");
            InputStream inputStream = filePart.getInputStream();

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int count = 0;

            Map<String, Integer> headerMap = new HashMap<>();
            Row headerRow = sheet.getRow(1);

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                headerMap.put(normalizeString(cell.getStringCellValue()), i);
            }
            List<String> missingColumns = new ArrayList<>();

            for (String col : requestColumns) {
                if (!headerMap.containsKey(col)) {
                    missingColumns.add(col);
                }
            }

            if (!missingColumns.isEmpty()) {
                request.setAttribute("errorMessage", "Thiếu cột: " + String.join(", ", missingColumns));
                request.getRequestDispatcher("/product-manage.jsp").forward(request, response);
                return;
            }

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Book book = new Book();
                book.setBookCode(row.getCell(headerMap.get("book code")).getStringCellValue());
            }
        }catch(Exception e){
            response.sendRedirect("login");
        }

    }
}
