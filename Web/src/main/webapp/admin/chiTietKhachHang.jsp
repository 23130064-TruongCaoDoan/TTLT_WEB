<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết khách hàng</title>
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <link rel="stylesheet" href="assets/css_admin/chiTietKhachHang.css">
    <link rel="stylesheet" href="assets/css_admin/notifySuccess.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"/>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"/>

        <div class="customer-detail-container">

            <div class="page-header">
                <h2>
                    <i class="fa-solid fa-user-circle"></i>
                    Chi tiết khách hàng
                </h2>
                <div class="header-actions">
                    <button class="btn-secondary" onclick="openNotifPopup()">
                        <i class="fa-solid fa-bell"></i> Thông báo
                    </button>
                    <button class="btn-primary" onclick="openCreateOrderPopup()">
                        <i class="fa-solid fa-plus"></i> Tạo đơn hàng
                    </button>
                </div>
            </div>

            <div class="stats-row">
                <div class="stat-card orders">
                    <div class="stat-icon"><i class="fa-solid fa-box"></i></div>
                    <div class="stat-info">
                        <h4>Tổng đơn hàng</h4>
                        <p><fmt:formatNumber value="${totalOrder}" type="number" groupingUsed="true"
                                             maxFractionDigits="0"/></p>
                    </div>
                </div>
                <div class="stat-card money">
                    <div class="stat-icon"><i class="fa-solid fa-money-bill-wave"></i></div>
                    <div class="stat-info">
                        <h4>Tổng giao dịch</h4>
                        <p><fmt:formatNumber value="${totalAmount}" type="number" groupingUsed="true"
                                             maxFractionDigits="0"/> đ</p>
                    </div>
                </div>
            </div>

            <div class="section-card">
                <div class="section-header">
                    <h3><i class="fa-solid fa-id-card"></i> Thông tin cá nhân</h3>
                </div>
                <div class="section-body">
                    <div class="personal-info-grid">
                        <div class="avatar-row">
                            <div class="avatar-wrapper">
                                <img src="${user.avatar != null ? user.avatar : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFCzxivJXCZk0Kk8HsHujTO3Olx0ngytPrWw&s'}"
                                     alt="Avatar" id="avatarImg"/>
                                <button class="avatar-edit-btn" title="Đổi avatar" onclick="triggerAvatarUpload()">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                                <input type="file" id="avatarInput" style="display:none" accept="image/*"
                                       onchange="uploadAvatar(this)"/>
                            </div>
                            <div>
                                <div class="avatar-name">${user.name}</div>
                                <div class="avatar-role">${user.role} · #${user.id}</div>
                            </div>
                        </div>

                        <div class="info-item">
                            <label>Mã khách hàng</label>
                            <div class="info-value-row">
                                <span class="info-value">#${user.id}</span>
                            </div>
                        </div>

                        <div class="info-item" id="field-name">
                            <label>Tên khách hàng</label>
                            <div class="info-value-row">
                                <span class="info-value">${user.name}</span>
                                <button class="edit-icon-btn" onclick="startEdit('name','${user.name}','text')">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                            </div>
                        </div>

                        <div class="info-item" id="field-birthYear">
                            <label>Năm sinh</label>
                            <div class="info-value-row">
                                <span class="info-value"> ${user.birthday.dayOfMonth}/${user.birthday.monthValue}/${user.birthday.year}</span>
                                <button class="edit-icon-btn"
                                        onclick="startEdit('birthYear','${user.birthday}','number')">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                            </div>
                        </div>

                        <div class="info-item" id="field-email">
                            <label>Email</label>
                            <div class="info-value-row">
                                <span class="info-value">${user.email}</span>
                                <button class="edit-icon-btn" onclick="startEdit('email','${user.email}','email')">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                            </div>
                        </div>

                        <div class="info-item" id="field-phone">
                            <label>Số điện thoại</label>
                            <div class="info-value-row">
                                <span class="info-value">${user.phone != null ? user.phone : 'Chưa cập nhật'}</span>
                                <button class="edit-icon-btn" onclick="startEdit('phone','${user.phone}','text')">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                            </div>
                        </div>


                        <div class="info-item" id="field-status">
                            <label>Trạng thái</label>
                            <div class="info-value-row">
                                <c:choose>
                                    <c:when test="${user.status}">
                                        <span class="badge active">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge inactive">Bị khóa</span>
                                    </c:otherwise>
                                </c:choose>
                                <button class="edit-icon-btn"
                                        onclick="startEditSelect('status','${user.status}',['active:Hoạt động','inactive:Bị khóa'])">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                            </div>
                        </div>

                        <div class="info-item" id="field-role">
                            <label>Quyền tài khoản</label>
                            <div class="info-value-row">
                                <span class="badge ${user.role}">${user.role}</span>
                                <button class="edit-icon-btn"
                                        onclick="startEditSelect('role','${customer.role}',['admin:Admin','manager:Quản lý','accountant:Kế toán','staff:Nhân viên','user:Người dùng'])">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="section-card">
                <div class="section-header">
                    <h3><i class="fa-solid fa-location-dot"></i> Sổ địa chỉ</h3>
                </div>
                <div class="section-body">
                    <div class="address-list">
                        <c:choose>
                            <c:when test="${not empty addresses}">
                                <c:forEach items="${addresses}" var="addr">
                                    <div class="address-item">
                                        <div class="address-info">
                                            <div class="address-name">${addr.name}</div>
                                            <div class="address-phone">${addr.phone}</div>
                                            <div class="address-detail">${addr.specificAddress}, ${addr.ward}, ${addr.districts}, ${addr.city}</div>
                                        </div>
                                        <i class="fa-solid fa-trash icon-trash"
                                           onclick="confirmDeleteItem('address',${addr.id})"></i>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <i class="fa-solid fa-map-location-dot"></i>
                                    Chưa có địa chỉ nào
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="section-card">
                <div class="section-header">
                    <h3><i class="fa-solid fa-ticket-simple"></i> Danh sách voucher</h3>
                    <button class="btn-primary" onclick="openGiftVoucherPopup()">
                        <i class="fa-solid fa-gift"></i> Tặng voucher
                    </button>
                </div>
                <div class="section-body" style="padding: 0;">
                    <div class="table-wrapper">
                        <table class="data-table">
                            <thead>
                            <tr>
                                <th>Mã voucher</th>
                                <th>Tên</th>
                                <th>Giá trị</th>
                                <th>Điều kiện</th>
                                <th>Loại</th>
                                <th>Xóa</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty voucherList}">
                                    <c:forEach items="${voucherList}" var="cv">
                                        <tr>
                                            <td><strong>${cv.code}</strong></td>
                                            <td>${cv.description}</td>
                                            <td>${cv.valuee}</td>
                                            <td><c:if test="${cv.conditionPrice > 0}">
                                                - Đơn tối thiểu: ${cv.conditionPrice} đ <br/>
                                            </c:if>

                                                <c:if test="${not empty cv.conditionBook}">
                                                    - Sách áp dụng: ${cv.conditionBook} <br/>
                                                </c:if>

                                                <c:if test="${not empty cv.conditionPublisher}">
                                                    - Nhà xuất bản: ${cv.conditionPublisher}
                                                </c:if></td>
                                            <td>${cv.type}</td>
                                            <td>
                                                <i class="fa-solid fa-trash icon-trash"
                                                   onclick="confirmDeleteItem('voucher',${cv.id})"></i>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7">
                                            <div class="empty-state">
                                                <i class="fa-solid fa-ticket-simple"></i>
                                                Chưa có voucher nào
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="section-card">
                <div class="section-header">
                    <h3><i class="fa-solid fa-bags-shopping"></i> Danh sách đơn hàng</h3>
                    <div class="order-filters">
                        <select class="filter-select" id="filterStatus" onchange="filterOrders()">
                            <option value="">Tất cả trạng thái</option>
                            <option value="PENDING">Chờ xác nhận</option>
                            <option value="PROCESSING">Đang xử lý</option>
                            <option value="SHIPPING">Đang vận chuyển</option>
                            <option value="COMPLETED">Đã giao</option>
                            <option value="CANCELLED">Đã hủy</option>
                            <option value="REFUNDED">Hoàn trả</option>
                        </select>
                        <select class="filter-select" id="filterTime" onchange="filterOrders()">
                            <option value="">Sắp xếp</option>
                            <option value="newest">Mới nhất</option>
                            <option value="oldest">Cũ nhất</option>
                        </select>
                    </div>
                </div>
                <div class="section-body" style="padding: 0;">
                    <div class="table-wrapper" style="max-height:380px;">
                        <table class="data-table" id="orderTable">
                            <thead>
                            <tr>
                                <th>Mã đơn</th>
                                <th>Tổng tiền</th>
                                <th>SL sản phẩm</th>
                                <th>Ngày đặt</th>
                                <th>Trạng thái</th>
                                <th>Thanh toán</th>
                                <th>Sản phẩm</th>
                                <th>Sửa</th>
                                <th>Xóa</th>
                            </tr>
                            </thead>
                            <tbody id="orderTableBody">
                            <c:choose>
                                <c:when test="${not empty orders}">
                                    <c:forEach items="${orders}" var="order">
                                        <tr data-status="${order.status}" data-date="${order.orderDate}">
                                            <td>#${order.id}</td>
                                            <td><fmt:formatNumber value="${order.totalAmount}" type="number"
                                                                  groupingUsed="true"
                                                                  maxFractionDigits="0"/>đ
                                            </td>
                                            <td>${order.totalQuantity}</td>
                                            <td>${order.getOrderDate()}</td>
                                            <td>${order.status}</td>
                                            <td>${order.paymentMethod}</td>
                                            <td>
                                                <button class="btn-view-products"
                                                        onclick="viewOrderProducts(${order.id})">
                                                    <i class="fa-solid fa-eye"></i> Xem
                                                </button>
                                            </td>
                                            <td>
                                                <i class="fa-solid fa-pen icon-trash"
                                                   style="color:#3b7ddd;cursor:pointer;"
                                                   onclick="openEditOrderPopup(${order.id})"></i>
                                            </td>
                                            <td>
                                                <i class="fa-solid fa-trash icon-trash"
                                                   onclick="confirmDeleteItem('order',${order.id})"></i>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9">
                                            <div class="empty-state">
                                                <i class="fa-solid fa-box-open"></i>
                                                Chưa có đơn hàng nào
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <div id="overlay" onclick="closeAllPopups()"></div>

    <div id="deletePopup" class="delete-popup">
        <p id="deletePopupMsg">Bạn có chắc muốn xóa không?</p>
        <div class="delete-actions">
            <button class="btn-delete" onclick="confirmDelete()">Xóa</button>
            <button class="btn-cancel" onclick="closeAllPopups()">Hủy</button>
        </div>
    </div>

    <div id="giftVoucherPopup" class="popup-box">
        <button class="popup-close" onclick="closeAllPopups()">&times;</button>
        <h3><i class="fa-solid fa-gift"></i> Tặng voucher cho khách hàng</h3>
        <input type="text" class="voucher-search-input" id="voucherSearchInput"
               placeholder="Tìm kiếm voucher..." oninput="searchVouchers(this.value)">
        <div class="voucher-gift-list" id="voucherGiftList">
            <c:forEach items="${availableVoucher}" var="v">
                <div class="voucher-gift-item" data-id="${v.id}" data-code="${v.code}" data-type="${v.type}"
                     data-desc="${v.description}"
                     onclick="toggleGiftVoucher(this)">
                    <input type="checkbox" value="${v.code}">
                    <div class="voucher-gift-info">
                        <div class="voucher-gift-code">${v.code}</div>
                        <div class="voucher-gift-desc">${v.description} · ${v.type}</div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="popup-actions">
            <button class="btn-secondary" onclick="closeAllPopups()">Hủy</button>
            <button type="button" class="btn-primary" onclick="submitGiftVoucher()">
                <i class="fa-solid fa-paper-plane"></i> Tặng
            </button>
        </div>
    </div>

    <div id="orderProductsPopup" class="popup-box" style="width: 600px;">
        <button class="popup-close" onclick="closeAllPopups()">&times;</button>
        <h3><i class="fa-solid fa-list"></i> Sản phẩm đơn hàng</h3>
        <div class="table-wrapper" style="max-height: 320px;">
            <table class="order-products-table">
                <thead>
                <tr>
                    <th>Tên sách</th>
                    <th>Số lượng</th>
                    <th>Đơn giá</th>
                    <th>Thành tiền</th>
                </tr>
                </thead>
                <tbody id="orderProductsBody"></tbody>
            </table>
        </div>
    </div>

    <div id="editOrderPopup" class="popup-box" style="width: 580px;">
        <button class="popup-close" onclick="closeAllPopups()">&times;</button>
        <h3><i class="fa-solid fa-pen-to-square"></i> Chỉnh sửa đơn hàng</h3>
        <div class="edit-order-form">
            <input type="hidden" id="editOrderId">
            <div class="form-group">
                <label>Người nhận</label>
                <input type="text" id="editReceiver" placeholder="Tên người nhận">
            </div>
            <div class="form-group">
                <label>Số điện thoại</label>
                <input type="text" id="editOrderPhone" placeholder="Số điện thoại">
            </div>
            <div class="form-group">
                <label>Địa chỉ</label>
                <input type="text" id="editOrderAddress" placeholder="Địa chỉ giao hàng">
            </div>
            <div class="form-group">
                <label>Ghi chú</label>
                <textarea id="editOrderNote" placeholder="Ghi chú..."></textarea>
            </div>
            <div class="form-group">
                <label>Tổng thanh toán (đ)</label>
                <input type="number" id="editOrderTotal" placeholder="Tổng tiền">
            </div>
            <div class="form-group">
                <label>Thêm sản phẩm</label>
                <input type="text" class="product-search-input" id="productSearchInput"
                       placeholder="Tìm sách..." oninput="searchProducts(this.value, 'edit')">
                <div class="product-search-results" id="productSearchResults"></div>
            </div>
            <div class="form-group">
                <label>Sản phẩm trong đơn</label>
                <div class="edit-products-list" id="editProductsList"></div>
            </div>
            <div class="popup-actions">
                <button class="btn-secondary" onclick="closeAllPopups()">Hủy</button>
                <button class="btn-primary" onclick="saveEditOrder()">
                    <i class="fa-solid fa-floppy-disk"></i> Lưu
                </button>
            </div>
        </div>
    </div>

    <div id="createOrderPopup" class="popup-box" style="width: 560px;">
        <button class="popup-close" onclick="closeAllPopups()">&times;</button>
        <h3><i class="fa-solid fa-plus-circle"></i> Tạo đơn hàng mới</h3>
        <div class="create-order-form">
            <div class="form-group">
                <label>Địa chỉ giao hàng</label>
                <select id="createOrderAddress">
                    <option value="">-- Tự nhập địa chỉ --</option>
                    <c:forEach items="${addressList}" var="addr">
                        <option value="${addr.id}">${addr.receiverName}
                            - ${addr.specificAddress}, ${addr.ward}, ${addr.district}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group" id="customAddressGroup">
                <label>Hoặc nhập địa chỉ</label>
                <input type="text" id="createOrderCustomAddress" placeholder="Địa chỉ cụ thể...">
            </div>
            <div class="form-group">
                <label>Phương thức vận chuyển</label>
                <select id="createOrderShipping">
                    <option value="standard">Tiêu chuẩn</option>
                    <option value="express">Nhanh</option>
                    <option value="sameday">Hỏa tốc</option>
                </select>
            </div>
            <div class="form-group">
                <label>Tìm sản phẩm</label>
                <input type="text" class="product-search-input" id="createProductSearch"
                       placeholder="Tìm sách..." oninput="searchProducts(this.value, 'create')">
                <div class="product-search-results" id="createProductResults"></div>
            </div>
            <div class="form-group">
                <label>Sản phẩm đã chọn</label>
                <div class="edit-products-list" id="createProductsList"></div>
            </div>
            <div class="form-group">
                <label>Tổng số tiền (đ)</label>
                <input type="number" id="createOrderTotal" placeholder="Tự động tính toán...">
            </div>
            <div class="popup-actions">
                <button class="btn-secondary" onclick="closeAllPopups()">Hủy</button>
                <button class="btn-primary" onclick="submitCreateOrder()">
                    <i class="fa-solid fa-paper-plane"></i> Tạo đơn
                </button>
            </div>
        </div>
    </div>

    <div id="notifPopup" class="popup-box">
        <button class="popup-close" onclick="closeAllPopups()">&times;</button>
        <h3><i class="fa-solid fa-bell"></i> Gửi thông báo</h3>
        <div class="notif-form">
            <div class="form-group">
                <label>Tiêu đề</label>
                <input type="text" id="notifTitle" placeholder="Tiêu đề thông báo">
            </div>
            <div class="form-group">
                <label>Nội dung</label>
                <textarea id="notifContent" placeholder="Nội dung thông báo..."></textarea>
            </div>
            <div class="popup-actions">
                <button class="btn-secondary" onclick="closeAllPopups()">Hủy</button>
                <button class="btn-primary" onclick="sendNotification()">
                    <i class="fa-solid fa-paper-plane"></i> Gửi
                </button>
            </div>
        </div>
    </div>


