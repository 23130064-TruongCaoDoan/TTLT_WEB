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
    <title>Sản Phẩm Yêu Thích</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Cormorant+Garamond:ital,wght@0,300..700;1,300..700&family=Libre+Franklin:ital,wght@0,100..900;1,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Playwrite+DE+SAS:wght@100..400&family=Sarabun:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="assets/css/home.css">
    <link rel="stylesheet" href="assets/css/errolpage.css">
    <link rel="stylesheet" href="assets/css/user.css">
    <link rel="stylesheet" href="assets/css/address.css">
    <link rel="stylesheet" href="assets/css/spYeuThich.css">
</head>

<body>
<div class="page-wrapper">
<c:import url="/user/headerUser.jsp"></c:import>
<div class="content">
    <div class="container">
        <div class="menuUser">
            <c:import url="/user/menuUser.jsp"></c:import>
        </div>
        <div class="favorite-products">
            <h2>Sản phẩm yêu thích của tôi</h2>
            <div class="listProducts">
                <c:forEach var="book" items="${favoriteBookList}">
                    <c:url var="detailUrl" value="/productDetail">
                        <c:param name="id" value="${book.id}"/>
                        <c:param name="type" value="${book.type}"/>
                    </c:url>
                    <div class="card">
                        <span class="remove-btn" onclick="removeFavourite(${book.id}, this)">
                            <i class="fa-solid fa-xmark" style="color: #ed1212;"></i>
                        </span>
                        <a href="${detailUrl}">
                            <img src="${book.coverImgUrl}" alt="Ảnh sản phẩm"/>
                            <p class="book-name">${book.title}</p>
                        </a>
                        <p class="rating"></p>
                        <div class="price-cart">
                            <p class="price">
                                <c:if test="${book.priceDiscounted > 0}">
                                    <s><fmt:formatNumber value="${book.price}" type="number" groupingUsed="true"
                                                         maxFractionDigits="0"/> Đ</s>
                                    <span><fmt:formatNumber value="${book.priceDiscounted}" type="number"
                                                            groupingUsed="true" maxFractionDigits="0"/> Đ</span>
                                </c:if>
                                <c:if test="${book.priceDiscounted == 0}">
                                        <span><fmt:formatNumber value="${book.price}" type="number" groupingUsed="true"
                                                                maxFractionDigits="0"/> Đ</span>
                                </c:if>
                            </p>
                            <i class="fa-solid fa-cart-plus" onclick="addToCart(${book.id},1)"></i>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</div>
<c:import url="footerUser.jsp"> </c:import>
<script>
    const contextPath = "${pageContext.request.contextPath}";

    function removeFavourite(bookId, btn) {
        fetch(contextPath + "/deleteFavouriteBook", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "id=" + bookId
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    btn.closest(".card").remove();
                } else {
                    alert(data.message);
                }
            })
            .catch(err => console.error(err));
    }


    function addToCart(bookId, quantity) {
        fetch("addItemShopping?bookId=" + bookId + "&quantity=" + quantity)
            .then(res => res.json())
            .then(data => {
                document.getElementById("totalItem").innerText = data.total;
                if (data.success) {
                    show("Đã thêm vào giỏ hàng");
                } else {
                    show("Không thể thêm do quá số lượng")
                }
            })
            .catch(err => console.log(err));
    }

    function show(message) {
        const toast = document.getElementById("toast");
        toast.innerText = message;
        toast.classList.add("show");
        setTimeout(() => {
            toast.classList.remove("show");
        }, 2000);
    }
</script>
</body>
</html>