package controler.adminVoucher;

import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "deleteVoucher", value = "/deleteVoucher")
public class deleteVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id")==null?"0":request.getParameter("id"));
        VoucherService voucherService = new VoucherService();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        boolean success= voucherService.deleteVoucher(id);
        String json;
        if (success) {
            json = "{\"success\":true,\"message\":\"Xóa voucher thành công\"}";
        } else {
            json = "{\"success\":false,\"message\":\"Xóa voucher thất bại do tồn tai\"}";
        }
        response.getWriter().write(json);
    }
}