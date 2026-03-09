<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Address</title>
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/adrress.css">
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
                <div class="address-header">
                    <h2>Sổ địa chỉ</h2>
                    <a href="addAddress" class="add-address"><i class="fa-solid fa-plus"></i> Thêm địa chỉ mới</a>
                </div>

                <div class="address-list">
                    <c:if test="${empty listAddress}">
                        <div class="emtyCard">
                            <span>Bạn chưa có địa chỉ nào</span>
                        </div>
                    </c:if>
                    <c:forEach var="addr" items="${listAddress}">
                        <div class="address-card">
                            <div class="address-info">
                                <div class="address-title">
                                    <strong>${addr.name}</strong>
                                    <span class="divider">|</span>
                                    <span class="phone">${addr.phone}</span>
                                    <c:if test="${addr.isDefault}">
                                        <span class="default-tag blue">Địa chỉ thanh toán mặc định</span>
                                    </c:if>
                                </div>
                                <p>
                                        ${addr.specificAddress}<br>
                                        ${addr.ward}, ${addr.city}
                                </p>
                            </div>
                            <div class="address-actions">
                                <a href="editAddress?id=${addr.id}"><i class="fa-solid fa-pen"></i></a>
                                <span class="divider">|</span>
                                <button type="button" class="delete-btn" data-id="${addr.id}">
                                    <i class="fa-solid fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="emtyCard"><span> Bạn chưa có địa chỉ nào</span></div>
            </div>
        </div>
    </div>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
<script>
    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            const id = this.dataset.id;
            const card = this.closest(".address-card");

            if (!confirm("Bạn có chắc muốn xóa địa chỉ này không?")) return;

            fetch("deleteAddress", {
                method: "POST",
                headers: {"Content-Type": "application/x-www-form-urlencoded"},
                body: "id=" + encodeURIComponent(id)
            })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        card.remove();
                        checkEmpty();
                        alert("Xóa địa chỉ thành công!");
                    } else {
                        alert("Lỗi: " + data.message);
                    }
                })
                .catch(err => {
                    console.error(err);
                    alert("Có lỗi xảy ra khi xóa!");
                });
        });
    });

    function checkEmpty() {
        const emtyCard = document.querySelector(".emtyCard");
        const cards = document.querySelectorAll('.address-card');

        if (cards.length === 0) {
            emtyCard.style.display = "block";
        } else {
            emtyCard.style.display = "none";
        }
    }

    checkEmpty();

</script>
</body>
</html>