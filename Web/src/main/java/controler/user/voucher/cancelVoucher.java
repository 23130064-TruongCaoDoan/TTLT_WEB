package controler.user.voucher;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "cancelVoucher", value = "/cancelVoucher")
public class cancelVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String type = request.getParameter("type");
        int numApplyVoucher= (int) session.getAttribute("numApplyVoucher");
        if ("discount".equals(type) && session.getAttribute("appliedDiscountVoucher") != null) {
            session.removeAttribute("appliedDiscountVoucher");
            numApplyVoucher--;
        }
        else if ("ship".equals(type) && session.getAttribute("appliedShipVoucher") != null) {
            session.removeAttribute("appliedShipVoucher");
            numApplyVoucher--;
        }
        session.setAttribute("numApplyVoucher", numApplyVoucher);

        int page = Integer.parseInt(request.getParameter("page")==null?"0":request.getParameter("page"));

        if (page == 1) {
            response.sendRedirect("ShoppingCart");
        }
        if (page == 2) {
            response.sendRedirect("ThanhToan");
        }
    }

}