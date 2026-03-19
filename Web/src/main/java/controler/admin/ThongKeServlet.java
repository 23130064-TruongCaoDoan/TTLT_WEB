package controler.admin;

import DTO.BookWithSoldDTO;
import DTO.RevenueDTO;
import DTO.UserWithTotalSpentDTO;
import Service.EventService;
import Service.ThongKeService;
import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ThongKe", value = "/ThongKe")
public class ThongKeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EventService eventService = new EventService();
        eventService.updatBookPriceForEvent();
        ThongKeService thongKeService = new ThongKeService();
        double totalRevenue =0;
        List<UserWithTotalSpentDTO> getTop10Users =null;
        UserWithTotalSpentDTO getTopCustomer=null;
        BookWithSoldDTO getBestSeller=null;
        BookWithSoldDTO getWorstSeller=null;
        List<BookWithSoldDTO> getTop10Books=null;
        String year=request.getParameter("year");



        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }
        User user = (User) session.getAttribute("user");
        UserService userService = new UserService();
        if (user == null || !userService.checkRole(user)) {
            response.sendRedirect("login");
            return;
        }

        String type = request.getParameter("type");
        String fromStr = request.getParameter("fromDate");
        String toStr = request.getParameter("toDate");
        LocalDate from ;
        LocalDate to;
        if(fromStr==null||toStr==null){
            from = LocalDate.now();
            to = LocalDate.now().plusDays(1);
        }else{
            from = LocalDate.parse(fromStr);
            to = LocalDate.parse(toStr);
        }
        if(to.isBefore(from)){
            LocalDate temp = to;
            to = from;
            from = temp;
        }

        request.setAttribute("from",from);
        request.setAttribute("to",to);
        if("year".equals(type)){
            totalRevenue = thongKeService.getTotalRevenue(year);
            getTop10Users = thongKeService.getTop10Users(year);
            getTopCustomer = thongKeService.getTopCustomer(year);
            getBestSeller = thongKeService.getBestSeller(year);
            getWorstSeller = thongKeService.getWorstSeller(year);
            getTop10Books = thongKeService.getTop10Books(year);
        }else {
            totalRevenue = thongKeService.getTotalRevenue(from,to);
            getTop10Users = thongKeService.getTop10Users(from,to);
            getTopCustomer = thongKeService.getTopCustomer(from,to);
            getBestSeller = thongKeService.getBestSeller(from,to);
            getWorstSeller = thongKeService.getWorstSeller(from,to);
            getTop10Books = thongKeService.getTop10Books(from,to);
        }
        request.setAttribute("totalRevenue",totalRevenue);
        request.setAttribute("top10Customers", getTop10Users);
        request.setAttribute("topCustomer", getTopCustomer);
        request.setAttribute("bestBook", getBestSeller);
        request.setAttribute("worstBook", getWorstSeller);
        request.setAttribute("top10Books", getTop10Books);
        request.setAttribute("listYear", thongKeService.getListYear());
        request.getRequestDispatcher("admin/ThongKe.jsp")
                .forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}