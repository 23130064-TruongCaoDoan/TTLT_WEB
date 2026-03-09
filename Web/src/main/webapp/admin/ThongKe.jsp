<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thống kê</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css_admin/ThongKe.css">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
</head>
<body>
<main class="main">
    <c:import url="headerAdmin.jsp"></c:import>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp" ></c:import>
        <div class="thongke-container">
            <h2>Thống kê</h2>
            <div class="filter-bar">
                <select id="filter">
                    <option value="month" ${type == 'month' ? 'selected' : ''}>Thống kê theo tháng</option>
                    <option value="week"  ${type == 'week'  ? 'selected' : ''}>Thống kê theo tuần</option>
                    <option value="day"   ${type == 'day'   ? 'selected' : ''}>Thống kê theo ngày</option>
                    <option value="year"  ${type == 'year'  ? 'selected' : ''}>Thống kê theo năm</option>
                </select>

            </div>
            <div class="cards">
                <div class="card">
                    <i class="fa-solid fa-money-bill-wave"></i>
                    <h3>Tổng doanh thu</h3>
                    <p>${totalRevenue} ₫</p>
                </div>
                <div class="card">
                    <i class="fa-solid fa-user-tie"></i>
                    <h3>Khách hàng mua nhiều nhất</h3>
                    <p><c:choose>
                        <c:when test="${topCustomer != null}">
                            ${topCustomer.name}
                        </c:when>
                        <c:otherwise>
                            Chưa có dữ liệu
                        </c:otherwise>
                    </c:choose></p>
                </div>
                <div class="card">
                    <i class="fa-solid fa-box-open"></i>
                    <h3>Sản phẩm bán chạy nhất</h3>
                    <p><c:choose>
                        <c:when test="${bestBook != null}">
                            ${bestBook.title}
                        </c:when>
                        <c:otherwise>
                            Chưa có dữ liệu
                        </c:otherwise>
                    </c:choose>
                    </p>
                </div>
                <div class="card">
                    <i class="fa-solid fa-box"></i>
                    <h3>Sản phẩm bán ít nhất</h3>
                    <p><c:choose>
                        <c:when test="${worstBook != null}">
                            ${worstBook.title}
                        </c:when>
                        <c:otherwise>
                            Chưa có dữ liệu
                        </c:otherwise>
                    </c:choose></p>
                </div>
                <div class="card top10-product">
                    <i class="fa-solid fa-box"></i>
                    <h3>Top 10 Sản phẩm bán chạy nhất</h3>
                </div>
                <div class="card top10-customer">
                    <i class="fa-solid fa-user-tie"></i>
                    <h3>Top 10 Khách hàng mua nhiều nhất</h3>
                </div>
            </div>

            <div class="chart">
                <h2>Biểu đồ doanh thu</h2>
                <div class="bar-container">
                    <c:forEach items="${revenueData}" var="r">
                        <div>
                            <div class="figure">${r.revenue} ₫</div>
                            <c:choose>
                                <c:when test="${singleBar}">
                                    <div class="bar" style="height:250px"></div>
                                </c:when>
                                <c:otherwise>
                                    <div class="bar"
                                         style="height:${((r.revenue - minRevenue) / rangeRevenue) * 200 + 60}px;">
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="bar-label">${r.label}</div>
                        </div>
                    </c:forEach>
                </div>

            </div>
        </div>
    </div>
    <div id="top10-customer-panel">
        <div id="top10-customer-container">
            <div class="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã Khách Hàng</th>
                            <th>Tên Khách Hàng</th>
                            <th>Email</th>
                            <th>Point</th>
                            <th>Tổng tiền</th>
                        </tr>
                        </thead>
                        <tbody id="userTable">
                        <c:forEach items="${top10Customers}" var="c">
                            <tr class="infUser">
                                <td>${c.customerCode}</td>
                                <td>${c.name}</td>
                                <td>${c.email}</td>
                                <td>${c.point}</td>
                                <td>${c.totalSpent}</td>
                            </tr>
                        </c:forEach>

                        
                        </tbody>
                    </table>
                </div>
        </div>
    </div>
    <div id="top10-product-panel">
        <div id="top10-product-container">
        <div class="table-wrapper" >
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
                        <c:forEach items="${top10Books}" var="b">
                            <tr>
                                <td>${b.bookCode}</td>
                                <td>${b.title}</td>
                                <td>—</td>
                                <td>${b.price}</td>
                                <td>${b.totalSold}</td>
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

</main>
</body>
<script>
    const panel = document.getElementById("top10-customer-panel");

    document.querySelector(".top10-customer").addEventListener("click", () => {
        panel.style.display = "flex";
    });

    panel.addEventListener("click", (e) => {
        if (e.target === panel) {
            panel.style.display = "none";
        }
    });

    const popup_panel = () =>{
        const top10ProductPanel = document.getElementById("top10-product-panel");

        document.querySelector('.top10-product').addEventListener("click", () => {
        top10ProductPanel.style.display = "flex";
        });

        top10ProductPanel.addEventListener("click", (e) => {
            if (e.target === top10ProductPanel) {
                top10ProductPanel.style.display = "none";
            }
        });
    }
    popup_panel()

    document.getElementById("filter").addEventListener("change", function () {
        window.location = "ThongKe?type=" + this.value;
    });


</script>
</html>