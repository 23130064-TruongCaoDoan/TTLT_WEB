package controler.admin.adminLog;

import dao.AdminLogDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;

@WebFilter(filterName = "AdminLogFilter", urlPatterns = {"/*"})
public class AdminLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        request.setCharacterEncoding("UTF-8");

        HttpSession sessionBefore = request.getSession(false);
        User userBefore = (sessionBefore != null) ? (User) sessionBefore.getAttribute("user") : null;

        String method = request.getMethod();
        String uri = request.getRequestURI().toLowerCase();

        if (uri.contains("event")) {
            chain.doFilter(request, response);
            return;
        }

        String action = "";
        String logContent = "";

        if ("POST".equalsIgnoreCase(method)) {
            if (uri.contains("login") || uri.contains("dangnhap")) {
                action = "ĐĂNG NHẬP";
                logContent = "Bạn đã đăng nhập thành công";
            }
            else if (uri.contains("add") || uri.contains("create") || uri.contains("insert")) {
                action = "THÊM";
                if (uri.contains("author") || uri.contains("tacgia") || uri.contains("admin-add-author")) {
                    String authorName = request.getParameter("name");
                    logContent = "Bạn đã thêm tác giả: " + (authorName != null ? authorName : "");
                } else if (uri.contains("book") || uri.contains("sach")) {
                    String bookTitle = request.getParameter("title");
                    logContent = "Bạn đã thêm sách: " + (bookTitle != null ? bookTitle : "");
                } else {
                    logContent = "Bạn đã thêm dữ liệu mới";
                }
            }
            else if (uri.contains("update") || uri.contains("edit") || uri.contains("admin-edit-author")) {
                action = "SỬA";
                if (uri.contains("author") || uri.contains("tacgia") || uri.contains("admin-edit-author")) {
                    String authorName = request.getParameter("name");
                    logContent = "Bạn đã cập nhật thông tin tác giả: " + (authorName != null ? authorName : "");
                } else {
                    logContent = "Bạn đã chỉnh sửa dữ liệu";
                }
            }
            else if (uri.contains("delete") || uri.contains("remove") || uri.contains("admin-delete-author")) {
                action = "XÓA";
                String authorName = request.getParameter("authorName");

                if (uri.contains("author") || uri.contains("tacgia") || uri.contains("admin-delete-author")) {
                    logContent = "Bạn đã xóa tác giả: " + (authorName != null ? authorName : "");
                } else {
                    logContent = "Bạn đã xóa dữ liệu thành công";
                }
            }
        }
        else if ("GET".equalsIgnoreCase(method)) {
            if (uri.contains("delete") || uri.contains("remove")) {
                action = "XÓA";
                String id = request.getParameter("id");
                String authorName = request.getParameter("authorName");

                if (uri.contains("author") || uri.contains("tacgia")) {
                    logContent = "Bạn đã xóa tác giả: " + (authorName != null ? authorName : "ID " + id);
                } else {
                    logContent = "Bạn đã xóa dữ liệu có ID: " + (id != null ? id : "");
                }
            }
        }

        chain.doFilter(request, response);

        User finalUser = userBefore;

        if (finalUser == null && "ĐĂNG NHẬP".equals(action)) {
            HttpSession sessionAfter = request.getSession(false);
            if (sessionAfter != null) {
                try {
                    finalUser = (User) sessionAfter.getAttribute("user");
                } catch (IllegalStateException e) {
                }
            }
        }

        if (finalUser != null && !action.isEmpty() && !logContent.isEmpty()) {
            AdminLogDAO logDAO = new AdminLogDAO();
            logDAO.insertLog(finalUser.getId(), action, logContent);
        }
    }
}