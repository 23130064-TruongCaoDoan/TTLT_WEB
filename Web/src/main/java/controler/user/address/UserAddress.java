package controler.user.address;

import Service.AddressService;
import Service.EventService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "address", value = "/address")
public class UserAddress extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        AddressService addressService = new AddressService();
        List<Address> listAddress = addressService.getAddress(userId);

        request.setAttribute("listAddress", listAddress);

        request.getRequestDispatcher("user/user-address.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}