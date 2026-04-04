<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="vi_VN"/>
<c:if test="${empty orders}">
    <p>Chưa có đơn hàng nào.</p>
</c:if>

<c:forEach var="o" items="${orders}">
    <div class="card-order">

        <div class="top">
            <p class="order-id">Mã đơn hàng: #${o.orderId}</p>

            <div class="order-status">
                <p class="time">${o.orderDate}</p>

                <p class="
                                        ${o.status.toLowerCase() == 'completed' ? 'status-delivered' :
                                          o.status.toLowerCase() == 'pending'   ? 'status-waiting'   :
                                          o.status.toLowerCase() == 'nopaid'   ? 'status-waiting'   :
                                          o.status.toLowerCase() == 'shipping'  ? 'status-shipping'  :
                                          o.status.toLowerCase() == 'cancelled' ? 'status-cancel'    : ''}">

                    <c:choose>
                        <c:when test="${o.status.toLowerCase() == 'completed'}">Đã giao</c:when>
                        <c:when test="${o.status.toLowerCase() == 'pending'}">Đang xử lý</c:when>
                        <c:when test="${o.status.toLowerCase() == 'nopaid'}">Đang xử lý</c:when>
                        <c:when test="${o.status.toLowerCase() == 'cancelled'}">Đã huỷ</c:when>
                        <c:otherwise>${o.status.toLowerCase()}</c:otherwise>
                    </c:choose>
                </p>

            </div>
        </div>

        <div class="center" style="display: flex">
            <div class="image">
                <img src="${o.firstBookImage}" alt="" />
            </div>

            <div class="info">
                <p class="book-name">Sản phẩm trong đơn hàng</p>
            </div>
        </div>

        <div class="bottom">
            <div class="quantity">
                Số lượng sản phẩm ${o.totalQuantity}
            </div>

            <div class="price-cart">
                <div class="total-price">
                    <span class="total">Tổng tiền:</span>
                    <span class="price">
                            <fmt:formatNumber value="${ o.totalAmount}" type="currency"/>
                        </span>
                </div>

                <div class="button">
                    <button onclick="window.location='my-order?id=${o.orderId}'">
                        Xem chi tiết
                    </button>
                </div>
            </div>
        </div>

    </div>
</c:forEach>
