<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/common/all_movies.jsp" scope="request"/>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <title>All users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/all_users.css">
</head>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<body>
<div class="container mt-2">
    <ctg:all_users/>
</div>
</body>
</html>
