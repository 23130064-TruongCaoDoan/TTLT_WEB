
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="Menu">
    <div class="title"><span>CHỨC NĂNG</span></div>
    <div class="menfunction">
        <a href="<c:url value="/ThongKe" />" class="function thongke">Thống kê</a>
        <a href="<c:url value="/product-manage" />" class="function qlsanpham">Quản lý sản phẩm</a>
        <a href="<c:url value="/user-manage" />" class="function qlkhachhang">Quản lý khách hàng</a>
        <a href="<c:url value="/OrderManagerServlet" />" class="function qldonhang">Quản lý đơn hàng</a>
        <a href="<c:url value="/admin-return-manager" />" class="function qltrahang">Quản lý trả hàng</a>
        <a href="<c:url value="/KhoVoucher" />" class="function storeVoucher">Kho Voucher</a>
        <a href="<c:url value="/Event" />" class="function event">Sự kiện</a>
        <a href="<c:url value="/Rate" />" class="function rating">Đánh giá</a>
    </div>
</div>
</body>
</html>
