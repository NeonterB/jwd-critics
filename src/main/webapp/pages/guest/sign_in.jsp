<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="properties/content"/>
<%--<c:set var="currentPage" value="/pages/guest/signIn.jsp" scope="session"/>--%>
<html>
<head>
    <title>Log in</title>
    <link rel="stylesheet" href="../../css/sign-in.css">

</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="content">
    <div class="container" id="container">
        <div class="form-container sign-up-container">
            <form name="registerForm" method="POST" action="<c:url value="/controller?command=register"/>">
                <h1><fmt:message key="text.createAccount"/></h1>
                <input type="text"
                       name="firstName"
                       pattern="^[A-Z][a-z]{1,14}"
                       title="<fmt:message key="validation.firstName"/>"
                       required placeholder="<fmt:message key="label.firstName"/>"/>
                <input type="text"
                       name="lastName"
                       pattern="^[A-Z][a-z]{1,14}"
                       title="<fmt:message key="validation.lastName"/>"
                       required placeholder="<fmt:message key="label.lastName"/>"/>
                <input type="email"
                       name="email"
                       title="<fmt:message key="validation.email"/>"
                       required placeholder="<fmt:message key="label.email"/>"/>
                <input type="text"
                       name="login"
                       pattern="^[a-zA-Z0-9._-]{3,25}$"
                       title="<fmt:message key="validation.login"/>"
                       required placeholder="<fmt:message key="label.login"/>"/>
                <input type="password"
                       name="password"
                       pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$"
                       title="<fmt:message key="validation.password"/>"
                       required placeholder="<fmt:message key="label.password"/>"/>
                <button type="submit"><fmt:message key="button.register"/></button>
            </form>
        </div>
        <div class="form-container sign-in-container">
            <form name="signInForm" method="POST" action="<c:url value="/controller?command=sign_in"/>">
                <h1><fmt:message key="button.signIn"/></h1>
                <input type="text"
                       name="login"
                       pattern="^[a-zA-Z0-9._-]{3,25}$"
                       title="<fmt:message key="validation.login"/>"
                       required placeholder="<fmt:message key="label.login"/>"/>
                <input type="password"
                       name="password"
                       pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$"
                       title="<fmt:message key="validation.password"/>"
                       required placeholder="<fmt:message key="label.password"/>"/>
                <a href="#">Forgot your password?</a>
                <button type="submit"><fmt:message key="button.signIn"/></button>
            </form>
        </div>
        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h1><fmt:message key="text.welcome"/></h1>
                    <p><fmt:message key="text.login"/></p>
                    <button class="ghost" id="signIn"><fmt:message key="button.signIn"/></button>
                </div>
                <div class="overlay-panel overlay-right">
                    <h1><fmt:message key="text.hello"/></h1>
                    <p><fmt:message key="text.registration"/></p>
                    <button class="ghost" id="signUp"><fmt:message key="button.register"/></button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
<script type="text/javascript" src="../../scripts/signIn.js"></script>