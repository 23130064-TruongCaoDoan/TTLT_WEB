package controler.admin.ProductManage;

import Service.HistoryImportService;
import dao.HistoryImportDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ImportOrderDetails;

import java.io.IOException;
import java.util.List;

@WebServlet(name="OrderImportDetail", value = "/OrderImportDetail")
public class OrderImportDetail extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderImportId = Integer.parseInt(request.getParameter("id"));
        HistoryImportService historyImportService = new HistoryImportService();
        List<ImportOrderDetails> importOrderDetails = historyImportService.getImportOrderDetailById(orderImportId);
        request.setAttribute("importOrderDetails", importOrderDetails);
        request.getRequestDispatcher("/admin/popupOrderImportDetail.jsp").forward(request, response);
    }
}
