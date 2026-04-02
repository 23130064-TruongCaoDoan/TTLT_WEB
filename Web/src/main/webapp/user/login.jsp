<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
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
                <div id="or">OR</div>
                <div class="login-google-and_facebook">
                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/Web_war_exploded/login-google&response_type=code&client_id=846603349467-fma7pe8c0b03i56hibab7psvktbnluj7.apps.googleusercontent.com&approval_prompt=force">
                        <img src="https://freelogopng.com/images/all_img/1657952440google-logo-png-transparent.png"
                             alt="google">
                    </a>
                    <a href="https://www.facebook.com/v18.0/dialog/oauth?client_id=${fbClientId}&redirect_uri=http://localhost:8080/Web_war_exploded/login-facebook&scope=email,public_profile">
                        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/2023_Facebook_icon.svg/960px-2023_Facebook_icon.svg.png"
                             alt="facebook">
                    </a>
                </div>
                <div class="signUp"><a href="<c:url value='dangki' />"><span>Đăng ký</span></a></div>
            </div>

        </form>
    </div>
</div>
<c:import url="footerUser.jsp"></c:import>
<div class="overlay" id="overlay"></div>
<div class="quenmk">
    <p>GỬI LẠI MẬT KHẨU</p>
    <div class="khungk">
        <input type="email" name="emailMK" class="nhapemail" placeholder="Nhập email lấy OTP cho tài khoản" required>
        <div class="errorEmail">${errorMail}</div>
        <button type="button" class="send" onclick="send_OTP()">Lấy OTP</button>
    </div>
    <div class="otp-box" style="display: none">
        <div class="khung">
            <input name="otp" class="nhapOTP" placeholder="Nhập mã OTP">
            <div class="errorVerify">${errorVerify}</div>
            <button type="button" class="send" id="verify">Xác thực</button>
        </div>
        <p id="time" style="color: #444444; font-size: 18px;margin: 15px"></p>
    </div>
</div>
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
        errorEmailDiv.innerText = "";
        document.querySelector(".otp-box").style.display = "none";
        document.querySelector(".khungk").style.display = "block";
        clearMessageErrorMail();
    });
    overlay.addEventListener('click', () => {
        overlay.style.display = "none";
        popup.style.display = "none";
    });
    <c:if test="${openQMKPopup}">
    overlay.style.display = "block";
    popup.style.display = "block";
    </c:if>
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


    let countdownTime = 120;
    let countdownInterval;

    function startCountdown() {

        const timeDisplay = document.getElementById("time");
        const verifyBtn = document.getElementById("verify");

        if (!timeDisplay || !verifyBtn) return;

        if (countdownInterval) {
            clearInterval(countdownInterval);
        }

        countdownTime = 120;
        verifyBtn.disabled = false;

        countdownInterval = setInterval(function () {
            let minutes = Math.floor(countdownTime / 60);
            let seconds = countdownTime % 60;

            seconds = seconds < 10 ? "0" + seconds : seconds;
            timeDisplay.innerText = "Mã OTP hết hạn sau: " + minutes + ":" + seconds;

            countdownTime--;

            if (countdownTime < 0) {
                clearInterval(countdownInterval);
                timeDisplay.innerText = "OTP đã hết hạn!";
                verifyBtn.disabled = true;
            }
        }, 1000);
    }

    const emailInput = document.querySelector("input[name='emailMK']");
    const errorEmailDiv = document.querySelector(".errorEmail");


    function clearMessageErrorMail() {
        errorEmailDiv.innerText = "";
        errorEmailDiv.classList.remove("error", "success");
    }

    function isValidEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    function send_OTP() {
        const email = emailInput.value.trim();
        errorEmailDiv.innerText = "";
        clearMessageErrorMail();
        if (email == "") {
            errorEmailDiv.innerText = "Vui lòng nhập email";
            errorEmailDiv.classList.remove("success");
            errorEmailDiv.classList.add("error");
            return;
        }
        if (!isValidEmail(email)) {
            errorEmailDiv.innerText = "Email Không đúng định dạng";
            errorEmailDiv.classList.remove("success");
            errorEmailDiv.classList.add("error");
            return;
        }

        errorEmailDiv.innerText ="Đang gửi";
        fetch("send_otp", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "emailMK=" + encodeURIComponent(email)
        })
            .then(response => response.json())
            .then(data => {
                if (data.successSend === "success") {
                    document.querySelector(".otp-box").style.display = "block";
                    document.querySelector(".khungk").style.display = "none";

                    startCountdown();

                } else {
                    errorEmailDiv.innerText = data.message;
                    errorEmailDiv.classList.remove("success");
                    errorEmailDiv.classList.add("error");
                }
            })
            .catch(error => {
                console.error("Lỗi:", error);
            });
    }




</script>
</body>
</html>