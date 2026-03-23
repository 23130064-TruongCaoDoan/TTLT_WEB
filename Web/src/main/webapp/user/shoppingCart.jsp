<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Giỏ Hàng</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="stylesheet" href="assets/css/header.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Cormorant+Garamond:ital,wght@0,300..700;1,300..700&family=Libre+Franklin:ital,wght@0,100..900;1,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Playwrite+DE+SAS:wght@100..400&family=Sarabun:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" href="assets/css/shoppingCart.css"/>
    <link rel="stylesheet" href="assets/css/footer.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Bungee&family=Lobster&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" href="assets/css/voucher.css">
    <link rel="stylesheet" href="assets/css/ThanhToan.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="headerUser.jsp"> </c:import>
    <div class="title-cart">
        <i class="fa-solid fa-cart-shopping"></i>
        <h1>Giỏ hàng</h1>
    </div>

    <div class="container-cart">
        <div class="left-container">
            <div class="left">
                <div class="header-cart">
                    <span></span>
                    <div class="header-product">
                        <span>Sản phẩm</span>
                        <small> <c:if test="${empty cart.items}">0</c:if> <c:if
                                test="${not empty cart.items}">${cart.totalQuantity}</c:if> sản phẩm</small>
                    </div>

                    <span class="center">Số lượng</span>
                    <span class="center">Thành tiền</span>
                    <a href="#" onclick="updateItem(0,0)" class="delete-all">
                        <i class="fa-solid fa-trash"></i> Xóa tất cả
                    </a>

                </div>
                <c:if test="${empty cart.items}">
                    <div class="empty-cart">
                        KHÔNG CÓ SẢN PHẨM
                    </div>
                </c:if>
                <c:forEach var="item" items="${cart.items}">
                    <div class="card-item">
                        <img
                                src="${item.book.coverImgUrl}"
                                alt=""
                        />
                        <div class="item-info">
                            <h4>${item.book.title}</h4>
                            <div class="detail">
                                <p>Tác giả: ${item.book.author}</p>
                                <p>NXB: ${item.book.publisher}</p>
                                <p>Năm: ${item.book.publishedDate}</p>
                            </div>
                        </div>
                        <div class="quantity">
                            <form class="number-input">
                                <input type="hidden" name="id" value="${item.book.id}">
                                <button type="button" class="minus" onclick="changeQty(this, -1)">-</button>
                                <input type="number"
                                       name="quantity"
                                       value="${item.quantity}"
                                       min="1"
                                       max="${book.stock}"
                                       onkeydown="handleEnter(event, this)"
                                       onblur="updateQtyInput(this)">

                                <button type="button" class="plus" onclick="changeQty(this, 1)">+</button>
                            </form>
                        </div>
                        <div class="total-cost" style="text-align: center">
                            <p class="cost"><fmt:formatNumber value="${item.price}" pattern="#.###"/> đ</p>
                            <c:if test="${item.book.getPriceDiscounted() >0}">
                            <div class="order-price-old"><p class="cost"><fmt:formatNumber value="${item.book.getPrice()}" pattern="#.###"/> đ</p></div>
                            </c:if>
                        </div>
                        <i class="fa-solid fa-trash" style="color: black"
                           onclick="updateItem(${item.getBook().getId()},0)"></i>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="right-container">
            <div class="voucher">
                <a href="" class="more-voucher">
                    <p><i class="fa-solid fa-ticket"></i> Khuyến Mãi</p>
                    <p>Xem thêm ></p>
                </a>
                <c:choose>
                    <c:when test="${not empty sessionScope.appliedDiscountVoucher}">
                        <div class="info-voucher">
                            <h4>${sessionScope.appliedDiscountVoucher.description}</h4>
                            <p>Đơn hàng từ <fmt:formatNumber value="${sessionScope.appliedDiscountVoucher.conditionPrice}" pattern="#,###"/> đ</p>
                            <div class="expired">
                                <div>
                                    <p>HSD: ${sessionScope.appliedDiscountVoucher.getEndDateFormatted()}</p>
                                </div>
                                <form action="cancelVoucher" method="post" style="display:inline;">
                                    <input type="hidden" name="page" value="1">
                                    <input type="hidden" name="type" value="discount">
                                    <button type="submit">Hủy</button>
                                </form>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="info-voucher">
                            <p style="text-align:center; color:#888; font-style:italic;">Chưa áp dụng mã giảm giá</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="bill">
                <div class="thanh-tien">
                    <p>Thành tiền</p>
                    <p><c:if test="${empty cart.items}">0 đ</c:if>
                        <c:if test="${not empty cart.items}">
                            <fmt:formatNumber value="${cart.totalBill}" pattern="#,###"/> đ
                        </c:if></p>
                </div>
                <div>
                    <h3 class="total-price">Tổng số tiền (áp dụng mã giảm giá)</h3>
                    <p><fmt:formatNumber value="${finalTotal}" pattern="#,###"/> đ</p>
                </div>
                <button class="check-out" onclick="window.location.href='<c:url value="/ThanhToan"/>'">Đặt hàng</button>
            </div>
        </div>
    </div>

    <c:import url="footerUser.jsp"> </c:import>
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
                                        <form action="cancelVoucher" method="post">
                                            <button type="submit" style="background:#dc3545;color:white;border:none;padding:6px 12px;border-radius:4px;">
                                                <input type="hidden" name="page" value="1">
                                                <input type="hidden" name="type" value="discount">
                                                Hủy
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${empty sessionScope.appliedDiscountVoucher}">
                                            <form action="applyVoucher" method="post">
                                                <input type="hidden" name="page" value="1">
                                                <input type="hidden" name="voucherId" value="${voucher.id}">
                                                <button type="submit" style="background:#28a745;color:white;border:none;padding:6px 12px;border-radius:4px;">
                                                    Áp dụng
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${not empty sessionScope.appliedDiscountVoucher}">
                                            <button disabled style="opacity:0.6;cursor:not-allowed;padding:6px 12px;border-radius:4px;">
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
                    <li id="detailCategoriesLi" style="display:none;">Áp dụng cho các danh mục: <span id="detailCategories"></span></li>
                    <li id="detailPublishersLi" style="display:none;">Áp dụng cho nhà xuất bản: <span id="detailPublishers"></span></li>
                </ul>
                <p class="note-combine">Có thể sử dụng đồng thời với mã giảm phí vận chuyển.</p>
            </div>
        </div>
        <button class="cancel">×</button>
    </div>
