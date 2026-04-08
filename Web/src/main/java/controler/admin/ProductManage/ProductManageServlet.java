package controler.admin.ProductManage;

import Service.AuthorService;
import Service.BookService;
import Service.ThongKeService;
import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Book;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductManageServlet", value = "/product-manage")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 10 * 1024 * 1024, maxRequestSize = 50 * 1024 * 1024)
public class ProductManageServlet extends HttpServlet {
    BookService bookService = new BookService();
    AuthorService authorService = new AuthorService();
    UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login");
            return;
        }
        User user = (User) session.getAttribute("user");

        if (!userService.checkRole(user)) {
            response.sendRedirect("login");
            return;
        }
        request.setAttribute("authors", authorService.getAllAuthors());
        String q = request.getParameter("q");
        String type = request.getParameter("type");
        String stock = request.getParameter("sortStock");
        request.setAttribute("types", bookService.getAllBookTypes());
        List<Book> lsBook = bookService.searchAndFilter(q, type, stock);
        request.setAttribute("lsbook", lsBook);

        ThongKeService thongKeService = new ThongKeService();

        int totalSoldProducts = thongKeService.getTotalSoldProductsAllTime();
        int totalStock = thongKeService.getTotalStock();
        int outOfStockCount = thongKeService.getOutOfStockCount();
        List<Book> outOfStockBooks = thongKeService.getOutOfStockBooks();

        request.setAttribute("totalSoldProducts", totalSoldProducts);
        request.setAttribute("totalStock", totalStock);
        request.setAttribute("outOfStockCount", outOfStockCount);
        request.setAttribute("outOfStockBooks", outOfStockBooks);

        request.getRequestDispatcher("admin/ManageProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            Part mainImage = request.getPart("img-main");

            List<Part> detailImages = request.getParts().stream()
                    .filter(p -> "imgDetail".equals(p.getName()) && p.getSize() > 0)
                    .toList();
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isBlank()) {
                int id = Integer.parseInt(idStr);
                bookService.updateBook(id, request.getParameterMap(), mainImage, detailImages);
            } else {
                bookService.addBook(
                        request.getParameterMap(),
                        mainImage,
                        detailImages);
            }

            response.sendRedirect(request.getContextPath() + "/product-manage");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}