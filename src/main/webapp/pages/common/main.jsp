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
<div class="container" style="margin-top: 200px">
    <div class="row">
        <p class="text-center fs-1">
            Welcome to the Epam Critics
        </p>
    </div>
    <div class="row mt-4">
        <p class="text-center fs-3">
            If you love movies and want to share some thoughts on them, then our service is right up you alley!
        </p>
        <p class="fs-6 mt-5">
            P.S. Start by using the navigation bar
        </p>
    </div>

</div>
</body>
</html>
