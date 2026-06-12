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
            else if (uri.contains("updateorderstatus")) {
                action = "CẬP NHẬT TRẠNG THÁI ĐƠN";
                String orderId = request.getParameter("orderId");
                String status = request.getParameter("orderStatus");
                logContent = "Bạn đã chuyển trạng thái đơn hàng ID " + orderId + " sang: [" + status + "]";
            }
            else if (uri.contains("updateorder") && !uri.contains("updateorderstatus")) {
                action = "SỬA ĐƠN HÀNG";
                String orderId = request.getParameter("id");
                String status = request.getParameter("status");
                String total = request.getParameter("total");
                logContent = "Bạn đã chỉnh sửa đơn hàng ID " + orderId + " (Trạng thái: " + status + ", Tổng tiền: " + total + ")";
            }
            else if (uri.contains("deleteorderservlet")) {
                action = "XÓA ĐƠN HÀNG";
                String idRaw = request.getParameter("id");
                String userCodes = request.getParameter("userId");
                if (userCodes == null || userCodes.trim().isEmpty()) {
                    logContent = "Bạn đã xóa đơn hàng ID " + idRaw;
                } else {
                    logContent = "Bạn đã xóa đơn hàng ID " + idRaw + " của khách hàng ID " + userCodes;
                }
            }
            else if (uri.contains("process-return")) {
                action = "XỬ LÝ TRẢ HÀNG";
                String returnAction = request.getParameter("action");
                String requestId = request.getParameter("requestId");

                if ("APPROVE".equalsIgnoreCase(returnAction)) {
                    logContent = "Bạn đã DUYỆT yêu cầu trả hàng ID: " + requestId;
                } else if ("REJECT".equalsIgnoreCase(returnAction)) {
                    String rejectReason = request.getParameter("rejectReason");
                    logContent = "Bạn đã TỪ CHỐI yêu cầu trả hàng ID: " + requestId + " với lý do: " + (rejectReason != null ? rejectReason : "");
                }
            }
            else if (uri.contains("admin-add-user")) {
                action = "TẠO TÀI KHOẢN";
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                logContent = "Bạn đã tạo tài khoản mới: " + (name != null ? name : "") + " (" + (email != null ? email : "") + ")";
            }
            else if (uri.contains("giftvoucher") || uri.contains("gift-voucher")) {
                action = "TẶNG VOUCHER";
                if (uri.contains("gift-voucher")) {
                    String target = request.getParameter("chon");
                    String code = request.getParameter("voucherCode");
                    if ("all".equals(target)) {
                        logContent = "Bạn đã tặng voucher [" + code + "] cho tất cả khách hàng";
                    } else {
                        String userIds = request.getParameter("userIds");
                        logContent = "Bạn đã tặng voucher [" + code + "] cho khách hàng có ID: " + userIds;
                    }
                } else {
                    String userId = request.getParameter("userId");
                    String voucherIds = request.getParameter("voucherIds");
                    logContent = "Bạn đã tặng các Voucher ID [" + voucherIds + "] cho khách hàng có ID: " + userId;
                }
            }
            else if (uri.contains("notify")) {
                action = "TẠO THÔNG BÁO";
                String title = request.getParameter("title");
                if (uri.contains("notify-user")) { // Gửi nhiều người cùng lúc (NotifyUserServlet)
                    String[] userIds = request.getParameterValues("userIds");
                    int count = (userIds != null) ? userIds.length : 0;
                    logContent = "Bạn đã gửi thông báo '" + title + "' đến " + count + " khách hàng";
                } else {
                    String userId = request.getParameter("userIds");
                    logContent = "Bạn đã gửi thông báo '" + title + "' đến khách hàng có ID: " + userId;
                }
            }
            else if (uri.contains("change-role")) {
                action = "THAY ĐỔI QUYỀN";
                String userId = request.getParameter("userId");
                String roleParam = request.getParameter("role");
                String roleName = "1".equals(roleParam) ? "Admin" : "User";
                logContent = "Bạn đã thay đổi quyền của khách hàng ID " + userId + " thành [" + roleName + "]";
            }
            else if (uri.contains("change-status") || (uri.contains("updatauserservlet") && "status".equals(request.getParameter("field")))) {
                action = "THAY ĐỔI TRẠNG THÁI";
                String userId = request.getParameter("userId");
                String statusParam = request.getParameter("status");
                if (statusParam == null) {
                    statusParam = request.getParameter("value");
                }
                String statusName = "1".equals(statusParam) ? "Mở" : "Khóa";
                logContent = "Bạn đã thay đổi trạng thái của khách hàng ID " + userId + " thành [" + statusName + "]";
            }
            else if (uri.contains("createorderforcustomer")) {
                action = "TẠO ĐƠN HỘ";
                String receiverName = request.getParameter("receiverName");
                String finalTotal = request.getParameter("finalTotal"); // hoặc lấy các thông tin cần thiết
                logContent = "Bạn đã tạo hộ đơn hàng cho khách: " + (receiverName != null ? receiverName : "") + " từ trang quản trị";
            }
            else if (uri.contains("addvoucher")) {
                action = "THÊM VOUCHER";
                String code = request.getParameter("code");
                logContent = "Bạn đã thêm mã voucher mới : " + (code != null ? code : "");
            }
            else if (uri.contains("updatevoucher")) {
                action = "SỬA VOUCHER";
                String code = request.getParameter("code");
                logContent = "Bạn đã cập nhật thông tin mã voucher: " + (code != null ? code : "");
            }
            else if (uri.contains("deletevoucher")) {
                action = "XÓA VOUCHER";
                String id = request.getParameter("id");
                String userCodes = request.getParameter("userId");
                if (userCodes == null || userCodes.trim().isEmpty()) {
                    logContent = "Bạn đã xóa voucher có ID " + id;
                } else {
                    logContent = "Bạn đã gỡ bỏ voucher ID " + id + " khỏi khách hàng có ID " + userCodes;
                }
            }
            else if (uri.contains("updatauserservlet")) {
                String field = request.getParameter("field");
                String value = request.getParameter("value");
                String userId = request.getParameter("userId");

                action = "CẬP NHẬT THÔNG TIN";
                String fieldName = "";
                if ("name".equals(field)) fieldName = "Tên";
                else if ("email".equals(field)) fieldName = "Email";
                else if ("phone".equals(field)) fieldName = "Số điện thoại";
                else if ("birthYear".equals(field)) fieldName = "Năm sinh";

                logContent = "Bạn đã cập nhật trường [" + fieldName + "] của khách hàng ID " + userId + " thành: " + value;
            }
            else if (uri.contains("updateavatarservlet")) {
                action = "CẬP NHẬT AVATAR";
                String userId = request.getParameter("userId");
                logContent = "Bạn đã cập nhật ảnh đại diện mới cho khách hàng ID: " + userId;
            }
            else if (uri.contains("updateorderservlet")) {
                action = "CẬP NHẬT ĐƠN HÀNG";
                String orderId = request.getParameter("orderId");
                String status = request.getParameter("status");
                logContent = "Bạn đã chỉnh sửa đơn hàng ID " + orderId + ", trạng thái mới: [" + status + "]";
            }
            else if (uri.contains("admin-add-publisher")) {
                action = "THÊM NHÀ XUẤT BẢN";
                String name = request.getParameter("name");
                String code = request.getParameter("code");
                logContent = "Bạn đã thêm nhà xuất bản mới: " + (name != null ? name : "") + " (Mã: " + (code != null ? code : "") + ")";
            }
            else if (uri.contains("admin-edit-publisher")) {
                action = "SỬA NHÀ XUẤT BẢN";
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                logContent = "Bạn đã cập nhật thông tin nhà xuất bản ID " + id + " thành: " + (name != null ? name : "");
            }
            else if (uri.contains("admin-delete-publisher")) {
                action = "XÓA NHÀ XUẤT BẢN";
                String id = request.getParameter("id");
                String name = request.getParameter("publisherName"); // lấy từ form ẩn
                if (name != null && !name.trim().isEmpty()) {
                    logContent = "Bạn đã xóa nhà xuất bản: " + name + " (ID: " + id + ")";
                } else {
                    logContent = "Bạn đã xóa nhà xuất bản có ID: " + id;
                }
            }
            else if (uri.contains("add") || uri.contains("create") || uri.contains("insert")
                    || (uri.contains("product-manage") && (request.getParameter("id") == null || request.getParameter("id").isBlank()))) {
                action = "THÊM";
                if (uri.contains("author") || uri.contains("tacgia") || uri.contains("admin-add-author")) {
                    String authorName = request.getParameter("name");
                    logContent = "Bạn đã thêm tác giả: " + (authorName != null ? authorName : "");
                } else if (uri.contains("book") || uri.contains("sach") || uri.contains("product-manage")) {
                    String bookTitle = request.getParameter("title");
                    String bookCode = request.getParameter("bookCode");
                    if (bookCode == null) bookCode = request.getParameter("book_code");
                    logContent = "Bạn đã thêm sách: " + (bookTitle != null ? bookTitle : "");
                } else {
                    logContent = "Bạn đã thêm dữ liệu mới";
                }
            }
            else if (uri.contains("update") || uri.contains("edit") || uri.contains("admin-edit-author")
                    || (uri.contains("product-manage") && request.getParameter("id") != null && !request.getParameter("id").isBlank())) {
                action = "SỬA";
                if (uri.contains("author") || uri.contains("tacgia") || uri.contains("admin-edit-author")) {
                    String authorName = request.getParameter("name");
                    logContent = "Bạn đã cập nhật thông tin tác giả: " + (authorName != null ? authorName : "");
                } else if (uri.contains("book") || uri.contains("sach") || uri.contains("product-manage")) {
                    String bookTitle = request.getParameter("title");
                    String bookId = request.getParameter("id");
                    logContent = "Bạn đã cập nhật thông tin sách: " + (bookTitle != null ? bookTitle : "");
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

        if (userBefore != null) {
            AdminLogDAO dao = new AdminLogDAO();
            int unreadCount = dao.getUnreadCount(userBefore.getId());
            request.setAttribute("unreadLogCount", unreadCount);
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
        Boolean isSuccess = (Boolean) request.getAttribute("logSuccess");
        if ("ĐĂNG NHẬP".equals(action) && finalUser != null) {
            isSuccess = true;
        }

        if (Boolean.TRUE.equals(isSuccess) && finalUser != null && !action.isEmpty() && !logContent.isEmpty()) {
            AdminLogDAO logDAO = new AdminLogDAO();
            logDAO.insertLog(finalUser.getId(), action, logContent);
        }
    }
}