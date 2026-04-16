<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ThanhToan</title>
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css/ThanhToan.css">
    <link rel="stylesheet" href="assets/css/voucher.css">
</head>
<body>
<c:if test="${not empty error}">
    <div id="toast-error" class="error-message toast-error">
        <i class="fa-solid fa-circle-exclamation"></i>
            ${error}
    </div>
</c:if>
<div id="toast-term" class="error-message toast-error" style="display:none;">
    <i class="fa-solid fa-circle-exclamation"></i>
    Vui lòng đồng ý Điều khoản & Điều kiện trước khi thanh toán
</div>
<div class="page-wrapper">
    <c:import url="headerUser.jsp"> </c:import>
    <div class="content">
        <form method="get" action="ThanhToan" id="checkoutForm">
            <div class="container">
                <input type="hidden" name="mode" value="${param.mode}">
                <div class="checkout-section">
                    <div class="section-title">VOUCHER</div>
                    <div class="gift-infor">
                        <div class="input-row">
                            <div class="input-group">
                                <a href="#" class="more-voucher" id="choose-code">Chọn mã khuyến mãi</a>
                            </div>
                            <div style="color: #f7941d">
                                <c:if test="${numApplyVoucher >0}">
                                    <span>Đã áp dụng ${numApplyVoucher} voucher </span> </c:if>
                                <c:if test="${numApplyVoucher <=0}"><span> Chưa voucher nào được áp dụng </span> </c:if>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="checkout-section">
                    <div class="section-title">ĐỊA CHỈ GIAO HÀNG</div>
                    <c:forEach var="address" items="${listAddress}">
                        <div class="address-item">
                            <div class="address-info">
                                <input type="radio" name="addressId"
                                       value="${address.id}"
                                    ${address.id == selectedAddressId ? "checked" : ""}>
                                <span><b>${address.getName()}</b> | ${address.getSpecificAddress()}, ${address.getWard()},${address.getDistricts()}, ${address.getCity()}| ${address.getPhone()}</span>
                            </div>
                            <div class="address-actions">
                                <a href="editAddress?id=${address.id}" class="edit-btn" title="Chỉnh sửa">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                    <a class="add-address" href="addAddress"><i class="fa-solid fa-plus"></i> Giao hàng đến địa
                        chỉ khác</a>
                </div>
                <div class="checkout-section">
                    <div class="section-title">PHƯƠNG THỨC VẬN CHUYỂN</div>
                    <div id="shippingContainer">
                    </div>
                </div>
                <div class="checkout-section">
                    <div class="section-title">PHƯƠNG THỨC THANH TOÁN</div>
                    <div class="payment-item">
                        <input type="radio" class="vnpay" value="vnpay" name="payment">
                        <img src="https://vinadesign.vn/uploads/images/2023/05/vnpay-logo-vinadesign-25-12-57-55.jpg"
                             alt="">
                        <span>VNPAY</span>
                    </div>
                    <div class="payment-item">
                        <input type="radio" class="momo" value="momo" name="payment">
                        <img src="https://itviec.com/rails/active_storage/representations/proxy/eyJfcmFpbHMiOnsiZGF0YSI6MjA0NjgzMiwicHVyIjoiYmxvYl9pZCJ9fQ==--6d1081fa86f1300daa38e2cb2fd3ffc5a28b6592/eyJfcmFpbHMiOnsiZGF0YSI6eyJmb3JtYXQiOiJwbmciLCJyZXNpemVfdG9fbGltaXQiOlszMDAsMzAwXX0sInB1ciI6InZhcmlhdGlvbiJ9fQ==--e1d036817a0840c585f202e70291f5cdd058753d/MoMo%20Logo.png"
                             alt="">
                        <span>Ví Momo</span>
                    </div>
                    <div class="payment-item">
                        <input type="radio" class="money" value="cod" name="payment" checked>
                        <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5lvx2wxsU3oCisTG1mwVJfl7Jb8et02zZwg&s"
                             alt="">
                        <span>Thanh toán tiền mặt khi nhận hàng</span>
                    </div>
                </div>

                <div class="checkout-section">
                    <div class="section-title">THÀNH VIÊN</div>
                    <div class="member-info">
                        <div>Số Point hiện có: <span class="highlight"><fmt:formatNumber value="${user.getPoint()}"/></span></div>
                        <label >
                            <input type="checkbox" id="usePoint" name="usePoint" value="1"
                            ${user.point < 100 ? "disabled" : ""}>
                            Dùng point để thanh toán
                        </label></br>
                        <c:if test="${user.point < 100}">
                            <div style="color:red;font-size:13px;margin-top:5px;">
                                Bạn cần tối thiểu 100 point để sử dụng
                            </div>
                        </c:if>
                    </div>
                </div>


                <div class="checkout-section">
                    <div class="section-title">THÔNG TIN KHÁC</div>
                    <div class="other-content">
                        <label for="orderNote" class="other-label">Ghi chú</label>
                        <textarea id="orderNote"
                                  name="orderNote"
                                  class="other-textarea"
                                  placeholder="Nhập ghi chú (nếu có)...">${orderNote}</textarea>
                    </div>
                </div>

                <div class="checkout-section">
                    <div class="section-title">KIỂM TRA LẠI ĐƠN HÀNG</div>
                    <div class="order-review">
                        <c:forEach var="item" items="${cart.items}">
                            <div class="order-item">
                                <img src="${item.book.coverImgUrl}" alt="De men" class="order-img">
                                <div class="order-info">
                                    <div class="order-name">${item.book.title}</div>
                                </div>
                                <div class="order-prices">
                                    <div class="order-price-current"><p class="cost"><fmt:formatNumber
                                            value="${item.price}"/>
                                        đ</p></div>
                                    <c:if test="${item.book.getPriceDiscounted() >0}">
                                        <div class="order-price-old"><p class="cost"><fmt:formatNumber
                                                value="${item.book.getPrice()}" /> đ</p></div>
                                    </c:if>
                                </div>
                                <div class="order-qty">${item.quantity}</div>
                                <div class="order-total"><p class="cost"><fmt:formatNumber
                                        value="${item.price*item.quantity}" /> đ</p></div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </form>
        <div class="checkout-summary">
            <div class="container">
                <div class="checkout-total">
                    <div class="total-row">
                        <span>Thành tiền</span>
                        <span>
                         <fmt:formatNumber value="${totalBill}" /> đ
                        </span>
                    </div>

                    <div class="total-row">
                        <span>Phí vận chuyển</span>
                        <span>
                            <span class="shipping-fee">0 đ</span>
                        </span>
                    </div>

                    <c:if test="${discountMoney > 0}">
                        <div class="total-row">
                            <span>Giảm giá</span>
                            <span class="highlight">
                                -<fmt:formatNumber value="${discountMoney}" pattern="#,###"/> đ
                            </span>
                        </div>
                    </c:if>
                    <div class="total-row" id="pointRow" style="display:none;">
                        <span>Giảm bằng point</span>
                        <span class="highlight" id="pointDiscountText"></span>
                    </div>


                    <div class="total-row total-final">
                        <strong>Tổng thanh toán</strong>
                        <strong class="total-price">
                            <fmt:formatNumber value="${finalTotal}"/> đ
                        </strong>
                    </div>

                </div>
                <form method="post" action="CreateOrder" id="orderForm">
                    <div class="buttonAndTerm">
                        <input type="hidden" name="mode" value="${param.mode}">
                        <input type="hidden" name="addressId" id="finalAddressId">
                        <input type="hidden" name="shipType" id="finalShipType">
                        <input type="hidden" name="usePoint" id="finalUsePoint">
                        <input type="hidden" name="orderNote" id="finalNote">
                        <input type="hidden" name="payment" id="finalPayment">

                        <div class="terms">
                            <input type="checkbox" id="agree">
                            <label for="agree">
                                Bằng việc tiến hành Mua hàng, Bạn đã đồng ý với
                                <a href="<c:url value="/DieuKhoanSuDung" />">Điều khoản & Điều kiện của Chúng Tôi</a>
                            </label>
                        </div>
                        <button type="submit" class="confirm-payment-btn">Xác nhận thanh toán</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="overlay" id="overlay"></div>
