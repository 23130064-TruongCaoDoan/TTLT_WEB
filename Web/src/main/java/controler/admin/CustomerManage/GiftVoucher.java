package controler.admin.CustomerManage;

import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GiftVoucher", value = "/GiftVoucher")
public class GiftVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        VoucherService voucherService = new VoucherService();

        String userCodes = request.getParameter("userId");
        int userId = Integer.parseInt(userCodes);

        List<Integer> listU = new ArrayList<>();
        listU.add(userId);

        String voucherIds = request.getParameter("voucherIds");

        String[] ids = voucherIds.split(",");

        for (String id : ids) {
            voucherService.tangVoucher(listU, id);
        }

        response.getWriter().write("{\"success\":true, \"message\":\"Tặng voucher thành công\"}");

    }
}