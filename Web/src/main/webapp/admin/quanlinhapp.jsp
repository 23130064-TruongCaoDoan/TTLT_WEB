<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Nhà Phân Phối</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css_admin/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css_admin/provider.css?v=1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
</head>
<body>

<main>
    <c:import url="headerAdmin.jsp"></c:import>

    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp" ></c:import>
        <div class="publisher">
            <h2>Quản lý Nhà Phân Phối</h2>
            <form method="get" action="${pageContext.request.contextPath}/provider-manage">
                <div class="function">
                    <div>
                        <button id="addProviderBtn" type="button" style="background-color: #28a745; color: white; padding: 10px; border: none; border-radius: 5px; cursor: pointer; margin-right: 5px;">
                            <i class="fas fa-plus"></i> Thêm NPP
                        </button>
                    </div>

                    <div class="timkiem">
                        <input type="text" class="search" name="q" placeholder="Nhập mã hoặc tên NPP..." value="${param.q}">
                        <button class="buttonSearch" type="submit">Tìm kiếm</button>
                    </div>
                </div>

                <div class="title">
                    <h3>Danh sách Nhà Phân Phối</h3>
                </div>
            </form>

            <div class="publisher-list">
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã NPP</th>
                            <th>Tên Nhà Phân Phối</th>
                            <th>Thông Tin Liên Hệ</th>
                            <th>Đang Phân Phối Sách</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="p" items="${providers}">
                            <tr class="infUser">
                                <td><strong>${p.code}</strong></td>
                                <td>${p.name}</td>
                                <td style="text-align: left; padding-left: 20px;">
                                    <div>SĐT: ${p.phone != null && !p.phone.isEmpty() ? p.phone : 'N/A'}</div>
                                    <div>Email: ${p.email != null && !p.email.isEmpty() ? p.email : 'N/A'}</div>
                                    <div style="color: gray; font-size: 0.9em;">ĐC: ${p.address != null && !p.address.isEmpty() ? p.address : 'N/A'}</div>
                                </td>
                                <td style="text-align: center; font-weight: bold;">${p.bookCount}</td>
                                <td>
                                    <button type="button" class="btn-edit"
                                            onclick="openEditModal('${p.id}', '${p.name}', '${p.address}', '${p.email}', '${p.phone}')"
                                            style="background-color: #ffc107; color: #000; border: none; padding: 5px 10px; border-radius: 3px; cursor: pointer;">
                                        <i class="fas fa-edit"></i> Sửa
                                    </button>
                                    <button type="button" class="btn-delete"
                                            onclick="confirmDelete('${p.id}', '${p.name}')"
                                            style="background-color: #dc3545; color: white; border: none; padding: 5px 10px; border-radius: 3px; cursor: pointer; margin-left: 5px;">
                                        <i class="fas fa-trash"></i> Xóa
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty providers}">
                            <tr>
                                <td colspan="5" style="text-align: center; color: gray;">Không tìm thấy nhà phân phối nào.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
                <c:if test="${totalPages > 1}">
                    <div class="pagination-container">
                        <ul class="pagination">
                            <li class="${currentPage == 1 ? 'disabled' : ''}">
                                <a href="?page=${currentPage - 1}&q=${param.q}">
                                    <i class="fas fa-angle-left"></i>
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="${currentPage == i ? 'active' : ''}">
                                    <a href="?page=${i}&q=${param.q}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="${currentPage == totalPages ? 'disabled' : ''}">
                                <a href="?page=${currentPage + 1}&q=${param.q}">
                                    <i class="fas fa-angle-right"></i>
                                </a>
                            </li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <div id="overlay"></div>

    <form id="taoProviderForm" method="post" action="${pageContext.request.contextPath}/admin-add-provider" class="modal-form" style="display: none;">
        <h3>TẠO NHÀ PHÂN PHỐI MỚI</h3>
        <div class="errorAdd" style="color: red; text-align: center; margin-bottom: 15px; font-weight: bold;"></div>

        <div class="form-group">
            <label>Mã NPP <span style="color:red;">*</span></label>
            <input type="text" name="code" id="addCode" placeholder="Nhập mã nhà phân phối" required>
        </div>
        <div class="form-group">
            <label>Tên NPP <span style="color:red;">*</span></label>
            <input type="text" name="name" id="addName" placeholder="Nhập tên nhà phân phối" required>
        </div>
        <div class="form-group">
            <label>Địa Chỉ</label>
            <input type="text" name="address" id="addAddress" placeholder="Nhập địa chỉ">
        </div>
        <div class="form-group">
            <label>Số Điện Thoại</label>
            <input type="text" name="phone" id="addPhone" placeholder="Nhập số điện thoại">
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" id="addEmail" placeholder="Nhập email">
        </div>

        <button type="submit" class="confirm" style="background: #007bff; color: white;">Thêm NPP</button>
    </form>

    <form id="suaProviderForm" method="post" action="${pageContext.request.contextPath}/admin-edit-provider" class="modal-form" style="display: none;">
        <h3>CẬP NHẬT NHÀ PHÂN PHỐI</h3>
        <div class="errorEdit" style="color: red; text-align: center; margin-bottom: 15px; font-weight: bold;"></div>

        <input type="hidden" name="id" id="editId">

        <div class="form-group">
            <label>Tên NPP <span style="color:red;">*</span></label>
            <input type="text" name="name" id="editName" placeholder="Tên NPP" required>
        </div>
        <div class="form-group">
            <label>Địa Chỉ</label>
            <input type="text" name="address" id="editAddress" placeholder="Địa chỉ">
        </div>
        <div class="form-group">
            <label>Số Điện Thoại</label>
            <input type="text" name="phone" id="editPhone" placeholder="Số điện thoại">
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" id="editEmail" placeholder="Email">
        </div>

        <button type="submit" class="confirm" style="background: #ffc107; color: black;">Cập Nhật</button>
    </form>

    <form id="deleteProviderForm" method="post" action="${pageContext.request.contextPath}/admin-delete-provider" style="display:none;">
        <input type="hidden" name="id" id="deleteId">
        <input type="hidden" name="providerName" id="deleteProviderName">
    </form>
