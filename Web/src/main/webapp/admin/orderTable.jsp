<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

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
