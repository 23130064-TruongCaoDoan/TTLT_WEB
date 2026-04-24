<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
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
    <link rel="stylesheet" href="assets/css/myOrder.css?v=2">
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
                <div class="menu-item active" data-status="ALL">
                    <p>Tất cả</p>
                </div>
                <div class="menu-item" data-status="PENDING">
                    <p>Chờ xác nhận</p>
                </div>
                <div class="menu-item" data-status="PROCESSING">
                    <p>Đang xử lí</p>
                </div>
                <div class="menu-item" data-status="SHIPPING">
                    <p>Đang giao</p>
                </div>
                <div class="menu-item" data-status="COMPLETED">
                    <p>Hoàn thành</p>
                </div>
                <div class="menu-item" data-status="CANCELLED">
                    <p>Đã hủy</p>
                </div>
                <div class="menu-item" data-status="REFUNDED">
                    <p>Hoàn trả</p>
                </div>
            </div>
            <div class="order-content" id="myOrderList">

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
                                          o.status.toLowerCase() == 'processing'   ? 'status-waiting'   :
                                          o.status.toLowerCase() == 'shipping'  ? 'status-shipping'  :
                                          o.status.toLowerCase() == 'cancelled' ? 'status-cancel'    : ''}">

                                    <c:choose>
                                        <c:when test="${o.status.toLowerCase() == 'completed'}">Đã giao</c:when>
                                        <c:when test="${o.status.toLowerCase() == 'pending'}">Chờ xác nhận</c:when>
                                        <c:when test="${o.status.toLowerCase() == 'processing'}">Đang xử lý</c:when>
                                        <c:when test="${o.status.toLowerCase() == 'cancelled'}">Đã huỷ</c:when>
                                        <c:when test="${o.status.toLowerCase() == 'shipping'}">Đang vận chuyển</c:when>
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
                                    <span class="price"><fmt:formatNumber value="${ o.totalAmount}" type="currency"/></span>
                                </div>
                                <div id="groupButtonOrder">
                                    <c:if test="${ o.status.toLowerCase() == 'pending' || o.status.toLowerCase() == 'processing'}">
                                        <div class="button">
                                            <button id="BtnCancelled" data-order-id="${o.orderId}">
                                                Hủy đơn hàng
                                            </button>
                                        </div>
                                    </c:if>
                                    <c:if test="${ o.status.toLowerCase() == 'completed'}">
                                        <div class="button">
                                            <button onclick="window.location='rebuy?OrderId=${o.orderId}'">
                                                Mua lại
                                            </button>
                                        </div>
                                        <c:if test="${o.canReturn}">
                                            <div class="button">
                                                <button onclick="openReturnModal(${o.orderId})" class="btn-return">
                                                    Trả hàng / Hoàn tiền
                                                </button>
                                            </div>
                                        </c:if>
                                    </c:if>
                                    <div class="button">
                                        <button onclick="window.location='my-order?id=${o.orderId}'">
                                            Xem chi tiết
                                        </button>
                                    </div>
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

    <div id="returnModal" class="modal-return">
        <div class="modal-return-content">
            <h3>Yêu cầu Trả hàng</h3>
            <form id="returnForm" enctype="multipart/form-data">
                <input type="hidden" name="orderId" id="returnOrderId">

                <div class="form-group">
                    <label>Lý do trả hàng:</label>
                    <select name="reason" required>
                        <option value="" disabled selected>-- Chọn lý do --</option>
                        <option value="Hàng lỗi, rách bìa, hỏng">1. Hàng lỗi, rách bìa, hỏng</option>
                        <option value="Giao sai sản phẩm">2. Giao sai sản phẩm</option>
                        <option value="Thiếu sách">3. Thiếu sách</option>
                        <option value="Sản phẩm khác với mô tả">4. Sản phẩm khác với mô tả</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Ảnh chứng minh (Bắt buộc):</label>
                    <input type="file" name="proofImage" accept="image/*" required>
                </div>

                <div class="modal-actions">
                    <button type="button" onclick="closeReturnModal()" class="btn-secondary">Hủy</button>
                    <button type="submit" class="btn-danger">Gửi yêu cầu</button>
                </div>
            </form>
        </div>
    </div>
<script>
        const menuItems = document.querySelectorAll(".menu-item");
        menuItems.forEach(item => {
            item.addEventListener("click", function() {
                menuItems.forEach(i => i.classList.remove("active"));
                this.classList.add("active");
                const status = this.dataset.status;
                fetch("${pageContext.request.contextPath}/my-orders?status=" + encodeURIComponent(status)+ "&sort=true")                    .then(request => request.text())
                    .then(html =>{
                        document.getElementById("myOrderList").innerHTML = html
                    })
                    .catch(err => console.error(err));
            });
        });
</script>
<script>
    const BtnCancelled = document.getElementById("BtnCancelled");
    BtnCancelled.addEventListener("click", function(e) {
        e.preventDefault();

        const orderId = this.getAttribute("data-order-id");

        if (!confirm("Bạn có chắc chắn muốn hủy đơn này?")) return;

        fetch("cancel-order", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "id=" + orderId
        })
            .then(res => {
                if (res.ok) {
                    alert("Đơn hàng hủy thành công");
                    location.reload();
                } else {
                    alert("Đơn hàng không thể hủy!");
                }
            })
            .catch(err => {
                console.error(err);
                alert("Có lỗi xảy ra!");
            });
    });
</script>
<script>
    function openReturnModal(orderId) {
        document.getElementById('returnOrderId').value = orderId;
        document.getElementById('returnModal').style.display = 'block';
    }

    function closeReturnModal() {
        document.getElementById('returnModal').style.display = 'none';
        document.getElementById('returnForm').reset();
    }

    document.getElementById('returnForm').addEventListener('submit', function(e) {
        e.preventDefault(); // Chặn tải lại trang

        let formData = new FormData(this);

        let submitBtn = this.querySelector('button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Đang gửi...';

        fetch('request-return', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if(data.success) {
                alert(data.message);
                closeReturnModal();
                location.reload();
            } else {
                alert(data.message);
                submitBtn.disabled = false;
                submitBtn.textContent = 'Gửi yêu cầu';
            }
        })
        .catch(err => {
            console.error(err);
            alert("Lỗi khi tải ảnh hoặc kết nối server!");
            submitBtn.disabled = false;
            submitBtn.textContent = 'Gửi yêu cầu';
        });
    });
</script>
</body>
</html>