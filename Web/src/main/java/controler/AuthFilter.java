package controler;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter(urlPatterns = {"/ThanhToan", "/favoriteBook","/my-vouchers","/my-orders","/SetUpAccount","/address","/DoiMK"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {

            String currentUrl = req.getRequestURI();
            String queryString = req.getQueryString();

            if (queryString != null) {
                currentUrl += "?" + queryString;
            }

            res.sendRedirect(req.getContextPath()
                    + "/login?redirect=" + currentUrl);
            return;
        }

        chain.doFilter(request, response);
    }
}
