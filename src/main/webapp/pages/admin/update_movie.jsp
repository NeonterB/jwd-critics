<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/admin/update_movie.jsp" scope="request"/>
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
    <c:when test="${not empty movie}">
        <c:set var="imagePath" value="${movie.imagePath}" scope="page"/>
        <c:set var="formAction"
               value="${pageContext.request.contextPath}/controller?command=update_movie&movieId=${movie.id}"
               scope="page"/>
    </c:when>
    <c:otherwise>
        <c:set var="imagePath" value="movie-posters/default_movie.png" scope="page"/>
        <c:set var="formAction" value="${pageContext.request.contextPath}/controller?command=create_movie"
               scope="page"/>
    </c:otherwise>
</c:choose>
<html>
<head>
    <title>Update Movie</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update.css">
    <link class="jsbin" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css"
          rel="stylesheet" type="text/css"/>
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script class="jsbin" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/scripts/html-duration-picker.min.js"></script>
</head>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<body>
<div class="container mt-4">
    <div class="row mt-4">
        <div class="col-4">
            <img src="${pageContext.request.contextPath}/picture?currentPicture=${imagePath}"
                 id="user-image" class="img-thumbnail">
        </div>
        <div class="col-4">
            <form method="POST" enctype="multipart/form-data" action="<c:url value="${formAction}"/>">
                <input type="hidden" name="previousPage" value="${currentPage}"/>
                <label for="movieNameInput" class="form-label"><fmt:message key="label.title"/></label>
                <input type="text"
                       class="form-control"
                       id="movieNameInput"
                       value="${movie.name}"
                       name="movieName"
                       pattern="[\w\s.,?!@'#$:;*+-=%]{1,150}"
                       title="<fmt:message key="validation.movieName"/>"
                       required/>
                <label for="releaseDateInput" class="form-label mt-2"><fmt:message key="label.releaseDate"/></label>
                <input type="date"
                       class="form-control"
                       id="releaseDateInput"
                       value="${movie.releaseDate}"
                       name="movieReleaseDate"
                       required/>
                <label for="runtimeInput" class="form-label mt-2"><fmt:message key="label.runtime"/></label>
                <input class="form-control html-duration-picker"
                       data-hide-seconds
                       id="runtimeInput"
                       value="${movie.runtime}"
                       name="movieRuntime"
                       required/>
                <label for="countryInput" class="form-label mt-2"><fmt:message key="label.country"/></label>
                <input type="text"
                       class="form-control"
                       id="countryInput"
                       pattern="[A-Za-z]{3,56}"
                       title="<fmt:message key="validation.country"/>"
                       value="${movie.country}"
                       name="movieCountry"
                       required/>
                <label for="ageRestrictionInput" class="form-label mt-2">
                    <fmt:message key="label.ageRestriction"/></label>
                <input type="text"
                       class="form-control"
                       id="ageRestrictionInput"
                       pattern="[\w_-]{1,10}"
                       title="<fmt:message key="validation.ageRestriction"/>"
                       value="${movie.ageRestriction}"
                       name="movieAgeRestriction"
                       required/>
                <label for="summaryInput" class="form-label mt-2">
                    <fmt:message key="label.summary"/></label>
                <textarea minLength="100"
                          maxLength="10000"
                          class="form-control"
                          id="summaryInput"
                          rows="7"
                          name="movieSummary"
                          required>${movie.summary}</textarea>
                <label for="genresInput" class="form-label mt-2"><fmt:message key="label.genres"/></label>
                <div class="form-group" id="genresInput">
                    <ctg:genres/>
                </div>

                <label for="fileInput" class="form-label mt-2"><fmt:message key="label.movieBanner"/></label>
                <input class="form-control" type="file" onchange="readURL(this)" accept="image/*" name="content"
                       id="fileInput">
                <button type="submit" class="submit mt-2"><fmt:message key="button.submit"/></button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<c:remove var="newImage"/>
<script src="${pageContext.request.contextPath}/scripts/uploadFile.js"></script>