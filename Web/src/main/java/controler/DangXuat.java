package controler;

import dao.AuthTokenDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DangXuat", value = "/DangXuat")
public class DangXuat extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_me".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    if (value != null && value.contains(":")) {
                        String selector = value.split(":")[0];
                        new AuthTokenDao().deleteBySelector(selector);
                    }
                    cookie.setMaxAge(0);
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        HttpSession newSession = request.getSession();
        newSession.setAttribute("logoutSuccess", "Đăng xuất thành công");
        response.sendRedirect("login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}