</div>
<script>
    function changeQty(btn, delta) {
        const form = btn.closest("form");
        const input = form.querySelector("input[name='quantity']");
        const id = form.querySelector("input[name='id']").value;

        let value = parseInt(input.value) + delta;
        if (value < 1) value = 1;
        updateItem(id, value);
    }

    //voucher

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


    // hiển thị chi tiết voucher
    const detailBtns = document.querySelectorAll(".voucher-detail");
    const voucherPopup = document.getElementById("voucherPopup");
    const cancelBtn = document.querySelector("#voucherPopup .cancel");

    detailBtns.forEach(btn => {
        btn.addEventListener("click", () => {
            const voucherItem = btn.closest(".voucher-item");

            // Lấy dữ liệu từ data attributes
            const description = voucherItem.dataset.description || voucherItem.querySelector("b").textContent;
            const code = voucherItem.dataset.code || voucherItem.querySelector(".voucher-code").textContent;
            const start = voucherItem.dataset.start || "";
            const endDate = voucherItem.dataset.end || "";
            const minPrice = parseInt(voucherItem.dataset.conditionPrice || 0).toLocaleString();
            const categories = voucherItem.dataset.categories;
            const publishers = voucherItem.dataset.publishers;

            // Cập nhật nội dung popup
            document.getElementById("detailDescription").textContent = description;
            document.getElementById("detailCode").textContent = code;
            document.getElementById("detailExpiry").textContent = 'Hiệu lực: '+ start +' - '+endDate;
            document.getElementById("detailMinPrice").textContent = minPrice + " VNĐ";

            // Xử lý danh mục và NXB (nếu có)
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
    setupSectionToggle('.layout.ships', '.list.ship', '.toggle-btnShip');

    function updateItem(id, quantity) {
        fetch("updateItem?id=" + id + "&quantity=" + quantity)
            .then(res => res.json())
            .then(data => {
                document.getElementById("totalItem").innerText = data.total;
                if (!data.success) {
                    show("Không thể thêm quá số lượng tồn kho");
                }
                location.reload();
            })
            .catch(err => console.log(err));

    }

    document.querySelectorAll(".number-input").forEach(form => {
        form.addEventListener("submit", e => {
            e.preventDefault();
        });
    });

    document.querySelectorAll(".voucher-item").forEach((item, index) => {
        console.log(`Voucher ${index + 1}:`);
        console.log("  Code:", item.dataset.code);
        console.log("  Description:", item.dataset.description);
        console.log("  Start date:", "'" + item.dataset.start + "'");
        console.log("  End date:", "'" + item.dataset.end + "'");
    });
    document.querySelectorAll('.voucher-form').forEach(form => {
        form.addEventListener('submit', function(e) {
            document.getElementById('overlay').style.display = 'none';
            document.getElementById('voucherListPopup').style.display = 'none';
        });
    });

    function handleEnter(e, input) {
        if (e.key === "Enter") {
            e.preventDefault();
            updateQtyInput(input);
        }
    }

    function updateQtyInput(input) {
        const form = input.closest("form");
        const id = form.querySelector("input[name='id']").value;

        let value = parseInt(input.value);

        if (isNaN(value) || value < 1) value = 1;
        if (value > 100) value = 100;

        input.value = value;
        updateItem(id, value);
    }


    function show(message) {
        const toast = document.getElementById("toast");
        toast.innerText = message;
        toast.classList.add("show");
        setTimeout(() => {
            toast.classList.remove("show");
        }, 2000);
    }
</script>
</body>
</html>
