package controler.admin.CustomerManage;

import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "GiftVoucherServlet", value = "/gift-voucher")
public class GiftVoucherServlet extends HttpServlet {
    VoucherService vs = new VoucherService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIds = request.getParameter("userIds");
        String code = request.getParameter("voucherCode");
        String target = request.getParameter("chon");
        if (code == null || code.isBlank()) {
            response.sendRedirect("user-manage?error=invalid_code");
            return;
        }

        if ("all".equals(target)) {
            vs.insertVoucherForAll(code);

        } else if ("selected".equals(target)) {

            if (userIds == null || userIds.isBlank()) {
                response.sendRedirect("user-manage?error=no_user_selected");
                return;
            }
            vs.insertVoucherForUsers(code, userIds);

        } else {
            response.sendRedirect("user-manage?error=invalid_target");
            return;
        }

        response.sendRedirect("user-manage?success=gifted");
    }

}