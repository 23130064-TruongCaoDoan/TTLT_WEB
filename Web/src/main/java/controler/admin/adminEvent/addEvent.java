package controler.admin.adminEvent;

import Service.EventService;
import Service.UserService;
import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "addEvent", value = "/addEvent")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,   // 1MB
        maxFileSize = 10 * 1024 * 1024,    // 10MB
        maxRequestSize = 20 * 1024 * 1024 // 20MB
)
public class addEvent extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            String code = request.getParameter("code");
            String title = request.getParameter("title");
            String startDate = request.getParameter("startdate");
            String endDate = request.getParameter("enddate");
            String valueParam = request.getParameter("giatri");
            String voucherParam = request.getParameter("voucher") != null ? request.getParameter("voucher") : "";
            String specialVoucherParam = request.getParameter("v") != null ? request.getParameter("v") : "";
            String pointParam = request.getParameter("point");
            String age = request.getParameter("age");
            String author = request.getParameter("author");
            String pulisher = request.getParameter("pulisher");
            String[] typeBooks = request.getParameterValues("typeBook");
            Part imagePart = request.getPart("image");

            if (code == null || code.trim().isEmpty()) {
                writeError(response, "Mã sự kiện không được để trống");
                return;
            }

            if (eventService.existsByCode(code.trim())) {
                writeError(response, "Mã sự kiện đã tồn tại");
                return;
            }

            if (title == null || title.trim().isEmpty()
                    || startDate == null || startDate.isEmpty()
                    || endDate == null || endDate.isEmpty()
                    || valueParam == null || valueParam.trim().isEmpty()
                    || imagePart == null || imagePart.getSize() == 0) {
                writeError(response, "Vui lòng nhập đầy đủ thông tin bắt buộc");
                return;
            }

            double value;
            try {
                value = Double.parseDouble(valueParam);
                if (value <= 0 || value >= 100) {
                    writeError(response, "Giá trị giảm phải từ 1 đến 99%");
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
                    if (minPoint < 0) {
                        writeError(response, "Điểm tối thiểu không hợp lệ");
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


            if (age != null && !age.trim().isEmpty()) {
                // bỏ khoảng trắng: "18, 20,21" → "18,20,21"
                age = age.replaceAll("\\s+", "");

                // chỉ cho phép: số,số,số...
                if (!age.matches("^\\d+(,\\d+)*$")) {
                    writeError(response, "Độ tuổi phải là các số, ngăn cách bằng dấu phẩy (VD: 18,20,21)");
                    return;
                }

                // kiểm tra từng tuổi hợp lệ
                String[] ages = age.split(",");
                for (String a : ages) {
                    int ageValue = Integer.parseInt(a);
                    if (ageValue <= 0 || ageValue > 120) {
                        writeError(response, "Độ tuổi không hợp lệ: " + ageValue);
                        return;
                    }
                }
            }



            VoucherService voucherService = new VoucherService();

            voucherParam = (voucherParam != null && !voucherParam.trim().isEmpty())
                    ? voucherParam.trim()
                    : null;

            specialVoucherParam = (specialVoucherParam != null && !specialVoucherParam.trim().isEmpty())
                    ? specialVoucherParam.trim()
                    : null;

            if (voucherParam != null && !voucherService.voucherExists(voucherParam)) {
                writeError(response, "Voucher tặng chung không tồn tại");
                return;
            }

            if (specialVoucherParam != null && !voucherService.voucherExists(specialVoucherParam)) {
                writeError(response, "Voucher tặng theo điểm không tồn tại");
                return;
            }
            if (specialVoucherParam != null) {
                if (pointParam == null || pointParam.trim().isEmpty()) {
                    writeError(response, "Voucher theo điểm bắt buộc phải nhập điểm tối thiểu");
                    return;
                }

                if (minPoint <= 0) {
                    writeError(response, "Điểm tối thiểu phải lớn hơn 0 khi dùng voucher theo điểm");
                    return;
                }
            }


            String contentType = imagePart.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                writeError(response, "File upload phải là hình ảnh");
                return;
            }

            boolean hasTypeBook = typeBooks != null && typeBooks.length > 0;
            boolean hasPublisher = pulisher != null && !pulisher.trim().isEmpty();
            boolean hasAge = age != null && !age.trim().isEmpty();
            boolean hasAuthor = author != null && !author.trim().isEmpty();

            if (!hasTypeBook && !hasPublisher && !hasAge && !hasAuthor) {
                writeError(response, "Phải nhập ít nhất 1 điều kiện áp dụng");
                return;
            }

            String typeBookApply = hasTypeBook ? String.join(",", typeBooks) : null;
            pulisher = hasPublisher ? pulisher : null;
            author = hasAuthor ? author : null;
            age = hasAge ? age : null;


            UserService userService = new UserService();
            List<Integer> listU = userService.getAllUserIds();

            List<Integer> listUPoint = userService.getUserPoint(minPoint);


            boolean success = eventService.addEvent(
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
                    voucherService.tangVoucher(listU, voucherParam);
                }

                if (specialVoucherParam != null) {
                    voucherService.tangVoucher(listUPoint, specialVoucherParam);
                }
                response.getWriter().write("{\"success\":true,\"message\":\"Thêm sự kiện thành công\"}");
            } else {
                writeError(response, "Thêm sự kiện thất bại");
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