<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý sản phẩm</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/search-suggest.css">
    <link rel="stylesheet" href="assets/css_admin/mProduct.css">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>
        <div class="product-container">
            <h2>Quản lý sản phẩm</h2>
            <form method="get" action="${pageContext.request.contextPath}/product-manage">
                <div class="function">
                    <button id="add" type="button">Thêm sản phẩm</button>
                    <div class="find">
                    <div class="search-wrapper admin-search">
                        <input type="text"
                               class="search-input"
                               name="q"
                               placeholder="Tìm kiếm sản phẩm"
                               autocomplete="off"
                               value="${param.q}"/>
                               <div class="suggest-box"></div>
                        </div>
                        <button class="buttonSearch" type="submit">Tìm kiếm</button>
                    </div>
                </div>

                <div class="title">
                    <h3>Danh sách sản phẩm</h3>
                    <div>

                        <select class="filter-sp" name="sortStock" onchange="this.form.submit()">
                            <option value="">Tất cả</option>
                            <option value="asc" ${param.stock == 'asc' ? 'selected' : ''}>
                                Số lượng tăng dần
                            </option>
                            <option value="desc" ${param.stock == 'desc' ? 'selected' : ''}>
                                Số lượng giảm dần
                            </option>
                        </select>

                        <!-- FILTER TYPE  -->
                        <select class="filter-sp" name="type" onchange="this.form.submit()">
                            <option value="">Tất cả</option>
                            <c:forEach var="t" items="${types}">
                                <option value="${t}" ${param.type == t ? 'selected' : ''}>
                                        ${t}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </form>

            <div class="table-wrapper">
                <table>
                    <thead>
                    <tr>
                        <th>Mã sách</th>
                        <th>Tên sách</th>
                        <th>Tác giả</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th>Loại sách</th>
                        <th>Độ tuổi</th>
                        <th>Hình ảnh</th>
                        <th>Chỉnh sửa</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${lsbook}">
                        <tr>
                            <td>${p.bookCode}</td>
                            <td>${p.title}</td>
                            <td>${p.author}</td>
                            <td style="white-space: nowrap;" ><p><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"
                                                  maxFractionDigits="0"/></p></td>
                            <td>${p.stock}</td>
                            <td>${p.type}</td>
                            <td>${p.age}+</td>
                            <td><img src="${p.coverImgUrl}" width="60"></td>
                            <td><i class="fa-solid fa-pen sua"
                                   data-id="${p.id}"
                                   data-code="${p.bookCode}"
                                   data-title="${p.title}"
                                   data-author="${p.authorId}"
                                   data-price="${p.price}"
                                   data-price-discounted="${p.priceDiscounted}"
                                   data-age="${p.age}"
                                   data-stock="${p.stock}"
                                   data-type="${p.type}"
                                   data-publisher="${p.publisher}"
                                   data-published="${p.publishedDate}"
                                   data-provider="${p.provider}"
                                   data-weight="${p.weight}"
                                   data-size="${p.bookSize}"
                                   data-page="${p.pagesNumber}"
                                   data-format="${p.format}"
                                   data-description="${p.description}">
                            </i>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
