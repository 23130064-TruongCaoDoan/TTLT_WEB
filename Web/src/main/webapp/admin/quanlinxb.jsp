<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Nhà Xuất Bản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css_admin/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css_admin/publisher.css?v=1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
</head>
<body>

<main>
    <c:import url="headerAdmin.jsp"></c:import>

    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp" ></c:import>
        <div class="publisher">
            <h2>Quản lý Nhà Xuất Bản</h2>
            <form method="get" action="${pageContext.request.contextPath}/publisher-manage">
                <div class="function">
                    <div>
                        <button id="addPublisherBtn" type="button" style="background-color: #28a745; color: white; padding: 10px; border: none; border-radius: 5px; cursor: pointer; margin-right: 5px;">
                            <i class="fas fa-plus"></i> Thêm NXB
                        </button>
                    </div>

                    <div class="timkiem">
                        <input type="text" class="search" name="q" placeholder="Nhập mã hoặc tên NXB..." value="${param.q}">
                        <button class="buttonSearch" type="submit">Tìm kiếm</button>
                    </div>
                </div>

                <div class="title">
                    <h3>Danh sách nhà xuất bản</h3>
                </div>
            </form>

            <div class="publisher-list">
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã NXB</th>
                            <th>Tên Nhà Xuất Bản</th>
                            <th>Địa Chỉ</th>
                            <th>Số Điện Thoại</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="p" items="${publishers}">
                            <tr class="infUser">
                                <td>${p.publisherCode}</td>
                                <td>${p.name}</td>
                                <td>${p.address}</td>
                                <td>${p.phone}</td>
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
    <div id="custom-toast"></div>

    <form id="taoNXBForm" method="post" action="${pageContext.request.contextPath}/admin-add-publisher">
        <h3>TẠO NHÀ XUẤT BẢN MỚI</h3>
        <div class="errorAdd" style="color: red; text-align: center; margin-bottom: 15px; font-weight: bold;"></div>

        <div class="form-group">
            <label>Mã NXB</label>
            <input type="text" name="code" id="addCode" placeholder="Nhập mã nhà xuất bản" required>
        </div>
        <div class="form-group">
            <label>Tên NXB</label>
            <input type="text" name="name" id="addName" placeholder="Nhập tên nhà xuất bản" required>
        </div>
        <div class="form-group">
            <label>Địa Chỉ</label>
            <input type="text" name="address" id="addAddress" placeholder="Nhập địa chỉ" required>
        </div>
        <div class="form-group">
            <label>Số Điện Thoại</label>
            <input type="text" name="phone" id="addPhone" placeholder="Nhập số điện thoại" required>
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" id="addEmail" placeholder="Nhập email" required>
        </div>

        <button type="submit" class="confirm" style="background: #007bff;">Thêm Nhà Xuất Bản</button>
    </form>

    <form id="suaNXBForm" method="post" action="${pageContext.request.contextPath}/admin-edit-publisher">
        <h3>CẬP NHẬT NHÀ XUẤT BẢN</h3>
        <div class="errorEdit" style="color: red; text-align: center; margin-bottom: 15px; font-weight: bold;"></div>

        <input type="hidden" name="id" id="editId">

        <div class="form-group">
            <label>Tên NXB</label>
            <input type="text" name="name" id="editName" placeholder="Tên NXB">
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
    <form id="deletePublisherForm" method="post" action="${pageContext.request.contextPath}/admin-delete-publisher" style="display:none;">
        <input type="hidden" name="id" id="deleteId">
        <input type="hidden" name="publisherName" id="deletePublisherName">
    </form>
</main>

<script>
    const overlay = document.getElementById("overlay");
    const addPublisherBtn = document.getElementById("addPublisherBtn");
    const taoNXBForm = document.getElementById("taoNXBForm");
    const suaNXBForm = document.getElementById("suaNXBForm");

    function showToast(message, type) {
        const toast = document.getElementById("custom-toast");
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
        <c:if test="${param.error == 'duplicate_name'}">
            msg = "Thất bại: Tên NXB đã tồn tại!"; type = "error";
        </c:if>
        <c:if test="${param.error == 'invalid_name'}">
            msg = "Thất bại: Tên NXB không hợp lệ!"; type = "error";
        </c:if>
        <c:if test="${param.success == 'add'}">
            msg = "Thêm NXB thành công!"; type = "success";
        </c:if>
        <c:if test="${param.success == 'edit'}">
            msg = "Cập nhật NXB thành công!"; type = "success";
        </c:if>
        <c:if test="${param.success == 'delete'}">
            msg = "Đã xóa Nhà Xuất Bản thành công!"; type = "success";
        </c:if>
        <c:if test="${param.error == 'delete_failed'}">
            msg = "Thất bại: Không thể xóa NXB này (có thể do đang có sách thuộc NXB)!"; type = "error";
        </c:if>

        if (msg !== "") {
            showToast(msg, type);
        }
    });

    addPublisherBtn.addEventListener("click", () => {
        overlay.style.display = "block";
        taoNXBForm.style.display = "block";
        document.querySelector(".errorAdd").innerText = "";
    });

    function confirmDelete(id, name) {
            if (confirm("Bạn có chắc chắn muốn xóa nhà xuất bản '" + name + "' không?")) {
                document.getElementById("deleteId").value = id;
                document.getElementById("deletePublisherName").value = name;
                document.getElementById("deletePublisherForm").submit();
            }
        }

        addPublisherBtn.addEventListener("click", () => {
            overlay.style.display = "block";
            taoNXBForm.style.display = "block";
            document.querySelector(".errorAdd").innerText = "";
    });

    function openEditModal(id, name, address, email, phone) {
        document.getElementById("editId").value = id;
        document.getElementById("editName").value = name;
        document.getElementById("editAddress").value = address;
        document.getElementById("editEmail").value = email;
        document.getElementById("editPhone").value = phone;

        document.querySelector(".errorEdit").innerText = "";
        overlay.style.display = "block";
        suaNXBForm.style.display = "block";
    }

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        taoNXBForm.style.display = "none";
        suaNXBForm.style.display = "none";
    });

    taoNXBForm.addEventListener("submit", function(e) {
        let isError = false;
        const errorAddDiv = document.querySelector(".errorAdd");
        const nameVal = document.getElementById("addName").value.trim();
        const phoneVal = document.getElementById("addPhone").value.trim();

        errorAddDiv.innerText = "";
        if (nameVal === "") {
            errorAddDiv.innerText = "Tên không được để trống!";
            isError = true;
        } else if (!/^[0-9+ ]{8,15}$/.test(phoneVal)) { // Regex sdt
            errorAddDiv.innerText = "Số điện thoại không hợp lệ!";
            isError = true;
        }

        if (isError) {
            e.preventDefault();
        }
    });

    suaNXBForm.addEventListener("submit", function(e) {
        let isError = false;
        const errorEditDiv = document.querySelector(".errorEdit");
        const phoneVal = document.getElementById("editPhone").value.trim();

        errorEditDiv.innerText = "";
        if (phoneVal !== "" && !/^[0-9+ ]{8,15}$/.test(phoneVal)) {
            errorEditDiv.innerText = "Số điện thoại mới không hợp lệ!";
            isError = true;
        }

        if (isError) {
            e.preventDefault();
        }
    });
</script>
</body>
</html>