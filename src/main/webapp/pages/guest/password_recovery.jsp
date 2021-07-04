<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/guest/password_recovery.jsp" scope="request"/>
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
    <title>Update your password</title>
    <link rel="stylesheet" href="../../css/forgot_password.css">

</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="content">
    <div class="container-f">
        <div class="form-container">
            <form name="registerForm" method="POST" action="<c:url value="/controller?command=update_password"/>">
                <h1><fmt:message key="text.updateYourPassword"/></h1>
                <input type="password"
                       id="password"
                       name="newPassword"
                       minLength="8"
                       maxLength="20"
                       pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$"
                       required placeholder="<fmt:message key="label.newPassword"/>"/>
                <input type="password"
                       id="confirm_password"
                       name="confirmedNewPassword"
                       required placeholder="<fmt:message key="label.confirmPassword"/>"/>
                <input type="hidden" name="userId" value="${sessionScope.userId}"/>
                <button type="submit"><fmt:message key="button.submit"/></button>
            </form>
        </div>
    </div>
</div>
<c:remove var="userId" scope="session"/>
</body>
</html>
<script>
    const password = document.getElementById("password")
    const confirm_password = document.getElementById("confirm_password");

    function validatePassword() {
        if (password.value !== confirm_password.value) {
            confirm_password.setCustomValidity("Passwords Don't Match");
        } else {
            confirm_password.setCustomValidity('');
        }
    }

    password.onchange = validatePassword;
    confirm_password.onkeyup = validatePassword;
</script>