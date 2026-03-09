<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Errol</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Cormorant+Garamond:ital,wght@0,300..700;1,300..700&family=Libre+Franklin:ital,wght@0,100..900;1,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Playwrite+DE+SAS:wght@100..400&family=Sarabun:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="assets/css/home.css">
    <link rel="stylesheet" href="assets/css/errolpage.css">
</head>
<body>
<c:if test="${showOTP}">
    <div id="otpModal" class="modal active">
        <div class="modal-content">
            <h3>Xác thực tài khoản</h3>
            <p>Mã xác thực đã được gửi đến email của bạn.</p>

            <form action="Verify" method="post">
                <input type="hidden" name="email" value="${email}">
                <label for="otp">Nhập mã:</label>
                <input type="text" id="otp" name="otp" maxlength="16" placeholder="Nhập mã gồm 16 ký tự">

                <button type="submit" class="btn-confirm">Xác nhận</button>
                <div style="color: red">${error}</div>
            </form>

            <form action="ReSendMail" method="post">
                <button type="submit" class="btn-resend">Gửi lại mã</button>
            </form>
        </div>
    </div>
</c:if>

<c:import url="headerUser.jsp"></c:import>
<main class="errol_container">
    <div class="container">
        <h2>Đăng ký tài khoản</h2>
        <c:if test="${not empty message}">
            <div class="alert alert-${type}">
                <i class="fa-solid ${type == 'success' ? 'fa-check-circle' : 'fa-exclamation-triangle'}"></i>
                    ${message}
            </div>
        </c:if>
        <form id="registerForm" action="dangki" method="post">
            <section class="form-section">
                <h3>Thông tin cá nhân</h3>
                <div class="form-group">
                    <label for="fullname">Họ và Tên *</label>
                    <input type="text" id="fullname" name="fullname" placeholder="Nhập họ và tên">
                    <small class="error"></small>
                </div>
            </section>

            <section class="form-section">
                <h3>Thông tin đăng ký</h3>

                <div class="form-group">
                    <label for="email">Email </label>
                    <input type="text" id="email" name="email" placeholder="Nhập số email của bạn">
                    <small class="error"></small>
                </div>

                <div class="form-group">
                    <div class="container-password">
                        <div class="password-content">
                            <label for="password">Mật khẩu *</label>
                            <input type="password" id="password" name="password" placeholder="Nhập mật khẩu">
                        </div>
                        <div class="show-password">
                            <i class="fa-solid fa-eye" id="togglePassword"></i>
                        </div>
                    </div>
                    <small class="error"></small>
                </div>

                <div class="form-group">
                    <label for="confirm-password">Xác nhận mật khẩu *</label>
                    <input type="password" id="confirm-password" name="confirm-password"
                           placeholder="Xác nhận lại mật khẩu">
                    <small class="error"></small>
                </div>

                <button type="submit" id="btnSubmit">Tạo tài khoản</button>
            </section>

        </form>

    </div>
</main>
<c:import url="footerUser.jsp"></c:import>
</body>
<script>
    const fullname = document.getElementById("fullname");
    const email = document.getElementById("email");
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirm-password");
    const toggle = document.getElementById("togglePassword");
    const form = document.getElementById("registerForm");

    toggle.addEventListener("click", () => {
        const type = password.type === "password" ? "text" : "password";
        password.type = type;
        confirmPassword.type = type;
    });

    function setError(inputElement, message) {
        const group = inputElement.closest('.form-group');
        if (group) {
            const error = group.querySelector('.error');
            if (error) {
                error.textContent = message;
                error.style.color = 'red';
            }
        }
        inputElement.style.border = '1px solid red';
    }

    function clearError(inputElement) {
        const group = inputElement.closest('.form-group');
        if (group) {
            const error = group.querySelector('.error');
            if (error) {
                error.textContent = '';
            }
        }
        inputElement.style.border = '1px solid #0d3164';
    }
    function checkPassword(pass) {
        const regex = /^(?=.*[0-9])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
        return regex.test(pass);
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        let hasError = false;

        // Fullname
        if (fullname.value.trim() === "") {
            setError(fullname, "Vui lòng nhập họ tên");
            hasError = true;
        } else clearError(fullname);

        // Email
        if (email.value.trim() === "") {
            setError(email, "Vui lòng nhập email");
            hasError = true;
        } else if (!email.value.includes("@")) {
            setError(email, "Email không hợp lệ");
            hasError = true;
        } else clearError(email);

        // Password
        if (password.value.trim() === "") {
            setError(password, "Vui lòng nhập mật khẩu");
            hasError = true;
        } else if (!checkPassword(password.value.trim())) {
            setError(password, "Mật khẩu phải ít nhất 8 ký tự, có số và ký tự đặc biệt");
            hasError = true;
        } else clearError(password);

        // Confirm password
        if (confirmPassword.value.trim() === "") {
            setError(confirmPassword, "Vui lòng xác nhận mật khẩu");
            hasError = true;
        }
        else if (confirmPassword.value !== password.value) {
            setError(confirmPassword, "Mật khẩu không trùng khớp");
            hasError = true;
        }
        else {
            clearError(confirmPassword);
        }

        if (!hasError) {
            form.submit();
        }
    });

    const modal = document.getElementById("otpModal");

    if (modal) {
        modal.addEventListener("click", function (e) {
            if (e.target === modal) {
                modal.classList.remove("active");
            }
        });
    }
</script>

</html>