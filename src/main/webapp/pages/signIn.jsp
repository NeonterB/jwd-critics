<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="page" value="/pages/signIn.jsp" scope="session"/>
<html>
<head>
    <title>Log in</title>
    <link rel="stylesheet" href="../css/signIn.css">

</head>
<body>
<c:import url="componets/header.jsp"/>
<c:import url="componets/message.jsp"/>
<div class="content">
    <div class="container" id="container">
        <div class="form-container sign-up-container">
            <form name="registerForm" method="POST" action="<c:url value="/controller?command=register"/>">
                <h1>Create Account</h1>
                <input type="text"
                       name="firstName"
                       pattern="^[A-Z][a-z]{1,14}"
                       title="Must contain letters only, must start with an uppercase letter"
                       required placeholder="First name"/>
                <input type="text"
                       name="lastName"
                       pattern="^[A-Z][a-z]{1,14}"
                       title="Must contain letters only, must start with an uppercase letter"
                       required placeholder="Last name"/>
                <input type="email"
                       name="email"
                       required placeholder="Email"/>
                <input type="text"
                       name="login"
                       pattern="^[a-zA-Z0-9._-]{3,25}$"
                       title="Minimum 3 and maximum 25 characters, can contain letters, numbers, and characters &quot;._-&quot;"
                       required placeholder="Login"/>
                <input type="password"
                       name="password"
                       pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$"
                       title="Minimum 8 and maximum 20 characters, at least one uppercase letter, one lowercase letter, one number and one special character &quot;@$!%*&?&quot;"
                       required placeholder="Password"/>
                <button>Sign Up</button>
            </form>
        </div>
        <div class="form-container sign-in-container">
            <form name="signInForm" method="POST" action="<c:url value="/controller?command=sign_in"/>">
                <h1>Sign in</h1>
                <input type="text"
                       name="login"
                       pattern="^[a-zA-Z0-9._-]{3,25}$"
                       title="Minimum 3 and maximum 25 characters, can contain letters, numbers, and characters ._-"
                       required placeholder="Login"/>
                <input type="password"
                       name="password"
                       pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$"
                       title="Minimum 8 and maximum 20 characters, at least one uppercase letter, one lowercase letter, one number and one special character"
                       required placeholder="Password"/>
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
<c:remove var="serviceError"/>
<c:remove var="validationErrors"/>