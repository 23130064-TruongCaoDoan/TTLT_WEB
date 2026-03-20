package controler.admin.adminRateManagement;

import Service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AdminBookRateView;
import model.CommentAdmin;
import model.User;

import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name="Rate", value = "/Rate")
public class RateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        if(user==null){
            response.sendRedirect("login");
            return;
        }
        if(!user.isRole()){
            response.sendRedirect("login");
        }
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();

        request.setAttribute("from",from);
        request.setAttribute("to",to);
        CommentService commentService = new CommentService();
        int[] stars = new int[6];
        int max = 0;

        for (int i = 1; i <= 5; i++) {
            stars[i] = commentService.countByStar(i, from, to);
            max = Math.max(max, stars[i]);
        }
        List<AdminBookRateView> listHigh = commentService.getAdminBookRateHigh(from,to);
        List<AdminBookRateView> listLow = commentService.getAdminBookRateLow(from, to);
        List<CommentAdmin> listRate = commentService.getCommentAdmin(from, to);

        request.setAttribute("stars", stars);
        request.setAttribute("max", max == 0 ? 1 : max);
        request.setAttribute("listHigh", listHigh);
        request.setAttribute("listLow", listLow);
        request.setAttribute("listRate", listRate);
        request.getRequestDispatcher("admin/DanhGia.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fromStr = request.getParameter("startDate");
        String toStr = request.getParameter("endDate");

        int number = Integer.parseInt(request.getParameter("typeFilter"));

        String type;
        switch (number) {
            case 1: type = "Truyện tranh"; break;
            case 2: type = "Sách ảnh"; break;
            case 3: type = "Sách giáo dục"; break;
            case 4: type = "Sách tô màu"; break;
            default: type = "all";
        }
        LocalDate from = null, to = null;
        boolean flag = true;
        if(fromStr.isBlank() || toStr.isBlank()){
            flag = false;
        }else{
             from = LocalDate.parse(fromStr);
             to = LocalDate.parse(toStr);
            if (from.isAfter(to)) {
                LocalDate temp = from;
                from = to;
                to = temp;
            }
        }


        CommentService commentService = new CommentService();
        int[] stars = new int[6];
        int max = 0;

        for (int i = 1; i <= 5; i++) {
            if(flag){
                stars[i] = type.equals("all") ?commentService.countByStar(i, from, to):commentService.countByStar(i, from, to, type);
            }else{
                stars[i] = type.equals("all") ?commentService.countByStar(i):commentService.countByStar(i,type);
            }
            max = Math.max(max, stars[i]);
        }
        List<AdminBookRateView> listHigh = new ArrayList<>();
        List<AdminBookRateView> listLow = new ArrayList<>();
        List<CommentAdmin> listRate = new ArrayList<>();
        if(flag){
            listHigh = type.equals("all") ? commentService.getAdminBookRateHigh(from, to):commentService.getAdminBookRateHigh(from, to, type);
            listLow = type.equals("all") ? commentService.getAdminBookRateLow(from, to):commentService.getAdminBookRateLow(from, to, type);
            listRate = type.equals("all") ? commentService.getCommentAdmin(from, to) : commentService.getCommentAdmin(from, to, type);
        }else{
            listHigh = type.equals("all") ? commentService.getAdminBookRateHigh():commentService.getAdminBookRateHigh(type);
            listLow = type.equals("all") ? commentService.getAdminBookRateLow():commentService.getAdminBookRateLow(type);
            listRate = type.equals("all") ? commentService.getCommentAdmin() : commentService.getCommentAdmin(type);

        }

        request.setAttribute("from", from);
        request.setAttribute("to", to);
        request.setAttribute("type", number);
        request.setAttribute("stars", stars);
        request.setAttribute("listHigh", listHigh);
        request.setAttribute("listLow", listLow);
        request.setAttribute("listRate", listRate);
        request.setAttribute("max", max == 0 ? 1 : max);

        request.getRequestDispatcher("admin/DanhGia.jsp").forward(request, response);
    }
}
