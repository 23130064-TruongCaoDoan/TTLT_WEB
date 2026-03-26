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
    <link rel="stylesheet" href="assets/css_admin/user.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
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
                    <div>
                        <select class="filter-sp" name="sortStock" onchange="this.form.submit()">
                            <option value="">Tất cả</option>
                            <option value="pAsc"  ${param.sortStock == 'pAsc'  ? 'selected' : ''}>
                                Điểm giảm dần
                            </option>
                            <option value="pDesc" ${param.sortStock == 'pDesc' ? 'selected' : ''}>
                                Điểm tăng dần
                            </option>
                            <option value="mAsc"  ${param.sortStock == 'mAsc'  ? 'selected' : ''}>
                                Tổng tiền giảm dần
                            </option>
                            <option value="mDesc" ${param.sortStock == 'mDesc' ? 'selected' : ''}>
                                Tổng tiền tăng dần
                            </option>
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
                                                alert('Bạn không thể tự bỏ quyền quản trị của chính mình');
                                                this.value = '1';
                                                return false;
                                                }
                                                this.form.submit();
                                                ">
                                            <c:choose>
                                                <c:when test="${u.role == 0}">
                                                    <option value="0" selected>USER</option>
                                                    <option value="1">ADMIN</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="0">USER</option>
                                                    <option value="1" selected>ADMIN</option>
                                                </c:otherwise>
                                            </c:choose>

                                        </select>
                                    </form>
                                </td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/change-status">
                                        <input type="hidden" name="userId" value="${u.id}">
                                        <select name="status" onchange="
                                                if (${u.id == sessionScope.user.id} && this.value == '0') {
                                                alert('Bạn không thể tự bỏ trạng thái hoạt động của chính mình');
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
        <h3>TẶNG VOUCHER</h3>
        <div class="form-group">
            <label>Mã Voucher</label>
            <input type="text" name="voucherCode" placeholder="Nhập mã voucher " required>
        </div>
        <div class="form-group">
            <label>Chọn Khách Hàng</label>
            <div class="cacluaChon">
                <div class="chonAll"><input type="radio" name="chon" value="all" selected><label>Tất cả khách hàng</label></div>
                <div class="dieukien"><input type="radio" name="chon" value="selected"><input type="text" name="userIds" placeholder="Nhập mã khách hàng (ngăn cách bởi dấu phẩy)"></div>
            </div>
        </div>
        <c:if test="${param.error == 'invalid_code'}">
            <p class="error">Mã voucher không hợp lệ</p>
        </c:if>

        <c:if test="${param.error == 'no_user_selected'}">
            <p class="error">Chưa chọn khách hàng</p>
        </c:if>
        <c:if test="${param.success == 'notify'}">
            <script>
                alert("Tạo thông báo thành công");
            </script>
        </c:if>
        <button type="submit" class="confirm">Xác nhận</button>
    </form>

    <form id="taoThongBao" method="post" action="${pageContext.request.contextPath}/notify-user">
        <h3>THÔNG BÁO</h3>

        <div class="form-group">
            <label>Tiêu Đề</label>
            <input type="text" name="title" placeholder="Nhập tiêu đề" required>
        </div>

        <div class="form-group">
            <label>Mô tả</label>
            <textarea name="content" class="mota" placeholder="Nhập mô tả"></textarea>
        </div>

        <div class="form-group">
            <label>Người nhận</label>
            <select name="userIds" class="mota" multiple required>
                <c:forEach items="${users}" var="u">
                    <option value="${u.id}">${u.email}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="confirm">Xác nhận</button>
    </form>


</main>
<c:if test="${param.error == 'invalid_code'}">
    <script>
        alert(" Mã voucher không hợp lệ");
    </script>
</c:if>

<c:if test="${param.error == 'no_user_selected'}">
    <script>
        alert(" Chưa chọn khách hàng");
    </script>
</c:if>

<c:if test="${param.success == 'gifted'}">
    <script>
        alert(" Tặng voucher thành công");
    </script>
</c:if>

<script>
    const overlay = document.getElementById("overlay");
    const add = document.getElementById("add");
    const notify = document.getElementById("notify");
    const popupVoucher = document.getElementById("tangVoucherForm");
    const popupNotify = document.getElementById("taoThongBao");

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        popupVoucher.style.display = "none";
        popupNotify.style.display = "none";
    });

    add.addEventListener("click", () => {
        overlay.style.display = "block";
        popupVoucher.style.display = "block";
    });

    notify.addEventListener("click", () => {
        overlay.style.display = "block";
        popupNotify.style.display = "block";
    });
</script>

</body>
</html>
