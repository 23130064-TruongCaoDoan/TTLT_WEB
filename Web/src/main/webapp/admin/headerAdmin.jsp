<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<header>
    <div class="logo left"><img src="assets/img/logo/logoChinh.png" alt="logo"></div>
    <div class="right">
        <i class="fa-solid fa-user"></i>
        <div class="ten">
            <c:if test="${not empty user}">
                ${user.getDisplayName()}
            </c:if>
            <c:if test="${empty user}">
                Tài khoản
            </c:if>
        </div>
        <button class="dangxuat" onclick="window.location.href='DangXuat'">Đăng xuất</button>
    </div>
</header>
<div id="toast" class="toast"></div>
</body>
</html>
