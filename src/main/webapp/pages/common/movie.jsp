<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="page" value="/pages/common/movie.jsp" scope="session"/>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <title>${movie.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie.css">
</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="container mt-5">
    <div class="row">
        <div class="col-4">
            <img src="${movie.imagePath}" alt="${movie.name}" class="img-thumbnail">
        </div>
        <div class="col-4">
            <div class="row">
                <h4>${movie.name}</h4>
            </div>
            <div class="row">
                <div class="col">
                    <strong><fmt:message key="movie.releaseDate"/>:</strong> ${movie.releaseDate}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <strong><fmt:message key="movie.runtime"/>:</strong> ${movie.runtime}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <strong><fmt:message key="movie.country"/>:</strong> ${movie.country}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <strong><fmt:message key="movie.ageRestriction"/>:</strong> ${movie.ageRestriction}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <strong><fmt:message key="movie.rating"/>:</strong> ${movie.rating}
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <strong><fmt:message key="movie.reviewCount"/>:</strong> ${movie.reviewCount}
                </div>
            </div>
            <hr/>
            <c:if test="${not empty movie.staff}">
                <c:forEach var="position" items="${movie.staff}">
                    <div class="row">
                        <div class="col">
                            <strong>${position.key}</strong>:
                            <c:forEach var="celebrity" items="${position.value}">
                                ${celebrity.firstName} ${celebrity.lastName},
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </div>
        <div class="col-4">
            <strong><fmt:message key="movie.summary"/>:</strong> ${movie.summary}
        </div>
    </div>
    <ctg:reviews/>
</div>
</body>
</html>
