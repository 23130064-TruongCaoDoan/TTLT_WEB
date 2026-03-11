<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Title</title>
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
                    <div class="danhMuc sach">
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
                    <input type="search" name="bSearch" placeholder="Tìm kiếm sách"/>
                    <button type="submit">Tìm Kiếm</button>
                </form>
                <a href="<c:url value='${empty user ? "/login" : "/SetUpAccount"}' />" class="button bt taikhoan">
                    <i class="fa-solid fa-user"></i>
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
                    <c:if test="${numNotiFy > 0}">
                    <span class="number">${sessionScope.numNotiFy}</span>
                    </c:if>
                    <span>Thông báo</span>
                </a>
            </div>
        </div>
    </div>
</div>
<div id="toast" class="toast"></div>
</body>
</html>
