<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Home</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="stylesheet" href="assets/css/header.css"/>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Cormorant+Garamond:ital,wght@0,300..700;1,300..700&family=Libre+Franklin:ital,wght@0,100..900;1,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Playwrite+DE+SAS:wght@100..400&family=Sarabun:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" href="assets/css/footer.css"/>
    <link rel="stylesheet" href="assets/css/home.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Bungee&family=Lobster&display=swap" rel="stylesheet">
    <script src="assets/js/user/home.js"></script>
</head>
<body>
<div class="page-wrapper">
    <c:import url="headerUser.jsp"> </c:import>
    <div class="content">
        <div class="container">
            <div class="event-carousel">
                <button class="prev">&#10094;</button>
                <div class="slides">
                    <c:forEach var="event" items="${events}" varStatus="st">
                        <div class="slide ${st.first ? 'active' : ''}">

                            <c:choose>

                                <c:when test="${eventHasBooks[event.id]}">
                                    <a href="${pageContext.request.contextPath}/dsSanPham?type=4&title=${event.title}&idEvent=${event.id}">
                                        <img src="${event.imgUrl}" alt="event-img">
                                    </a>
                                </c:when>


                                <c:otherwise>
                                    <div class="event-no-product">
                                        <img src="${event.imgUrl}" alt="event-img">
                                    </div>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </c:forEach>


                </div>
                <button class="next">&#10095;</button>
            </div>
            <div class="sachh sale">
                <div class="title t"><span>Giảm giá</span><img src="assets/img/icon/sale.png" alt=""></div>
                <div class="dsbooks">
                    <c:forEach var="book" items="${booksListSale}" begin="0" end="4">
                        <c:url var="detailUrl" value="/productDetail">
                            <c:param name="id" value="${book.id}"/>
                            <c:param name="type" value="${book.type}"/>
                        </c:url>
                        <div class="card">
                            <a href="${detailUrl}">
                                <img src="${book.coverImgUrl}" alt="Ảnh sản phẩm"/>
                                <p class="book-name">${book.title}</p>
                            </a>
                            <p class="rating">
                            </p>
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
                <div class="bt btsale">
                    <button onclick="window.location.href='dsSanPham?type=1'" class="xemThem">Xem Thêm<i
                            class="fa-solid fa-arrow-right"></i></button>
                </div>
            </div>
            <div class="sachh moi">
                <div class="title"><span>Góc sách mới</span><img src="assets/img/icon/iconNew.png"></div>
                <div class="dsbooks">
                    <c:forEach var="book" items="${booksListNew}" begin="0" end="4">
                        <c:url var="detailUrl" value="/productDetail">
                            <c:param name="id" value="${book.id}"/>
                            <c:param name="type" value="${book.type}"/>
                        </c:url>
                        <div class="card">
                            <a href="${detailUrl}">
                                <a href="productDetail?id=${book.id}&type=${book.type}">
                                    <img src="${book.coverImgUrl}" alt="${book.title}"/>
                                    <p class="book-name">${book.title}</p>
                                </a>
                                <p class="rating">
                                </p>
                                <div class="price-cart">
                                    <p class="price">
                                        <c:if test="${book.priceDiscounted > 0}">
                                            <s><fmt:formatNumber value="${book.price}" type="number" groupingUsed="true"
                                                                 maxFractionDigits="0"/> Đ</s>
                                            <span><fmt:formatNumber value="${book.priceDiscounted}" type="number"
                                                                    groupingUsed="true" maxFractionDigits="0"/> Đ</span>
                                        </c:if>
                                        <c:if test="${book.priceDiscounted == 0}">
                                            <span><fmt:formatNumber value="${book.price}" type="number"
                                                                    groupingUsed="true" maxFractionDigits="0"/> Đ</span>
                                        </c:if>
                                    </p>
                                    <i class="fa-solid fa-cart-plus" onclick="addToCart(${book.id},1)"></i>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>

                <div class="bt btmoi">
                    <button onclick="window.location.href='dsSanPham?type=2'" class="xemThem">Xem Thêm<i
                            class="fa-solid fa-arrow-right"></i></button>
                </div>
            </div>
            <div class="sachh yeuThich">
                <div class="title"><span>Sách được yêu thích nhất</span><i class="fa-solid fa-heart"></i></div>
                <div class="dsbooks">
                    <c:forEach var="book" items="${booksListFavourite}" begin="0" end="4">
                        <c:url var="detailUrl" value="/productDetail">
                            <c:param name="id" value="${book.id}"/>
                            <c:param name="type" value="${book.type}"/>
                        </c:url>
                        <div class="card">
                            <a href="${detailUrl}">
                                <a href="productDetail?id=${book.id}&type=${book.type}">
                                    <img src="${book.coverImgUrl}" alt="${book.title}"/>
                                    <p class="book-name">${book.title}</p>
                                </a>
                                <p class="rating">
                                </p>
                                <div class="price-cart">
                                    <p class="price">
                                        <c:if test="${book.priceDiscounted > 0}">
                                            <s><fmt:formatNumber value="${book.price}" type="number" groupingUsed="true"
                                                                 maxFractionDigits="0"/> Đ</s>
                                            <span><fmt:formatNumber value="${book.priceDiscounted}" type="number"
                                                                    groupingUsed="true" maxFractionDigits="0"/> Đ</span>
                                        </c:if>
                                        <c:if test="${book.priceDiscounted == 0}">
                                            <span><fmt:formatNumber value="${book.price}" type="number"
                                                                    groupingUsed="true" maxFractionDigits="0"/> Đ</span>
                                        </c:if>
                                    </p>
                                    <i class="fa-solid fa-cart-plus" onclick="addToCart(${book.id},1)"></i>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
                <div class="bt btmoi">
                    <button onclick="window.location.href='dsSanPham?type=3'" class="xemThem">Xem Thêm<i
                            class="fa-solid fa-arrow-right"></i></button>
                </div>
            </div>
            <div class="recommend">
                <div class="slogan"><h3>Chúng tôi coi trọng chất lượng và sự độc đáo</h3></div>
                <div class="valua-container">
                    <div class="valua-card">
                        <figure>
                            <img src="assets/img/slogan/card1.png" alt="">
                            <figcaption>Chất lượng cao cấp</figcaption>
                        </figure>
                    </div>
                    <div class="valua-card">
                        <figure>
                            <img src="assets/img/slogan/card2.png" alt="">
                            <figcaption>Trải nghiệm nhập vai</figcaption>
                        </figure>
                    </div>
                    <div class="valua-card">
                        <figure>
                            <img src="assets/img/slogan/card3.png" alt="">
                            <figcaption>Sự lựa trọn hàng đầu</figcaption>
                        </figure>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <c:import url="footerUser.jsp"> </c:import>
</div>
</body>
</html>