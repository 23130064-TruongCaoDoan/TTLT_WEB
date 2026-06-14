package controler.admin.adminProvider;

import Service.ProviderService;
import Service.UserService;
import model.Provider;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;

import static Util.RolesGroup.ALL_STAFF_ROLE;

@WebServlet(name = "ProviderManagerServlet", urlPatterns = {"/provider-manage", "/admin-add-provider", "/admin-edit-provider", "/admin-delete-provider"})
public class ProviderManagerServlet extends HttpServlet {
    private final ProviderService providerService = new ProviderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login");
            return;
        }

        UserService userService = new UserService();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        int role = userService.checkRole(user);
        if (!ALL_STAFF_ROLE.contains(role)) {
            response.sendRedirect("login");
            return;
        }

        String keyword = request.getParameter("q");
        int page = 1;
        int recordsPerPage = 5;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * recordsPerPage;
        int noOfRecords = providerService.countTotal(keyword);
        int totalPages = (int) Math.ceil((double) noOfRecords / recordsPerPage);

        List<Provider> providers = providerService.getProviders(keyword, recordsPerPage, offset);

        request.setAttribute("providers", providers);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("/admin/quanlinhapp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        if ("/admin-delete-provider".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean success = providerService.deleteProvider(id);
                if (success) {
                    request.setAttribute("logSuccess", true);
                }
                response.sendRedirect(request.getContextPath() + "/provider-manage?" + (success ? "success=delete" : "error=delete_failed"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/provider-manage?error=invalid_id");
            }
            return;
        }

        if ("/admin-add-provider".equals(action)) {
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/provider-manage?error=invalid_name");
                return;
            }

            if (providerService.existsByCode(code.trim())) {
                response.sendRedirect(request.getContextPath() + "/provider-manage?error=duplicate_code");
                return;
            }

            Provider p = new Provider();
            p.setCode(code.trim());
            p.setName(name.trim());
            p.setAddress(address);
            p.setEmail(email);
            p.setPhone(phone);

            boolean success = providerService.addProvider(p);
            if (success) {
                request.setAttribute("logSuccess", true);
            }
            response.sendRedirect(request.getContextPath() + "/provider-manage?" + (success ? "success=add" : "error=server"));

        } else if ("/admin-edit-provider".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String address = request.getParameter("address");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                Provider existing = providerService.getById(id);
                if (existing == null) {
                    response.sendRedirect(request.getContextPath() + "/provider-manage?error=not_found");
                    return;
                }

                existing.setName(name != null && !name.trim().isEmpty() ? name.trim() : existing.getName());
                existing.setAddress(address);
                existing.setEmail(email);
                existing.setPhone(phone);

                boolean success = providerService.updateProviderSelective(existing);
                if (success) {
                    request.setAttribute("logSuccess", true);
                }
                response.sendRedirect(request.getContextPath() + "/provider-manage?" + (success ? "success=edit" : "error=edit_failed"));

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/provider-manage?error=invalid_id");
            }
        }
    }
}