package controler.user.homepage;

import Service.BookService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/suggest")
public class SuggestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String keyword = request.getParameter("q");

        response.setContentType("application/json;charset=UTF-8");

        BookService service = new BookService();
        List<String> list = service.getSuggest(keyword);

        PrintWriter out = response.getWriter();

        out.print("[");
        for (int i = 0; i < list.size(); i++) {
            out.print("\"" + list.get(i) + "\"");
            if (i < list.size() - 1) out.print(",");
        }
        out.print("]");
    }
}
