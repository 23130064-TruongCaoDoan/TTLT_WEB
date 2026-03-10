package controler.admin.adminVoucher;

import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "addVoucher", value = "/addVoucher")
public class addVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VoucherService voucherService = new VoucherService();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String code = request.getParameter("code");
        String description = request.getParameter("description");
        String type = request.getParameter("type");
        String conditionBook = request.getParameter("loaisach");
        String conditionPublisher = request.getParameter("nxb");
        String start_date = request.getParameter("start_date");
        String end_date = request.getParameter("end_date");

        int conditionPrice = 0;
        String giaParam = request.getParameter("gia");
        if (giaParam != null && !giaParam.trim().isEmpty()) {
            try {
                conditionPrice = Integer.parseInt(giaParam);
            } catch (NumberFormatException ignored) {
            }
        }

        int usage_limit = 0;
        String limitParam = request.getParameter("usage_limit");
        if (limitParam != null && !limitParam.trim().isEmpty()) {
            try {
                usage_limit = Integer.parseInt(limitParam);
            } catch (NumberFormatException ignored) {
            }
        }

        double value = 0;
        String valueParam = request.getParameter("value");
        if (valueParam != null && !valueParam.trim().isEmpty()) {
            try {
                value = Double.parseDouble(valueParam);
            } catch (NumberFormatException ignored) {
            }
        }

        if (code == null || code.trim().isEmpty()
                || description == null || description.trim().isEmpty()
                || type == null || type.trim().isEmpty()
                || start_date == null || start_date.isEmpty()
                || end_date == null || end_date.isEmpty()
                || usage_limit <= 0
                || value <= 0) {

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Vui lòng nhập đầy đủ thông tin\"}"
            );
            return;
        }

        if (voucherService.voucherExists(code)) {

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"mã voucher đã tồn tại\"}"
            );
            return;
        }

        if (conditionPrice <= 0
                && (conditionBook == null || conditionBook.trim().isEmpty())
                && (conditionPublisher == null || conditionPublisher.trim().isEmpty())) {

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Phải nhập ít nhất 1 điều kiện\"}"
            );
            return;
        }

        if (start_date.compareTo(end_date) > 0) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Ngày bắt đầu phải nhỏ hơn ngày kết thúc\"}"
            );
            return;
        }
        try {


            boolean success = voucherService.addVoucher(
                    code,
                    description,
                    conditionPrice,
                    conditionBook,
                    conditionPublisher,
                    start_date,
                    end_date,
                    usage_limit,
                    value,
                    type
            );

            if (success) {
                response.getWriter().write(
                        "{\"success\":true,\"message\":\"Thêm voucher thành công\"}"
                );
            } else {
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Thêm voucher thất bại\"}"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Đã xảy ra lỗi\"}");
        }
    }
}