<div class="popup">
    <h3>CHỌN KHUYẾN MÃI</h3>
    <div class="listVoucher">
        <div class="layout discounts">
            <div class="title Discount">
                <div class="ten">Mã giảm giá</div>
                <div class="numberAply">Áp dụng tối đa: 1</div>
            </div>
            <div class="list discount">
                <c:if test="${empty listVoucherDiscount}">
                    <div style="text-align: center; color: #444444; margin: 10px 0px">KHÔNG CÓ VOUCHER</div>
                </c:if>
                <c:forEach var="voucher" items="${listVoucherDiscount}">
                    <div class="voucher-item"
                         data-code="${voucher.code}"
                         data-description="${voucher.description}"
                         data-condition-price="${voucher.conditionPrice}"
                         data-categories="${voucher.conditionBook}"
                         data-publishers="${voucher.conditionPublisher}"
                         data-start="${voucher.getStartDateFormatted()}"
                         data-end="${voucher.getEndDateFormatted()}"
                    >
                        <div class="voucher-left"><i class="fa-solid fa-ticket"></i></div>
                        <div class="voucher-right">
                            <b>${voucher.description}</b>
                            <button class="voucher-detail" data-voucher="voucher1">Chi tiết</button>
                            <br>
                            Đơn hàng từ ${voucher.conditionPrice} <br>
                            <div class="voucher-code">${voucher.code}</div>
                            <div class="voucher-footer">
                                <span>HSD:${voucher.getStartDateFormatted()} - ${voucher.getEndDateFormatted()}</span>
                                <c:choose>
                                    <c:when test="${sessionScope.appliedDiscountVoucher != null && sessionScope.appliedDiscountVoucher.id == voucher.id}">
                                        <button type="button"
                                                onclick="cancelVoucher(${sessionScope.appliedDiscountVoucher.id})"
                                                class="cancel-btn">
                                            Hủy
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${empty sessionScope.appliedDiscountVoucher}">
                                            <button type="button"
                                                    onclick="applyVoucher(${voucher.id})"
                                                    class="apply-btn">
                                                Áp dụng
                                            </button>
                                        </c:if>
                                        <c:if test="${not empty sessionScope.appliedDiscountVoucher}">
                                            <button disabled
                                                    style="opacity:0.6;cursor:not-allowed;padding:6px 12px;border-radius:4px;">
                                                Áp dụng
                                            </button>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${listVoucherDiscount.size()>2}">
                <button class="toggle-btnDis">Xem thêm</button>
            </c:if>
        </div>
        <div class="layout ships">
            <div class="title Discount">
                <div class="ten">Mã vận chuyển</div>
                <div class="numberAply">Áp dụng tối đa: 1</div>
            </div>
            <div class="list ship">
                <c:if test="${empty listVoucherShip}">
                    <div style="text-align: center; color: #444444; margin: 10px 0px">KHÔNG CÓ VOUCHER</div>
                </c:if>
                <c:forEach var="voucher" items="${listVoucherShip}">
                    <div class="voucher-item"
                         data-code="${voucher.code}"
                         data-description="${voucher.description}"
                         data-condition-price="${voucher.conditionPrice}"
                         data-categories="${voucher.conditionBook}"
                         data-publishers="${voucher.conditionPublisher}"
                         data-start="${voucher.getStartDateFormatted()}"
                         data-end="${voucher.getEndDateFormatted()}"
                    >
                        <div class="voucher-left"><i class="fa-solid fa-ticket"></i></div>
                        <div class="voucher-right">
                            <b>${voucher.description}</b>
                            <button class="voucher-detail" data-voucher="voucher1">Chi tiết</button>
                            <br>
                            Đơn hàng từ ${voucher.conditionPrice} <br>
                            <div class="voucher-code">${voucher.code}</div>
                            <div class="voucher-footer">
                                <span>HSD:${voucher.getStartDateFormatted()} - ${voucher.getEndDateFormatted()}</span>
                                <c:choose>
                                    <c:when test="${sessionScope.appliedShipVoucher != null && sessionScope.appliedShipVoucher.id == voucher.id}">
                                        <button type="button"
                                                onclick="cancelVoucher(${sessionScope.appliedShipVoucher.id})"
                                                class="cancel-btn">
                                            Hủy
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${empty sessionScope.appliedShipVoucher}">
                                            <button type="button"
                                                    onclick="applyVoucher(${voucher.id})"
                                                    class="apply-btn">
                                                Áp dụng
                                            </button>
                                        </c:if>
                                        <c:if test="${not empty sessionScope.appliedShipVoucher}">
                                            <button disabled
                                                    style="opacity:0.6;cursor:not-allowed;padding:6px 12px;border-radius:4px;">
                                                Áp dụng
                                            </button>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${listVoucherShip.size()>2}">
                <button class="toggle-btnShip">Xem thêm</button>
            </c:if>
        </div>
    </div>
