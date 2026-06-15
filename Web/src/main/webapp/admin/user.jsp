<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý user</title>
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <link rel="stylesheet" href="assets/css_admin/user.css?v=6">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
</head>
<body>

<main>
    <c:import url="headerAdmin.jsp"></c:import>

    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp" ></c:import>
        <div class="user">
            <h2>Quản lý khách hàng</h2>
            <form method="get" action="${pageContext.request.contextPath}/user-manage">

                <div class="function">
                    <div>
                        <button id="addAcc" type="button" style="background-color: #28a745; color: white; padding: 10px; border: none; border-radius: 5px; cursor: pointer; margin-right: 5px;">
                            <i class="fas fa-plus"></i> Tạo tài khoản
                        </button>
                        <button id="add" type="button">Tặng voucher</button>
                        <button id="notify" type="button">Tạo thông báo</button>
                    </div>

                    <div class="timkiem">
                        <input type="text"
                               class="search"
                               name="q"
                               placeholder="Tìm kiếm khách hàng"
                               value="${param.q}">
                        <button class="buttonSearch" type="submit">Tìm kiếm</button>
                    </div>
                </div>

                <div class="title">
                    <h3>Danh sách khách hàng</h3>
                        <div style="display: flex; gap: 10px;">
                            <select class="filter-sp" name="roleFilter" onchange="this.form.submit()">
                                <option value="">Tất cả quyền</option>
                                <c:forEach var="r" items="${roles}">
                                    <option value="${r.id}" ${param.roleFilter == r.id ? 'selected' : ''}>
                                        ${r.roleName}
                                    </option>
                                </c:forEach>
                            </select>

                            <select class="filter-sp" name="statusFilter" onchange="this.form.submit()">
                                <option value="">Tất cả trạng thái</option>
                                <option value="1" ${param.statusFilter == '1' ? 'selected' : ''}>Mở</option>
                                <option value="0" ${param.statusFilter == '0' ? 'selected' : ''}>Khóa</option>
                            </select>

                            <select class="filter-sp" name="sortStock" onchange="this.form.submit()">
                                <option value="">Mặc định</option>
                                <option value="pAsc"  ${param.sortStock == 'pAsc'  ? 'selected' : ''}>Điểm giảm dần</option>
                                <option value="pDesc" ${param.sortStock == 'pDesc' ? 'selected' : ''}>Điểm tăng dần</option>
                                <option value="mAsc"  ${param.sortStock == 'mAsc'  ? 'selected' : ''}>Tổng tiền giảm dần</option>
                                <option value="mDesc" ${param.sortStock == 'mDesc' ? 'selected' : ''}>Tổng tiền tăng dần</option>
                            </select>

                        </div>
                </div>

            </form>

            <div class="user-list">
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã Khách Hàng</th>
                            <th>Tên Khách Hàng</th>
                            <th>Email</th>
                            <th>Point</th>
                            <th>Tổng tiền mua</th>
                            <th>Quyền</th>
                            <th>Trang thái</th>
                            <th>Chi tiết</th>
                        </tr>
                        </thead>
                        <tbody id="userTable">
                        <c:forEach var="u" items="${users}">
                            <tr class="infUser">
                                <td>${u.customerCode}</td>
                                <td>${u.name}</td>
                                <td>${u.email}</td>
                                <td>${u.point}</td>
                                <td><p><fmt:formatNumber value="${u.totalSpent}" type="number" groupingUsed="true"
                                                         maxFractionDigits="0"/> đ</p></td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/change-role">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <select name="role" onchange="
                                                if (${u.id == sessionScope.user.id} && this.value == '0') {
                                                showToast('Bạn không thể tự bỏ quyền quản trị của chính mình', 'error');
                                                this.value = '1';
                                                return false;
                                                }
                                                this.form.submit();
                                                ">
                                            <c:forEach var="r" items="${roles}">
                                                    <option value="${r.id}"${r.id == u.role ? 'selected' : ''}>
                                                            ${r.roleName}
                                                    </option>
                                            </c:forEach>
                                        </select>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/change-status">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <select name="status" onchange="
                                                if (${u.id == sessionScope.user.id} && this.value == '0') {
                                                showToast('Bạn không thể tự bỏ trạng thái hoạt động của chính mình', 'error');
                                                this.value = '1';
                                                return false;
                                                }
                                                this.form.submit();
                                                ">
                                            <c:choose>
                                                <c:when test="${u.status == 0}">
                                                    <option value="0" selected>Khóa</option>
                                                    <option value="1">Mở</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="0">Khóa</option>
                                                    <option value="1" selected>Mở</option>
                                                </c:otherwise>
                                            </c:choose>

                                        </select>
                                    </form>
                                </td>
                                <td> <i class="fas fa-info-circle" onclick="window.location.href='<%= request.getContextPath() %>/UserDetail?id=${u.id}'"></i></td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                </div>
            </div>
        </div>
    </div>
    <div id="overlay"></div>

    <form id="tangVoucherForm" method="post" action="${pageContext.request.contextPath}/gift-voucher">
            <h3 class="popup-title">TẶNG VOUCHER</h3>

            <div class="gift-container">
                <div class="gift-column">
                    <h4><i class="fas fa-users"></i> Chọn Khách Hàng</h4>

                    <div class="radio-group">
                        <label class="radio-label">
                            <input type="radio" name="chon" value="all" id="chonTatCaU" onchange="toggleUserListGift()">
                            Tất cả khách hàng
                        </label>
                        <label class="radio-label">
                            <input type="radio" name="chon" value="selected" id="chonMotSoU" checked onchange="toggleUserListGift()">
                            Chọn từ danh sách
                        </label>
                    </div>

                    <div id="userSelectionBlock" class="selection-block">
                        <div class="search-box">
                            <i class="fas fa-search"></i>
                            <input type="text" id="searchUserGift" placeholder="Tìm tên/email khách hàng..." onkeyup="filterGiftList('searchUserGift', 'listUserGift')">
                        </div>

                        <div class="gift-list-box">
                            <div class="gift-list-header">
                                <label><input type="checkbox" id="selectAllUGift" onchange="toggleSelectAllGift('listUserGift', this)"> Chọn tất cả</label>
                            </div>
                            <div id="listUserGift" class="gift-list-content">
                                <c:forEach var="u" items="${users}">
                                    <label class="gift-item">
                                        <input type="checkbox" name="userIds" value="KH${u.id}">
                                        <span><strong>${u.name}</strong> - ${u.email}</span>
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="gift-column">
                    <h4><i class="fas fa-ticket-alt"></i> Chọn Voucher</h4>
                    <div style="height: 40px;"></div>
                    <div class="selection-block">
                        <div class="search-box">
                            <i class="fas fa-search"></i>
                            <input type="text" id="searchVoucherGift" placeholder="Tìm mã voucher..." onkeyup="filterGiftList('searchVoucherGift', 'listVoucherGift')">
                        </div>

                        <div class="gift-list-box">
                            <div class="gift-list-header">
                                <label><input type="checkbox" id="selectAllVGift" onchange="toggleSelectAllGift('listVoucherGift', this)"> Chọn tất cả</label>
                            </div>
                            <div id="listVoucherGift" class="gift-list-content">
                                <c:forEach items="${listVoucher}" var="v">
                                    <label class="gift-item">
                                        <input type="checkbox" name="voucherCodes" value="${v.code}">
                                        <span><strong>${v.code}</strong> - ${v.description}</span>
                                    </label>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="gift-actions">
                <button type="button" class="btn-cancel" onclick="document.getElementById('overlay').click()">Hủy</button>
                <button type="submit" class="btn-submit">Xác nhận tặng</button>
            </div>
        </form>

    <form id="taoThongBao" method="post" action="${pageContext.request.contextPath}/notify-user" style="width: 550px; max-width: 95vw;">
            <h3 class="popup-title">TẠO THÔNG BÁO</h3>
            <div class="form-group">
                <label style="font-weight: bold; color: #0d3164;">Tiêu Đề</label>
                <input type="text" name="title" placeholder="Nhập tiêu đề" required style="margin-top: 8px;">
            </div>
            <div class="form-group">
                <label style="font-weight: bold; color: #0d3164;">Mô tả</label>
                <textarea name="content" class="mota" placeholder="Nhập nội dung thông báo..." required style="margin-top: 8px; height: 80px; resize: none;"></textarea>
            </div>
            <div class="form-group">
                <label style="font-weight: bold; color: #0d3164;"><i class="fas fa-users"></i> Chọn Người Nhận</label>
                <div class="selection-block" style="margin-top: 10px;">
                    <div class="search-box">
                        <i class="fas fa-search"></i>
                        <input type="text" id="searchUserNotify" placeholder="Tìm tên/email người nhận..." onkeyup="filterGiftList('searchUserNotify', 'listUserNotify')">
                    </div>
                    <div class="gift-list-box">
                        <div class="gift-list-header">
                            <label>
                                <input type="checkbox" id="selectAllUNotify" onchange="toggleSelectAllGift('listUserNotify', this)">
                                Chọn tất cả
                            </label>
                        </div>
                        <div id="listUserNotify" class="gift-list-content" style="max-height: 200px;">
                            <c:forEach items="${users}" var="u">
                                <label class="gift-item">
                                    <input type="checkbox" name="userIds" value="${u.id}">
                                    <span><strong>${u.name}</strong> - ${u.email}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="gift-actions">
                <button type="button" class="btn-cancel" onclick="document.getElementById('overlay').click()">Hủy</button>
                <button type="submit" class="btn-submit">Xác nhận gửi</button>
            </div>
    </form>

    <form id="taoTaiKhoanForm" method="post" action="${pageContext.request.contextPath}/admin-add-user">
        <h3>TẠO TÀI KHOẢN MỚI</h3>
        <div class="errorAdd" style="color: red; text-align: center; margin-bottom: 15px; font-weight: bold;"></div>
        <div class="form-group">
            <label>Họ và Tên</label>
            <input type="text" name="name" placeholder="Nhập họ và tên" required>
        </div>

        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" placeholder="Nhập email" required>
        </div>

        <div class="form-group">
            <label>Mật khẩu</label>
            <input type="password" name="password" placeholder="Nhập mật khẩu" required>
        </div>

        <div class="form-group-inline">
            <div style="flex: 1;">
                <label>Phân quyền</label>
                <select name="role">
                    <c:forEach var="r" items="${roles}">
                        <c:if test="${!(sessionScope.user.role == 2 && (r.id == 1 || r.id == 2))}">
                            <option value="${r.id}"
                                    <c:if test="${r.id == u.role}">selected</c:if>>
                                    ${r.roleName}
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <div style="flex: 1;">
                <label>Trạng thái</label>
                <select name="status">
                    <option value="1">Mở</option>
                    <option value="0">Khóa</option>
                </select>
            </div>
        </div>

        <button type="submit" class="confirm">Tạo Tài Khoản</button>
    </form>

