<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/search-suggest.css">
</head>
<body>
<div id="home-page">
    <div id="page-header">
        <div class="header-message">
            <div class="message"></div>
            <div class="messageBorder"></div>
        </div>
        <div class="container">
            <div class="header-title">
                <a href="" class="logo">
                    <img
                            src="${pageContext.request.contextPath}/assets/img/logo/logoChinh.png"
                            alt="Sách thiếu nhi cho bé"
                    />
                </a>
            </div>
            <div class="header-menu">
                <a href="<c:url value="/home"   />" class="button bt"
                ><i class="fa-solid fa-house"></i><span>Trang chủ</span></a
                >
                <div class="button category">
                    <a href="<c:url value="/dsSanPham" />" class="button bt danhmuc">
                        <i class="fa-solid fa-list"></i><span>Danh mục</span></a
                    >
                    <div class="danhMuc sach" style="display: none">
                        <div class="item truyenTranh">
                            <a href="search?bSearch=Truyện tranh" class="it truyen-tranh"
                            ><span>Truyện tranh</span></a
                            >
                        </div>
                        <div class="item anh">
                            <a href="search?bSearch=Sách ảnh" class="it sach-anh"><span>Sách ảnh</span></a>
                        </div>
                        <div class="item giaoDuc">
                            <a href="search?bSearch=Giáo dục" class="it giao-duc"><span>Giáo dục</span></a>
                        </div>
                        <div class="item toMau">
                            <a href="search?bSearch=Sách tô màu" class="it to-mau"><span>Sách tô màu</span></a>
                        </div>
                    </div>
                </div>
                <form action="search" method="get" class="search">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input type="search" class="search-input" name="bSearch" value="${bSearch != null ? bSearch : ''}" autocomplete="off" placeholder="Tìm kiếm sách"/>
                    <div class="suggest-box"></div>
                    <button type="submit">Tìm Kiếm</button>
                </form>
                <a href="<c:url value='${empty user ? "/login" : "/SetUpAccount"}' />" class="button bt taikhoan">
                    <c:choose>
                        <c:when test="${not empty user and not empty user.avatar}">
                            <img src="${user.avatar}" alt="Avatar" style="width: 25px; height: 25px; border-radius: 50%; object-fit: cover; margin-right: 5px;">
                                </c:when>
                                <c:otherwise>
                                    <i class="fa-solid fa-user"></i>
                                </c:otherwise>
                    </c:choose>
                    <span>
                            <c:if test="${not empty user}">
                                ${user.getDisplayName()}
                            </c:if>
                            <c:if test="${empty user}">
                                Tài khoản
                            </c:if>
                        </span>
                </a>
                <a href="<c:url value="/ShoppingCart" />" class="button bt gio">
                    <i class="fa-solid fa-cart-shopping"><span class="number" id="totalItem"><c:if test="${cart.totalQuantity > 0}">${cart.totalQuantity}</c:if></span></i>
                    <span>Giỏ hàng</span>
                </a>
                <a href="thong-bao" class="button bt thongbao">
                    <i class="fa-solid fa-bell"></i>
                    <c:if test="${sessionScope.numNotiFy > 0}">
                    <span class="number" id="noti-badge">${sessionScope.numNotiFy}</span>
                    </c:if>
                    <span>Thông báo</span>
                </a>
            </div>
        </div>
    </div>
</div>
<div id="toast" class="toast"></div>

<!-- Global Confirm Popup cho User -->
<div class="overlay" id="globalConfirmOverlay" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:9998;"></div>
<div id="globalConfirmPopup" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:25px; border-radius:12px; box-shadow:0 5px 15px rgba(0,0,0,0.3); z-index:9999; text-align:center; min-width: 320px;">
    <p style="margin-bottom:25px; font-size:16px; color:#333; font-weight: 500;" id="globalConfirmMessage"></p>
    <div style="display:flex; justify-content:center; gap:15px;">
        <button id="btnGlobalConfirmYes" style="background:#d9534f; color:white; border:none; padding:10px 20px; border-radius:6px; cursor:pointer; font-weight: bold; font-size: 14px; transition: background 0.3s;">Xác nhận</button>
        <button id="btnGlobalConfirmNo" style="background:#f0f0f0; color:#333; border:none; padding:10px 20px; border-radius:6px; cursor:pointer; font-weight: bold; font-size: 14px; transition: background 0.3s;">Hủy</button>
    </div>
</div>

<script>
    let globalConfirmCallback = null;

    function showConfirm(message, callback) {
        document.getElementById("globalConfirmMessage").innerText = message;
        document.getElementById("globalConfirmOverlay").style.display = "block";
        document.getElementById("globalConfirmPopup").style.display = "block";
        globalConfirmCallback = callback;
    }

    function closeGlobalConfirm() {
        document.getElementById("globalConfirmOverlay").style.display = "none";
        document.getElementById("globalConfirmPopup").style.display = "none";
        globalConfirmCallback = null;
    }

    document.getElementById("btnGlobalConfirmNo").addEventListener("click", closeGlobalConfirm);
    document.getElementById("globalConfirmOverlay").addEventListener("click", closeGlobalConfirm);
    document.getElementById("btnGlobalConfirmYes").addEventListener("click", function() {
        if (globalConfirmCallback) globalConfirmCallback();
        closeGlobalConfirm();
    });

    const toast = document.getElementById("toast");

    <c:if test="${not empty sessionScope.loginSuccess}">
    toast.innerText = "${sessionScope.loginSuccess}";
    toast.classList.add("show");
    setTimeout(() => toast.classList.remove("show"), 3000);
    <c:remove var="loginSuccess" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.logoutSuccess}">
    toast.innerText = "${sessionScope.logoutSuccess}";
    toast.classList.add("show");
    setTimeout(() => toast.classList.remove("show"), 3000);
    <c:remove var="logoutSuccess" scope="session"/>
    </c:if>

    <c:if test="${not empty toastMessage}">
    toast.innerText = "${toastMessage}";
    toast.classList.add("show");
    setTimeout(() => toast.classList.remove("show"), 3000);
    </c:if>

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

    function showToast(message, isSuccess = true) {
        const t = document.getElementById("toast");
        if (t) {
            t.innerText = message;
            t.classList.remove("success", "error");
            if (isSuccess) t.classList.add("success");
            else t.classList.add("error");
            t.classList.add("show");
            setTimeout(() => t.classList.remove("show"), 3000);
        }
    }
</script>
<script src="${pageContext.request.contextPath}/assets/js/search-suggest.js"></script>
</body>
</html>
