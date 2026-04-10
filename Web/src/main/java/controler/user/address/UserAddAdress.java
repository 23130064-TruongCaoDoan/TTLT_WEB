package controler.user.address;

import Service.AddressService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;

import java.io.IOException;

@WebServlet(name = "addAddress", value = "/addAddress")
public class UserAddAdress extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("user/user-newAddress.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("hoten");
        String phone = request.getParameter("sdt");
        String city = request.getParameter("tinhName");
        String districts = request.getParameter("huyenName");
        String ward = request.getParameter("xaName");
        String specificAddress = request.getParameter("diachi");


        Address address = new Address();
        address.setUserId(userId);
        address.setName(name);
        address.setPhone(phone);
        address.setCity(city);
        address.setDistricts(districts);
        address.setWard(ward);
        address.setSpecificAddress(specificAddress);
        address.setIsDefault(false);
        AddressService addressService = new AddressService();
        try {
            addressService.insertAddress(address);
            response.sendRedirect("address");


        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi lưu địa chỉ!");
            request.getRequestDispatcher("user/user-newAddress.jsp").forward(request, response);
        }
    }
}