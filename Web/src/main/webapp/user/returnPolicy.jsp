<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Chính Sách Đổi Trả</title>
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
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <main class="return-policy">
        <section class="policy-intro">
            <h1>Chính Sách Đổi Trả Sách Thiếu Nhi Cho Bé</h1>
            <p>
                <strong>Sách Thiếu Nhi Cho Bé</strong> luôn mong muốn mang đến cho quý khách
                và các bé những trải nghiệm đọc sách vui vẻ, an toàn và chất lượng nhất.
                Vì vậy, chúng tôi xây dựng chính sách đổi trả linh hoạt, minh bạch nhằm đảm bảo
                quyền lợi tối đa cho khách hàng.
            </p>
            <p>
                Chính sách này được áp dụng cho tất cả các đơn hàng mua trực tiếp tại cửa hàng
                và mua online qua website <em>sachthieunhichobe.vn</em>.
            </p>
        </section>

        <section class="policy-section">
            <h2>1. Điều kiện đổi trả</h2>
            <ul>
                <li>Sản phẩm còn nguyên vẹn, chưa qua sử dụng, không bị rách, ướt hoặc viết vẽ lên.</li>
                <li>Có đầy đủ bao bì, tem, nhãn mác của nhà sản xuất và hóa đơn mua hàng.</li>
                <li>Thời gian yêu cầu đổi trả trong vòng <strong>7 ngày kể từ ngày nhận hàng</strong>.</li>
                <li>Trường hợp đổi sang sản phẩm khác, giá trị sản phẩm đổi phải bằng hoặc cao hơn sản phẩm cũ.</li>
            </ul>
        </section>

        <section class="policy-section">
            <h2>2. Trường hợp được đổi trả</h2>
            <ul>
                <li>Sách bị lỗi kỹ thuật (thiếu trang, mờ chữ, sai nội dung, lệch gáy).</li>
                <li>Giao sai tựa sách, sai số lượng hoặc sai phiên bản.</li>
                <li>Sản phẩm bị hư hỏng do quá trình vận chuyển.</li>
                <li>Hàng không đúng mô tả hoặc hình ảnh trên website.</li>
            </ul>
            <p>
                Trong các trường hợp trên, khách hàng sẽ được <strong>đổi mới miễn phí 100%</strong> hoặc
                <strong>hoàn lại tiền</strong> nếu không còn sản phẩm thay thế.
            </p>
        </section>

        <section class="policy-section">
            <h2>3. Trường hợp không được đổi trả</h2>
            <ul>
                <li>Sách đã qua sử dụng hoặc có dấu hiệu bị tác động bởi người dùng (rách, viết bút, dính nước...).</li>
                <li>Sản phẩm đã mua trong các chương trình giảm giá, khuyến mãi, combo (trừ khi bị lỗi in ấn).</li>
                <li>Khách hàng đổi trả không có hóa đơn hoặc thông tin đơn hàng hợp lệ.</li>
                <li>Thời gian yêu cầu đổi trả vượt quá 7 ngày kể từ ngày nhận hàng.</li>
            </ul>
        </section>

        <section class="policy-section">
            <h2>4. Quy trình đổi trả sản phẩm</h2>
            <ol>
                <li>Liên hệ bộ phận chăm sóc khách hàng qua hotline hoặc email để thông báo vấn đề gặp phải.</li>
                <li>Gửi hình ảnh minh chứng lỗi hoặc tình trạng sản phẩm cho nhân viên xác nhận.</li>
                <li>Đóng gói sản phẩm kèm hóa đơn và gửi về địa chỉ của chúng tôi.</li>
                <li>Sau khi kiểm tra, chúng tôi sẽ tiến hành đổi mới hoặc hoàn tiền trong vòng <strong>3 - 5 ngày làm việc</strong>.</li>
            </ol>
        </section>

        <section class="policy-section">
            <h2>5. Hình thức hoàn tiền</h2>
            <ul>
                <li>Hoàn tiền qua tài khoản ngân hàng (đối với đơn hàng online).</li>
                <li>Hoàn tiền trực tiếp tại cửa hàng (đối với đơn hàng mua trực tiếp).</li>
                <li>Trường hợp thanh toán qua ví điện tử, chúng tôi sẽ hoàn về cùng ví trong vòng 48 giờ sau khi xác nhận.</li>
            </ul>
        </section>

        <section class="policy-section">
            <h2>6. Lưu ý khi đổi trả</h2>
            <p>
                Quý khách vui lòng kiểm tra kỹ hàng hóa ngay khi nhận. Nếu phát hiện lỗi, xin liên hệ
                với chúng tôi trong vòng 24h để được hỗ trợ nhanh nhất.
            </p>
            <p>
                Mọi khiếu nại sau thời hạn 7 ngày kể từ khi nhận hàng có thể không được chấp nhận
                do chúng tôi không thể xác minh tình trạng sản phẩm ban đầu.
            </p>
        </section>

        <section class="policy-contact policy-section">
            <h2>7. Thông tin liên hệ hỗ trợ</h2>
            <p><strong>Hotline:</strong> 0909 123 456 (8h - 20h mỗi ngày)</p>
            <p><strong>Email:</strong> hotro@sachthieunhichobe.vn</p>
            <p><strong>Địa chỉ cửa hàng:</strong> 123 Đường Sách, Quận 1, TP. Hồ Chí Minh</p>
            <p><strong>Fanpage:</strong> <a href="#">facebook.com/sachthieunhichobe</a></p>
            <p><em>Chúng tôi luôn sẵn lòng hỗ trợ và mang đến cho bé những quyển sách đáng yêu nhất!</em></p>
        </section>
    </main>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
<script src="assets/js/home.js"></script>
</body>
</html>
