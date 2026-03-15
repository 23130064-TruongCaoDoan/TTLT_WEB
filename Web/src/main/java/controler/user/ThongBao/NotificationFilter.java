package controler.user.ThongBao;

import Service.NotificationService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter("/*")
public class NotificationFilter implements Filter {

    private final NotificationService service = new NotificationService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");

        if (user != null) {
            int count = service.countUnread(user.getId());
            session.setAttribute("numNotiFy", count);
        }

        chain.doFilter(request, response);
    }
}