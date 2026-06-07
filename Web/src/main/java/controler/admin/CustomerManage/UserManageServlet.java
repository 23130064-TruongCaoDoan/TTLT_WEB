package controler.admin.CustomerManage;

import DTO.UserWithTotalSpentDTO;
import Service.UserService;
import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Role;
import model.User;

import java.util.List;
import java.io.IOException;

import static Util.RolesGroup.USER_MANAGER_ROLE;


@WebServlet(name = "UserManageServlet", value = "/user-manage")
public class UserManageServlet extends HttpServlet {
    UserService userService = new UserService();
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
        int role = userService.checkRole(user);
        if (!USER_MANAGER_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }
        String q = request.getParameter("q");
        if (q != null) {
            q = q.trim();
            if (q.isEmpty()) q = null;
        }

        String stock = request.getParameter("sortStock");
        stock = (stock == null || stock.isEmpty()) ? null : stock;
        List<UserWithTotalSpentDTO> lsUser = userService.getUserWithTotalSpent(q,stock);
        List<Role> listRoles = userService.getAllRoles();
        request.setAttribute("users", lsUser);
        request.setAttribute("roles", listRoles);
        VoucherService voucherService = new VoucherService();
        request.setAttribute("listVoucher", voucherService.getListVoucherStillValid());
        request.getRequestDispatcher("admin/user.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}