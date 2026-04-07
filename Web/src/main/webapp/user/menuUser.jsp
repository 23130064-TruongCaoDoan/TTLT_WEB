<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="nameUser">
    <div class="anh" style="display: flex; justify-content: center; margin-bottom: 10px;">
        <c:choose>
            <c:when test="${not empty sessionScope.user.avatar}">
                <img src="${sessionScope.user.avatar}" alt="Avatar" style="width: 70px; height: 70px; border-radius: 50%; object-fit: cover; border: 2px solid #ccc;">
                    </c:when>
                    <c:otherwise>
                        <img src="assets/images/default-avatar.png" alt="Avatar" style="width: 70px; height: 70px; border-radius: 50%; object-fit: cover; border: 2px solid #ccc;">
                    </c:otherwise>
        </c:choose>
    </div>
    <div class="name">
        ${sessionScope.user.name}
    </div>
    <div class="point">
       Điểm: ${sessionScope.user.point}
    </div>
</div>
<div class="menuMain">
    <a href="SetUpAccount" class="menu ttcn">
        <i class="fa-regular fa-user"></i>
        <span>Thông tin cá nhân</span>
        <i class="fa-solid fa-arrow-down"></i>
    </a>

    <div class="menuInfor">
        <a href="SetUpAccount" class="title prof"><span>Hồ sơ cá nhân</span></a>
        <a href="address" class="title address"><span>Sổ địa chỉ</span></a>
        <a href="DoiMK" class="title passw"><span>Đổi mật khẩu</span></a>
    </div>

    <a href="my-orders" class="menu donhang">
        <i class="fa-solid fa-receipt"></i>
        <span>Đơn hàng của tôi</span>
    </a>

    <a href="my-vouchers" class="menu Voucher">
        <i class="fa-solid fa-ticket"></i>
        <span>Ví voucher</span>
    </a>

    <a href="thong-bao" class="menu thongbao">
        <i class="fa-regular fa-bell"></i>
        <span>Thông báo</span>
    </a>

    <a href="favoriteBook" class="menu spYeuThich">
        <i class="fa-regular fa-heart"></i>
        <span>Sản phẩm yêu thích</span>
    </a>
</div>

<div class="btDangXuat">
    <a href="DangXuat" class="dangXuat">Đăng xuất</a>
</div>