</main>
<c:if test="${param.error == 'invalid_code'}">
    <script>
        document.addEventListener('DOMContentLoaded', () => showToast("Mã voucher không hợp lệ", "error"));
    </script>
</c:if>

<c:if test="${param.error == 'no_user_selected'}">
    <script>
        document.addEventListener('DOMContentLoaded', () => showToast("Chưa chọn khách hàng", "error"));
    </script>
</c:if>

<c:if test="${param.success == 'gifted'}">
    <script>
        document.addEventListener('DOMContentLoaded', () => showToast("Tặng voucher thành công", "success"));
    </script>
</c:if>
<c:if test="${param.success == 'notify'}">
    <script>
        document.addEventListener('DOMContentLoaded', () => showToast("Tạo thông báo thành công", "success"));
    </script>
</c:if>

<script>
    const overlay = document.getElementById("overlay");
    const add = document.getElementById("add");
    const notify = document.getElementById("notify");
    const addAcc = document.getElementById("addAcc");
    const popupVoucher = document.getElementById("tangVoucherForm");
    const popupNotify = document.getElementById("taoThongBao");
    const popupAddAcc = document.getElementById("taoTaiKhoanForm");

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        popupVoucher.style.display = "none";
        popupNotify.style.display = "none";
        popupAddAcc.style.display = "none";
    });

    add.addEventListener("click", () => {
        overlay.style.display = "block";
        popupVoucher.style.display = "block";
    });

    notify.addEventListener("click", () => {
        overlay.style.display = "block";
        popupNotify.style.display = "block";
    });

    addAcc.addEventListener("click", () => {
        overlay.style.display = "block";
        popupAddAcc.style.display = "block";
    });

    function showToast(message, type) {
        const toast = document.getElementById("toast");
        toast.innerText = message;
        toast.classList.remove("success", "error", "show");
        toast.classList.add(type, "show");
        setTimeout(() => {
            toast.classList.remove("show");
        }, 3000);
    }

        const formTaoTaiKhoan = document.getElementById("taoTaiKhoanForm");
        const errorAddDiv = document.querySelector(".errorAdd");
        function checkPassword(password) {
                const regex = /^(?=.*[0-9])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
                return regex.test(password);
            }

        formTaoTaiKhoan.addEventListener("submit", function(event) {
            event.preventDefault();

            errorAddDiv.innerText = "";
            errorAddDiv.style.color = "red";

            const passwordValue = this.password.value.trim();

            if (!checkPassword(passwordValue)) {
                errorAddDiv.innerText = "Mật khẩu phải có ít nhất 8 ký tự, có số và kí tự đặc biệt";
                    return; // Dừng lại không gửi dữ liệu
            }

            const formData = new FormData(this);
            const params = new URLSearchParams(formData);

            fetch(this.action, {
                method: this.method,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString()
            })
            .then(response => {
                if (response.url.includes("error=email_exists")) {
                    errorAddDiv.innerText = "Thất bại: Email này đã tồn tại trong hệ thống!";
                } else if (response.url.includes("error=invalid_password")) {
                    errorAddDiv.innerText = "Mật khẩu phải có ít nhất 8 ký tự, có số và kí tự đặc biệt";
                } else if (response.url.includes("success=add_user")) {
                    errorAddDiv.style.color = "green";
                    errorAddDiv.innerText = "Tạo tài khoản mới thành công!";
                    setTimeout(() => { window.location.reload(); }, 1500);
                }
            })
            .catch(error => {
                errorAddDiv.innerText = "Có lỗi xảy ra phía máy chủ, vui lòng thử lại!";
            });
    });

        function filterGiftList(inputId, listId) {
            let filter = document.getElementById(inputId).value.toLowerCase();
            let labels = document.getElementById(listId).getElementsByTagName('label');
            for (let i = 0; i < labels.length; i++) {
                let text = labels[i].innerText.toLowerCase();
                labels[i].style.display = text.includes(filter) ? "block" : "none";
            }
        }

        function toggleSelectAllGift(listId, masterCheckbox) {
            let list = document.getElementById(listId);
            let checkboxes = list.querySelectorAll('input[type="checkbox"]');
            for (let i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].parentElement.style.display !== "none") {
                    checkboxes[i].checked = masterCheckbox.checked;
                }
            }
        }

        function toggleUserListGift() {
            const isAll = document.getElementById("chonTatCaU").checked;
            const block = document.getElementById("userSelectionBlock");
            if(isAll) {
                block.style.opacity = "0.4";
                block.style.pointerEvents = "none";
            } else {
                block.style.opacity = "1";
                block.style.pointerEvents = "auto";
            }
        }
</script>

</body>
</html>
