package controler.admin.CustomerManage;

import DTO.MyOrderDTO;
import Service.AddressService;
import Service.OrderService;
import Service.UserService;
import Service.VoucherService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.OrderView;
import model.User;
import model.Voucher;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "UserDetail", value = "/UserDetail")
public class UserDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = new UserService();
        VoucherService voucherService = new VoucherService();
        OrderService orderService = new OrderService();
        AddressService addressService = new AddressService();
        String id = request.getParameter("id");
        int uid=Integer.parseInt(id);


        List<Address> addresses=addressService.getAddressOfUser(uid);
        List<Voucher> voucherList=voucherService.getListVoucherOfUser(uid);
        List<Voucher> allVoucher=voucherService.getListVoucherStillValid();

        Set<Integer> userVoucherIds = voucherList.stream()
                .map(Voucher::getId)
                .collect(Collectors.toSet());

        List<Voucher> availableVoucher = allVoucher.stream()
                .filter(v -> !userVoucherIds.contains(v.getId()))
                .toList();


        List<OrderView> orders = orderService.getOrderOfUser(uid);
        User user = userService.getUserById(uid);
        int totalOrder = orderService.totalOrder(uid);
        double totalAmount = orderService.totalAmountOrder(uid);

        request.setAttribute("totalOrder",totalOrder);
        request.setAttribute("totalAmount",totalAmount);

        request.setAttribute("addresses",addresses);
        request.setAttribute("voucherList",voucherList);
        request.setAttribute("availableVoucher",availableVoucher);
        request.setAttribute("orders",orders);
        request.setAttribute("user",user);

        request.getRequestDispatcher("admin/chiTietKhachHang.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}