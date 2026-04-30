package controler.admin.CustomerManage;

import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdataUserServlet", value = "/UpdataUserServlet")
public class UpdataUserServlet extends HttpServlet {
    UserService userService=new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int userId = Integer.parseInt(request.getParameter("userId"));
        String field = request.getParameter("field");
        String value = request.getParameter("value");

        boolean success = false;

        switch (field) {
            case "name": {
                success=userService.updateName(userId,value);
                break;
            }
            case "email": {
                success=userService.updateEmail(userId,value);
                break;
            }
            case "phone": {
                success=userService.updatePhone(userId,value);
                break;
            }
            case "birthYear": {
                success=userService.updateBirthDay(userId,value);
                break;
            }
            case "status": {
                boolean status = (value.equals("1"));
                success=userService.updateStatus(userId,status);
                break;
            }
            case "role": {
                break;
            }
        }
        response.getWriter().write("{\"success\":"+success+"}");
    }
}