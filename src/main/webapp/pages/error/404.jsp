<%--
  Created by IntelliJ IDEA.
  User: barka
  Date: 6/7/2021
  Time: 7:28 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <div class="msg">Maybe this page moved? Got deleted? Is hiding out in quarantine? Never existed in the first place?
        <p>Let's go <a href="${pageContext.request.contextPath}/controller"><i>home</i></a> and try from there.</p></div>
</div>
</body>
</html>
<script src="https://kit.fontawesome.com/4b9ba14b0f.js" crossorigin="anonymous"></script>