<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="assets/css_admin/headerAdmin.css">
    <link rel="stylesheet" href="assets/css_admin/notifySuccess.css">
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

<!-- Global Confirm Popup cho Admin -->
<div class="overlay" id="globalConfirmOverlay" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:9998;"></div>
<div id="globalConfirmPopup" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:25px; border-radius:12px; box-shadow:0 5px 15px rgba(0,0,0,0.3); z-index:9999; text-align:center; min-width: 320px;">
    <p style="margin-bottom:25px; font-size:16px; color:#333; font-weight: 500;" id="globalConfirmMessage"></p>
    <div style="display:flex; justify-content:center; gap:15px;">
        <button id="btnGlobalConfirmYes" style="background:#d9534f; color:white; border:none; padding:10px 20px; border-radius:6px; cursor:pointer; font-weight: bold; font-size: 14px; transition: background 0.3s;">Xác nhận</button>
        <button id="btnGlobalConfirmNo" style="background:#f0f0f0; color:#333; border:none; padding:10px 20px; border-radius:6px; cursor:pointer; font-weight: bold; font-size: 14px; transition: background 0.3s;">Hủy</button>
    </div>
</div>
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

    let globalConfirmCallback = null;
    let globalCancelCallback = null;

    function showConfirm(message, callback, cancelCallback = null) {
        document.getElementById("globalConfirmMessage").innerText = message;
        document.getElementById("globalConfirmOverlay").style.display = "block";
        document.getElementById("globalConfirmPopup").style.display = "block";
        globalConfirmCallback = callback;
        globalCancelCallback = cancelCallback;
    }

    function closeGlobalConfirm() {
        document.getElementById("globalConfirmOverlay").style.display = "none";
        document.getElementById("globalConfirmPopup").style.display = "none";
        globalConfirmCallback = null;
        globalCancelCallback = null;
    }

    document.getElementById("btnGlobalConfirmNo").addEventListener("click", () => {
        if (globalCancelCallback) globalCancelCallback();
        closeGlobalConfirm();
    });
    document.getElementById("globalConfirmOverlay").addEventListener("click", closeGlobalConfirm);
    document.getElementById("btnGlobalConfirmYes").addEventListener("click", function() {
        if (globalConfirmCallback) globalConfirmCallback();
        closeGlobalConfirm();
    });
</script>
</body>
</html>
