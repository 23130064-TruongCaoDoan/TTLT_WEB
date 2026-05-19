<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Lịch sử nhập kho</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <link rel="stylesheet" href="assets/css_admin/mProduct.css">
    <link rel="stylesheet" href="assets/css_admin/importManagement.css">


</head>

<body>

<main>

    <c:import url="headerAdmin.jsp"></c:import>

    <div class="content">

        <c:import url="MenuFunctionAdmin.jsp"></c:import>

        <div class="product-container">

            <h2>Lịch sử nhập kho</h2>

            <div class="function">

                <div class="find">
                    <form method="get" action="${pageContext.request.contextPath}/import-history" class="form-filter-import">

                        <input type="text" class="search" name="q" value="${param.q}" placeholder="Tìm mã phiếu nhập">

                        <div class="date-filter">
                            <label>Từ:</label>
                            <input type="date" name="fromDate" value="${param.fromDate}">
                        </div>
                        <div class="date-filter">
                            <label>Đến:</label>
                            <input type="date" name="toDate" value="${param.toDate}">
                        </div>
                        <button type="submit" class="buttonSearch">
                            Tìm kiếm
                        </button>

                    </form>

                </div>

            </div>

            <div class="order-list">

                <div class="title">

                    <h3>Danh sách phiếu nhập</h3>

                </div>

                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>Mã phiếu nhập</th>

                            <th>Nhà cung cấp</th>

                            <th>Ngày nhập</th>

                            <th>Tổng tiền</th>

                            <th>Nhân viên nhập</th>

                            <th>Ghi chú</th>

                            <th>Chi tiết</th>

                        </tr>

                        </thead>

                        <tbody>

                        <c:forEach var="o" items="${historyImportDTOList}">

                            <tr>

                                <td>${o.id}</td>

                                <td>${o.provider}</td>

                                <td>${o.importDate}</td>

                                <td>

                                    <fmt:formatNumber value="${o.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                                </td>

                                <td>${o.employeeName}</td>

                                <td>${o.note}</td>

                                <td>

                                    <button class="btn-detail xemchitiet"
                                            data-id="${o.id}">

                                        Xem chi tiết

                                    </button>

                                </td>

                            </tr>

                        </c:forEach>

                        </tbody>

                    </table>

                </div>

            </div>

        </div>

    </div>

</main>


<div id="overlay"></div>

<div id="import-detail-popup">

    <div class="popup-header">

        <h3>Chi tiết phiếu nhập</h3>

        <button id="closePopup">

            <i class="fa-solid fa-xmark"></i>

        </button>

    </div>

    <div class="table-wrapper">

        <table>

            <thead>

            <tr>

                <th>Mã sách</th>

                <th>Tên sách</th>

                <th>Số lượng</th>

                <th>Giá nhập</th>

                <th>Thành tiền</th>

            </tr>

            </thead>

            <tbody id="import-detail-body">

            </tbody>

        </table>

    </div>

</div>
<script>

    const overlay = document.getElementById("overlay");

    const popup = document.getElementById("import-detail-popup");

    document.querySelectorAll(".xemchitiet")
        .forEach(btn => {

            btn.addEventListener("click", () => {
                overlay.style.display = "block";
                popup.style.display = "block";
                const importId = btn.dataset.id;
                console.log(importId);

            });

        });

    document.getElementById("closePopup").addEventListener("click", () => {
            overlay.style.display = "none";
            popup.style.display = "none";
        });

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        popup.style.display = "none";
    });

</script>
</body>
</html>