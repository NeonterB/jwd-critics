<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>500 - Eternal Server Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <h1 class="text-center text-white fw-light" style="font-size: 10rem">500</h1>
        <p class="text-center text-white display-5">
            ${commandError}
        </p>
        <p class="text-center text-white display-6">
            Sorry, we failed to process your request. Let's go
            <a href="${pageContext.request.contextPath}/controller" class="link-light"><i>home</i></a>
            and try again.
        </p>
    </div>
</div>
</body>
</html>
<c:remove var="commandError"/>