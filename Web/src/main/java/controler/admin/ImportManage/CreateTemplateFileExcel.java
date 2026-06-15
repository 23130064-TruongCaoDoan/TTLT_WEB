package controler.admin.ImportManage;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

import static Util.Format.requestColumns;

@WebServlet("/downloadTemplateFileExcel")
public class CreateTemplateFileExcel extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("DanhSachNhap");
            Row note = sheet.createRow(0);
            String noteText = "Tất cả các cột đều bắt buộc phải có dữ liệu" +
                            "\nCột bút danh mặc định là tên tác giả nếu như tác giả không có"+
                            "\nĐối với ảnh bìa và ảnh chi tiết cần phải nhập ten.jpg tương ứng với tên trong file zip"+
                            "\nTất cả ảnh bắt buộc ngăn cách bằng dấu , ";
            note.createCell(0).setCellValue(noteText);
            CellStyle noteStyle = workbook.createCellStyle();
            noteStyle.setWrapText(true);
            note.getCell(0).setCellStyle(noteStyle);

            int lineCount = noteText.split("\n").length;
            note.setHeightInPoints(lineCount * 18);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,requestColumns.size()-1));

            Row header = sheet.createRow(note.getRowNum()+1);
            for (int i = 0; i < requestColumns.size(); i++) {
                header.createCell(i).setCellValue(requestColumns.get(i));
            }

            Row sample = sheet.createRow(header.getRowNum()+1);

            sample.createCell(0).setCellValue("301239127001");
            sample.createCell(1).setCellValue("Lập trình Java cơ bản");
            sample.createCell(2).setCellValue(120000);
            sample.createCell(3).setCellValue(90000);
            sample.createCell(4).setCellValue("Công nghệ thông tin");
            sample.createCell(5).setCellValue(15);
            sample.createCell(6).setCellValue("anhcover.jpg");
            sample.createCell(7).setCellValue("Sách dành cho người mới bắt đầu học Java.");
            sample.createCell(8).setCellValue("NXB Trẻ");
            sample.createCell(9).setCellValue("Fahasa");
            sample.createCell(10).setCellValue(2025);
            sample.createCell(11).setCellValue(500);
            sample.createCell(12).setCellValue("24 x 16 cm");
            sample.createCell(13).setCellValue("Bìa mềm");
            sample.createCell(14).setCellValue("anhchitiet1.jpg,anhchitiet2.jpg,anhchitiet3.jpg");
            sample.createCell(15).setCellValue("Tên tác giả");
            sample.createCell(16).setCellValue("Ngày sinh của tác giả");
            sample.createCell(17).setCellValue("Bút danh(nếu có)");

            for (int i = 0; i < requestColumns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            response.setHeader("Content-Disposition", "attachment; filename=template_nhap_sach.xlsx");

            workbook.write(response.getOutputStream());
        }
    }
}
