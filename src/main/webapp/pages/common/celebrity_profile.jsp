<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/common/celebrity_profile.jsp" scope="request"/>
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
    <title>Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="container mt-5">
    <div class="row">
        <div class="col-4">
            <img src="${pageContext.request.contextPath}/picture?currentPicture=${celebrity.imagePath}"
                 alt="${celebrity.firstName}" class="img-thumbnail">
        </div>
        <div class="col-4">
            <h4>${celebrity.firstName} ${celebrity.lastName}</h4>
            <c:if test="${user.role eq 'ADMIN'}">
                <p class="mt-4">
                    <a class="btnRef"
                       href="${pageContext.request.contextPath}/controller?command=open_update_celebrity&celebrityId=${celebrity.id}&previousPage=${currentPage}">
                        <fmt:message key="button.edit"/>
                    </a>
                    <a class="btnRef"
                       href="${pageContext.request.contextPath}/controller?command=delete_celebrity&celebrityId=${celebrity.id}"><fmt:message
                            key="button.delete"/>
                    </a>
                </p>
            </c:if>
        </div>
    </div>
    <div class="row mt-4">
        <h1><fmt:message key="text.knownFor"/></h1>
        <c:if test="${not empty celebrity.jobs}">
            <c:forEach var="job" items="${celebrity.jobs}">
                <c:set var="movie" value="${job.key}" scope="page"/>
                <c:set var="positions" value="${job.value}" scope="page"/>

                <div class="col-2">
                    <a href="${pageContext.request.contextPath}/controller?command=open_movie&movieId=${movie.id}">
                        <img class="img-thumbnail"
                             src="${pageContext.request.contextPath}/picture?currentPicture=${movie.imagePath}"
                             alt="${movie.name}">
                    </a>
                    <p class="text-center">
                        <strong>${movie.name}</strong><br>
                        <fmt:message key="movie.rating"/>: ${movie.rating}<br>
                        <fmt:message key="celebrity.positions"/>:<br>
                        <c:forEach var="position" items="${positions}">
                            ${position},
                        </c:forEach>
                    </p>
                </div>

            </c:forEach>
        </c:if>
    </div>
</div>
</body>
</html>
