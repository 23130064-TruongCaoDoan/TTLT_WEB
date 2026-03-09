<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Nhà Phân Phối</title>
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
    <link rel="stylesheet" href="assets/css/NhaPhanPhoi.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <main class="return-policy">
        <section class="policy-intro">
            <h1>Nhà Phân Phối</h1>
            <p>
                <strong>Sách Thiếu Nhi Cho Bé</strong> hợp tác với cái nhà phân phối sau để cung cấp sản phẩm đến tay
                người tiêu dùng
            </p>

        </section>

        <section class="policy-section">
            <p>1. Công Ty Cổ Phần Phát Hành Sách Tp. HCM</p>
            <p>2. Trí Tuệ - Công Ty Cổ Phần Sách & Thiết Bị Giáo Dục Trí Tuệ</p>
            <p>3. Công Ty TNHH Đăng Nguyên</p>
            <p>4. Công Ty Cổ Phần Sách Mcbooks</p>
            <p>5. Công Ty TNHH Văn Hóa Việt Long</p>

        </section>

    </main>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
</body>
</html>
