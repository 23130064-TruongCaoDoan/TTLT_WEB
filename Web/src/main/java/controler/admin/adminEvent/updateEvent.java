package controler.admin.adminEvent;

import Service.EventService;
import Service.UserService;
import Service.VoucherService;
import dao.AdminLogDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "updateEvent", value = "/updateEvent")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class updateEvent extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();
        VoucherService voucherService = new VoucherService();
        UserService userService = new UserService();

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            String code = request.getParameter("code");
            String title = request.getParameter("title");
            String startDate = request.getParameter("startdate");
            String endDate = request.getParameter("enddate");
            String valueParam = request.getParameter("giatri");

            String voucherParam = request.getParameter("voucher");
            String specialVoucherParam = request.getParameter("v");
            String pointParam = request.getParameter("point");

            String age = request.getParameter("age");
            String author = request.getParameter("author");
            String pulisher = request.getParameter("pulisher");
            String[] typeBooks = request.getParameterValues("typeBook");
            Part imagePart = request.getPart("image");

            if (code == null || code.trim().isEmpty()) {
                writeError(response, "Thiếu mã sự kiện");
                return;
            }

            if (!eventService.existsByCode(code.trim())) {
                writeError(response, "Sự kiện không tồn tại");
                return;
            }

            if (title == null || title.trim().isEmpty()
                    || startDate == null || endDate == null
                    || valueParam == null || valueParam.trim().isEmpty()) {
                writeError(response, "Thiếu thông tin bắt buộc");
                return;
            }

            double value;
            try {
                value = Double.parseDouble(valueParam);
                if (value <= 0 || value >= 100) {
                    writeError(response, "Giá trị giảm phải từ 1–99%");
                    return;
                }
            } catch (NumberFormatException e) {
                writeError(response, "Giá trị giảm không hợp lệ");
                return;
            }

            int minPoint = 0;
            if (pointParam != null && !pointParam.trim().isEmpty()) {
                try {
                    minPoint = Integer.parseInt(pointParam);
                    if (minPoint <= 0) {
                        writeError(response, "Điểm tối thiểu phải > 0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    writeError(response, "Điểm tối thiểu không hợp lệ");
                    return;
                }
            }

            try {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                if (start.isAfter(end)) {
                    writeError(response, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
                    return;
                }
            } catch (DateTimeParseException e) {
                writeError(response, "Định dạng ngày không hợp lệ");
                return;
            }

            voucherParam = (voucherParam != null && !voucherParam.trim().isEmpty())
                    ? voucherParam.trim()
                    : null;

            specialVoucherParam = (specialVoucherParam != null && !specialVoucherParam.trim().isEmpty())
                    ? specialVoucherParam.trim()
                    : null;

            if (voucherParam != null && !voucherService.voucherExists(voucherParam)) {
                writeError(response, "Voucher chung không tồn tại");
                return;
            }

            if (specialVoucherParam != null && !voucherService.voucherExists(specialVoucherParam)) {
                writeError(response, "Voucher theo điểm không tồn tại");
                return;
            }

            if (specialVoucherParam != null && minPoint <= 0) {
                writeError(response, "Voucher theo điểm bắt buộc có điểm tối thiểu");
                return;
            }

            boolean hasTypeBook = typeBooks != null && typeBooks.length > 0;
            boolean hasPublisher = pulisher != null && !pulisher.trim().isEmpty();
            boolean hasAge = age != null && !age.trim().isEmpty();
            boolean hasAuthor = author != null && !author.trim().isEmpty();

            if (!hasTypeBook && !hasPublisher && !hasAge && !hasAuthor) {
                writeError(response, "Phải có ít nhất 1 điều kiện áp dụng");
                return;
            }

            String typeBookApply = hasTypeBook ? String.join(",", typeBooks) : null;
            pulisher = hasPublisher ? pulisher : null;
            author = hasAuthor ? author : null;
            age = hasAge ? age : null;

            boolean success = eventService.updateEvent(
                    code.trim(),
                    imagePart,
                    title.trim(),
                    value,
                    startDate,
                    endDate,
                    typeBookApply,
                    pulisher,
                    author,
                    voucherParam,
                    specialVoucherParam,
                    minPoint,
                    age
            );

            if (success) {
                if (voucherParam != null) {
                    List<Integer> allUsers = userService.getAllUserIds();
                    voucherService.tangVoucher(allUsers, voucherParam);
                }

                if (specialVoucherParam != null) {
                    List<Integer> usersByPoint = userService.getUserPoint(minPoint);
                    voucherService.tangVoucher(usersByPoint, specialVoucherParam);
                }

                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("user") != null) {
                    User user = (User) session.getAttribute("user");
                    AdminLogDAO logDAO = new AdminLogDAO();
                    logDAO.insertLog(user.getId(), "SỬA", "Bạn đã cập nhật sự kiện: " + title.trim());
                }

                response.getWriter().write("{\"success\":true,\"message\":\"Cập nhật sự kiện thành công\"}");
            } else {
                writeError(response, "Cập nhật sự kiện thất bại");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeError(response, "Lỗi server");
        }
    }
    private void writeError(HttpServletResponse response, String message) throws IOException {
        response.getWriter().write(
                "{\"success\":false,\"message\":\"" + message + "\"}"
        );
    }
}