</main>
<script>

    let deleteType = null;
    let deleteItemId = null;

    let editOrderProducts = [];
    let createOrderProducts = [];


    function showOverlay() {
        document.getElementById("overlay").style.display = "block";
    }

    function hideOverlay() {
        document.getElementById("overlay").style.display = "none";
    }

    function openPopup(id) {
        showOverlay();
        document.getElementById(id).style.display = "block";
    }

    function closeAllPopups() {
        hideOverlay();
        document.querySelectorAll(".popup-box, .delete-popup")
            .forEach(p => p.style.display = "none");
    }

    document.getElementById("overlay").addEventListener("click", closeAllPopups);


    function openNotifPopup() {
        openPopup("notifPopup");
    }

    const userId = ${user.id};

    function sendNotification() {
        const ititle = document.getElementById("notifTitle");
        const icontent = document.getElementById("notifContent");
        const title = document.getElementById("notifTitle").value;
        const content = document.getElementById("notifContent").value;


        ititle.classList.remove("input-error");
        icontent.classList.remove("input-error");
        if (!title) {
            ititle.classList.add("input-error");
            ititle.focus();
            return;
        }
        if (!content) {
            icontent.classList.add("input-error");
            icontent.focus();
            return;
        }
        fetch("Notify", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "userIds=" + encodeURIComponent(userId) +
                "&title=" + encodeURIComponent(title) +
                "&content=" + encodeURIComponent(content)
        })
            .then(res => res.json())
            .then(data => {
                closeAllPopups();
                if (data.success) {
                    show(data.message);
                } else {
                    show(data.message, false);
                }
            })
            .catch(err => console.log(err));


    }


    function openGiftVoucherPopup() {
        openPopup("giftVoucherPopup");
    }

    function toggleGiftVoucher(el) {
        el.classList.toggle("selected");
        el.querySelector("input").checked =
            el.classList.contains("selected");
    }

    function submitGiftVoucher() {
        const selected = document.querySelectorAll(".voucher-gift-item.selected");
        if (selected.length === 0) {
            show("Chưa chọn voucher", false);
            return;
        }

        const voucherIds = Array.from(selected).map(item => item.dataset.code);

        fetch("GiftVoucher", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "userId=" + encodeURIComponent(userId) + "&voucherIds=" + voucherIds.join(",")
        })
            .then(res => res.json())
            .then(data => {
                closeAllPopups();
                if (data.success) {
                    show(data.message);
                    location.reload()
                } else {
                    show(data.message, false);
                }
            })
            .catch(err => console.log(err));
    }


    function confirmDeleteItem(type, id) {
        deleteType = type;
        deleteItemId = id;

        document.getElementById("deletePopupMsg")
            .innerText = "Bạn có chắc muốn xoá không?";

        openPopup("deletePopup");
    }

    function confirmDelete() {

        if (deleteType === "voucher") {
            fetch("deleteVoucher", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: "userId=" + encodeURIComponent(userId) + "&id=" + encodeURIComponent(deleteItemId)
            })
                .then(res => res.json())
                .then(data => {
                    closeAllPopups();
                    if (data.success) {
                        show(data.message);
                        location.reload();
                    } else {
                        show(data.message, false);
                    }
                })
                .catch(err => console.log(err));

            closeAllPopups();
        }
    }


    function parseDate(str) {
        if (!str) return new Date(0);

        const [time, date] = str.split(" ");
        const [hours, minutes, seconds] = time.split(":");
        const [day, month, year] = date.split("-");

        return new Date(year, month - 1, day, hours, minutes, seconds);
    }

    function filterOrders() {
        const status = document.getElementById("filterStatus").value;
        const time = document.getElementById("filterTime").value;
        const tbody = document.getElementById("orderTableBody");
        let rows = Array.from(tbody.querySelectorAll("tr"));

        rows.forEach(r => {
            if (!status) {
                r.style.display = "";
            } else {
                r.style.display = r.dataset.status === status ? "" : "none";
            }
        });

        let visibleRows = rows.filter(r => r.style.display !== "none");

        if (time === "newest") {
            visibleRows.sort((a, b) =>
                parseDate(b.dataset.date) - parseDate(a.dataset.date));
        }
        if (time === "oldest") {
            visibleRows.sort((a, b) =>
                parseDate(a.dataset.date) - parseDate(b.dataset.date));
        }

        visibleRows.forEach(r => tbody.appendChild(r));

    }


    function viewOrderProducts(orderId) {
        openPopup("orderProductsPopup");
    }


    function startEdit(field, currentVal, type) {
        const row = document.querySelector("#field-" + field + " .info-value-row");
        row.innerHTML = `
        <input type="${type}" value="${currentVal}" id="edit-${field}">
        <button onclick="saveInline('${field}')">Lưu</button>
        <button onclick="location.reload()">Hủy</button>
    `;
    }

    function saveInline(field) {
        alert("Đã lưu (demo UI)");
        location.reload();
    }


    function openEditOrderPopup(orderId) {
        editOrderProducts = [];
        renderEditProducts();
        openPopup("editOrderPopup");
    }

    function renderEditProducts() {
        const list = document.getElementById("editProductsList");
        if (editOrderProducts.length === 0) {
            list.innerHTML = "<div>Chưa có sản phẩm</div>";
            return;
        }
        list.innerHTML = editOrderProducts.map((p, i) => `
        <div class="edit-product-item">
            ${p.name} x${p.quantity}
            <i class="fa-solid fa-trash" onclick="removeEditProduct(${i})"></i>
        </div>
    `).join("");
    }

    function removeEditProduct(i) {
        editOrderProducts.splice(i, 1);
        renderEditProducts();
    }

    function saveEditOrder() {
        alert("Đã lưu chỉnh sửa đơn hàng (demo UI)");
        closeAllPopups();
    }


    function openCreateOrderPopup() {
        createOrderProducts = [];
        renderCreateProducts();
        openPopup("createOrderPopup");
    }

    function renderCreateProducts() {
        const list = document.getElementById("createProductsList");
        if (createOrderProducts.length === 0) {
            list.innerHTML = "<div>Chưa có sản phẩm</div>";
            return;
        }
        list.innerHTML = createOrderProducts.map((p, i) => `
        <div class="edit-product-item">
            ${p.name} x${p.quantity}
            <i class="fa-solid fa-trash" onclick="removeCreateProduct(${i})"></i>
        </div>
    `).join("");
    }

    function removeCreateProduct(i) {
        createOrderProducts.splice(i, 1);
        renderCreateProducts();
    }

    function submitCreateOrder() {
        alert("Đã tạo đơn hàng (demo UI)");
        closeAllPopups();
    }


    function triggerAvatarUpload() {
        document.getElementById("avatarInput").click();
    }

    function uploadAvatar(input) {
        if (input.files.length > 0) {
            alert("Đã chọn avatar (demo UI)");
        }
    }


    function show(message, isSuccess = true) {
        const toast = document.getElementById("toast");
        toast.innerText = message;
        toast.classList.remove("success", "error");
        if (isSuccess) {
            toast.classList.add("success");
        } else {
            toast.classList.add("error");
        }
        toast.classList.add("show");
        setTimeout(() => {
            toast.classList.remove("show");
        }, 2000);
    }

    function searchVouchers(keyword) {
        keyword = keyword.toLowerCase().trim();

        const items = document.querySelectorAll(".voucher-gift-item");
        const list = document.getElementById("voucherGiftList");
        const oldEmpty = document.getElementById("noVoucherFound");

        let hasResult = false;

        if (oldEmpty) oldEmpty.remove();

        if (keyword === "") {
            items.forEach(item => item.style.display = "");
            return;
        }

        items.forEach(item => {
            const code = item.dataset.code?.toLowerCase() || "";
            const type = item.dataset.type?.toLowerCase() || "";
            const desc = item.dataset.desc?.toLowerCase() || "";
            if (code.includes(keyword) || type.includes(keyword) || desc.includes(keyword)) {
                hasResult = true;
                item.style.display = "";
            } else {
                item.style.display = "none";
            }
        });

        if (!hasResult) {
            const div = document.createElement("div");
            div.id = "noVoucherFound";
            div.style.padding = "10px";
            div.style.textAlign = "center";
            div.innerHTML = "<i class='fa-solid fa-circle-exclamation'></i> Không tìm thấy voucher";
            list.appendChild(div);
        }

    }
</script>
</body>
</html>
