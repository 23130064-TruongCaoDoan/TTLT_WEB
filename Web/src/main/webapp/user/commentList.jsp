<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach var="cmt" items="${commentViewList}">
    <div class="comment-item">
        <div class="comment-header">
            <span class="comment-author">${cmt.name}</span>
            <span class="comment-date">${cmt.createAt}</span>
        </div>

        <p class="comment-rating" style="color: #FFD700">
            <c:forEach begin="1" end="${cmt.rating}">
                ★
            </c:forEach>
        </p>

        <p class="comment-text">${cmt.content}</p>

        <c:if test="${not empty cmt.imgComment}">
            <img src="${cmt.imgComment}" alt="ảnh cmt">
        </c:if>
    </div>
</c:forEach>

<c:if test="${empty comments}">
    <p>Không có đánh giá phù hợp</p>
</c:if>

