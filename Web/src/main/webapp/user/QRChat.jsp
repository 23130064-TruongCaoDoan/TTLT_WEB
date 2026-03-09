<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>QRChat</title>
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css/QRChat.css">
</head>
<body>

<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="content">
        <div class="container">
            <div class="zalo-box">
                <h3 class="zalo-title">Chat trên Zalo</h3>
                <img class="zalo-qr" src="assets/img/QR/zalo.jpg" alt="QR Zalo">
                <p class="zalo-desc">Quét QR code để chat trực tiếp</p>
            </div>
        </div>
    </div>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
</body>
</html>