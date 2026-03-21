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

            <div class="filter-title"><h2>Bộ lọc</h2></div>
            <hr>
            <div class="filter-group">
                <div class="filter-header" onclick="toggle(this,'categoryBox')">
                    Thể loại
                    <i class="fa-solid fa-chevron-down arrow"></i>
                </div>
                <div class="filter-options" id="categoryBox">
                    <c:forEach var="c" items="${categories}">
                        <label><input type="checkbox" value="${c}"> ${c}</label>
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
                        <label><input type="checkbox" value="${p}"> ${p}</label>
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
                        <label><input type="checkbox" value="${a}"> ${a}</label>
                    </c:forEach>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header" onclick="toggle(this,'ageBox')">
                    Độ tuổi
                    <i class="fa-solid fa-chevron-down arrow"></i>
                </div>
                <div class="filter-options" id="ageBox">
                    <label><input type="radio" name="age" value="0-1"> 0-1 tuổi</label>
                    <label><input type="radio" name="age" value="1-3"> 1-3 tuổi</label>
                    <label><input type="radio" name="age" value="4-10"> 4-10 tuổi</label>
                    <label><input type="radio" name="age" value="10+"> >10 tuổi</label>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header">Khoảng giá</div>
                <div class="price-slider">
                    <input type="range" id="priceRange" min="0" max="500000" step="10000">
                    <div class="price-value">
                        Tối đa: <span id="priceValue">0</span> đ
                    </div>
                </div>
            </div>

            <div class="filter-group">
                <div class="filter-header">Năm xuất bản</div>
                <select id="yearSelect">
                    <option value="">-- Chọn năm --</option>
                    <c:forEach var="y" items="${years}">
                        <option value="${y}">${y}</option>
                    </c:forEach>
                </select>
            </div>

            <button id="applyFilter" class="apply-filter">Lọc</button>
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

        const BASE_URL = "${pageContext.request.contextPath}/dsSanPham";
        const params = new URLSearchParams(window.location.search);


        document.querySelectorAll("#categoryBox input").forEach(cb => {
            cb.onchange = () => {
                const selected = Array.from(
                    document.querySelectorAll("#categoryBox input:checked")
                ).map(i => i.value);

                if (selected.length > 0) {
                    params.set("category", selected.join(","));
                } else {
                    params.delete("category");
                }
            };
        });

        document.querySelectorAll("#publisherBox input").forEach(cb => {
            cb.onchange = () => {
                const selected = Array.from(
                    document.querySelectorAll("#publisherBox input:checked")
                ).map(i => i.value);

                if (selected.length > 0) {
                    params.set("publisher", selected.join(","));
                } else {
                    params.delete("publisher");
                }
            };
        });

        document.querySelectorAll("#authorBox input").forEach(cb => {
            cb.onchange = () => {
                const selected = Array.from(
                    document.querySelectorAll("#authorBox input:checked")
                ).map(i => i.value);

                if (selected.length > 0) {
                    params.set("author", selected.join(","));
                } else {
                    params.delete("author");
                }
            };
        });

        document.querySelectorAll("input[name='age']").forEach(r => {
            r.onchange = () => {
                params.set("age", r.value);
            };
        });

        const priceRange = document.getElementById("priceRange");
        const priceValue = document.getElementById("priceValue");

        if (priceRange && priceValue) {
            priceValue.innerText = Number(priceRange.value).toLocaleString();

            priceRange.oninput = function () {
                priceValue.innerText = Number(this.value).toLocaleString();
            };

            priceRange.onchange = function () {
                params.set("maxPrice", this.value);
            };
        }

        const yearSelect = document.getElementById("yearSelect");
        if (yearSelect) {
            yearSelect.onchange = function () {
                if (this.value) {
                    params.set("year", this.value);
                } else {
                    params.delete("year");
                }
            };
        }

        document.getElementById("applyFilter").onclick = () => {
            params.delete("page");
            window.location.href = BASE_URL + "?" + params.toString();
        };

        document.querySelector(".clear-filter").onclick = () => {
            window.location.href = BASE_URL;
        };

    });
</script>

</html>
