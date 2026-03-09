package controler.user.address;

import Service.AddressService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebServlet(name = "defaultAddress", value = "/defaultAddress")
public class DefaultAddress extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if(user==null){
            response.sendRedirect("login");
            return;
        }
        AddressService  addressService = new AddressService();
        int addressId = Integer.parseInt(request.getParameter("isDefault"));
        int userId = user.getId();
        addressService.setAddressDefault(addressId,userId);


    }
}