</div>
<div id="voucherPopup" class="popup">
    <div class="popup-content">
        <h3>CHI TIẾT KHUYẾN MÃI</h3>
        <div class="popup-body">
            <div class="voucher-header-detail">
                <div class="voucher-left"><i class="fa-solid fa-ticket"></i></div>
                <div class="voucher-right-detail">
                    <b id="detailDescription"></b>
                    <div class="voucher-code" id="detailCode"></div>
                    <div class="voucher-expiry" id="detailExpiry"></div>
                </div>
            </div>

            <div class="voucher-conditions-detail">
                <h4>Điều kiện áp dụng</h4>
                <ul id="detailConditionsList">
                    <li>Đơn hàng tối thiểu: <span id="detailMinPrice"></span> VNĐ</li>
                    <li id="detailCategoriesLi" style="display:none;">Áp dụng cho các danh mục: <span
                            id="detailCategories"></span></li>
                    <li id="detailPublishersLi" style="display:none;">Áp dụng cho nhà xuất bản: <span
                            id="detailPublishers"></span></li>
                </ul>
                <p class="note-combine">Có thể sử dụng đồng thời với mã giảm phí vận chuyển.</p>
            </div>
        </div>
        <button class="cancel">×</button>
    </div>
</div>
<script>
    const openPopup = document.querySelector(".more-voucher");
    const overlay = document.getElementById("overlay");
    const popup = document.querySelector(".popup");

    openPopup.addEventListener("click", (e) => {
        e.preventDefault();
        overlay.style.display = "block";
        popup.style.display = "block";
    });

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        popup.style.display = "none";
        voucherPopup.style.display = "none";
    });
    const detailBtns = document.querySelectorAll(".voucher-detail");
    const voucherPopup = document.getElementById("voucherPopup");
    const cancelBtn = document.querySelector("#voucherPopup .cancel");

    detailBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            const voucherItem = btn.closest(".voucher-item");


            const description = voucherItem.dataset.description || voucherItem.querySelector("b").textContent;
            const code = voucherItem.dataset.code || voucherItem.querySelector(".voucher-code").textContent;
            const start = voucherItem.dataset.start || "";
            const endDate = voucherItem.dataset.end || "";
            const minPrice = parseInt(voucherItem.dataset.conditionPrice || 0).toLocaleString();
            const categories = voucherItem.dataset.categories;
            const publishers = voucherItem.dataset.publishers;


            document.getElementById("detailDescription").textContent = description;
            document.getElementById("detailCode").textContent = code;
            document.getElementById("detailExpiry").textContent = 'Hiệu lực: ' + start + ' - ' + endDate;
            document.getElementById("detailMinPrice").textContent = minPrice + " đ";


            const categoriesLi = document.getElementById("detailCategoriesLi");
            const publishersLi = document.getElementById("detailPublishersLi");

            if (categories && categories.trim() !== "") {
                document.getElementById("detailCategories").textContent = categories;
                categoriesLi.style.display = "list-item";
            } else {
                categoriesLi.style.display = "none";
            }

            if (publishers && publishers.trim() !== "") {
                document.getElementById("detailPublishers").textContent = publishers;
                publishersLi.style.display = "list-item";
            } else {
                publishersLi.style.display = "none";
            }

            popup.style.display = "none";
            voucherPopup.style.display = "block";
        });
    });


    cancelBtn.addEventListener("click", () => {
        voucherPopup.style.display = "none";
        popup.style.display = "block";
    });

    function setupSectionToggle(layoutSelector, listSelector, toggleBtnClass) {
        const layout = document.querySelector(layoutSelector);
        if (!layout) return;

        const items = layout.querySelectorAll(listSelector + ' .voucher-item');
        const toggleBtn = layout.querySelector(toggleBtnClass);

        if (!toggleBtn) return;

        items.forEach((v, i) => {
            if (i > 1) v.style.display = 'none';
            else v.style.display = 'flex';
        });

        toggleBtn.addEventListener('click', () => {
            const hiddenItems = Array.from(items).filter(v => v.style.display === 'none');

            if (hiddenItems.length > 0) {
                hiddenItems.forEach(v => v.style.display = 'flex');
                toggleBtn.textContent = 'Thu gọn';
            } else {
                items.forEach((v, i) => {
                    if (i > 1) v.style.display = 'none';
                });
                toggleBtn.textContent = 'Xem thêm';
            }
        });
    }

    setupSectionToggle('.layout.discounts', '.list.discount', '.toggle-btnDis');
    setupSectionToggle('.layout.ships', '.list.ship', '.toggle-btnShip')


    document.querySelectorAll(".voucher-item").forEach((item, index) => {
        console.log(`Voucher ${index + 1}:`);
        console.log("  Code:", item.dataset.code);
        console.log("  Description:", item.dataset.description);
        console.log("  Start date:", "'" + item.dataset.start + "'");
        console.log("  End date:", "'" + item.dataset.end + "'");
    });
    document.querySelectorAll('.voucher-form').forEach(form => {
        form.addEventListener('submit', function (e) {
            document.getElementById('overlay').style.display = 'none';
            document.getElementById('voucherListPopup').style.display = 'none';
        });
    });
    const agree = document.getElementById("agree");
    const confirmBtn = document.querySelector(".confirm-payment-btn");



    document.getElementById("orderForm").addEventListener("submit", function (e) {

        if (!agree.checked) {
            e.preventDefault();

            const toast = document.getElementById("toast-term");
            toast.style.display = "flex";

            setTimeout(() => {
                toast.classList.add("toast-hide");
            }, 3000);

            setTimeout(() => {
                toast.style.display = "none";
                toast.classList.remove("toast-hide");
            }, 3400);

            return;
        }


        const addressChecked = document.querySelector('input[name="addressId"]:checked');
        document.getElementById("finalAddressId").value = addressChecked?.value || "";


        const shipChecked = document.querySelector('input[name="shipService"]:checked');
        document.getElementById("finalShipType").value = shipChecked?.value || "";


        document.getElementById("finalUsePoint").value =
            document.querySelector('input[name="usePoint"]')?.checked ? "1" : "0";


        document.getElementById("finalNote").value =
            document.querySelector('textarea[name="orderNote"]').value;

        document.getElementById("finalPayment").value = document.querySelector("input[name='payment']:checked").value;

    });
    document.addEventListener("DOMContentLoaded", function () {
        const usePointCheckbox = document.getElementById("usePoint");
        const totalPriceEl = document.querySelector(".total-price");
        const pointRow = document.getElementById("pointRow");
        const pointDiscountText = document.getElementById("pointDiscountText");

        usePointCheckbox.addEventListener("change", function () {

            const baseTotal = Number("${totalBill}");
            const discount = Number("${discountMoney}");
            const selectedShip = document.querySelector('input[name="shipService"]:checked');

            let shipFee = 0;
            if (selectedShip) {
                const rawFee = Number(selectedShip.dataset.fee);
                const shipDiscount = Number("${sessionScope.shipDiscount != null ? sessionScope.shipDiscount : 0}");
                shipFee = rawFee - shipDiscount;
                if (shipFee < 0) shipFee = 0;
            }

            const currentTotal = baseTotal + shipFee - discount;

            if (this.checked && userPoint >= 100) {

                const pointUsed = Math.min(userPoint, currentTotal);

                pointDiscountText.innerText =
                    "-" + pointUsed.toLocaleString("vi-VN") + " đ";

                totalPriceEl.innerText =
                    (currentTotal - pointUsed).toLocaleString("vi-VN") + " đ";

                pointRow.style.display = "flex";

            } else {

                totalPriceEl.innerText =
                    currentTotal.toLocaleString("vi-VN") + " đ";

                pointRow.style.display = "none";
            }

        });
        const userPoint = Number("${user.point}");

        usePointCheckbox.addEventListener("change", function () {

            if (this.checked && userPoint >= 100) {

                const pointUsed = Math.min(userPoint, originalTotal);

                pointDiscountText.innerText =
                    "-" + pointUsed.toLocaleString("vi-VN") + " đ";

                totalPriceEl.innerText =
                    (originalTotal - pointUsed).toLocaleString("vi-VN") + " đ";

                pointRow.style.display = "flex";

            } else {

                totalPriceEl.innerText =
                    originalTotal.toLocaleString("vi-VN") + " đ";

                pointRow.style.display = "none";
            }

        });

        const toast = document.getElementById("toast-error");
        if (toast) {
            setTimeout(() => {
                toast.classList.add("toast-hide");
            }, 3000);


            setTimeout(() => {
                toast.remove();
            }, 3400);
        }

        const addressRadios = document.querySelectorAll('input[name="addressId"]');

        addressRadios.forEach(radio => {
            radio.addEventListener("change", function () {
                loadShipping(this.value);
            });
        });

        const firstChecked = document.querySelector('input[name="addressId"]:checked');

        if (firstChecked) {
            showLoadingShipping();
            loadShipping(firstChecked.value);
        } else {
            showLoadingShipping();
        }

    });


    const container = document.getElementById("shippingContainer");

    function showLoadingShipping() {
        container.innerHTML = "<p>Đang xử lý phương thức vận chuyển...</p>";
    }


    function applyVoucher(voucherId) {
        const mode = new URLSearchParams(window.location.search).get("mode");
        fetch("applyVoucher", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "voucherId=" + voucherId + "&mode=" + mode
        })
            .then(res => res.json())
            .then(data => {

                if (data.success) {
                    location.reload();
                } else {
                    alert(data.message);
                }

            })
            .catch(err => console.log(err));
    }

    function cancelVoucher(voucherId) {

        fetch("cancelVoucher", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "voucherId=" + voucherId
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                }
            })
            .catch(err => console.log(err));
    }



    function loadShipping(addressId) {
        const mode = new URLSearchParams(window.location.search).get("mode");
        fetch("calculateShipping", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "addressId=" + addressId + "&mode=" + mode
        })
            .then(res => res.json())
            .then(data => {
                console.log("Shipping response:", data);

                const container = document.getElementById("shippingContainer");
                container.innerHTML = "";

                if (!data.services || data.services.length === 0) {
                    container.innerHTML = "<p>Không có phương thức vận chuyển</p>";
                    return;
                }

                data.services.forEach((service, index) => {

                    const div = document.createElement("div");
                    div.className = "shipping-item";

                    const checkedAttr = index == 0 ? "checked" : "";

                    div.innerHTML =
                        '<input type="radio" ' +
                        'name="shipService" ' +
                        'value="' + service.service_id + '" ' +
                        'data-fee="' + service.fee + '" ' +
                        checkedAttr + '>' +

                        '<div>' +
                        '<strong>' + service.service_name + ': ' +
                        Number(service.fee).toLocaleString("vi-VN") + ' đ</strong><br>' +
                        'Dự kiến giao: ' +
                        new Date(service.leadtime * 1000).toLocaleDateString("vi-VN") +
                        '</div>';

                    container.appendChild(div);
                });

                updateShippingFee();
            })
            .catch(err => console.log(err));
    }

    function updateShippingFee() {

        const selected = document.querySelector('input[name="shipService"]:checked');
        if (!selected) return;

        const fee = Number(selected.dataset.fee);

        const shipDiscount = Number("${sessionScope.shipDiscount != null ? sessionScope.shipDiscount : 0}");

        let shippingAfterDiscount = fee - shipDiscount;
        if (shippingAfterDiscount < 0) shippingAfterDiscount = 0;

        document.querySelector(".shipping-fee").innerText =
            shippingAfterDiscount.toLocaleString("vi-VN") + " đ";

        const baseTotal = Number("${totalBill}");
        const discount = Number("${discountMoney}");

        const final = baseTotal + shippingAfterDiscount - discount;

        document.querySelector(".total-price").innerText =
            final.toLocaleString("vi-VN") + " đ";

        // update hidden field
        document.getElementById("finalShipType").value = selected.value;
    }


    document.addEventListener("change", function (e) {
        if (e.target.name === "shipService") {
            updateShippingFee();
        }
    });

</script>
</body>
</html>