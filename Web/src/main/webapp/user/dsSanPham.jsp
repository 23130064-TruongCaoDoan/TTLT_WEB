<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Danh sách sản phẩm</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Cormorant+Garamond:ital,wght@0,300..700;1,300..700&family=Libre+Franklin:ital,wght@0,100..900;1,100..900&family=Merriweather+Sans:ital,wght@0,300..800;1,300..800&family=Playwrite+DE+SAS:wght@100..400&family=Sarabun:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dsSanPham.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>

</head>
<body>
<div class="page-wrapper">
    <c:import url="/user/headerUser.jsp"></c:import>
    <div class="banner" style="background-color: ${color}">
        <h1><c:if test="${not empty search}">
            ${search}
        </c:if>
            <c:if test="${empty search}">
                Sản Phẩm
            </c:if></h1>
        <c:if test="${not empty icon}">
        <img src="${icon}" alt="">
        </c:if>
    </div>
    <div class="content">
        <div class="filter">
            <div class="recipient">
                <div class="filter-title"><h2>Bộ lọc</h2></div>
                <hr>
                <div class="filter-header" onclick="toggleOptions()">Đối tượng</div>
                <div class="filter-options" id="options">
                    <button data-age="0">Trẻ sơ sinh</button>
                    <button data-age-from="1" data-age-to="3">Trẻ 1-3 tuổi</button>
                    <button data-age-from="4">Trẻ >4 tuổi</button>
                </div>
            </div>
            <div class="occasions">
                <hr>
                <div class="filter-header" onclick="toggleOptions2()">Theo thể loại</div>
                <div class="filter-options" id="options-2">
                    <button data-category="Truyện tranh">Truyện tranh</button>
                    <button data-category="Sách giáo dục">Sách giáo dục</button>
                    <button data-category="Sách ảnh">Sách ảnh</button>
                    <button data-category="Giáo dục">Giáo dục</button>
                    <button data-category="Sách tô màu">Sách tô màu</button>
                </div>
            </div>
            <button class="clear-filter">Xoá bộ lọc</button>
        </div>
        <c:if test="${empty bookList}"><span STYLE="text-align: center; font-size: 20px; color: gray; margin: auto">KHÔNG CÓ SẢN PHẨM</span></c:if>
        <div class="listProducts">
            <c:forEach var="book" items="${bookList}">
                <div class="card">
                    <a href="productDetail?id=${book.id}&type=${book.type}">
                        <img src="${book.coverImgUrl}" alt="${book.title}"/>
                        <p class="book-name">${book.title}</p>
                    </a>
                    <p class="rating">
                    </p>
                    <div class="price-cart">
                        <p class="price">
                            <c:if test="${book.priceDiscounted > 0}">
                                    <span><fmt:formatNumber value="${book.priceDiscounted}" type="number"
                                                            groupingUsed="true" maxFractionDigits="0"/> Đ</span>
                                <s><fmt:formatNumber value="${book.price}" type="number" groupingUsed="true"
                                                     maxFractionDigits="0"/> Đ</s>
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


            <c:if test="${totalPages > 1}">
                <div class="pagination">

                    <c:choose>
                        <c:when test="${mode == 'search'}">
                            <c:if test="${currentPage > 1}">
                                <a class="page-btn prev"
                                   href="search?page=${currentPage - 1}&bSearch=${search}">«</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a class="page-btn ${i == currentPage ? 'active' : ''}"
                                   href="search?page=${i}&bSearch=${search}">
                                        ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a class="page-btn next"
                                   href="search?page=${currentPage + 1}&bSearch=${search}">»</a>
                            </c:if>
                        </c:when>
                        <c:when test="${mode == 'filter'}">

                            <c:if test="${currentPage > 1}">
                                <a class="page-btn prev"
                                   href="dsSanPham/filter?page=${currentPage - 1}&${qs}">«</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a class="page-btn ${i == currentPage ? 'active' : ''}"
                                   href="dsSanPham/filter?page=${i}&${qs}">
                                        ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a class="page-btn next"
                                   href="dsSanPham/filter?page=${currentPage + 1}&${qs}">»</a>
                            </c:if>

                        </c:when>

                        <c:otherwise>
                            <c:if test="${currentPage > 1}">
                                <a class="page-btn prev"
                                   href="dsSanPham?page=${currentPage - 1}&type=${type}&idEvent=${idEvent}&title=${title}">«</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a class="page-btn ${i == currentPage ? 'active' : ''}"
                                   href="dsSanPham?page=${i}&type=${type}&idEvent=${idEvent}&title=${title}">
                                        ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a class="page-btn next"
                                   href="dsSanPham?page=${currentPage + 1}&type=${type}&idEvent=${idEvent}&title=${title}">»</a>
                            </c:if>
                        </c:otherwise>

                    </c:choose>
                </div>

            </c:if>
        </div>

    </div>
    <c:import url="/user/footerUser.jsp"></c:import>
</div>
</body>

<script>
    function addToCart(bookId, quantity) {
        fetch("addItemShopping?bookId=" + bookId + "&quantity=" + quantity)
            .then(res => res.json())
            .then(data => {
                document.getElementById("totalItem").innerText = data.total;
                show("Đã thêm vào giỏ hàng");
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
<script>
    const BASE_URL = "${pageContext.request.contextPath}/filter";
    function toggleOptions() {
        const el = document.getElementById("options");
        if (el) {
            el.style.display = el.style.display === "none" ? "block" : "none";
        }
    }

    function toggleOptions2() {
        const el = document.getElementById("options-2");
        if (el) {
            el.style.display = el.style.display === "none" ? "block" : "none";
        }
    }
    const params = new URLSearchParams(window.location.search);


    document.querySelectorAll("[data-age-from]").forEach(btn => {
        btn.onclick = () => {
            params.set("ageFrom", btn.dataset.ageFrom);
            if (btn.dataset.ageTo) {
                params.set("ageTo", btn.dataset.ageTo);
            } else {
                params.set("ageTo", 100);
            }
            params.delete("page");
            reload();
        }
    });



    document.querySelectorAll("[data-category]").forEach(btn => {
        btn.onclick = () => {
            params.set("category", btn.dataset.category);
            params.delete("page");
            reload();
        }
    });

    console.log("FILTER JS LOADED");
    document.querySelector(".clear-filter").onclick = () => {
        window.location.href = "${pageContext.request.contextPath}/dsSanPham";
    };

    function reload(){
        window.location.href = BASE_URL + "?" + params.toString();
    }
</script>

</html>
