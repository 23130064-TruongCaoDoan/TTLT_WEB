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

        String[] voucherCodes = request.getParameterValues("voucherCodes");
        String[] userIds = request.getParameterValues("userIds");
        String target = request.getParameter("chon");
        if (voucherCodes == null || voucherCodes.length == 0) {
            response.sendRedirect("user-manage?error=invalid_code");
            return;
        }

        for (String code : voucherCodes) {
            if ("all".equals(target)) {
                vs.insertVoucherForAll(code);
            } else if ("selected".equals(target)) {
                if (userIds == null || userIds.length == 0) {
                    response.sendRedirect("user-manage?error=no_user_selected");
                    return;
                }
                String userIdsRaw = String.join(",", userIds);
                vs.insertVoucherForUsers(code, userIdsRaw);
            }
        }
        response.sendRedirect("user-manage?success=gifted");
    }

}