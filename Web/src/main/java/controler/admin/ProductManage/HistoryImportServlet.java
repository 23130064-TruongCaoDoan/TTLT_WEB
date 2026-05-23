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
import java.util.List;

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
        UserService userService = new UserService();
        if (user == null || !userService.checkRole(user)) {
            response.sendRedirect("login");
            return;
        }
        HistoryImportService historyImportService = new HistoryImportService();
        List<HistoryImportDTO> historyImportDTOList = historyImportService.getHistoryImportList();
        request.setAttribute("historyImportDTOList", historyImportDTOList);
        request.getRequestDispatcher("/admin/importManagement.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
