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
                 id="image" class="img-thumbnail">
        </div>
        <div class="col-8">
            <form method="POST" enctype="multipart/form-data" action="<c:url value="${formAction}"/>">
                <div class="row">
                    <div class="col-5">
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
                        <div class="row">
                            <div class="col-8">
                                <label for="releaseDateInput" class="form-label mt-2"><fmt:message
                                        key="label.releaseDate"/></label>
                                <input type="date"
                                       class="form-control"
                                       id="releaseDateInput"
                                       value="${movie.releaseDate}"
                                       name="movieReleaseDate"
                                       required/>
                            </div>
                            <div class="col-4">
                                <label for="runtimeInput" class="form-label mt-2"><fmt:message
                                        key="label.runtime"/></label>
                                <input class="form-control html-duration-picker"
                                       data-hide-seconds
                                       id="runtimeInput"
                                       value="<ctg:duration>${movie.runtime}</ctg:duration>"
                                       name="movieRuntime"
                                       required/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">
                                <label for="countryInput" class="form-label mt-2"><fmt:message
                                        key="label.country"/></label>
                                <input type="text"
                                       class="form-control"
                                       id="countryInput"
                                       pattern="[A-Za-z]{3,56}"
                                       title="<fmt:message key="validation.country"/>"
                                       value="${movie.country}"
                                       name="movieCountry"
                                       required/>
                            </div>
                        </div>

                        <label for="summaryInput" class="form-label mt-2">
                            <fmt:message key="label.summary"/></label>
                        <textarea minLength="100"
                                  maxLength="10000"
                                  class="form-control"
                                  id="summaryInput"
                                  rows="7"
                                  name="movieSummary"
                                  required>${movie.summary}</textarea>
                        <label for="fileInput" class="form-label mt-2"><fmt:message key="label.movieBanner"/></label>
                        <input class="form-control" type="file" onchange="readURL(this)" accept="image/*" name="content"
                               id="fileInput">
                        <button type="submit" class="submit mt-2"><fmt:message key="button.submit"/></button>
                    </div>
                    <div class="col-3">
                        <label for="genresInput" class="form-label mt-2"><fmt:message key="label.genres"/></label>
                        <div class="form-group" id="genresInput">
                            <ctg:genres/>
                        </div>
                    </div>
                    <div class="col-3">
                        <label for="restrictionInput" class="form-label mt-2"><fmt:message
                                key="label.ageRestriction"/></label>
                        <div class="form-group" id="restrictionInput">
                            <ctg:ageRestrictions/>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <c:if test="${not empty movie}">
        <div class="row mt-4">
            <div class="col-6">
                <c:forEach var="position" items="${movie.staff}">
                    <strong>${position.key}: </strong>
                    <c:forEach var="celebrity" items="${position.value}">
                        <a class="dark-link"
                           href="${pageContext.request.contextPath}/controller?command=remove_celebrity_from_position&movieId=${movie.id}&celebrityId=${celebrity.id}&positionId=${position.key.id}">
                                ${celebrity.firstName} ${celebrity.lastName},
                        </a>
                    </c:forEach><br>
                </c:forEach>
            </div>
            <div class="col-6">
                <form method="post" action="<c:url value="/controller?command=assign_celebrity_on_position"/>">
                    <input type="hidden" name="movieId" value="${movie.id}"/>
                    <label for="celebrityFirstNameInput" class="form-label mt-2"><fmt:message
                            key="label.firstName"/></label>
                    <input type="text"
                           class="form-control"
                           id="celebrityFirstNameInput"
                           pattern="^[A-Z][a-z]{1,14}"
                           title="<fmt:message key="validation.firstName"/>"
                           name="firstName"
                           required/>
                    <label for="celebrityLastNameInput" class="form-label mt-2"><fmt:message
                            key="label.lastName"/></label>
                    <input type="text"
                           class="form-control"
                           id="celebrityLastNameInput"
                           pattern="^[A-Z][a-z]{1,14}"
                           title="<fmt:message key="validation.lastName"/>"
                           name="lastName"
                           required/>
                    <ctg:positions/>
                    <button type="submit" class="submit mt-2"><fmt:message key="button.submit"/></button>
                </form>
            </div>
        </div>
    </c:if>

</div>
</body>
</html>
<c:remove var="newImage"/>
<script src="${pageContext.request.contextPath}/scripts/uploadFile.js"></script>