<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: barka
  Date: 6/5/2021
  Time: 2:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log in</title>
    <link rel="stylesheet" href="../css/login.css">
</head>
<body>
<c:import url="componets/header.jsp"/>
<div class="content">
    <div class="container" id="container">
        <div class="form-container sign-up-container">
            <form action="#">
                <h1>Create Account</h1>
                <input type="text" placeholder="First name"/>
                <input type="text" placeholder="Last name"/>
                <input type="email" placeholder="Email"/>
                <input type="text" placeholder="Login"/>
                <input type="password" placeholder="Password"/>
                <button>Sign Up</button>
            </form>
        </div>
        <div class="form-container sign-in-container">
            <form name="loginForm" method="POST" action="<c:url value="/controller"/>">
                <h1>Sign in</h1>
                <input type="text" placeholder="Login"/>
                <input type="password" placeholder="Password"/>
                <a href="#">Forgot your password?</a>
                <button type="submit">Sign In</button>
            </form>
        </div>
        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h1>Welcome Back!</h1>
                    <p>To keep connected with us please login with your personal info</p>
                    <button class="ghost" id="signIn">Sign In</button>
                </div>
                <div class="overlay-panel overlay-right">
                    <h1>Hello, Friend!</h1>
                    <p>Enter your personal details and start journey with us</p>
                    <button class="ghost" id="signUp">Sign Up</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
<script type="text/javascript" src="../scripts/login.js"></script>