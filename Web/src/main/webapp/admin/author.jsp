<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Tác giả</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css_admin/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css_admin/author.css?v=2">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
</head>
<body>

<main>
    <c:import url="headerAdmin.jsp"></c:import>

    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp" ></c:import>
        <div class="author">
            <h2>Quản lý Tác giả</h2>
            <form method="get" action="${pageContext.request.contextPath}/author-manage">
                <div class="function">
                    <div>
                        <button id="addAuthorBtn" type="button" style="background-color: #28a745; color: white; padding: 10px; border: none; border-radius: 5px; cursor: pointer; margin-right: 5px;">
                            <i class="fas fa-plus"></i> Thêm tác giả
                        </button>
                    </div>

                    <div class="timkiem">
                        <input type="text" class="search" name="q" placeholder="Nhập mã hoặc tên tác giả..." value="${param.q}">
                        <button class="buttonSearch" type="submit">Tìm kiếm</button>
                    </div>
                </div>

                <div class="title">
                    <h3>Danh sách tác giả</h3>
                </div>
            </form>

            <div class="author-list">
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã Tác Giả</th>
                            <th>Tên Tác Giả</th>
                            <th>Năm Sinh</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="a" items="${authors}">
                            <tr class="infUser">
                                <td>${a.id}</td>
                                <td>${a.name}</td>
                                <td>${a.birthday}</td>
                                <td>
                                    <button type="button" class="btn-edit"
                                            onclick="openEditModal('${a.id}', '${a.name}', '${a.birthday}')"
                                            style="background-color: #ffc107; color: #000; border: none; padding: 5px 10px; border-radius: 3px; cursor: pointer;">
                                        <i class="fas fa-edit"></i> Sửa
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
    <form id="taoTacGiaForm" method="post" action="${pageContext.request.contextPath}/admin-add-author" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: white; padding: 20px; border-radius: 10px; z-index: 1000; width: 400px; box-shadow: 0 4px 8px rgba(0,0,0,0.2);">
        <h3>TẠO TÁC GIẢ MỚI</h3>
            <div class="errorAdd" style="color: red; text-align: center; margin-bottom: 15px; font-weight: bold;"></div>
            <div class="form-group">
                <label>Tên Tác Giả</label>
                <input type="text" name="name" id="addName" placeholder="Nhập tên tác giả" required style="width: 100%; padding: 8px; margin-bottom: 10px;">
            </div>
            <div class="form-group">
                <label>Ngày tháng năm sinh</label>
                <input type="date" name="birthday" id="addBirthday" required style="width: 100%; padding: 8px; margin-bottom: 15px;">
            </div>
            <button type="submit" class="confirm" style="width: 100%; background: #007bff; color: white; padding: 10px; border: none; border-radius: 5px; cursor: pointer;">Thêm Tác Giả</button>
    </form>

    <form id="suaTacGiaForm" method="post" action="${pageContext.request.contextPath}/admin-edit-author" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: white; padding: 20px; border-radius: 10px; z-index: 1000; width: 400px; box-shadow: 0 4px 8px rgba(0,0,0,0.2);">
        <h3>CẬP NHẬT TÁC GIẢ</h3>
            <div class="errorEdit" style="color: red; text-align: center; margin-bottom: 15px; font-weight: bold;"></div>
                <input type="hidden" name="id" id="editId">
            <div class="form-group">
                <label>Tên Tác Giả</label>
                <input type="text" name="name" id="editName" placeholder="Bỏ trống nếu không đổi" style="width: 100%; padding: 8px; margin-bottom: 10px;">
            </div>
            <div class="form-group">
                <label>Ngày tháng năm sinh</label>
                <p style="font-size: 12px; margin: 0; color: #007bff;" id="currentBirthdayDisplay"></p>
                <input type="date" name="birthday" id="editBirthday" style="width: 100%; padding: 8px; margin-bottom: 15px; margin-top: 5px;">
            </div>
            <button type="submit" class="confirm" style="width: 100%; background: #ffc107; color: black; padding: 10px; border: none; border-radius: 5px; cursor: pointer;">Cập Nhật</button>
        </form>
</main>
<script>
    const overlay = document.getElementById("overlay");
    const addAuthorBtn = document.getElementById("addAuthorBtn");
    const taoTacGiaForm = document.getElementById("taoTacGiaForm");
    const suaTacGiaForm = document.getElementById("suaTacGiaForm");

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
            <c:if test="${param.error == 'invalid_name'}">
                msg = "Thất bại: Tên tác giả không hợp lệ!"; type = "error";
            </c:if>
            <c:if test="${param.error == 'invalid_date'}">
                msg = "Thất bại: Ngày sinh không hợp lệ!"; type = "error";
            </c:if>
            <c:if test="${param.success == 'add'}">
                msg = "Thêm tác giả thành công!"; type = "success";
            </c:if>
            <c:if test="${param.success == 'edit'}">
                msg = "Cập nhật tác giả thành công!"; type = "success";
            </c:if>

            if (msg !== "") {
                showToast(msg, type);
            }
    });

    addAuthorBtn.addEventListener("click", () => {
        overlay.style.display = "block";
        taoTacGiaForm.style.display = "block";
        document.querySelector(".errorAdd").innerText = "";
    });

    function openEditModal(id, currentName, currentBirthday) {
        document.getElementById("editId").value = id;
        document.getElementById("editName").placeholder = "Hiện tại: " + currentName;
        document.getElementById("currentBirthdayDisplay").innerText = "Hiện tại: " + currentBirthday;
        document.getElementById("editBirthday").value = "";

        document.querySelector(".errorEdit").innerText = "";
        overlay.style.display = "block";
        suaTacGiaForm.style.display = "block";
    }

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        taoTacGiaForm.style.display = "none";
        suaTacGiaForm.style.display = "none";
    });

    taoTacGiaForm.addEventListener("submit", function(e) {
        let isError = false;
        const errorAddDiv = document.querySelector(".errorAdd");
        const nameVal = document.getElementById("addName").value.trim();
        const birthdayVal = document.getElementById("addBirthday").value;
        const regexName = /^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\s]+$/;
        errorAddDiv.innerText = "";

        if (!regexName.test(nameVal)) {
            errorAddDiv.innerText = "Tên không được chứa số hoặc ký tự đặc biệt!";
            isError = true;
        } else if (!birthdayVal) {
            errorAddDiv.innerText = "Vui lòng nhập đầy đủ ngày, tháng và năm sinh!";
            isError = true;
        }

        if (isError) {
            e.preventDefault();
        }
    });

    suaTacGiaForm.addEventListener("submit", function(e) {
        const errorEditDiv = document.querySelector(".errorEdit");
        const nameVal = document.getElementById("editName").value.trim();
        const regexName = /^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\s]+$/;

        errorEditDiv.innerText = "";
        if (nameVal !== "" && !regexName.test(nameVal)) {
            errorEditDiv.innerText = "Tên mới không được chứa số hoặc ký tự đặc biệt!";
            e.preventDefault();
        }
    });
</script>
</body>
</html>