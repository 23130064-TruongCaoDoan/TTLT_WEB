<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %><!DOCTYPE html>
<fmt:setLocale value="vi_VN"/>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Đơn hàng của tôi</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Cormorant+Garamond:ital,wght@0,300..700;1,300..700&family=Libre+Franklin:ital,wght@0,100..900;1,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Playwrite+DE+SAS:wght@100..400&family=Sarabun:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/myOrder.css">
</head>
<body>
<c:import url="/user/headerUser.jsp"></c:import>
<div class="content">
    <div class="container">
        <div class="menuUser">
            <c:import url="/user/menuUser.jsp"></c:import>
        </div>
        <div class="manage-order">
            <div class="menu-bar">
                <div class="menu-item all active">
                    <p>Tất cả</p>
                </div>
                <div class="menu-item">
                    <p>Đang xử lí</p>
                </div>
                <div class="menu-item">
                    <p>Hoàn thành</p>
                </div>
                <div class="menu-item">
                    <p>Đã hủy</p>
                </div>
            </div>
            <div class="order-content">

                <c:if test="${empty orders}">
                    <p>Chưa có đơn hàng nào.</p>
                </c:if>

                <c:forEach var="o" items="${orders}">
                    <div class="card-order">

                        <!-- TOP -->
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

                        <!-- BOTTOM -->
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

                                    <c:if test="${o.status.toLowerCase() == 'completed' && !o.reviewed}">
                                        <button class="action-btn writeReviewBtn"
                                                data-order-id="${o.orderId}">
                                            Viết đánh giá
                                        </button>
                                    </c:if>

                                </div>
                            </div>
                        </div>

                    </div>
                </c:forEach>

            </div>

        </div>
        </div>
    </div>
<c:import url="/user/footerUser.jsp"></c:import>

    <div id="overlay" class="overlay"></div>
    <div id="reviewPopup" class="popup" style="display: none;">

        <form id="reviewForm" action="${pageContext.request.contextPath}/comment" method="post" enctype="multipart/form-data">
            <input type="hidden" id="order" name="orderId">
            <label>Đánh giá</label>
            <select id="reviewStars" name="rating" required>
                <option value="5">★★★★★</option>
                <option value="4">★★★★</option>
                <option value="3">★★★</option>
                <option value="2">★★</option>
                <option value="1">★</option>
            </select>
            <label>Nhận xét</label>
            <textarea rows="4" placeholder="Nhập đánh giá của bạn..." name="content" required></textarea>
            <input type="file" name="image" accept="image/*" >
            <div class="popup-actions">
                <button type="submit" id="submitReview">Gửi</button>
                <button type="button" class="close-popup">Hủy</button>
            </div>
        </form>
    </div>
<script>
        const menuItems = document.querySelectorAll(".menu-item");
        menuItems.forEach(item => {
            item.addEventListener("click", function() {
                menuItems.forEach(i => i.classList.remove("active"));
                this.classList.add("active");
            });
        });
</script>
<script>
    const overlay = document.getElementById("overlay");
    const popup = document.getElementById("reviewPopup");
    const closeBtn = document.querySelector(".close-popup");
    const orderIdInput = document.getElementById("order");
    const reviewForm = document.getElementById("reviewForm");

    // Mở popup
    document.querySelectorAll(".writeReviewBtn").forEach(btn => {
        btn.addEventListener("click", () => {
            const orderId = btn.dataset.orderId;
            orderIdInput.value = orderId;

            overlay.style.display = "block";
            popup.style.display = "block";
        });
    });

    function closePopup() {
        overlay.style.display = "none";
        popup.style.display = "none";
        reviewForm.reset();
        orderIdInput.value = "";
    }

    overlay.addEventListener("click", closePopup);
    closeBtn.addEventListener("click", closePopup);

    reviewForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const submitBtn = document.getElementById("submitReview");
        const formData = new FormData(this);

        submitBtn.disabled = true;
        submitBtn.textContent = "Đang gửi...";

        fetch(this.action, {
            method: "POST",
            body: formData,
            credentials: "same-origin"
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                alert("Đánh giá của bạn đã được gửi thành công!");
                closePopup();

                setTimeout(() => {
                    window.location.reload();
                }, 500);
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Có lỗi xảy ra. Vui lòng thử lại!");
            })
            .finally(() => {
                submitBtn.disabled = false;
                submitBtn.textContent = "Gửi";
            });
    });
</script>

</body>
</html>