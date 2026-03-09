<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/hoSoCaNhan.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="content">
        <div class="container">
            <div class="menuUser">
                <c:import url="/user/menuUser.jsp"></c:import>
            </div>
                <div class="profile-container">
                    <h2>Hồ sơ cá nhân</h2>
                    <form id="setUpForm" action="SetUpAccount" method="post">
                    <div class="form-group">
                        <label for="name">Họ và tên</label>
                        <input type="text" id="hoten" placeholder="Nhập họ và tên" name="name" value="${user.name}" aria-describedby="hotenError"/>
                        <div class="error" id="hotenError" role="alert" aria-live="polite">Vui lòng nhập họ và tên.</div>
                    </div>
                    <div class="form-group">
                        <label for="sdt">Số điện thoại</label>
                        <div class="form-inline">
                            <input type="text" id="sdt" name="phone" value="${user.phone}" placeholder="Nhập số điện thoại" oninput="this.value=this.value.replace(/[^0-9]/g,'')">
                        </div>
                        <div class="error" id="phoneError" role="alert" aria-live="polite">Số điện thoại không hợp lệ</div>
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <div class="form-inline">
                            <input type="email" id="email" name="email" value="${user.email}" readonly>
                            <a id="thayDoiE" href="#">Thay đổi</a>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Giới tính</label>
                        <div class="gender-group">
                            <label><input type="radio" name="gioitinh" value="Nam" <c:if test="${!isDefault}">checked</c:if> > Nam</label>
                            <label><input type="radio" name="gioitinh" value="Nu" <c:if test="${isDefault}">checked</c:if> > Nữ</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Birthday</label>
                        <div class="birthday-group">
                            <input type="date" name="birthday" value="${user.birthday}">
                        </div>
                    </div>
                    <button class="btn-save">Lưu thay đổi</button>
                    </form>
                </div>
        </div>
    </div>
    <c:import url="/user/footerUser.jsp"></c:import>

</div>
<div class="overlay" id="overlay"></div>
<div class="popup" id="thaydoiEmail">
    <h3>THAY ĐỔI EMAIL</h3>

    <form action="Confirm" method="post">
        <div class="form-group">
            <label>Email</label>
            <div class="email-row">
                <input type="email" name="email" placeholder="Enter Email" value="${newEmail}" <c:if test="${showOTP}">readonly</c:if> required/>
                <button type="submit" formaction="changeEmail" class="otp-btn" <c:if test="${showOTP}"> style="display: none" </c:if> >
                    Gửi mã OTP
                </button>
            </div>
        </div>

        <c:if test="${showOTP}">
            <div class="form-group">
                <label>Mã xác nhận OTP</label>
                <input type="text" name="otp" maxlength="8" required>
            </div>

            <div class="btn-group">
                <button type="submit" class="confirm">Xác nhận</button>
                <button type="button" class="cancel">Trở về</button>
            </div>
        </c:if>
    </form>
</div>

<c:if test="${showOTP}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            document.getElementById("overlay").style.display = "block";
            document.getElementById("thaydoiEmail").style.display = "block";
        });
    </script>
</c:if>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const form = document.getElementById("setUpForm");
        const phoneInput = document.getElementById("sdt");
        const phoneError = document.getElementById("phoneError");

        const phoneRegex = /^0(3|5|7|8|9)\d{8}$/;

        phoneError.style.display = "none";

        phoneInput.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, "");
            phoneError.style.display = "none";
        });

        form.addEventListener("submit", function (e) {
            const phone = phoneInput.value.trim();

            if (phone === "") {
                e.preventDefault();
                phoneError.innerText = "Vui lòng nhập số điện thoại";
                phoneError.style.display = "block";
                phoneInput.focus();
                return;
            }

            if (!phoneRegex.test(phone)) {
                e.preventDefault();
                phoneError.innerText = "Số điện thoại không hợp lệ VD:09xx...";
                phoneError.style.display = "block";
                phoneInput.focus();
                return;
            }

            phoneError.style.display = "none";
        });
    });
</script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const hoten = document.getElementById('hoten');
        const hotenError = document.getElementById('hotenError');
        const btnSave = document.querySelector('.btn-save');

        function showError(message) {
            hotenError.textContent = message || 'Vui lòng nhập họ và tên.';
            hotenError.style.display = 'block';
            hoten.classList.add('input-error');
        }

        function hideError() {
            hotenError.style.display = 'none';
            hoten.classList.remove('input-error');
        }

        btnSave.addEventListener('click', function (e) {
            const value = hoten.value.trim();
            if (!value) {
                e.preventDefault();
                showError('Họ và tên không được để trống.');
                hoten.focus();
                return;
            }
            if (value.length < 2) {
                e.preventDefault();
                showError('Vui lòng nhập đầy đủ họ và tên (ít nhất 2 ký tự).');
                hoten.focus();
                return;
            }
            hideError();
        });
        hoten.addEventListener('input', function () {
            if (hoten.value.trim()) hideError();
        });

        hoten.addEventListener('blur', function () {
            if (!hoten.value.trim()) showError('Họ và tên không được để trống.');
        });
    });

    const overlay = document.getElementById("overlay");
    const popupE = document.getElementById("thaydoiEmail");
    const cancelBtns = document.querySelectorAll(".cancel");
    const changeBtnE = document.getElementById("thayDoiE");

    changeBtnE.addEventListener('click', (e) => {
        e.preventDefault();
        overlay.style.display = "block";
        popupE.style.display = "block";
    });

    cancelBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            overlay.style.display = "none";
            popupE.style.display = "none";
        });
    });

    overlay.addEventListener('click', () => {
        overlay.style.display = "none";
        popupE.style.display = "none";
    });

</script>

<c:if test="${not empty error}">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            alert("${error}");
        });
    </script>
</c:if>

</body>
</html>