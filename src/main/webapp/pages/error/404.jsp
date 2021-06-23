<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <h1 class="text-center text-white fw-light" style="font-size: 10rem">404</h1>
        <p class="text-center text-white display-5">
            ${commandError}
        </p>
        <p class="text-center text-white display-6">
            Let's go <a href="${pageContext.request.contextPath}/controller" class="link-light"><i>home</i></a> and try from there.
        </p>
    </div>
</div>
</body>
</html>
<script src="https://kit.fontawesome.com/4b9ba14b0f.js" crossorigin="anonymous"></script>
<c:remove var="commandError"/>