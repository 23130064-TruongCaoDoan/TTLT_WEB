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
        <div class="notification-wrapper" style="position: relative; margin-right: 20px; cursor: pointer;" onclick="window.location.href='myHistory'">
            <i class="fa-solid fa-bell notification-icon"></i>
        </div>
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
<style>
    .notification-wrapper .badge {
            position: absolute;
            top: -5px;
            right: -8px;
            background-color: #e74c3c;
            color: white;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 11px;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
    }

    .toast {
        position: fixed;
        top: 90px;
        left: 50%;
        transform: translateX(-50%) translateY(-10px);

        background: #27ae60;
        color: #fff;
        padding: 12px 18px;
        border-radius: 8px;

        opacity: 0;
        pointer-events: none;
        transition: all 0.3s ease;

        z-index: 99999;
    }

    .toast.show {
        opacity: 1;
        transform: translateX(-50%) translateY(0);
    }
</style>

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
