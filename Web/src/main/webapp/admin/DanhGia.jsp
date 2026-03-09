<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Đánh giá</title>

    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
    />
    <link rel="stylesheet" href="assets/css_admin/danhgia.css"/>
    <link rel="stylesheet" href="assets/css_admin/admin.css"/>
</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>

        <div class="dashboard">
            <h2>Thống kê đánh giá</h2>

            <form id="filterForm" action="${pageContext.request.contextPath}/Rate" method="post">
                <div class="filter-section">
                    <div class="filter-item">
                        <label for="startDate">Từ ngày:</label>
                        <input type="date" id="startDate" name="startDate" value="${from}">
                    </div>
                    <div class="filter-item">
                        <label for="endDate">Đến ngày:</label>
                        <input type="date" id="endDate" name="endDate" value="${to}">
                    </div>
                    <div class="filter-item">
                        <label for="typeFilter">Thể loại:</label>
                        <select id="typeFilter" name="typeFilter">
                            <option value="0" ${type==0?"selected":""}>Tất cả</option>
                            <option value="1" ${type==1?"selected":""}>Truyện tranh</option>
                            <option value="2" ${type==2?"selected":""}>Sách ảnh</option>
                            <option value="3" ${type==3?"selected":""}>Sách giáo dục</option>
                            <option value="4" ${type==4?"selected":""}>Sách tô màu</option>
                        </select>
                    </div>
                    <button type="submit" id="applyFilter" class="btn-filter">Lọc</button>
                </div>
            </form>


            <div class="chart-container">
                <div class="chart-title">Phân bố số lượng đánh giá</div>
                <div class="chart">
                    <c:forEach begin="1" end="5" var="i">
                        <div class="column-wrapper">
                            <div class="figure">${stars[i]}</div>

                            <div class="columns-chart"
                                 style="height: ${(stars[i] * 200.0) / max}px">
                            </div>

                            <p class="label-star">${i} Sao</p>
                        </div>
                    </c:forEach>
                </div>
            </div>


            <div class="top-section">
                <div class="top-box">
                    <h3>Top sách được đánh giá cao</h3>
                    <ul id="topHighRated">
                        <c:if test="${empty listHigh}">
                            <li>Chưa có dữ liệu</li>
                        </c:if>
                        <c:forEach items="${listHigh}" var="item" varStatus="st" begin="0" end="10">
                            <li>
                                <span class="rank ${st.index == 0 ? 'rank1' : st.index == 1 ? 'rank2' : st.index == 2 ? 'rank3' : ''}">
                                        ${st.index + 1}
                                </span>
                                <span class="book-title" title="${item.title}">
                                        ${item.title}
                                </span>
                                <span class="stars">
                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i <= item.rating}">★</c:when>
                                            <c:otherwise>☆</c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="top-box">
                    <h3>Sách bị đánh giá thấp</h3>
                    <ul id="topLowRated">
                        <c:if test="${empty listLow}">
                            <li>Chưa có dữ liệu</li>
                        </c:if>
                        <c:forEach items="${listLow}" var="item" varStatus="st" begin="0" end="10">
                            <li>
                                <span class="rank ${st.index == 0 ? 'rank1' : st.index == 1 ? 'rank2' : st.index == 2 ? 'rank3' : ''}">
                                        ${st.index + 1}
                                </span>
                                <span class="book-title" title="${item.title}">
                                        ${item.title}
                                </span>
                                <span class="stars">
                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i <= item.rating}">★</c:when>
                                            <c:otherwise>☆</c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="table-wrapper">
                <table class="review-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên sách</th>
                        <th>Người đánh giá</th>
                        <th>Số sao ⭐</th>
                        <th>Nhận xét</th>
                        <th>Ngày</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>

                    <tbody id="reviewTable">

                    <c:if test="${empty listRate}">
                        <tr>
                            <td colspan="8" style="text-align:center">
                                Không có đánh giá nào
                            </td>
                        </tr>
                    </c:if>

                    <c:forEach items="${listRate}" var="r">
                        <tr class="${r.rating <= 2 ? 'bad-review' : ''}">
                            <td>${r.id}</td>

                            <td title="${r.title}">
                                    ${r.title}
                            </td>

                            <td title="User">
                                    ${r.name}
                            </td>

                            <td>
                                    ${r.rating}
                            </td>

                            <td class="comment" title="${r.content}">
                                    ${r.content}
                            </td>

                            <td>
                                <fmt:formatDate value="${r.createAt}" pattern="dd/MM/yyyy"/>
                            </td>

                            <td>
                                <label class="switch">
                                    <input type="checkbox"
                                           <c:if test="${r.active}">checked</c:if>
                                           data-id="${r.id}"
                                           onchange="toggleActive(this)">
                                    <span class="slider"></span>
                                </label>
                            </td>

                            <td>
                                <button class="btn-delete"
                                        onclick="deleteReview(${r.id})">
                                    <i class="fa-solid fa-trash xoa"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>
</main>
<script>
    const overlay = document.getElementById("overlay");
    const add = document.getElementById("addEvent");
    const sua = document.querySelector(".sua");
    const popup = document.getElementById("eventForm");

    overlay.addEventListener("click", () => {
        overlay.style.display = "none";
        popup.style.display = "none";
    });
    sua.addEventListener("click", () => {
        overlay.style.display = "block";
        popup.style.display = "block";
    });
    add.addEventListener("click", () => {
        overlay.style.display = "block";
        popup.style.display = "block";
    });
</script>

<script>
    function deleteReview(id) {
        if (!confirm("Bạn chắc chắn muốn xóa đánh giá này?")) return;

        fetch('${pageContext.request.contextPath}/deleteRate', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'id=' + id
        }).then(() => location.reload());
    }

    function toggleActive(el) {
        const id = el.dataset.id;

        fetch('${pageContext.request.contextPath}/hidden', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'id=' + id
        });
    }
</script>
</body>
</html>
