<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Voucher</title>
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css/ViVoucher.css">
    <link rel="stylesheet" href="assets/css/voucher.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="content">
        <div class="container">
            <div class="menuUser">
                <c:import url="/user/menuUser.jsp"></c:import>
            </div>
            <div class="voucher-box">
                <h2>Ví voucher</h2>
                <div class="voucher-list">
                    <c:if test="${empty discountVouchers and empty shipVouchers}">
                        <div class="no-voucher">
                            <p>Bạn chưa có voucher nào</p>
                        </div>
                    </c:if>

                    <!-- Voucher giảm giá -->
                    <c:forEach var="v" items="${discountVouchers}">
                        <div class="voucher-item">
                            <div class="voucher-left">
                                <i class="fa-solid fa-ticket"></i>
                            </div>
                            <div class="voucher-right">
                                <b>${v.description}</b>
                                <button class="voucher-detail"
                                        data-code="${v.code}"
                                        data-value="${v.valuee}"
                                        data-price="${v.conditionPrice}"
                                        data-end="${v.end_date}">
                                    Chi tiết
                                </button>
                                <br>
                                Đơn hàng từ ${v.conditionPrice}k
                                <c:if test="${not empty v.conditionBook}">
                                    - ${v.conditionBook}
                                </c:if>
                                <br>

                                <div class="voucher-code">${v.code}</div>

                                <div class="voucher-footer">
                    <span>
                        HSD:
                        ${v.end_date}
                    </span>
                                    <button class="copy-btn" data-code="${v.code}">Copy mã</button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Voucher vận chuyển -->
                    <c:forEach var="v" items="${shipVouchers}">
                        <div class="voucher-item">
                            <div class="voucher-left">
                                <i class="fa-solid fa-truck"></i>
                            </div>
                            <div class="voucher-right">
                                <b>${v.description}</b>
                                <br>
                                Áp dụng phí vận chuyển
                                <br>

                                <div class="voucher-code">${v.code}</div>

                                <div class="voucher-footer">
                    <span>
                        HSD:
                        ${v.end_date}
                    </span>
                                    <button class="voucher-detail"
                                            data-code="${v.code}"
                                            data-value="${v.valuee}"
                                            data-price="${v.conditionPrice}"
                                            data-end="${v.end_date}">
                                        Chi tiết
                                    </button>

                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>

            </div>
        </div>
    </div>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
<div class="overlay" id="overlay"></div>

<div id="voucherPopup" class="popup">
    <div class="popup-content">
        <h3>ĐIỀU KIỆN ÁP DỤNG</h3>

        <div class="popup-body">
            <div class="detail-popup-voucher">
                <p><b>Mã voucher:</b> <span id="pv-code"></span></p>
                <p><b>Giá trị:</b> <span id="pv-value"></span></p>
                <p><b>Đơn tối thiểu:</b> <span id="pv-price"></span></p>
                <p><b>Hạn sử dụng:</b> <span id="pv-end"></span></p>
            </div>

            <div class="voucher-condition">
                <p>- Áp dụng cho đơn hàng KHÔNG bao gồm Ngoại Văn, Manga, Phiếu Quà Tặng, SGK...</p>
                <p>- Có thể áp dụng cùng mã giảm phí vận chuyển.</p>
            </div>
        </div>

        <button class="copy-code">COPY MÃ</button>
        <button class="cancel">×</button>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {

        const overlay = document.getElementById("overlay");
        const popup = document.getElementById("voucherPopup");
        const cancelBtn = popup.querySelector(".cancel");
        const copyBtn = popup.querySelector(".copy-code");

        const pvCode  = document.getElementById("pv-code");
        const pvValue = document.getElementById("pv-value");
        const pvPrice = document.getElementById("pv-price");
        const pvEnd   = document.getElementById("pv-end");

        document.addEventListener("click", function (e) {
            const btn = e.target.closest(".voucher-detail");
            if (!btn) return;

            e.preventDefault();

            const { code, value, price, end } = btn.dataset;

            pvCode.textContent  = code || "";
            pvValue.textContent = value || "";
            pvPrice.textContent = price || "";
            pvEnd.textContent   = end || "";

            overlay.style.display = "block";
            popup.style.display = "block";
        });

        copyBtn.addEventListener("click", function () {
            const code = pvCode.textContent.trim();
            if (!code) return;
            navigator.clipboard.writeText(code);
        });

        function closePopup() {
            overlay.style.display = "none";
            popup.style.display = "none";
        }

        cancelBtn.addEventListener("click", closePopup);
        overlay.addEventListener("click", closePopup);
    });

</script>




</body>


</html>