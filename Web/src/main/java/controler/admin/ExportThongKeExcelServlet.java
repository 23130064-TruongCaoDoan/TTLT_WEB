package controler.admin;

import DTO.BookWithSoldDTO;
import DTO.UserWithTotalSpentDTO;
import Service.ThongKeService;
import Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

import static Util.RolesGroup.STATISTICAL_ROLE;

@WebServlet(name = "ExportThongKeExcel", value = "/ExportThongKeExcel")
public class ExportThongKeExcelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        if (!STATISTICAL_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }

        ThongKeService thongKeService = new ThongKeService();
        String type = request.getParameter("type");

        double totalRevenue = 0;
        double totalProfit = 0;
        int totalOrders = 0;
        int totalSoldProducts = 0;
        int totalCancelledOrders = 0;
        List<UserWithTotalSpentDTO> getTop10Users = null;
        List<BookWithSoldDTO> getTop10Books = null;
        List<DTO.RevenueDTO> getTotalRevenueChart = null;
        List<DTO.OrderDTOChart> getTotalOrderChart = null;

        String periodStr = "";

        if ("year".equals(type)) {
            String year = request.getParameter("year");
            periodStr = "Năm " + year;
            totalRevenue = thongKeService.getTotalRevenue(year);
            totalProfit = thongKeService.getProfit(year);
            totalOrders = thongKeService.getTotalOrders(year);
            totalSoldProducts = thongKeService.getTotalSoldProducts(year);
            totalCancelledOrders = thongKeService.getTotalCanceledOrders(year);
            getTop10Users = thongKeService.getTop10Users(year);
            getTop10Books = thongKeService.getTop10Books(year);
            getTotalRevenueChart = thongKeService.getRevenueChart(year);
            getTotalOrderChart = thongKeService.getOrderChart(year);
        } else {
            String fromStr = request.getParameter("fromDate");
            String toStr = request.getParameter("toDate");
            LocalDate from;
            LocalDate to;
            if (fromStr == null || toStr == null) {
                from = LocalDate.now();
                to = LocalDate.now().plusDays(1);
            } else {
                from = LocalDate.parse(fromStr);
                to = LocalDate.parse(toStr);
            }
            if (to.isBefore(from)) {
                LocalDate temp = to;
                to = from;
                from = temp;
            }
            periodStr = "Từ " + from.toString() + " đến " + to.toString();

            totalRevenue = thongKeService.getTotalRevenue(from, to);
            totalProfit = thongKeService.getProfit(from, to);
            totalOrders = thongKeService.getTotalOrders(from, to);
            totalSoldProducts = thongKeService.getTotalSoldProducts(from, to);
            totalCancelledOrders = thongKeService.getTotalCanceledOrders(from, to);
            getTop10Users = thongKeService.getTop10Users(from, to);
            getTop10Books = thongKeService.getTop10Books(from, to);
            getTotalRevenueChart = thongKeService.getRevenueChart(from, to);
            getTotalOrderChart = thongKeService.getOrderChart(from, to);
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Thống kê");

            int rownum = 0;
            Row row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Báo cáo thống kê");
            row.createCell(1).setCellValue(periodStr);

            rownum++;
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Tổng doanh thu:");
            row.createCell(1).setCellValue(totalRevenue);

            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Tổng lợi nhuận:");
            row.createCell(1).setCellValue(totalProfit);

            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Tổng đơn hàng:");
            row.createCell(1).setCellValue(totalOrders);

            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Tổng sản phẩm bán ra:");
            row.createCell(1).setCellValue(totalSoldProducts);

            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Số đơn bị hủy:");
            row.createCell(1).setCellValue(totalCancelledOrders);

            rownum++;
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Chi tiết theo thời gian");

            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Thời gian");
            row.createCell(1).setCellValue("Doanh thu");
            row.createCell(2).setCellValue("Lợi nhuận");
            row.createCell(3).setCellValue("Số lượng đơn hàng");

            if (getTotalRevenueChart != null) {
                for (DTO.RevenueDTO r : getTotalRevenueChart) {
                    row = sheet.createRow(rownum++);
                    row.createCell(0).setCellValue(r.getLabel());
                    row.createCell(1).setCellValue(r.getRevenue());
                    row.createCell(2).setCellValue(r.getNetProfit());

                    int ordersCount = 0;
                    if (getTotalOrderChart != null) {
                        for (DTO.OrderDTOChart o : getTotalOrderChart) {
                            if (o.getLabel() != null && o.getLabel().equals(r.getLabel())) {
                                ordersCount = o.getTotal();
                                break;
                            }
                        }
                    }
                    row.createCell(3).setCellValue(ordersCount);
                }
            }

            rownum++;
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Top 10 Khách Hàng");

            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Mã KH");
            row.createCell(1).setCellValue("Tên KH");
            row.createCell(2).setCellValue("Email");
            row.createCell(3).setCellValue("Điểm");
            row.createCell(4).setCellValue("Tổng tiền");

            if (getTop10Users != null) {
                for (UserWithTotalSpentDTO u : getTop10Users) {
                    row = sheet.createRow(rownum++);
                    row.createCell(0).setCellValue(u.getCustomerCode());
                    row.createCell(1).setCellValue(u.getName());
                    row.createCell(2).setCellValue(u.getEmail());
                    row.createCell(3).setCellValue(u.getPoint());
                    row.createCell(4).setCellValue(u.getTotalSpent());
                }
            }

            rownum++;
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Top 10 Sản Phẩm Bán Chạy");

            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue("Mã sách");
            row.createCell(1).setCellValue("Tên sách");
            row.createCell(2).setCellValue("Giá");
            row.createCell(3).setCellValue("Số lượng");
            row.createCell(4).setCellValue("Loại sách");
            row.createCell(5).setCellValue("Độ tuổi");

            if (getTop10Books != null) {
                for (BookWithSoldDTO b : getTop10Books) {
                    row = sheet.createRow(rownum++);
                    row.createCell(0).setCellValue(b.getBookCode());
                    row.createCell(1).setCellValue(b.getTitle());
                    row.createCell(2).setCellValue(b.getPrice());
                    row.createCell(3).setCellValue(b.getTotalSold());
                    row.createCell(4).setCellValue(b.getType());
                    row.createCell(5).setCellValue(b.getAge());
                }
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=thong_ke.xlsx");

            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
