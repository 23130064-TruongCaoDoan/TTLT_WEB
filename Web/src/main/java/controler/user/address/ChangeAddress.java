package controler.user.address;
import Service.AddressService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.User;

import java.io.IOException;

@WebServlet(name = "editAddress", value = "/editAddress")
public class ChangeAddress extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        AddressService addressService = new AddressService();
        int id = Integer.parseInt(request.getParameter("id"));
        Address address = addressService.getAddressById(id);
        if (address == null) {
            response.sendRedirect(request.getContextPath() + "/address");
        }
        boolean isDefault = addressService.isDefaultAddress(id);
        request.setAttribute("isDefault", isDefault);
        request.setAttribute("address", address);
        request.getRequestDispatcher("user/user-editAddress.jsp").forward(request, response);

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        AddressService addressService = new AddressService();


        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("hoten");
        String phone = request.getParameter("sdt");
        String city = request.getParameter("tinhName");
        String districts = request.getParameter("huyenName");
        String ward = request.getParameter("xaName");
        String specificAddress = request.getParameter("diachi");

        int id = Integer.parseInt(request.getParameter("id"));
        Address address = addressService.getAddressById(id);

        address.setId(id);
        address.setName(name);
        address.setPhone(phone);
        address.setCity(city);
        address.setDistricts(districts);
        address.setWard(ward);
        address.setSpecificAddress(specificAddress);
        address.setIsDefault(false);
        try {
            addressService.updateAddress(address);
            response.sendRedirect("address");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi sửa địa chỉ!");
            request.getRequestDispatcher("user/user-editAddress.jsp").forward(request, response);
        }
    }
}
