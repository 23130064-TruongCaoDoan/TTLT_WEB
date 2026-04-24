package controler.admin.adminOrder;


import Service.ReturnRequestService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.ReturnRequest;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReturnRequestManagerServlet", value = "/admin-return-manager")
public class ReturnRequestManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReturnRequestService service = new ReturnRequestService();
        List<ReturnRequest> list = service.getAllRequests();

        request.setAttribute("returnRequests", list);
        request.getRequestDispatcher("/admin/quanlitrahang.jsp").forward(request, response);
    }
}