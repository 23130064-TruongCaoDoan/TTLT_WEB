package controler.user.profile;

import Service.UserService;
import Service.UploadService;
import Util.EmailSender;
import Util.Token8;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "SetUpAccount", value = "/SetUpAccount")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class SetUpAccount extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            response.sendRedirect( "login");
//            return;
//        }
        User user = (User) session.getAttribute("user");
        LocalDate today = LocalDate.now();
        request.setAttribute("today", today);
        request.setAttribute("user", user);

        String message = (String) session.getAttribute("message");
        String error = (String) session.getAttribute("error");
        if (message != null) {
            request.setAttribute("message", message);
            session.removeAttribute("message");
        }
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }
        request.getRequestDispatcher("user/user-hoSoCaNhan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String sex = request.getParameter("sex");
        System.out.println(sex);
        boolean gender = false;
        if(sex.equals("1")){
            gender = true;
        }
        System.out.println(gender);
        String birthdayStr = request.getParameter("birthday");
        LocalDate birthday = null;
        if(!(birthdayStr==null||birthdayStr.equals(""))) {
             birthday = LocalDate.parse(birthdayStr);
        }

        String avatar = user.getAvatar();
        try {
            Part filePart = request.getPart("avatar");
            if (filePart != null && filePart.getSize() > 0) {
                UploadService uploadService = new UploadService();
                String uploadedUrl = uploadService.upload(filePart, "avatars");

                if (uploadedUrl != null) {
                    avatar = uploadedUrl;
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi upload avatar: " + e.getMessage());
        }

        int id = user.getId();
        UserService userService = new UserService();
        try {
            userService.updateProfile(id,name,phone, email, gender,birthday, avatar);
            user.setName(name);
            user.setPhone(phone);
            user.setEmail(email);
            user.setSex(gender);
            user.setBirthday(birthday);
            user.setAvatar(avatar);
            session.setAttribute("user", user);
            request.setAttribute("error", "Cập nhật thành công!");
            response.sendRedirect("SetUpAccount");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi cập nhật profile!");
            response.sendRedirect("SetUpAccount");
        }
    }

}