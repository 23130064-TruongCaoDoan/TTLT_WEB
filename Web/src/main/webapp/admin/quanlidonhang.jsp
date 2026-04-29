<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
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
                        <div class="find">
                            <form method="get" action="OrderManagerServlet" class="filter-form" id="filterForm">
                                <input type="text" class="search" name="q" value="${param.q}" placeholder="Tìm kiếm đơn hàng...">
                                <div class="date-filter">
                                    <label for="fromDate">Từ:</label>
                                        <input type="date" name="fromDate" id="fromDate" value="${param.fromDate}">
                                </div>
                                <div class="date-filter">
                                    <label for="toDate">Đến:</label>
                                    <input type="date" name="toDate" id="toDate" value="${param.toDate}">
                                </div>
                                <select class="locorder" name="statusFilter" onchange="this.form.submit()">
                                    <option value="all">Tất cả trạng thái</option>
                                    <option value="PENDING" ${param.statusFilter == 'PENDING' ? 'selected' : ''}>Chờ xác nhận</option>
                                    <option value="PROCESSING" ${param.statusFilter == 'PROCESSING' ? 'selected' : ''}>Đang xử lý</option>
                                    <option value="SHIPPING" ${param.statusFilter == 'SHIPPING' ? 'selected' : ''}>Đang vận chuyển</option>
                                    <option value="COMPLETED" ${param.statusFilter == 'COMPLETED' ? 'selected' : ''}>Đã giao</option>
                                    <option value="CANCELLED" ${param.statusFilter == 'CANCELLED' ? 'selected' : ''}>Đã huỷ</option>
                                    <option value="REFUNDED" ${param.statusFilter == 'REFUNDED' ? 'selected' : ''}>Hoàn trả</option>
                                </select>
                                <button type="submit" class="buttonSearch">Tìm kiếm</button>
                                <a href="OrderManagerServlet" class="buttonClear">Xóa lọc</a>
                            </form>
                        </div>
                    </div>
            <div class="order-list">
                <div class="title">
                    <h3>Danh sách đơn hàng</h3>
                        <select class="locorder" name="sortDate" form="filterForm" onchange="document.getElementById('filterForm').submit()">
                            <option value="all">Tất cả</option>
                            <option value="desc" ${param.sortDate == 'desc' ? 'selected' : ''}>Đơn hàng mới nhất</option>
                            <option value="asc" ${param.sortDate == 'asc' ? 'selected' : ''}>Đơn hàng cũ nhất</option>
                        </select>
                </div>
                <div class="table-wrapper" id="wrapper">
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
                                <td ><p><fmt:formatNumber value="${o.totalAmount}" type="number" groupingUsed="true"
                                                      maxFractionDigits="0"/> đ</p></td>
                                <td>${o.orderDate}</td>
                                <td>
                                    <input type="hidden" name="orderId" class="orderId" value="${o.id}" />
                                    <select name="status" class="status-select">
                                        <option value="${o.status}" selected disabled>
                                            <c:choose>
                                                <c:when test="${o.status.toUpperCase() == 'COMPLETED'}">Đã giao</c:when>
                                                <c:when test="${o.status.toUpperCase() == 'PENDING'}">Chờ xác nhận</c:when>
                                                <c:when test="${o.status.toUpperCase() == 'PROCESSING'}">Đang xử lý</c:when>
                                                <c:when test="${o.status.toUpperCase() == 'CANCELLED'}">Đã huỷ</c:when>
                                                <c:when test="${o.status.toUpperCase() == 'SHIPPING'}">Đang vận chuyển</c:when>
                                                <c:when test="${o.status.toUpperCase() == 'REFUNDED'}">Hoàn trả</c:when>
                                                <c:otherwise>${dto.order.status.toLowerCase()}</c:otherwise>
                                            </c:choose>
                                        </option>
                                        <c:forEach var="s" items="${transitions[o.status.toUpperCase()]}">
                                            <option value="${s}">
                                                <c:choose>
                                                    <c:when test="${s.toUpperCase() == 'COMPLETED'}">Đã giao</c:when>
                                                    <c:when test="${s.toUpperCase() == 'PENDING'}">Chờ xác nhận</c:when>
                                                    <c:when test="${s.toUpperCase() == 'PROCESSING'}">Đang xử lý</c:when>
                                                    <c:when test="${s.toUpperCase() == 'CANCELLED'}">Đã huỷ</c:when>
                                                    <c:when test="${s.toUpperCase() == 'SHIPPING'}">Đang vận chuyển</c:when>
                                                    <c:when test="${s.toUpperCase() == 'REFUNDED'}">Hoàn trả</c:when>
                                                    <c:otherwise>${dto.order.status.toLowerCase()}</c:otherwise>
                                                </c:choose>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
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
<script>
    function bindStatusEvents() {
        document.querySelectorAll(".status-select").forEach(select => {
            select.addEventListener("change", handleChange);
        });
    }
    function handleChange() {
        const statusSelected = this.value;
        const orderId = this.closest("td").querySelector(".orderId").value;

        const q = document.querySelector('input[name="q"]')?.value || "";
        const fromDate = document.querySelector('input[name="fromDate"]')?.value || "";
        const toDate = document.querySelector('input[name="toDate"]')?.value || "";
        const sortDate = document.querySelector('select[name="sortDate"]')?.value || "all";

        if (!confirm("Bạn chắc chắn muốn thay đổi trạng thái?")) {
            location.reload();
            return;
        }

        const data = new URLSearchParams();
                data.append("orderId", orderId);
                data.append("orderStatus", statusSelected);
                data.append("q", q);
                data.append("fromDate", fromDate);
                data.append("toDate", toDate);
                data.append("sortDate", sortDate);

        fetch(contextPath + "/UpdateOrderStatus", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "orderId=" + orderId + "&orderStatus=" + statusSelected
        })
            .then(res => res.text())
            .then(html => {
                document.getElementById("wrapper").innerHTML = html;

                bindStatusEvents();
            })
            .catch(err => {
                alert("Lỗi!");
                console.error(err);
            });
    }
</script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        bindStatusEvents();
    });
</script>
<script>
    let fromDateInput = document.getElementById("fromDate");
    let toDateInput = document.getElementById("toDate");

    if(fromDateInput && toDateInput) {
        fromDateInput.addEventListener("change", function (){
           toDateInput.setAttribute("min", this.value);
        });
        toDateInput.addEventListener("change", function (){
            fromDateInput.setAttribute("max", this.value);
        });
    }
</script>
</body>
</html>