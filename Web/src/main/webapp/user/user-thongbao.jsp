<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
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
    <link rel="stylesheet" href="assets/css/thongbao.css">
</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="content">
        <div class="container">

            <div class="menuUser">
                <c:import url="/user/menuUser.jsp"></c:import>
            </div>
            <div class="profile-container " >
                <div class="nav-inform">
                    <p class="tab-inform active">THÔNG BÁO</p>

                    <button id="markAllRead" >
                        Đánh dấu tất cả đã đọc
                    </button>
                </div>
                <hr/>
                <div style="overflow: scroll;height: 47vh">
                    <c:forEach var="n" items="${notifications}">
                        <div class="inform-card noti-item ${!n.read ? 'noti-new' : 'noti-old'}"
                             data-id="${n.notiId}"
                             data-title="${n.title}"
                             data-content="${n.noti}"
                             data-time="${n.createdAt}">

                            <div class="head-inform-card">
                                <p class="title-inform">${n.title}</p>
                                <p>${n.createdAt}</p>
                            </div>

                            <p class="content-inform">
                                    ${n.noti}
                            </p>
                        </div>
                    </c:forEach>

                    <c:if test="${empty notifications}">
                        <p style="text-align:center; margin-top:20px;">
                            Không có thông báo
                        </p>
                    </c:if>
                </div>

            </div>
        </div>
    </div>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
<div class="overlay" id="notiOverlay" style="display:none"></div>

<div class="popup" id="notiPopup" style="display:none">
    <h3 id="popupTitle"></h3>
    <p id="popupTime" style="font-size:13px;color:#777"></p>
    <hr/>
    <p id="popupContent"></p>

    <div class="btn-group" style="margin-top:20px">
        <button class="confirm" id="closeNoti">Đóng</button>
    </div>
</div>
</body>
<script>
    document.addEventListener("DOMContentLoaded", () => {

        const overlay = document.getElementById("notiOverlay");
        const popup = document.getElementById("notiPopup");

        const titleEl = document.getElementById("popupTitle");
        const timeEl = document.getElementById("popupTime");
        const contentEl = document.getElementById("popupContent");

        document.querySelectorAll(".noti-item").forEach(item => {
            item.addEventListener("click", () => {

                const id = item.dataset.id

                titleEl.innerText = item.dataset.title;
                contentEl.innerText = item.dataset.content;
                timeEl.innerText = item.dataset.time;

                overlay.style.display = "block";
                popup.style.display = "block";

                item.classList.remove("noti-new");
                item.classList.add("noti-old");

                fetch("${pageContext.request.contextPath}/read-notification?id=" + id)
                .then(() => {

                    const badge = document.getElementById("noti-badge");

                    if (badge) {
                        let count = parseInt(badge.innerText);

                        if (count > 0) {
                            count--;
                            badge.innerText = count;

                            if (count === 0) {
                                badge.style.display = "none";
                            }
                        }
                    }

                })
                .catch(err => console.log(err));
            });
        });

        function closePopup() {
            overlay.style.display = "none";
            popup.style.display = "none";
        }

        document.getElementById("closeNoti").onclick = closePopup;
        overlay.onclick = closePopup;
    });



    const markAllBtn = document.getElementById("markAllRead");

    if (markAllBtn) {
        markAllBtn.addEventListener("click", () => {

            fetch("${pageContext.request.contextPath}/read-all-notifications")
                .then(() => {
                    document.querySelectorAll(".noti-item").forEach(item => {
                        item.classList.remove("noti-new");
                        item.classList.add("noti-old");
                    });

                    const badge = document.getElementById("noti-badge");
                    if (badge) {
                        badge.innerText = "0";
                        badge.style.display = "none";
                    }

                })
                .catch(err => console.log(err));
        });
    }
</script>

</html>