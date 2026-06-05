package controler.admin.ProductManage;

import DTO.HistoryImportDTO;
import Service.HistoryImportService;
import Service.UserService;
import dao.HistoryImportDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Util.RolesGroup.IMPORT_ROLE;

@WebServlet(name = "history-import", value = "/history-import")
public class HistoryImportServlet extends HttpServlet {
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
        if (!IMPORT_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }
        List<User> employeeImportDTO = userService.getAllEmployeeImportProduct();
        HistoryImportService historyImportService = new HistoryImportService();
        List<HistoryImportDTO> historyImportDTOList = new ArrayList<>();
        request.setAttribute("employeeImportDTO", employeeImportDTO);

        String importId = request.getParameter("importId");
        String employeeId = request.getParameter("employeeId");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        boolean hasFilter = (importId != null && !importId.isBlank()) ||
                            (employeeId != null && !employeeId.isBlank()) ||
                            (fromDate != null && !fromDate.isBlank()) ||
                            (toDate != null && !toDate.isBlank());
        if (hasFilter) {
            historyImportDTOList = historyImportService.getHistoryImportDTOSearch(importId, employeeId, fromDate, toDate);
            request.setAttribute("historyImportDTOList", historyImportDTOList);
        }else{
            historyImportDTOList = historyImportService.getHistoryImportList();
            request.setAttribute("historyImportDTOList", historyImportDTOList);
        }
        request.getRequestDispatcher("/admin/importManagement.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
