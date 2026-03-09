<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>changePassword</title>
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css/changePassword.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="content">
        <div class="container">
            <div class="menuUser">
                <c:import url="/user/menuUser.jsp"></c:import>
            </div>
            <div class="password-container">
                <h2>Đổi mật khẩu</h2>
                <form class="password-form" id="passwordForm" method="post" novalidate>

                    <div class="form-group">
                        <label>Mật khẩu hiện tại</label>
                        <input type="password" id="oldPass" name="oldPass" placeholder="Nhập mật khẩu hiện tại">
                        <small class="error-msg"></small>
                    </div>

                    <div class="form-group">
                        <label>Mật khẩu mới</label>
                        <input type="password" id="newPass" name="newPass" placeholder="Nhập mật khẩu mới">
                        <small class="error-msg"></small>
                    </div>

                    <div class="form-group">
                        <label>Xác nhận mật khẩu mới</label>
                        <input type="password" id="confirmPass" name="confirmPass" placeholder="Nhập lại mật khẩu mới">
                        <small class="error-msg"></small>
                    </div>

                    <button type="submit" class="save-btn">Lưu mật khẩu</button>
                </form>
            </div>

        </div>
    </div>
    <c:import url="footerUser.jsp"> </c:import>
</div>
<script>
    document.getElementById("passwordForm").addEventListener("submit", function (e) {
        e.preventDefault();

        const oldPass = document.getElementById("oldPass");
        const newPass = document.getElementById("newPass");
        const confirmPass = document.getElementById("confirmPass");

        const fields = [
            {el: oldPass, name: "mật khẩu hiện tại"},
            {el: newPass, name: "mật khẩu mới"},
            {el: confirmPass, name: "xác nhận mật khẩu mới"}
        ];

        let isValid = true;

        fields.forEach(f => {
            const errorMsg = f.el.nextElementSibling;
            if (!f.el.value.trim()) {
                errorMsg.textContent = `Vui lòng nhập ${f.name}.`;
                errorMsg.style.display = "block";
                f.el.classList.add("error");
                isValid = false;
            } else {
                errorMsg.textContent = "";
                errorMsg.style.display = "none";
                f.el.classList.remove("error");
            }
        });

        const strongPassRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d]).{8,}$/;
        if (newPass.value && !strongPassRegex.test(newPass.value)) {
            const errorMsg = newPass.nextElementSibling;
            errorMsg.textContent =
                "Mật khẩu phải ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.";
            errorMsg.style.display = "block";
            newPass.classList.add("error");
            isValid = false;
        }

        // Check confirm password
        if (newPass.value && confirmPass.value && newPass.value !== confirmPass.value) {
            const errorMsg = confirmPass.nextElementSibling;
            errorMsg.textContent = "Mật khẩu xác nhận không trùng khớp.";
            errorMsg.style.display = "block";
            confirmPass.classList.add("error");
            isValid = false;
        }

        if (isValid) {
            const form = document.getElementById("passwordForm");
            const data = new URLSearchParams(new FormData(form));

            fetch("${pageContext.request.contextPath}/DoiMK", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
                },
                body: data.toString()
            })
                .then(response => response.json())
                .then(data => {
                    showToast(data.message, data.success);
                    if (data.success) {
                        oldPass.value = '';
                        newPass.value = '';
                        confirmPass.value = '';
                    }
                })
                .catch(err => {
                    console.error(err);
                    showToast("Có lỗi xảy ra. Vui lòng thử lại.", false);
                });
        }

        function showToast(message, success = true) {
            const toast = document.getElementById("toast");
            toast.innerText = message;
            toast.style.backgroundColor = success ? "#4CAF50" : "#f44336";
            toast.classList.add("show");
            setTimeout(() => {
                toast.classList.remove("show");
            }, 3000);
        }
    });
</script>

</body>
</html>