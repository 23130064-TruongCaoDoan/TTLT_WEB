<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử hoạt động</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"/>
    <link rel="stylesheet" href="assets/css_admin/admin.css">
    <link rel="stylesheet" href="assets/css_admin/myhistory.css">

</head>
<body>
<main>
    <c:import url="headerAdmin.jsp"></c:import>

    <div class="content">
        <c:import url="MenuFunctionAdmin.jsp"></c:import>

        <div class="history-container">
            <h2>Lịch sử thao tác của bạn</h2>

            <c:if test="${empty histories}">
                <p>Bạn chưa có lịch sử thao tác nào trên hệ thống.</p>
            </c:if>

            <c:if test="${not empty histories}">
                <div class="table-wrapper">
                    <table class="history-table">
                        <thead>
                            <tr>
                                <th>Hành động</th>
                                <th>Nội dung chi tiết</th>
                                <th>Thời gian</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="log" items="${histories}">
                                <tr>
                                    <td class="
                                        ${log.actionType == 'THÊM' ? 'action-them' : ''}
                                        ${log.actionType == 'SỬA' ? 'action-sua' : ''}
                                        ${log.actionType == 'XÓA' ? 'action-xoa' : ''}
                                    ">
                                        ${log.actionType}
                                    </td>
                                    <td>${log.actionUrl}</td>
                                    <td>
                                        <fmt:formatDate value="${log.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
    </div>
</main>
</body>
</html>