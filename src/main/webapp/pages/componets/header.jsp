<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<div class="wrapper">
    <div class="navbar">
        <div class="menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/controller"><fmt:message key="button.home"/></a></li>
                <li><a href="#"><fmt:message key="button.celebrities"/></a></li>
                <li><a href="#"><fmt:message key="button.movies"/></a></li>
                <li><a href="#"><fmt:message key="button.profile"/></a></li>
                <li>
                    <c:choose>
                        <c:when test="${empty sessionScope.userRole}">
                            <a href="${pageContext.request.contextPath}/controller?command=open_sign_in"><fmt:message
                                    key="button.signIn"/></a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=sign_out"><fmt:message
                                    key="button.signOut"/></a>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <form class="langForm" name="langForm" method="POST" action="<c:url value="/controller"/>">
                        <input type="hidden" name="command" value="change_language">
                        <input type="hidden" name="page" value="${ sessionScope.page }">
                        <select name="lang" onchange="submit()">
                            <option value="" <c:if test="${empty lang}">selected</c:if>>ENGLISH</option>
                            <option value="ru" <c:if test="${lang eq 'ru'}">selected</c:if>>РУССКИЙ</option>
                        </select>
                    </form>
                </li>
            </ul>
        </div>
        <div class="searchbar">
            <form action="<c:url value="/controller"/>" method="POST">
                <input type="hidden" name="command" value="find_movies_by_title"/>
                <input type="text" placeholder="<fmt:message key="label.search"/>">
                <div class="icon" id="icon">
                    <i class="fas fa-search" id="searchBtn"></i>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<script type="text/javascript" src="../../scripts/header.js"></script>