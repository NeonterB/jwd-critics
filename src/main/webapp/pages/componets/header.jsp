<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
</head>
<body>
<nav class="header navbar navbar-expand-lg navbar-light bg-transparent">
    <div class="container-fluid p-3">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active text-white" aria-current="page"
                       href="${pageContext.request.contextPath}/controller"><fmt:message key="button.home"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white"
                       href="${pageContext.request.contextPath}/controller?command=open_all_celebrities">
                        <fmt:message key="button.celebrities"/>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white"
                       href="${pageContext.request.contextPath}/controller?command=open_all_movies">
                        <fmt:message key="button.movies"/>
                    </a>
                </li>
                <li class="nav-item">
                    <c:choose>
                    <c:when test="${empty user}">
                        <a class="nav-link text-white"
                           href="${pageContext.request.contextPath}/controller?command=open_sign_in&previousPage=${requestScope.currentPage}">
                            <fmt:message key="button.signIn"/>
                        </a>
                    </c:when>
                    <c:otherwise>
                    <c:if test="${user.role eq 'ADMIN'}">
                    <a class="nav-link text-white"
                       href="${pageContext.request.contextPath}/controller?command=open_all_users">
                        <fmt:message key="button.users"/>
                    </a>
                </li>
                <li>
                    </c:if>
                    <a class="nav-link text-white"
                       href="${pageContext.request.contextPath}/controller?command=open_user_profile&userId=${user.id}">
                        <fmt:message key="button.profile"/>
                    </a>
                </li>
                <li>
                    <a class="nav-link text-white"
                       href="${pageContext.request.contextPath}/controller?command=sign_out&previousPage=${requestScope.currentPage}">
                        <fmt:message key="button.signOut"/>
                    </a>
                    </c:otherwise>
                    </c:choose>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdownMenuLink"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <c:choose>
                            <c:when test="${empty lang}">
                                English
                            </c:when>
                            <c:when test="${lang eq 'ru'}">
                                Русский
                            </c:when>
                        </c:choose>
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/controller?command=change_language&lang=ru&previousPage=${requestScope.currentPage}">
                                Русский
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item"
                               href="${pageContext.request.contextPath}/controller?command=change_language&previousPage=${requestScope.currentPage}">
                                English
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
            <form class="d-flex me-2 mb-2 mb-lg-0" method="GET" action="<c:url value="/controller"/>">
                <input type="hidden" name="command" value="find_movies"/>
                <input class="form-control me-2" type="search" placeholder="<fmt:message key="label.search"/> "
                       aria-label="Search" name="movieName">
                <button class="btn search-btn" type="submit"><i class="fa fa-search"></i></button>
            </form>
        </div>
    </div>
</nav>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
        crossorigin="anonymous"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
        crossorigin="anonymous"></script>
</body>
</html>