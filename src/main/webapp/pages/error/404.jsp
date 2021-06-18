<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/404.css"/>
</head>
<body>
<div class="mainbox">
    <div class="err">4</div>
    <i class="far fa-question-circle fa-spin"></i>
    <div class="err2">4</div>
    <div class="msg">${sessionScope.globalError}
        <p>Let's go <a href="${pageContext.request.contextPath}/controller"><i>home</i></a> and try from there.</p></div>
</div>
</body>
</html>
<script src="https://kit.fontawesome.com/4b9ba14b0f.js" crossorigin="anonymous"></script>
<c:remove var="globalError"/>