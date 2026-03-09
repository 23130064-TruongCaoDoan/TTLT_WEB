package controler.admin.adminVoucher;

import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "filterVoucher", value = "/filterVoucher")
public class filterVoucher extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String time = request.getParameter("time");
        String type = request.getParameter("type");
        VoucherService voucherService = new VoucherService();
        List<Voucher> list = voucherService.filterVoucher(keyword, type, time);
        request.setAttribute("listVoucher", list);
        request.getRequestDispatcher("admin/khoVoucher.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}