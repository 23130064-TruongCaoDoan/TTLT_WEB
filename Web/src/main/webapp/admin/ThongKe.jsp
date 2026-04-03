<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thống kê</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css_admin/ThongKe.css">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script>
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
                    <option value="day"   ${type == 'day'   ? 'selected' : ''}>Thống kê theo ngày</option>
                    <option value="year"  ${type == 'year'  ? 'selected' : ''}>Thống kê theo năm</option>
                </select>
                <select name="yearFiler" id="yearSelect" style="display: none">
                    <c:forEach var="y" items="${listYear}">
                        <option value="${y}">${y}</option>
                    </c:forEach>
                </select>
                <form  action="ThongKe" method="get" id="dateSelect">
                    <div>
                        <label for="fromDate">Từ ngày:</label>
                        <input type="date" name="fromDate" id="fromDate" value="${from}" max="" required>
                        <label for="toDate">Đến ngày:</label>
                        <input type="date" name="toDate" id="toDate" value="${to}" min="" required>
                        <button type="submit">Thống kê</button>
                    </div>
                </form>
            </div>
                <div id="thongke-content">
                <div class="cards" id="cards">
                    <div class="card">
                        <i class="fa-solid fa-money-bill-wave"></i>
                        <h3>Tổng doanh thu</h3>

                        <p><fmt:formatNumber value="${totalRevenue}" type="number"
                                             groupingUsed="true" maxFractionDigits="0"/> Đ</p>
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
                </div>
                <div class="chart">
                    <h2>Biểu đồ doanh thu</h2>
                    <canvas id="revenueChart"></canvas>
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
                                <td><fmt:formatNumber value="${c.totalSpent}" type="number"
                                                                     groupingUsed="true" maxFractionDigits="0"/></td>
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
                                <td><fmt:formatNumber value="${b.price}" type="number"
                                                                groupingUsed="true" maxFractionDigits="0"/></td>
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
    document.getElementById("fromDate").addEventListener("change", function (){
       document.getElementById("toDate").setAttribute("min", this.value);
    });
    document.getElementById("toDate").addEventListener("change", function (){
        document.getElementById("fromDate").setAttribute("max", this.value);
    });
</script>
<script>
    document.addEventListener("click", function (e) {

        if (e.target.closest(".top10-customer")) {
            document.getElementById("top10-customer-panel").style.display = "flex";
        }

        if (e.target.closest(".top10-product")) {
            document.getElementById("top10-product-panel").style.display = "flex";
        }

        if (e.target.closest(".out-of-stock-card")) {
            document.getElementById("out-of-stock-panel").style.display = "flex";
        }

        if (e.target.id === "top10-customer-panel") {
            e.target.style.display = "none";
        }

        if (e.target.id === "top10-product-panel") {
            e.target.style.display = "none";
        }

        if (e.target.id === "out-of-stock-panel") {
            e.target.style.display = "none";
        }
    });
</script>

<script>
    function loadThongKe(url) {
        fetch(url)
            .then(res => res.text())
            .then(html => {

                let parser = new DOMParser();
                let doc = parser.parseFromString(html, "text/html");

                let newContent = doc.querySelector("#thongke-content");

                document.querySelector("#thongke-content").innerHTML =
                    newContent.innerHTML;

                document.querySelector("#top10-customer-panel").innerHTML =
                    doc.querySelector("#top10-customer-panel").innerHTML;

                document.querySelector("#top10-product-panel").innerHTML =
                    doc.querySelector("#top10-product-panel").innerHTML;

                document.querySelector("#out-of-stock-panel").innerHTML =
                    doc.querySelector("#out-of-stock-panel").innerHTML;

                let scripts = doc.querySelectorAll("script");

                scripts.forEach(s => {
                    if (s.innerText) {
                        eval(s.innerText);
                    }
                });

                setTimeout(() => {
                    initChart();
                }, 0);

            });
    }
    document.getElementById("filter").addEventListener("change", function () {
        let type = this.value;
        if (type === "year") {
            document.getElementById("yearSelect").style.display = "block";
            document.getElementById("dateSelect").style.display = "none";
            let year = document.getElementById("yearSelect").value;
            loadThongKe("ThongKe?type=year&year=" + year);
        } else {
            document.getElementById("yearSelect").style.display = "none";
            document.getElementById("dateSelect").style.display = "block";
            loadThongKe("ThongKe?type=day");
        }
    });


    document.getElementById("yearSelect").addEventListener("change", function () {
        let year = this.value;
        loadThongKe("ThongKe?type=year&year=" + year);
    });


    document.getElementById("dateSelect").addEventListener("submit", function (e) {
        e.preventDefault();
        let from = document.getElementById("fromDate").value;
        let to = document.getElementById("toDate").value;
        loadThongKe("ThongKe?type=day&fromDate=" + from + "&toDate=" + to);

    });
</script>
<script>
    function formatterDate(date) {
        let element = date.split('-');
        let y = element[0];
        let m = element[1];
        let d = element[2];
        return d + "-" + m + "-" + y;
    }
    let type = document.getElementById("filter").value;
    let chartInstance = null;
    function initChart() {
        const labels = [
            <c:forEach var="l" items="${revenueChartData}" varStatus="s">
                <c:choose>
                    <c:when test="${type == 'year'}">
                        "${l.label}"
                    </c:when>
                    <c:otherwise>
                        formatterDate("${l.label}")
                    </c:otherwise>
                </c:choose>
                <c:if test="${!s.last}">,</c:if>
            </c:forEach>
        ];

        const revenueData = [
            <c:forEach var="r" items="${revenueChartData}">
                ${r.revenue},
            </c:forEach>
        ];

        const data = {
            labels: labels,
            datasets: [{
                label: 'Tổng doanh thu',
                data: revenueData,
                maxBarThickness: 50,
                categoryPercentage: 0.7,
                barPercentage: 0.7,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgb(54, 162, 235)',
                borderWidth: 1
            }]
        };

        const config = {
            type: 'bar',
            data: data,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    datalabels: {
                        color: 'rgb(54, 162, 235)',
                        anchor: 'end',
                        align: 'end',
                        formatter: (value) => value.toLocaleString("vi-VN") + " Đ",
                        font: {
                            weight: 'bold',
                            size: 14
                        }
                    }
                }
            },
            plugins: [ChartDataLabels]
        };
        const canvas = document.getElementById("revenueChart");
        if (!canvas) return;

        if (chartInstance) {
            chartInstance.destroy();
        }
        chartInstance = new Chart(canvas, config);
    }
    initChart();
</script>
</html>