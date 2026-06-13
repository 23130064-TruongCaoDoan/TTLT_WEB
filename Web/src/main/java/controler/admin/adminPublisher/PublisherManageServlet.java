package controler.admin.adminPublisher;

import Service.PublisherService;
import Service.UserService;
import model.Publisher;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;

import static Util.RolesGroup.ALL_STAFF_ROLE;

@WebServlet(name = "PublisherManageServlet", urlPatterns = {"/publisher-manage", "/admin-add-publisher", "/admin-edit-publisher", "/admin-delete-publisher"})
public class PublisherManageServlet extends HttpServlet {
    private PublisherService publisherService = new PublisherService();

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
        int noOfRecords = publisherService.countSearch(keyword);
        int totalPages = (int) Math.ceil((double) noOfRecords / recordsPerPage);

        List<Publisher> publishers = publisherService.searchAndPaginate(keyword, recordsPerPage, offset);

        request.setAttribute("publishers", publishers);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("/admin/quanlinxb.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        if ("/admin-delete-publisher".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean success = publisherService.deletePublisher(id);
                if (success) {
                    request.setAttribute("logSuccess", true);
                }
                response.sendRedirect(request.getContextPath() + "/publisher-manage?" + (success ? "success=delete" : "error=delete_failed"));
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/publisher-manage?error=invalid_id");
            }
            return;
        }

        if ("/admin-add-publisher".equals(action)) {
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            if (name == null || name.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/publisher-manage?error=invalid_name");
                return;
            }

            if (publisherService.isNameExists(name.trim())) {
                response.sendRedirect(request.getContextPath() + "/publisher-manage?error=duplicate_name");
                return;
            }

            boolean success = publisherService.addPublisher(code, name.trim(), address, email, phone);
            if (success) {
                request.setAttribute("logSuccess", true);
            }
            response.sendRedirect(request.getContextPath() + "/publisher-manage?" + (success ? "success=add" : "error=server"));

        } else if ("/admin-edit-publisher".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String address = request.getParameter("address");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                Publisher existing = publisherService.getById(id);
                if (existing == null) {
                    response.sendRedirect(request.getContextPath() + "/publisher-manage?error=not_found");
                    return;
                }

                if (name != null && !name.trim().isEmpty() && !existing.getName().equalsIgnoreCase(name.trim())) {
                    if (publisherService.isNameExists(name.trim())) {
                        response.sendRedirect(request.getContextPath() + "/publisher-manage?error=duplicate_name");
                        return;
                    }
                }

                existing.setName(name != null ? name.trim() : existing.getName());
                existing.setAddress(address);
                existing.setEmail(email);
                existing.setPhone(phone);

                boolean success = publisherService.updatePublisher(existing);
                if (success) {
                    request.setAttribute("logSuccess", true);
                }
                response.sendRedirect(request.getContextPath() + "/publisher-manage?" + (success ? "success=edit" : "error=edit_failed"));

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/publisher-manage?error=invalid_id");
            }
        }
    }
}
