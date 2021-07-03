<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/admin/update_celebrity.jsp" scope="request"/>
<c:choose>
    <c:when test="${not empty sessionScope.lang}">
        <fmt:setLocale value="${sessionScope.lang}" scope="session"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en" scope="session"/>
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="properties/content"/>
<c:choose>
    <c:when test="${not empty celebrity}">
        <c:set var="imagePath" value="${celebrity.imagePath}" scope="page"/>
        <c:set var="formAction"
               value="${pageContext.request.contextPath}/controller?command=update_celebrity&celebrityId=${celebrity.id}"
               scope="page"/>
    </c:when>
    <c:otherwise>
        <c:set var="imagePath" value="celebrity-icons/default_celebrity.jpg" scope="page"/>
        <c:set var="formAction" value="${pageContext.request.contextPath}/controller?command=create_celebrity"
               scope="page"/>
    </c:otherwise>
</c:choose>
<html>
<head>
    <title>Update Celebrity</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update.css">
    <link class="jsbin" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css"
          rel="stylesheet" type="text/css"/>
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
</head>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<body>
<div class="container mt-5">
    <div class="row">
        <div class="col-4">
            <img src="${pageContext.request.contextPath}/picture?currentPicture=${imagePath}" id="image"
                 class="img-thumbnail">
        </div>
        <div class="col-4">
            <form method="POST" enctype="multipart/form-data"
                  action="<c:url value="${formAction}"/>">
                <label for="firstNameInput" class="form-label"><fmt:message key="label.firstName"/></label>
                <input type="text"
                       class="form-control"
                       id="firstNameInput"
                       value="${celebrity.firstName}"
                       name="firstName"
                       pattern="^[A-Z][a-z]{1,14}"
                       title="<fmt:message key="validation.firstName"/>"
                       required/>
                <label for="lastNameInput" class="form-label mt-2"><fmt:message key="label.lastName"/></label>
                <input type="text"
                       class="form-control"
                       id="lastNameInput"
                       value="${celebrity.lastName}"
                       name="lastName"
                       pattern="^([A-Z][a-z ,.'-]+)+$"
                       title="<fmt:message key="validation.lastName"/>"
                       required/>
                <label for="input-file" class="form-label mt-2"><fmt:message key="label.profilePicture"/></label>
                <input class="form-control" type="file" onchange="readURL(this)" accept="image/*" name="content"
                       id="input-file">
                <button type="submit" class="submit mt-2"><fmt:message key="button.submit"/></button>
                <input type="hidden" name="previousPage" value="${currentPage}">
            </form>
        </div>
    </div>
</div>
</body>
</html>
<c:remove var="newImage"/>
<script src="${pageContext.request.contextPath}/scripts/uploadFile.js"></script>
