
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
        <c:if test="${canViewStatistic}">
            <a href="<c:url value="/ThongKe" />" class="function thongke">Thống kê</a>
        </c:if>
        <c:if test="${canManageProduct}">
            <a href="<c:url value="/product-manage" />" class="function qlsanpham">Quản lý sản phẩm</a>
        </c:if>
        <c:if test="${canManageUser}">
            <a href="<c:url value="/user-manage" />" class="function qlkhachhang">Quản lý khách hàng</a>
        </c:if>
        <c:if test="${canSale}">
            <a href="<c:url value="/OrderManagerServlet" />" class="function qldonhang">Quản lý đơn hàng</a>
            <a href="<c:url value="/admin-return-manager" />" class="function qltrahang">Quản lý trả hàng</a>
            <a href="<c:url value="/publisher-manage" />" class="function qlnxb">Quản lý nhà xuất bản</a>
            <a href="<c:url value="/provider-manage" />" class="function qlnpp">Quản lý nhà phân phối</a>
            <a href="<c:url value="/author-manage" />" class="function qltacgia">Quản lý tác giả</a>
            <a href="<c:url value="/KhoVoucher" />" class="function storeVoucher">Kho Voucher</a>
            <a href="<c:url value="/Event" />" class="function event">Sự kiện</a>
        </c:if>
        <c:if test="${isStaff}">
            <a href="<c:url value="/Rate" />" class="function rating">Đánh giá</a>
        </c:if>
        <c:if test="${canImport}">
            <a href="<c:url value="/history-import" />" class="function import">Lịch sử nhập kho</a>
        </c:if>
    </div>
</div>
</body>
</html>
