package controler.admin;

import DTO.BookWithSoldDTO;
import DTO.OrderDTOChart;
import DTO.RevenueDTO;
import DTO.UserWithTotalSpentDTO;
import Service.EventService;
import Service.ThongKeService;
import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import model.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<RevenueDTO> getTotalRevenueChart= new ArrayList<>();
        Map<String, Double> getPercentTypeChart=new HashMap<>();
        List<OrderDTOChart>  getTotalOrderChart=new ArrayList<>();
        int totalSoldProducts=0;
        int totalOrders=0;
        int totalCancelledOrders=0;

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
            getTotalRevenueChart = thongKeService.getRevenueChart(year);
            getPercentTypeChart = thongKeService.getPercentTypeSold(year);
            totalSoldProducts = thongKeService.getTotalSoldProducts(year);
            totalOrders = thongKeService.getTotalOrders(year);
            totalCancelledOrders = thongKeService.getTotalCanceledOrders(year);
            getTotalOrderChart = thongKeService.getOrderChart(year);
        }else {
            totalRevenue = thongKeService.getTotalRevenue(from,to);
            getTop10Users = thongKeService.getTop10Users(from,to);
            getTopCustomer = thongKeService.getTopCustomer(from,to);
            getBestSeller = thongKeService.getBestSeller(from,to);
            getWorstSeller = thongKeService.getWorstSeller(from,to);
            getTop10Books = thongKeService.getTop10Books(from,to);
            getTotalRevenueChart = thongKeService.getRevenueChart(from,to);
            getPercentTypeChart = thongKeService.getPercentTypeSold(from,to);
            totalSoldProducts = thongKeService.getTotalSoldProducts(from,to);
            totalOrders = thongKeService.getTotalOrders(from,to);
            totalCancelledOrders = thongKeService.getTotalCanceledOrders(from,to);
            getTotalOrderChart = thongKeService.getOrderChart(from,to);

        }
        request.setAttribute("totalRevenue",totalRevenue);
        request.setAttribute("top10Customers", getTop10Users);
        request.setAttribute("topCustomer", getTopCustomer);
        request.setAttribute("bestBook", getBestSeller);
        request.setAttribute("worstBook", getWorstSeller);
        request.setAttribute("top10Books", getTop10Books);
        request.setAttribute("listYear", thongKeService.getListYear());
        request.setAttribute("revenueChartData", getTotalRevenueChart);
        request.setAttribute("percentTypeSold", getPercentTypeChart);
        request.setAttribute("totalSoldProducts", totalSoldProducts);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalCancelledOrders", totalCancelledOrders);
        request.setAttribute("OrderChartData", getTotalOrderChart);
        request.setAttribute("type", type);
        request.getRequestDispatcher("admin/ThongKe.jsp")
                .forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}