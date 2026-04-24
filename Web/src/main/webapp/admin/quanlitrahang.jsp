<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Yêu cầu Trả hàng</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css_admin/quanlitrahang.css">
    <link rel="stylesheet" href="assets/css_admin/admin.css">
</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>
    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>
        <div class="order-container">
            <h2>Quản lý Trả hàng và Hoàn tiền</h2>
            <div class="order-list">
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã yêu cầu</th>
                                <th>Mã Đơn Hàng</th>
                                <th>Lý do trả</th>
                                <th>Ảnh minh chứng</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="req" items="${returnRequests}">
                                <tr>
                                    <td>#${req.id}</td>
                                    <td>${req.orderId}</td>
                                    <td>${req.reason}</td>
                                    <td>
                                        <a href="${req.proofImage}" target="_blank">
                                            <img src="${req.proofImage}" width="60" style="cursor:pointer; border:1px solid #ccc; height: 60px; object-fit: cover;">
                                        </a>
                                    </td>
                                    <td>
                                        <span style="font-weight:bold; color: ${req.status == 'PENDING' ? 'orange' : (req.status == 'APPROVED' ? 'green' : 'red')}">
                                            ${req.status}
                                        </span>
                                    </td>
                                    <td>
                                        <c:if test="${req.status == 'PENDING'}">
                                            <button onclick="processReturn(${req.id}, 'APPROVE')" style="background: green; color: white; border: none; padding: 5px 10px; cursor: pointer; margin-right: 5px; border-radius:3px;">Duyệt</button>
                                            <button onclick="processReturn(${req.id}, 'REJECT')" style="background: red; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius:3px;">Từ chối</button>
                                        </c:if>
                                        <c:if test="${req.status == 'REJECTED'}">
                                            <small title="${req.rejectReason}">Lý do từ chối: ${req.rejectReason}</small>
                                        </c:if>
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

<script>
function processReturn(requestId, action) {
    let data = new URLSearchParams();
    data.append("requestId", requestId);
    data.append("action", action);

    if (action === 'REJECT') {
        let reason = prompt("Nhập lý do từ chối (bắt buộc):");
        if (!reason) return;
        data.append("rejectReason", reason);
    }

    fetch('process-return', {
        method: 'POST',
        body: data
    }).then(res => res.json()).then(data => {
        alert(data.message);
        if(data.success) location.reload();
    });
}
</script>
</body>
</html>