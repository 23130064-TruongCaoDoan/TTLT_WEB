<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/Login.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />

</head>
<body>
<c:import url="headerUser.jsp"></c:import>
<div id="content">
    <div class="container">
        <c:if test="${not empty message}">
            <div class="login-success-alert">
                <div><i class="fa-solid fa-check-circle"></i> <strong>${message}</strong></div>
                <button type="button" class="close-btn" onclick="this.parentElement.remove()">&times;</button>
            </div>
        </c:if>
        <form action="login" method="post" class="login">
            <div class="title">Đăng nhập</div>
            <div class="inputIfor">
                <div class="khung user"><input value="${username}" type="text" id="iUser" name="user"
                                               placeholder="Email"></div>
                <div class="khung MK"><input value="${password}" type="password" id="iPass" name="password"
                                             placeholder="Mật khẩu">
                    <button type="button" class="show"><i class="fa-solid fa-eye "></i></button>
                </div>
                <div class="error" style="color: red">${error}</div>
            </div>
            <div class="forget"><a href="" class="qmk"><span>Quên mật khẩu</span></a></div>
            <div class="buttonLoginAndSignUp">
                <button type="submit" class="dangNhap">Đăng nhập</button>
                <div class="signUp"><a href="<c:url value='dangki' />"><span>Đăng ký</span></a></div>
            </div>
            <div class="login-google">
                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/Web_war_exploded/login-google&response_type=code&client_id=846603349467-fma7pe8c0b03i56hibab7psvktbnluj7.apps.googleusercontent.com&approval_prompt=force">
                    <img src="https://freelogopng.com/images/all_img/1657952440google-logo-png-transparent.png" alt="">
                    <span>Đăng nhập bằng Google</span>
                </a>
                <a href="https://www.facebook.com/v18.0/dialog/oauth?client_id=767353226134083&redirect_uri=http://localhost:8080/Web_war_exploded/login-facebook&scope=email,public_profile">
                    Login with Facebook
                </a>
            </div>
        </form>
    </div>
</div>
<c:import url="footerUser.jsp"></c:import>
<div class="overlay" id="overlay"></div>
<form action="quenMK" method="post" class="quenmk">
    <p>GỬI LẠI MẬT KHẨU</p>
    <div class="khung">
        <input type="email" name="emailMK" class="nhapemail" placeholder="Nhập email lấy lại mật khẩu" required>
        <div class="errorEmail" style="color: red; font-size: 14px;margin-top: 5px">${errorMail}</div>
    </div>
    <button type="submit" class="send">Chấp nhận</button>
</form>
<script>

    const passInput = document.getElementById('iPass');
    const bShow = document.querySelector(".show")
    bShow.addEventListener("click", function () {
        if (passInput.type === "password") {
            passInput.type = "text";
        } else {
            passInput.type = "password";
        }
    })

    const overlay = document.getElementById("overlay");
    const popup = document.querySelector(".quenmk");
    const quenmk = document.querySelector(".qmk");
    const send = document.querySelector(".send");

    quenmk.addEventListener('click', (e) => {
        e.preventDefault();
        overlay.style.display = "block";
        popup.style.display = "block";
    });
    overlay.addEventListener('click', () => {
        overlay.style.display = "none";
        popup.style.display = "none";
    });
    <c:if test="${openQMKPopup}">
    overlay.style.display = "block";
    popup.style.display = "block";
    </c:if>
    const errorEmail = document.querySelector(".error.Email");
    const successAlert = document.querySelector('.login-success-alert');
    if (successAlert) {
        setTimeout(() => {
            successAlert.style.transition = 'all 0.6s ease';
            successAlert.style.opacity = '0';
            successAlert.style.transform = 'scale(0.9)';
            setTimeout(() => successAlert.remove(), 600);
        }, 2500);
    }
    const form = document.querySelector(".login");
    const userInput = document.getElementById("iUser");
    const errorDiv = document.querySelector(".error");

    function checkPassword(password) {
        const regex = /^(?=.*[0-9])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
        return regex.test(password);
    }

    form.addEventListener("submit", function (e) {
        let hasError = false;
        errorDiv.innerText = "";

        if (userInput.value.trim() === "") {
            errorDiv.innerText = "Vui lòng nhập email";
            hasError = true;
        } else if (passInput.value.trim() === "") {
            errorDiv.innerText = "Vui lòng nhập mật khẩu";
            hasError = true;
        } else if (passInput.value.length < 8 && !checkPassword(passInput.value.trim())) {
            errorDiv.innerText = "Mật khẩu phải có ít nhất 8 ký tự, có số và kí tự đặc biệt";
            hasError = true;
        }
        if (hasError) {
            e.preventDefault();
        }
    });


</script>
</body>
</html>