</main>

<script>
    const overlay = document.getElementById("overlay");
    const addProviderBtn = document.getElementById("addProviderBtn");
    const taoProviderForm = document.getElementById("taoProviderForm");
    const suaProviderForm = document.getElementById("suaProviderForm");

    function showToast(message, type) {
        const toast = document.getElementById("toast");
        toast.innerText = message;
        toast.classList.remove("success", "error", "show");
        toast.classList.add(type, "show");
        setTimeout(() => {
            toast.classList.remove("show");
        }, 3000);
        const url = new URL(window.location);
        url.searchParams.delete('success');
        url.searchParams.delete('error');
        window.history.replaceState({}, document.title, url);
    }

    document.addEventListener("DOMContentLoaded", function() {
        let msg = "";
        let type = "";

        <c:if test="${param.error == 'duplicate_code'}">
            msg = "Thất bại: Mã NPP đã tồn tại!"; type = "error";
        </c:if>
        <c:if test="${param.error == 'invalid_name'}">
            msg = "Thất bại: Tên hoặc Mã không được trống!"; type = "error";
        </c:if>
        <c:if test="${param.success == 'add'}">
            msg = "Thêm Nhà Phân Phối thành công!"; type = "success";
        </c:if>
        <c:if test="${param.success == 'edit'}">
            msg = "Cập nhật NPP thành công!"; type = "success";
        </c:if>
        <c:if test="${param.success == 'delete'}">
            msg = "Đã xóa Nhà Phân Phối thành công!"; type = "success";
        </c:if>
        <c:if test="${param.error == 'delete_failed'}">
            msg = "Thất bại: Lỗi khi xóa NPP!"; type = "error";
        </c:if>

        if (msg !== "") {
            showToast(msg, type);
        }
    });

    addProviderBtn.addEventListener("click", () => {
        document.getElementById("addCode").value = "";
        document.getElementById("addName").value = "";
        document.getElementById("addAddress").value = "";
        document.getElementById("addPhone").value = "";
        document.getElementById("addEmail").value = "";

        overlay.style.display = "block";
        taoProviderForm.style.display = "block";
        document.querySelector(".errorAdd").innerText = "";
    });

    function openEditModal(id, name, address, email, phone) {
        document.getElementById("editId").value = id;
        document.getElementById("editName").value = name;
        document.getElementById("editAddress").value = address !== 'N/A' ? address : '';
        document.getElementById("editEmail").value = email !== 'N/A' ? email : '';
        document.getElementById("editPhone").value = phone !== 'N/A' ? phone : '';

        document.querySelector(".errorEdit").innerText = "";
        overlay.style.display = "block";
        suaProviderForm.style.display = "block";
    }

    function confirmDelete(id, name) {
        showConfirm("Bạn có chắc chắn muốn xóa nhà phân phối '" + name + "' không?", () => {
            document.getElementById("deleteId").value = id;
            document.getElementById("deleteProviderName").value = name;
            document.getElementById("deleteProviderForm").submit();
        });
    }

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        taoProviderForm.style.display = "none";
        suaProviderForm.style.display = "none";
    });
</script>
</body>
</html>