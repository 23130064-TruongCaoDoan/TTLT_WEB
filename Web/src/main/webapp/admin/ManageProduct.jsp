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
    <link rel="stylesheet" href="assets/css_admin/ThongKe.css">
    <link rel="stylesheet" href="assets/css_admin/mProduct.css?v=2">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>
        <div class="product-container">
            <h2>Quản lý sản phẩm</h2>

            <div class="thongke-container" style="width: 100%; box-shadow: none; padding: 0; margin-bottom: 20px;">
                <div class="cards">
                    <div class="card">
                        <i class="fa-solid fa-cart-shopping"></i>
                            <h3>Số lượng sản phẩm đã bán</h3>
                                <p><fmt:formatNumber value="${totalSoldProducts}" type="number" groupingUsed="true" maxFractionDigits="0"/></p>
                    </div>
                    <div class="card">
                        <i class="fa-solid fa-warehouse"></i>
                            <h3>Tổng tồn kho</h3>
                                <p><fmt:formatNumber value="${totalStock}" type="number" groupingUsed="true" maxFractionDigits="0"/></p>
                    </div>
                    <div class="card out-of-stock-card" title="Bấm để xem danh sách">
                        <i class="fa-solid fa-triangle-exclamation"></i>
                            <h3>Sản phẩm đã hết</h3>
                                <p>${outOfStockCount} sản phẩm</p>
                    </div>
                    <div class="card unsold-card" title="Bấm để xem danh sách" style="cursor: pointer;">
                        <i class="fa-solid fa-box-archive" style="color: #f39c12;"></i>
                            <h3>Sản phẩm không bán được</h3>
                                <p>${unsoldBooksCount} sản phẩm</p>
                    </div>
                </div>
            </div>
            <form class="uploadExcel" action="${pageContext.request.contextPath}/UploadFileExcel" method="post" enctype="multipart/form-data">
                <label for="fileBooks" class="btn-import">Chọn file excel để import</label>
                <input type="file" id="fileBooks" name="fileBooks" accept=".xlsx,.xls" hidden>
            </form>
            <form method="get" action="${pageContext.request.contextPath}/product-manage">
                <div class="function">
                    <c:if test="${canImport}">
                        <button id="add" type="button">Thêm sản phẩm</button>
                    </c:if>
                    <a href="downloadTemplateFileExcel" class="btn-downloadFile">
                        Tải mẫu nhập sách excel
                    </a>
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
                            <option value="asc" ${param.sortStock == 'asc' ? 'selected' : ''}>
                                Số lượng tăng dần
                            </option>
                            <option value="desc" ${param.sortStock == 'desc' ? 'selected' : ''}>
                                Số lượng giảm dần
                            </option>
                        </select>

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
                        <th>Tỷ lệ bán</th>
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
                                                  maxFractionDigits="0"/> đ</p></td>
                            <td>${p.stock}</td>
                            <td>${p.salesPercentage}%</td>
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

            <c:if test="${totalPages > 1}">
                <div class="pagination-container">
                    <ul class="pagination">
                        <li class="${currentPage == 1 ? 'disabled' : ''}">
                            <a href="?page=${currentPage - 1}&q=${param.q}&sortStock=${param.sortStock}&type=${param.type}">
                                <i class="fas fa-angle-left"></i>
                            </a>
                        </li>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="${currentPage == i ? 'active' : ''}">
                                <a href="?page=${i}&q=${param.q}&sortStock=${param.sortStock}&type=${param.type}">${i}</a>
                            </li>
                        </c:forEach>

                        <li class="${currentPage == totalPages ? 'disabled' : ''}">
                            <a href="?page=${currentPage + 1}&q=${param.q}&sortStock=${param.sortStock}&type=${param.type}">
                                <i class="fas fa-angle-right"></i>
                            </a>
                        </li>
                    </ul>
                </div>
            </c:if>

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

            <div class="form-group hiddenWhenCodeExist">
                <label>Tên sách</label>
                <input type="text" id="title" name="title" placeholder="Nhập tên sách">
            </div>

            <div class="form-group hiddenWhenCodeExist" >
                <label>Tác giả</label>
                <select name="author_id" required>
                    <c:forEach var="a" items="${authors}">
                        <option value="${a.id}">${a.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group hiddenWhenFix">
                <label>Giá nhập</label>
                <input type="number" name="price_import" placeholder="VD: 40000" required>
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Giá bán</label>
                <input type="number" name="price" placeholder="VD: 50000" required>
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Giá khuyến mãi</label>
                <input type="number" name="price_discounted" placeholder="VD: 45000">
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Độ tuổi</label>
                <input type="number" name="age" placeholder="VD: 6" required>
            </div>
            <div class="form-group">
                <label>Nhà cung cấp</label>
                <input type="text" name="provider" placeholder="VD: Tên nhà cung cấp">
            </div>

            <div class="form-group ">
                <label>Số lượng</label>
                <input type="number" id="quantity" name="stock" placeholder="VD: 50" required>
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Ảnh bìa</label>
                <input type="file" id="img-main" name="img-main" accept="image/*" placeholder="link ảnh">
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Hình ảnh chi tiết</label>
                <input type="file" name="imgDetail" multiple>
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Loại sách</label>
                <input type="text" id="type" name="type" placeholder="Truyện tranh, sách ảnh...." required>
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Nhà xuất bản</label>
                <input type="text" id="publisher" name="publisher" placeholder="Tên nhà xuất bản" required>
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Trọng lượng sách</label>
                <input type="number" id="weight" name="weight" placeholder="VD: 10 gram">
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Kích thước</label>
                <input type="text" id="size" name="size" placeholder="VD: 17x14">
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Số Trang</label>
                <input type="number" id="page_number" name="pageNumber" placeholder="VD: 30">
            </div>
            <div class="form-group-inline hiddenWhenCodeExist" >
                <div>
                    <label>Ngày xuất bản</label>
                    <input type="date" id="start_date" name="startDate" required>
                </div>
            </div>
            <div class="form-group hiddenWhenCodeExist">
                <label>Định dạng</label>
                <input type="text" name="format" placeholder="Bìa mềm / Bìa cứng" required>
            </div>
        </div>
        <div class="form-group hiddenWhenCodeExist">
            <label>Mô tả</label>
            <textarea name="description" id="description" cols="10" rows="4" placeholder="mô tả về sách"></textarea>
        </div>
        <input type="hidden" name="id" id="bookId">
        <c:if test="${canImport}">
            <button type="submit" class="btn-save">Thêm sản phẩm</button>
        </c:if>
    </form>

    <div id="out-of-stock-panel">
            <div id="out-of-stock-container">
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
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${outOfStockBooks}" var="b">
                            <tr>
                                <td>${b.bookCode}</td>
                                <td>${b.title}</td>
                                <td>—</td>
                                <td><fmt:formatNumber value="${b.price}" type="number"
                                                      groupingUsed="true" maxFractionDigits="0"/></td>
                                <td style="color: red; font-weight: bold;">0</td>
                                <td>${b.type}</td>
                                <td>${b.age}+</td>
                                <td><img src="${b.coverImgUrl}" width="60"></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
    </div>

    <div id="unsold-panel" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); justify-content: center; align-items: center; z-index: 9999;">
        <div id="unsold-container" style="background: #fff; padding: 25px; height: 80%; border-radius: 12px; width: 80%; overflow-y: auto;">
            <h2 style="color: #0d3164; text-align: center; margin-bottom: 20px;">Danh sách sản phẩm không bán được</h2>
                <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã sách</th>
                            <th>Tên sách</th>
                            <th>Giá</th>
                            <th>Tồn kho</th>
                            <th>Loại sách</th>
                            <th>Độ tuổi</th>
                            <th>Hình ảnh</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${unsoldBooks}" var="b">
                            <tr>
                                <td>${b.bookCode}</td>
                                <td>${b.title}</td>
                                <td><fmt:formatNumber value="${b.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ</td>
                                <td>${b.stock}</td> <td>${b.type}</td>
                                <td>${b.age}+</td>
                                <td><img src="${b.coverImgUrl}" width="60"></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
        </div>
    </div>
