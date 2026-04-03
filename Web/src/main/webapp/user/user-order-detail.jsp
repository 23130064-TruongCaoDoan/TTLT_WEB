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
    <link rel="stylesheet" href="assets/css/home.css">
    <link rel="stylesheet" href="assets/css/errolpage.css">
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/myOrder.css">
    <link rel="stylesheet" href="assets/css/orderDetail.css">
    <link rel="stylesheet" href="assets/css/shoppingCart.css">
</head>
<body>
<c:import url="/user/headerUser.jsp"></c:import>
<div class="content">
    <div class="container">
        <div class="menuUser">
            <c:import url="/user/menuUser.jsp"></c:import>
        </div>
        <div class="manage-order">
            <div class="order-detail">
                <div style="display: flex;justify-content: space-between;">
                    <div class="state">
                        <h2>Mã Đơn Hàng #${dto.order.id}</h2>
                        <p class="order-state">${dto.order.status}</p>

                    </div>
                    <p class="order-date">
                        Ngày mua: ${dto.order.orderDate}
                    </p>
                </div>
                
                <div class="order-progess">
                    <div class="new-order">
                        <i class="fa-solid fa-clipboard-list"></i>
                        <span style="position: relative;top:-7px">
                            <span style="font-weight: bold;">Đơn hàng mới</span>
                            <br>
                            <span style="position: absolute;left: 0.4px;width: 10vw;top: 22px;font-size: small;">${dto.order.orderDate}</span>

                        </span>
                        
                    </div>
                    <div class="order-pending">
                        <i class="fa-solid fa-box"></i>
                        <span style="font-weight: bold;">Đang xử lý</span>
                    </div>
                    <div class="order-end">
                        <i class="fa-solid fa-check"></i>
                        <span style="position: relative;top:-7px">
                            <span style="font-weight: bold;">Đã giao</span>
                        <br>
                        <span style="position: absolute;left: 0.4px;width: 10vw;top: 22px;font-size: small;">${dto.order.orderDate}</span>
                        </span>
                        
                    </div>
                </div>
                <div class="order-info">
                    <div class="user-info">
                        <h3>Thông tin người nhận</h3>
                        <p>${dto.address.name}</p>
                        <p>SĐT: ${dto.address.phone}</p>
                        <p>
                            Địa chỉ:
                            ${dto.address.specificAddress},
                            ${dto.address.ward},
                            ${dto.address.city}
                        </p>
                    </div>
                    <div class="payment-method">
                        <h3>Phương thức thanh toán</h3>
                        <p>${dto.order.paymentMethod}</p>
                    </div>
                    <div class="order-price">                   
                        <h3>Tổng tiền</h3>
                        <div>
                            <p>Tạm tính:</p>
                            <p>
                                <fmt:formatNumber value="${dto.order.totalAmount - dto.shipping.shippingCost}" type="currency"/>
                            </p>
                        </div>
                        <div>
                            <p>Phí vận chuyển</p>
                            <p>
                                <fmt:formatNumber value="${dto.shipping.shippingCost}" type="currency"/>
                            </p>
                        </div>
                        <div>
                            <h3>Tổng số tiền (Gồm VAT):</h3>
                            <p>
                                <fmt:formatNumber value="${dto.order.totalAmount}" type="currency"/>
                            </p>
                        </div>
                    </div>
                </div>
                <div style="display: flex;justify-content: space-between;margin-top: 20px;">
                    <p class="order-note">*Trạng thái đơn hàng:
                        <c:choose>
                        <c:when test="${dto.order.status == 'Completed'}">Đã giao</c:when>
                        <c:when test="${dto.order.status == 'PENDING'}">Đang xử lý</c:when>
                        <c:when test="${dto.order.status == 'NOPAID'}">Đang xử lý</c:when>
                        <c:when test="${dto.order.status == 'CANCELLED'}">Đã huỷ</c:when>
                        <c:otherwise>${dto.order.status}</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                </div>
  
            <div class="footer-order-detail">

                <div class="note" style="width: 100%">
                    <h3>Ghi chú:</h3>
                    <p>
                        <c:choose>
                            <c:when test="${empty dto.order.note}">
                                (không có)
                            </c:when>
                            <c:otherwise>
                                ${dto.order.note}
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
            <div class="order-detail-shipping">

            <div class="container-cart">
                <div class="left-container">
                <div class="left">
                    <div class="header-cart">
                    <p></p>
                    <p>sản phẩm</p>
                    <span>Số lượng</span>
                    <span>Thành tiền</span>
                    </div>
                    <c:forEach var="item" items="${dto.items}">
                        <div class="card-item">
                            <c:url var="detailUrl" value="/productDetail">
                                <c:param name="id" value="${item.bookId}"/>
                                <c:param name="type" value="${item.type}"/>
                            </c:url>
                            <a href="${detailUrl}"><img src="${item.coverImgUrl}" alt=""/></a>
                            <div class="item-info">
                                <a href="${detailUrl}" style="text-decoration: none">
                                    <h4>${item.title}</h4>
                                </a>
                            </div>

                            <div class="quantity">
                                <p>${item.quantity}</p>
                            </div>

                            <div class="total-cost">
                                <p class="cost">
                                    <fmt:formatNumber value="${item.subtotal}" type="currency"/>
                                </p>
                                    <c:if test="${isNewOrder}">
                                        <c:if test="${!item.reviewed && isCompleted}">
                                            <button class="writeReviewBtn" data-order-id="${dto.order.id}" data-book-id="${item.bookId}">
                                                Viết đánh giá
                                            </button>
                                        </c:if>

                                        <c:if test="${item.reviewed}">
                                            <button class="viewReview" data-order-id="${dto.order.id}" data-book-id="${item.bookId}">
                                                Xem đánh giá
                                            </button>
                                        </c:if>

                                    </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="total-order-detail">
                    <h3>TỔNG TIỀN: </h3>
                    <p class="total">
                        <fmt:formatNumber value="${dto.order.totalAmount}" type="currency"/>
                    </p>
                </div>
                </div>
            </div>
        </div>
        </div>
    </div>

    <c:import url="/user/footerUser.jsp"></c:import>
    <div id="overlay" class="overlay"></div>

    <div id="reviewPopup" class="popup" style="display: none;">

        <form id="reviewForm" action="${pageContext.request.contextPath}/comment" method="post" enctype="multipart/form-data">
            <label>Đánh giá</label>
            <select id="reviewStars" name="rating" required>
                <option value="5">★★★★★</option>
                <option value="4">★★★★</option>
                <option value="3">★★★</option>
                <option value="2">★★</option>
                <option value="1">★</option>
            </select>
            <label>Nhận xét</label>
            <input type="hidden" id="orderIdInput" name="orderId">
            <input type="hidden" id="bookIdInput" name="bookId">
            <textarea rows="4" placeholder="Nhập đánh giá của bạn..." name="content" required></textarea>
            <input type="file" name="image" accept="image/*" >
            <div class="popup-actions">
                <button type="submit" id="submitReview">Gửi</button>
                <button type="button" class="close-popup">Hủy</button>
            </div>
        </form>
    </div>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const menuItems = document.querySelectorAll(".menu-item");

        menuItems.forEach(item => {
            item.addEventListener("click", function() {
                menuItems.forEach(i => i.classList.remove("active"));
                this.classList.add("active");
            });
        });
    });
</script>
<script>
    const overlay = document.getElementById("overlay");
    const popup = document.getElementById("reviewPopup");
    const closeBtn = document.querySelector(".close-popup");
    const orderIdInput = document.getElementById("orderIdInput");
    const bookIdInput = document.getElementById("bookIdInput")
    const reviewForm = document.getElementById("reviewForm");

    document.querySelectorAll(".writeReviewBtn").forEach(btn => {
        btn.addEventListener("click", () => {
            const orderId = btn.dataset.orderId;
            const bookId = btn.dataset.bookId;
            orderIdInput.value = orderId;
            bookIdInput.value = bookId;

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