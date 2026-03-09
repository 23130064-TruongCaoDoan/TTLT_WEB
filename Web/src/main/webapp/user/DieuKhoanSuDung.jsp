<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Home</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="stylesheet" href="assets/css/header.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Cormorant+Garamond:ital,wght@0,300..700;1,300..700&family=Libre+Franklin:ital,wght@0,100..900;1,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Playwrite+DE+SAS:wght@100..400&family=Sarabun:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" href="assets/css/footer.css"/>
    <link rel="stylesheet" href="assets/css/home.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bungee&family=Lobster&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/returnP.css">
    <link rel="stylesheet" href="assets/css/dieukhoansd.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="headerUser.jsp"> </c:import>
    <main class="terms-container">
        <h1>Điều Khoản Sử Dụng</h1>
        <section class="terms-section">
            <p>Chào mừng quý khách đến mua sắm tại <strong>Sách Thiếu Nhi Cho Bé</strong>. Sau khi truy cập vào website hoặc mua hàng tại đây, quý khách đồng ý tuân thủ và ràng buộc với các điều khoản sử dụng dưới đây...</p>
        </section>

        <section class="terms-section">
            <h2>1. Tài khoản của khách hàng</h2>
            <p>- Để sử dụng đầy đủ tính năng của trang web, quý khách cần tạo tài khoản. Khi tạo tài khoản, vui lòng cung cấp thông tin chính xác và đầy đủ.</p>
            <p>- Quý khách chịu trách nhiệm bảo mật thông tin đăng nhập và hoạt động trên tài khoản của mình.</p>
        </section>

        <section class="terms-section">
            <h2>2. Quyền lợi & nghĩa vụ của khách hàng</h2>
            <p>- Khi mua sắm tại website, quý khách có quyền lựa chọn sản phẩm, phương thức thanh toán, và hình thức giao hàng.</p>
            <p>- Quý khách có nghĩa vụ thanh toán đầy đủ và đúng hạn theo quy định của cửa hàng.</p>
        </section>

        <section class="terms-section">
            <h2>3. Trách nhiệm của cửa hàng</h2>
            <p>- Cửa hàng cam kết cung cấp thông tin sản phẩm chính xác, hỗ trợ đổi trả theo quy định và bảo mật thông tin khách hàng.</p>
            <p>- Trong trường hợp có lỗi kỹ thuật hoặc sai sót, cửa hàng sẽ khắc phục trong thời gian sớm nhất.</p>
        </section>

        <section class="terms-section">
            <h2>4. Chính sách bảo mật thông tin</h2>
            <p>- Mọi thông tin khách hàng cung cấp sẽ được bảo mật tuyệt đối, chỉ sử dụng cho mục đích giao dịch và chăm sóc khách hàng.</p>
        </section>

        <section class="terms-section">
            <h2>5. Hiệu lực của điều khoản</h2>
            <p>- Điều khoản sử dụng này có hiệu lực kể từ ngày 01/01/2024 và có thể được điều chỉnh để phù hợp với quy định pháp luật hiện hành.</p>
        </section>

        <div class="terms-sign">
            <p><strong>ĐẠI DIỆN CÔNG TY TNHH SÁCH THIẾU NHI CHO BÉ</strong></p>
            <p><em>(Đã ký và đóng dấu)</em></p>
            <p><strong>Trường Huy Đoàn</strong></p>
            <p><em>Giám đốc điều hành</em></p>
        </div>
    </main>
    <c:import url="footerUser.jsp"> </c:import>
</div>
<script src="assets/js/home.js"></script>
</body>
</html>
