<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        <h1>
            <c:choose>

                <c:when test="${not empty search}">
                    Kết quả tìm kiếm: ${search}
                </c:when>

                <c:when test="${not empty ssearch}">
                    ${ssearch}
                </c:when>

                <c:otherwise>
                    Sản Phẩm
                </c:otherwise>

            </c:choose>
        </h1>
        <c:if test="${not empty icon}">
            <img src="${icon}" alt="">
        </c:if>
    </div>
    <c:if test="${not empty param.bSearch
          or not empty param.year
          or not empty param.maxPrice
          or not empty param.age
          or not empty paramValues.category
          or not empty paramValues.author
          or not empty paramValues.publisher}">

        <div class="active-filters">
            <b>Đã lọc:</b>

            <c:if test="${not empty param.year}">
                <span>Năm ${param.year}</span>
            </c:if>

            <c:if test="${not empty param.maxPrice}">
                <span>Giá ≤ <fmt:formatNumber value="${param.maxPrice}" type="number" groupingUsed="true"/> đ</span>
            </c:if>

            <c:if test="${not empty param.age}">
                <span>Độ tuổi ${param.age}</span>
            </c:if>

            <c:forEach var="c" items="${paramValues.category}">
                <span>${c}</span>
            </c:forEach>

            <c:forEach var="a" items="${paramValues.author}">
                <span>${a}</span>
            </c:forEach>

            <c:forEach var="p" items="${paramValues.publisher}">
                <span>${p}</span>
            </c:forEach>

        </div>
    </c:if>
    <div class="content">
        <div class="filter">

            <div class="filter-title"><h2>Bộ lọc</h2></div>
            <hr>
            <form id="filterForm" action="filter" method="get">
            <div class="filter-group">
                <div class="filter-header" onclick="toggle(this,'categoryBox')">
                    Thể loại
                    <i class="fa-solid fa-chevron-down arrow"></i>
                </div>
                <div class="filter-options" id="categoryBox">
                    <c:forEach var="c" items="${categories}">
                        <label><input type="checkbox" name="category" value="${c}"> ${c}</label>
                    </c:forEach>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header" onclick="toggle(this,'publisherBox')">
                    Nhà xuất bản
                    <i class="fa-solid fa-chevron-down arrow"></i>
                </div>
                <div class="filter-options" id="publisherBox">
                    <c:forEach var="p" items="${publishers}">
                        <label>
                            <input type="checkbox" name="publisher" value="${p}"
                            <c:if test="${fn:contains(fn:join(paramValues.publisher, ','), p)}">
                                   checked
                            </c:if>>
                                ${p}
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header" onclick="toggle(this,'authorBox')">
                    Tác giả
                    <i class="fa-solid fa-chevron-down arrow"></i>
                </div>
                <div class="filter-options" id="authorBox">
                    <c:forEach var="a" items="${authors}">
                        <label>
                            <input type="checkbox" name="author" value="${a}"
                            <c:if test="${fn:contains(fn:join(paramValues.author, ','), a)}">
                                   checked
                            </c:if>>
                                ${a}
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header" onclick="toggle(this,'ageBox')">
                    Độ tuổi
                    <i class="fa-solid fa-chevron-down arrow"></i>
                </div>
                <div class="filter-options" id="ageBox">
                    <label>
                        <input type="radio" name="age" value="0-1"
                               <c:if test="${param.age == '0-1'}">checked</c:if>>
                        0-1 tuổi
                    </label>

                    <label>
                        <input type="radio" name="age" value="1-3"
                               <c:if test="${param.age == '1-3'}">checked</c:if>>
                        1-3 tuổi
                    </label>

                    <label>
                        <input type="radio" name="age" value="4-10"
                               <c:if test="${param.age == '4-10'}">checked</c:if>>
                        4-10 tuổi
                    </label>

                    <label>
                        <input type="radio" name="age" value="10-100"
                               <c:if test="${param.age == '10-100'}">checked</c:if>>
                        >10 tuổi
                    </label>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header">Khoảng giá</div>
                <div class="price-slider">
                    <input type="range"
                           id="priceRange"
                           name="maxPrice"
                           value="${param.maxPrice != null ? param.maxPrice : 200000}"
                           min="0" max="500000" step="10000">
                    <div class="price-value">
                        Tối đa: <span id="priceValue">0</span> đ
                    </div>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header">Năm xuất bản</div>
                <select id="yearSelect" name="year">
                    <option value="">-- Chọn năm --</option>
                    <c:forEach var="y" items="${years}">
                        <option value="${y}"
                                <c:if test="${param.year == y}">selected</c:if>>
                                ${y}
                        </option>
                    </c:forEach>
                </select>
            </div>

                <input type="hidden" name="type" value="${type}" />
                <input type="hidden" name="idEvent" value="${idEvent}" />
                <input type="hidden" name="color" value="${color}" />
                <input type="hidden" name="icon" value="${icon}" />
                <input type="hidden" name="bSearch" value="${bSearch}" />
                <button type="submit" id="applyFilter" class="apply-filter">Lọc</button>
                <button type="button" class="clear-filter" onclick="clearFilter()">Xoá bộ lọc</button>
            </form>
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
                                   href="filter?page=${currentPage - 1}&${qs}">«</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a class="page-btn ${i == currentPage ? 'active' : ''}"
                                   href="filter?page=${i}&${qs}">
                                        ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a class="page-btn next"
                                   href="filter?page=${currentPage + 1}&${qs}">»</a>
                            </c:if>

                        </c:when>

                        <c:otherwise>
                            <c:if test="${currentPage > 1}">
                                <a class="page-btn prev"
                                   href="dsSanPham?page=${currentPage - 1}&type=${type}&idEvent=${idEvent}&ssearch=${ssearch}">«</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a class="page-btn ${i == currentPage ? 'active' : ''}"
                                   href="dsSanPham?page=${i}&type=${type}&idEvent=${idEvent}&ssearch=${ssearch}">
                                        ${i}
                                </a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a class="page-btn next"
                                   href="dsSanPham?page=${currentPage + 1}&type=${type}&idEvent=${idEvent}&ssearch=${ssearch}">»</a>
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

    function toggle(header, id) {
        const el = document.getElementById(id);
        const arrow = header.querySelector(".arrow");

        if (el.style.display === "flex") {
            el.style.display = "none";
            arrow.classList.remove("rotate");
        } else {
            el.style.display = "flex";
            arrow.classList.add("rotate");
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        const priceRange = document.getElementById("priceRange");
        const priceValue = document.getElementById("priceValue");

        if (priceRange && priceValue) {
            priceValue.innerText = Number(priceRange.value).toLocaleString();

            priceRange.oninput = function () {
                priceValue.innerText = Number(this.value).toLocaleString();
            };
        }
    });
    function clearFilter() {
        const mode = "${mode}";

        if (mode === "search") {
            window.location.href = "<c:url value='search'><c:param name='bSearch' value='${search}'/></c:url>"
        } else {
            window.location.href = "<c:url value='dsSanPham'><c:param name='type' value='${type}'/><c:param name='idEvent' value='${idEvent}'/><c:param name='ssearch' value='${ssearch}'/></c:url>"
        }
    }


</script>

</html>