<div id="overlay"></div>
    <form id="bookForm" method="post" action="${pageContext.request.contextPath}/product-manage"
          enctype="multipart/form-data">
        <div class="form-grid">
            <div class="form-group">
                <label>Mã Sách</label>
                <input type="text" id="code" name="code" placeholder="Nhập mã sách" required>
            </div>

            <div class="form-group">
                <label>Tên sách</label>
                <input type="text" id="title" name="title" placeholder="Nhập tên sách">
            </div>

            <div class="form-group">
                <label>Tác giả</label>
                <select name="author_id" required>
                    <c:forEach var="a" items="${authors}">
                        <option value="${a.id}">${a.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Giá gốc</label>
                <input type="number" name="price" placeholder="VD: 50000" required>
            </div>
            <div class="form-group">
                <label>Giá khuyến mãi</label>
                <input type="number" name="price_discounted" placeholder="VD: 45000">
            </div>
            <div class="form-group">
                <label>Độ tuổi</label>
                <input type="number" name="age" placeholder="VD: 6" required>
            </div>
            <div class="form-group">
                <label>Nhà phát hành</label>
                <input type="text" name="provider" placeholder="VD: Kim Đồng">
            </div>

            <div class="form-group">
                <label>Số lượng</label>
                <input type="number" id="quantity" name="stock" placeholder="VD: 50" required>
            </div>
            <div class="form-group">
                <label>Ảnh bìa</label>
                <input type="file" id="img-main" name="img-main" accept="image/*" placeholder="link ảnh">
            </div>
            <div class="form-group">
                <label>Hình ảnh chi tiết</label>
                <input type="file" name="imgDetail" multiple>
            </div>
            <div class="form-group">
                <label>Loại sách</label>
                <input type="text" id="type" name="type" placeholder="Truyện tranh, sách ảnh...." required>
            </div>
            <div class="form-group">
                <label>Nhà xuất bản</label>
                <input type="text" id="publisher" name="publisher" placeholder="Tên nhà xuất bản" required>
            </div>
            <div class="form-group">
                <label>Trọng lượng sách</label>
                <input type="number" id="weight" name="weight" placeholder="VD: 10 gram">
            </div>
            <div class="form-group">
                <label>Kích thước</label>
                <input type="text" id="size" name="size" placeholder="VD: 17x14">
            </div>
            <div class="form-group">
                <label>Số Trang</label>
                <input type="number" id="page_number" name="pageNumber" placeholder="VD: 30">
            </div>
            <div class="form-group-inline">
                <div>
                    <label>Ngày xuất bản</label>
                    <input type="date" id="start_date" name="startDate">
                </div>
            </div>
            <div class="form-group">
                <label>Định dạng</label>
                <input type="text" name="format" placeholder="Bìa mềm / Bìa cứng" required>
            </div>
        </div>
        <div class="form-group">
            <label>Mô tả</label>
            <textarea name="description" id="description" cols="10" rows="4" placeholder="mô tả về sách"></textarea>
        </div>
        <input type="hidden" name="id" id="bookId">
        <button type="submit" class="btn-save">Thêm sản phẩm</button>
    </form>
<script>

    const overlay = document.getElementById("overlay");
    const add = document.getElementById("add")
    const popup = document.getElementById("bookForm");
    overlay.addEventListener('click', () => {
        overlay.style.display = "none";
        popup.style.display = "none";
    });


    document.querySelectorAll(".sua").forEach(btn => {
        btn.addEventListener("click", () => {
            overlay.style.display = "block";
            popup.style.display = "block";
            document.getElementById("img-main").required = false;
            document.getElementById("start_date").required = false;
            document.getElementById("bookId").value = btn.dataset.id;
            document.getElementById("code").value = btn.dataset.code;
            document.getElementById("title").value = btn.dataset.title;
            document.querySelector("select[name='author_id']").value = btn.dataset.author;
            document.querySelector("input[name='price']").value = btn.dataset.price;
            document.querySelector("input[name='price_discounted']").value = btn.dataset.priceDiscounted;
            document.querySelector("input[name='age']").value = btn.dataset.age;
            document.querySelector("input[name='stock']").value = btn.dataset.stock;
            document.getElementById("type").value = btn.dataset.type;
            document.getElementById("publisher").value = btn.dataset.publisher;
            document.querySelector("input[name='provider']").value = btn.dataset.provider;
            document.getElementById("weight").value = btn.dataset.weight;
            document.getElementById("size").value = btn.dataset.size;
            document.getElementById("page_number").value = btn.dataset.page;
            document.querySelector("input[name='format']").value = btn.dataset.format;
            document.getElementById("description").value = btn.dataset.description;
            document.getElementById("start_date").value =
                btn.dataset.published + "-01-01";
            document.querySelector(".btn-save").innerText = "Cập nhật sản phẩm";
        });
    });
    add.addEventListener('click', () => {
        overlay.style.display = "block";
        popup.style.display = "block";
        popup.reset();
        document.getElementById("bookId").value = "";
        document.querySelector(".btn-save").innerText = "Thêm sản phẩm";
        document.getElementById("img-main").required = true;
        document.getElementById("start_date").required = true;
    })
</script>
<script src="${pageContext.request.contextPath}/assets/js/search-suggest.js"></script>
</body>
</html>