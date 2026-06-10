package controler.Authorization;

import Util.RolesGroup;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter("/*")
public class AuthorizationServlet implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);


        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("user");
        }

        if (session!= null && user != null) {
            int role = user.getRole();
            request.setAttribute("canViewStatistic", RolesGroup.canViewStatistic(role));
            request.setAttribute("canSale", RolesGroup.canSale(role));
            request.setAttribute("canManageUser", RolesGroup.canManageUser(role));
            request.setAttribute("canManageProduct", RolesGroup.canManageProduct(role));
            request.setAttribute("canImport", RolesGroup.canImport(role));
            request.setAttribute("isStaff", RolesGroup.isStaff(role));
            request.setAttribute("isAdmin", RolesGroup.isAdmin(role));
            request.setAttribute("isManager", RolesGroup.isManager(role));
        }

        chain.doFilter(request, response);
    }
}
