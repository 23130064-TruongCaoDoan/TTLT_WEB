<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hoá đơn</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css_admin/quanlidonhang.css">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <link rel="stylesheet" href="assets/css_admin/notifySuccess.css">

</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>

    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>

        <div class="order-container">
            <h2>Quản lý đơn hàng</h2>
            <div class="function">
                <div class="find" style="width: 100%">
                    <form action="" method="get" action="OrderManagerServlet">
                        <input type="text" class="search" name="q" value="${param.q}" placeholder="Tìm kiếm sản phẩm">
                        <select class="locorder" name="sortDate" style="width: 25%" onchange="this.form.submit()">
                            <option value="all">Tất cả</option>
                            <option value="desc" ${param.sortDate == 'desc' ? 'selected' : ''}>Đơn hàng mới nhất</option>
                            <option value="asc" ${param.sortDate == 'asc' ? 'selected' : ''}>Đơn hàng cũ nhất</option>
                        </select>
                        <button type="submit" class="buttonSearch">Tìm kiếm</button>
                    </form>
                </div>
            </div>
            <div class="order-list">
                <div class="title">
                    <h3>Danh sách đơn hàng</h3>
                </div>
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã đơn hàng</th>
                            <th>Tên khách hàng</th>
                            <th>Tổng tiền</th>
                            <th>Ngày đặt</th>
                            <th>Trạng thái</th>
                            <th>Chỉnh sửa</th>
                            <th>Các sản phẩm</th>
                        </tr>
                        </thead>
                        <tbody id="orderTable">
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td>${o.id}</td>
                                <td>${o.userName}</td>
                                <td>${o.totalAmount} đ</td>
                                <td>${o.orderDate}</td>
                                <td>${o.status}</td>
                                <td>
                                    <i class="fa-solid fa-pen sua"
                                       data-id="${o.id}"
                                       data-name="${o.userName}"
                                       data-phone="${o.phone}"
                                       data-address="${o.address}"
                                       data-total="${o.totalAmount}"
                                       data-note="${o.note}">
                                    </i>

                                    <i class="fa-solid fa-trash xoa"
                                       onclick="deleteOrder(${o.id})">
                                    </i>
                                </td>
                                <td class="cacsp" data-id="${o.id}">xem</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div id="overlay"></div>
    <div method="post" id="orderForm">
        <div class="form-group">
            <label>Mã đơn hàng</label>
            <input type="number" id="orderCode" value="001" disabled>
        </div>

        <div class="form-group">
            <label>Tên khách hàng</label>
            <input type="text" id="orderTitle" value="Nguyễn Văn A" disabled>
        </div>

        <div class="form-group">
            <label>Tổng tiền (VNĐ)</label>
            <input type="number" id="orderValue" value="50">
        </div>

        <div class="form-group">
            <label>Địa chỉ giao hàng</label>
            <input id="typeBook" placeholder="Nhập địa chỉ giao hàng..." disabled>
        </div>

        <div class="form-group">
            <label>Số điện thoại</label>
            <input id="ageBook" placeholder="Nhập số điện thoại khách hàng..." disabled>
        </div>

        <div class="form-group">
            <label>Phương thức thanh toán</label>
            <input id="bookCodes" placeholder="VD: Tiền mặt, Chuyển khoản..." disabled>
        </div>

        <div class="form-group">
            <label>Ghi chú</label>
            <textarea rows="4" cols="50" placeholder="Nhập nội dung..." disabled></textarea>
        </div>

        <div class="form-group-inline">
            <div>
                <label>Trạng thái đơn hàng</label>
                <select id="orderStatus">
                    <option value="Chờ xử lý">Chờ xử lý</option>
                    <option value="Completed">Đã giao</option>
                    <option value="Đã Huỷ">Đã Huỷ</option>
                </select>
            </div>
        </div>

        <button type="button" class="btn-save" id="btnSaveOrder">Lưu đơn hàng</button>
    </div>
    <div id="listSP">
        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Mã sách</th>
                    <th>Tên sách</th>
                    <th>Tác giả</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Loại sách</th>
                    <th>Độ tuổi</th>
                    <th>Hình ảnh</th>
                </tr>
                </thead>
                <tbody id="productTableBody">

                </tbody>
            </table>
        </div>
    </div>
</main>
<div id="deleteOrderPopup" class="delete-popup">
    <p>Bạn có chắc chắn muốn xóa đơn hàng này không?</p>
    <div class="delete-actions">
        <button class="btn-delete" onclick="confirmDeleteOrder()">Xóa</button>
        <button class="btn-cancel" onclick="closeDeleteOrderPopup()">Hủy</button>
    </div>
</div>
<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>

<script src="assets/js/qldh.js"></script>
</body>
</html>