package controler.admin.CustomerManage;

import Service.UploadService;
import Service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateAvatarServlet", value = "/UpdateAvatarServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 10 * 1024 * 1024, maxRequestSize = 50 * 1024 * 1024)
public class UpdateAvatarServlet extends HttpServlet {


    UploadService  uploadService = new UploadService();
    UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            Integer userId = Integer.parseInt(request.getParameter("userId"));
            Part filePart = request.getPart("avatar");

            if (filePart == null || filePart.getSize() == 0) {
                response.getWriter().write("{\"success\":false,\"message\":\"Chưa chọn ảnh\"}");
                return;
            }

            String avatarUrl = uploadService.upload(filePart,"avatar");

            if (avatarUrl == null) {
                response.getWriter().write("{\"success\":false,\"message\":\"Upload thất bại\"}");
                return;
            }

            userService.updateAvatar(userId, avatarUrl);

            response.getWriter().write(
                    "{\"success\":true,\"avatarUrl\":\"" + avatarUrl + "\"}"
            );

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"success\":false,\"message\":\"Lỗi server\"}");
        }

    }
}