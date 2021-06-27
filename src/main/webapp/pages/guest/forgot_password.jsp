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
    <title>Forgot Password</title>
    <link rel="stylesheet" href="../../css/forgot_password.css">

</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="content">
    <div class="container-f">
        <div class="form-container">
            <form name="registerForm" method="POST" action="<c:url value="/controller?command=send_recovery_mail"/>">
                <h1><fmt:message key="text.recoverYourPassword"/></h1>
                <input type="email"
                       name="email"
                       title="<fmt:message key="validation.email"/>"
                       required placeholder="<fmt:message key="label.email"/>"/>
                <button type="submit"><fmt:message key="button.submit"/></button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
