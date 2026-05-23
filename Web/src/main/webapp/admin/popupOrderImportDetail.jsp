<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<c:forEach var="d" items="${importOrderDetails}">
    <tr>
        <td>${d.bookId}</td>
        <td>${d.title}</td>
        <td>
            <img src="${d.coverImgUrl}" width="60">
        </td>
        <td>${d.quantity}</td>
        <td><fmt:formatNumber value="${d.priceImport}" type="number" groupingUsed="true" maxFractionDigits="0"/></td>
        <td><fmt:formatNumber value="${d.subtotal}" type="number" groupingUsed="true" maxFractionDigits="0"/></td>
    </tr>
</c:forEach>