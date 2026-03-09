<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<footer class="footer">
    <div class="wave-container">
        <svg
                viewBox="0 0 120 15"
                xmlns="http://www.w3.org/2000/svg"
                preserveAspectRatio="none"
        >
            <path
                    d="M0,10
                C10,15 20,5 30,10
                C40,15 50,5 60,10
                C70,15 80,5 90,10
                C100,15 115,5 120,10
                L120,20 0,20 Z"
            ></path>
        </svg>
    </div>
    <div class="footer-container">
        <div class="footer-column">
            <h3>Liên hệ chúng tôi</h3>
            <a href="#"><i class="fa-solid fa-phone"></i> 0981566177</a>
            <a href="QRChatServlet"
            ><i class="fa-brands fa-facebook-messenger"></i> Chat trực tiếp</a
            >
        </div>

        <div class="footer-column">
            <h3>Dịch vụ khách hàng</h3>
            <a href="my-orders">Theo dõi đơn hàng</a>
            <a href="SetUpAccount">Tài khoản</a>
            <a href="PolicyServlet">Chính sách đổi trả</a>

        </div>

        <div class="footer-column">
            <h3>Đối tác</h3>
            <a href="NhaPhanPhoi">Nhà phân phối</a>
            <a href="dsSanPham">Sách của chúng tôi</a>
        </div>

        <div class="footer-column">
            <h3>Bảo mật</h3>
            <a href="PrivatePolicyServlet">Chính sách bảo mật</a>
            <a href="DKSuDung">Điều khoản sử dụng</a>
        </div>
    </div>
    <div class="footer-bottom">
        <p>Copyright ©. All Rights Reserved.</p>
    </div>
</footer>
</body>
</html>
