package controler.user.address;

import Service.AddressService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "deleteAddress", value = "/deleteAddress")
public class DeleteAddress extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        boolean success = false;
        String message = "";

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                AddressService addressService = new AddressService();
                addressService.deleteAddress(id);
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                message = "Xóa địa chỉ thất bại!";
            }
        } else {
            message = "ID không hợp lệ!";
        }

        response.getWriter().write("{\"success\": " + success + ", \"message\": \"" + message + "\"}");
    }
}