<div id="toast-container"></div>

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
            document.querySelector(".hiddenWhenFix").style.display = 'none';
            document.querySelector("input[name='price_import']").required = false;

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
        document.querySelector(".hiddenWhenFix").style.display = 'block';
        document.querySelector("input[name='price_import']").required = true;
    });

    document.addEventListener("click", function (e) {
            if (e.target.closest(".out-of-stock-card")) {
                document.getElementById("out-of-stock-panel").style.display = "flex";
            }
            if (e.target.id === "out-of-stock-panel") {
                e.target.style.display = "none";
            }
            if (e.target.closest(".unsold-card")) {
                document.getElementById("unsold-panel").style.display = "flex";
            }
            if (e.target.id === "unsold-panel") {
                e.target.style.display = "none";
            }
        });

</script>
<script>
    const existing = [];

    <c:forEach var="bookCode" items="${listBookCode}">
        existing.push("${bookCode}".trim());
    </c:forEach>

    const bookCodeInput = document.getElementById("code");
    bookCodeInput.addEventListener("input", ()=>{
        const code = bookCodeInput.value.trim().toLowerCase();
        const isExist = existing.some(c => c.toLowerCase() === code);
        const hiddenFields = document.querySelectorAll(".hiddenWhenCodeExist");
        hiddenFields.forEach(field =>{
            field.style.display = isExist? "none":"";
            const inputs = field.querySelectorAll("input, select, textarea");
            inputs.forEach(input => {
                input.required = !isExist;
            });
        });
        document.querySelector(".btn-save").innerText =
            isExist ? "Nhập thêm hàng" : "Thêm sản phẩm";
    });
</script>
<script src="${pageContext.request.contextPath}/assets/js/search-suggest.js"></script>
<script>
    document.getElementById("fileBooks").addEventListener("change", function (){
        if (!this.files.length) return;
        const form = document.querySelector(".uploadExcel");
        const formData = new FormData(form);
        fetch(form.action,{
            method: "POST",
            body: formData
        })
        .then(res => res.json())
        .then(data => {
            alert("Import thất bại");
        })
        .catch(err => {
            alert("Import thất bại");
        });
    })
</script>
</body>
</html>