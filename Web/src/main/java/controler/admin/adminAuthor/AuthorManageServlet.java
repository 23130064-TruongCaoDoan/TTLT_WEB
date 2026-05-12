package controler.admin.adminAuthor;

import Service.AuthorService;
import model.Author;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AuthorManageServlet", urlPatterns = {"/author-manage", "/admin-add-author", "/admin-edit-author", "/admin-delete-author"})
public class AuthorManageServlet extends HttpServlet {
    private AuthorService authorService = new AuthorService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("q");

        int page = 1;
        int recordsPerPage = 30;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int noOfRecords = authorService.getTotalAuthors(keyword);
        int totalPages = (int) Math.ceil((double) noOfRecords / recordsPerPage);


        List<Author> authors = authorService.getAuthorsByPage(keyword, page, recordsPerPage);
        request.setAttribute("authors", authors);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("/admin/author.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();

        if ("/admin-delete-author".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = authorService.deleteAuthor(id);
            response.sendRedirect(request.getContextPath() + "/author-manage?" + (success ? "success=delete" : "error=delete_failed"));
        }

        if ("/admin-add-author".equals(action)) {
            String name = request.getParameter("name");
            String birthday = request.getParameter("birthday");

            if (!authorService.isValidName(name)) {
                response.sendRedirect(request.getContextPath() + "/author-manage?error=invalid_name");
                return;
            }

            if (!authorService.isValidDate(birthday)) {
                response.sendRedirect(request.getContextPath() + "/author-manage?error=invalid_date");
                return;
            }

            boolean success = authorService.addAuthor(name, birthday);
            response.sendRedirect(request.getContextPath() + "/author-manage?" + (success ? "success=add" : "error=server"));

        } else if ("/admin-edit-author".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String birthday = request.getParameter("birthday");

            if (birthday != null && !birthday.trim().isEmpty() && !authorService.isValidDate(birthday)) {
                response.sendRedirect(request.getContextPath() + "/author-manage?error=invalid_date");
                return;
            }

            boolean success = authorService.updateAuthor(id, name, birthday);
            response.sendRedirect(request.getContextPath() + "/author-manage?" + (success ? "success=edit" : "error=edit_failed"));
        }
    }
}
