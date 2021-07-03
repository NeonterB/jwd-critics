<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/common/main.jsp" scope="request"/>
<c:choose>
    <c:when test="${not empty sessionScope.lang}">
        <fmt:setLocale value="${sessionScope.lang}" scope="session"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en" scope="session"/>
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <title>Main</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="container mt-2">
    <c:choose>
        <c:when test="${not empty foundMovies}">
            <div class="row">
                <c:forEach var="movie" items="${foundMovies}">

                    <div class="col-3">
                        <a href="${pageContext.request.contextPath}/controller?command=open_movie&movieId=${movie.id}">
                            <img class="img-thumbnail"
                                 src="${pageContext.request.contextPath}/picture?currentPicture=${movie.imagePath}"
                                 alt="${movie.name}">
                        </a>
                        <p class="text-center">${movie.name}</p>
                        <p class="text-center"><fmt:message key="movie.rating"/>: ${movie.rating}</p>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <h1>Welcome to the Epam Critics</h1>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
<c:remove var="foundMovies" scope="session"/>