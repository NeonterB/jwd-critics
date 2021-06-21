<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/common/reviews.jsp" scope="session"/>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <title>Reviews</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review.css">
</head>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<body>
<div class="container mt-2">
    <a href="${pageContext.request.contextPath}/controller?command=open_movie&movieId=${movie.id}">Back to the movie</a>
    <ctg:reviews/>
</div>
</body>
</html>
