<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="assets/css_admin/headerAdmin.css">
</head>
<body>
<header>
    <div class="logo left"><img src="assets/img/logo/logoChinh.png" alt="logo"></div>
    <div class="right">
        <c:if test="${isAdmin || isManager}">
            <div class="notification-wrapper" style="position: relative; margin-right: 20px; cursor: pointer;" onclick="window.location.href='myHistory'">
                <i class="fa-solid fa-clock-rotate-left notification-icon"></i>
                    <c:if test="${unreadLogCount != null && unreadLogCount > 0}">
                        <span class="badge">${unreadLogCount}</span>
                    </c:if>
            </div>
        </c:if>
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
<script>
    const toast = document.getElementById("toast");

    <% if (session.getAttribute("loginSuccess") != null) { %>
            sessionStorage.setItem("toastMessage", "Đăng nhập thành công");
            <% session.removeAttribute("loginSuccess"); %>
    <% } %>

    <% if (session.getAttribute("logoutSuccess") != null) { %>
            sessionStorage.setItem("toastMessage", "Đăng xuất thành công");
            <% session.removeAttribute("logoutSuccess"); %>
    <% } %>

    window.addEventListener("load", function () {
        const message = sessionStorage.getItem("toastMessage");
            if (message && toast) {
                toast.innerText = message;
                toast.classList.add("show");
                    setTimeout(() => {
                        toast.classList.remove("show");
                        sessionStorage.removeItem("toastMessage");
                    }, 3000);
            }
    });
</script>
</body>
</html>
