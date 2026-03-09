<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Address</title>
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" />
    <link rel="stylesheet" href="assets/css/NewAddress.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="content">
        <div class="container">
            <div class="menuUser">
                <c:import url="/user/menuUser.jsp"></c:import>
            </div>
            <div class="address-container">
                <h2>Chỉnh sửa địa chỉ</h2>
                <div class="address_default">
                    <label for="isDefault">Địa chỉ mặc định</label>
                    <input type="radio" id="isDefault" name="isDefault" value="${address.id}" <c:if test="${isDefault}">checked</c:if>  onclick="setDefault(${address.id})">
                    <small class="error-msg"></small>
                </div>
                <form id="addressForm" class="address-form" action="editAddress" method="post" novalidate>
                    <input type="hidden" name="id" value="${address.id}">


                    <div class="form-group">
                        <label for="hoten">Họ và tên</label>
                        <input type="text" id="hoten" name="hoten" placeholder="Nhập họ và tên" value="${address.name}">
                        <small class="error-msg"></small>
                    </div>

                    <div class="form-group">
                        <label for="sdt">Điện thoại</label>
                        <input type="text" id="sdt" name="sdt" placeholder="Ex: 0972xxxxxx" value="${address.phone}">
                        <small class="error-msg"></small>
                    </div>

                    <div class="form-group">
                        <label for="tinh">Tỉnh/Thành phố</label>
                        <select id="tinh" name="tinh">
                            <option value="">${address.city}</option>
                        </select>
                        <small class="error-msg"></small>
                    </div>



                    <div class="form-group">
                        <label for="xa">Xã/Phường</label>
                        <select id="xa" name="xa">
                            <option value="">${address.ward}</option>
                        </select>
                        <small class="error-msg"></small>
                    </div>

                    <div class="form-group">
                        <label for="diachi">Địa chỉ</label>
                        <input type="text" id="diachi" name="diachi" placeholder="Địa chỉ cụ thể" value="${address.specificAddress}">
                        <small class="error-msg"></small>
                    </div>

                    <!-- hidden inputs để gửi textContent lên server -->
                    <input type="hidden" id="tinhInput" name="tinhName">
                    <input type="hidden" id="xaInput" name="xaName">

                    <button type="submit" class="save-btn">Lưu địa chỉ</button>
                </form>
            </div>
        </div>
    </div>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
<script>
    function setDefault(id){
        fetch("defaultAddress",{
            method:"POST",
            headers:{
                "Content-Type":"application/x-www-form-urlencoded"
            },
            body:"isDefault="+id
        })
    }
</script>
<script>
    const tinh = document.getElementById("tinh");
    const xa = document.getElementById("xa");

    // Load danh sách Tỉnh
    fetch("https://provinces.open-api.vn/api/v2/p/")
        .then(res => res.json())
        .then(data => {
            data.forEach(p => {
                const opt = document.createElement("option");
                opt.value = p.code;
                opt.textContent = p.name;
                tinh.appendChild(opt);
            });
        });

    // Khi đổi Tỉnh => load thẳng Xã/Phường
    tinh.addEventListener("change", function () {
        xa.innerHTML = `<option value="">-- Chọn xã/phường --</option>`;
        if (!this.value) return;

        fetch("https://provinces.open-api.vn/api/v2/p/" + this.value + "?depth=2")
            .then(res => res.json())
            .then(data => {
                if (!data.wards) return;
                data.wards.forEach(w => {
                    const opt = document.createElement("option");
                    opt.value = w.code;
                    opt.textContent = w.name;
                    xa.appendChild(opt);
                });
            });
    });

    // Submit form
    document.getElementById("addressForm").addEventListener("submit", function (e) {
        document.getElementById("tinhInput").value =
            tinh.options[tinh.selectedIndex]?.textContent || "";
        document.getElementById("xaInput").value =
            xa.options[xa.selectedIndex]?.textContent || "";

        const fields = [
            { id: "hoten", name: "Họ và tên" },
            { id: "sdt", name: "Điện thoại" },
            { id: "tinh", name: "Tỉnh/Thành phố" },
            { id: "xa", name: "Xã/Phường" },
            { id: "diachi", name: "Địa chỉ" },
        ];

        let isValid = true;

        fields.forEach(field => {
            const input = document.getElementById(field.id);
            const errorMsg = input.nextElementSibling;

            if (!input.value.trim()) {
                errorMsg.textContent = `Vui lòng nhập ${field.name.toLowerCase()}.`;
                errorMsg.style.display = "block";
                input.classList.add("error");
                isValid = false;
            } else {
                errorMsg.textContent = "";
                errorMsg.style.display = "none";
                input.classList.remove("error");
            }

            if (field.id === "sdt" && input.value.trim()) {
                const phoneRegex = /^(0[1-9][0-9]{8})$/;
                if (!phoneRegex.test(input.value)) {
                    errorMsg.textContent = "Số điện thoại không hợp lệ (VD: 0972xxxxxx).";
                    errorMsg.style.display = "block";
                    input.classList.add("error");
                    isValid = false;
                }
            }
        });

        if (!isValid) e.preventDefault();
    });
</script>

</body>
</html>
