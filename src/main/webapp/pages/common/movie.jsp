<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/common/movie.jsp" scope="request"/>
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
    <title>${movie.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movie.css">
</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="container mt-5">
    <a href="${pageContext.request.contextPath}/controller?command=open_all_movies"><fmt:message
            key="button.toMovies"/></a>
    <div class="row">
        <div class="col-4">
            <img src="${pageContext.request.contextPath}/picture?currentPicture=${movie.imagePath}" alt="${movie.name}"
                 class="img-thumbnail">
        </div>
        <div class="col-4">
            <h4>${movie.name}</h4>
            <strong><fmt:message key="movie.releaseDate"/>:</strong> ${movie.releaseDate}<br>
            <strong><fmt:message key="movie.runtime"/>:</strong> ${movie.runtime}<br>
            <strong><fmt:message key="movie.country"/>:</strong> ${movie.country}<br>
            <strong><fmt:message key="movie.ageRestriction"/>:</strong> ${movie.ageRestriction}<br>
            <strong><fmt:message key="movie.rating"/>:</strong> ${movie.rating}<br>
            <strong><fmt:message key="movie.reviewCount"/>:</strong> ${movie.reviewCount}
            <hr/>
            <c:if test="${not empty movie.staff}">
                <c:forEach var="position" items="${movie.staff}">
                    <div class="row">
                        <div class="col">
                            <strong>${position.key}</strong>:
                            <c:forEach var="celebrity" items="${position.value}">
                                ${celebrity.firstName} ${celebrity.lastName},
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </div>
        <div class="col-4">
            <strong><fmt:message key="movie.summary"/>:</strong> ${movie.summary}
        </div>
    </div>
    <c:choose>
        <c:when test="${empty user}">
            <p class="mt-2"><fmt:message key="text.signInToReview"/></p>
        </c:when>
        <c:when test="${user.status eq 'INACTIVE'}">
            <p class="mt-2"><fmt:message key="text.activateToReview"/></p>
        </c:when>
        <c:otherwise>
            <div class="row mt-4">
                <div class="col-8 review">
                    <c:choose>
                        <c:when test="${not empty userReview}">
                            <c:set var="reviewFormUrl" scope="page" value="/controller?command=update_movie_review"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="reviewFormUrl" scope="page" value="/controller?command=create_movie_review"/>
                        </c:otherwise>
                    </c:choose>
                    <form method="POST" action="<c:url value="${reviewFormUrl}"/>">
                        <input type="hidden" name="userId" value="${user.id}">
                        <input type="hidden" name="movieId" value="${movie.id}">
                        <input type="hidden" name="reviewId" value="${userReview.id}">
                        <input type="hidden" name="previousPage" value="${currentPage}">
                        <div class="mb-3">
                            <label for="scoreRange" class="form-label">
                                <fmt:message key="text.yourScore"/>:
                                <span id="scoreValue">
                                <c:choose>
                                    <c:when test="${not empty userReview}">
                                        ${userReview.score}
                                    </c:when>
                                    <c:otherwise>
                                        50
                                    </c:otherwise>
                                </c:choose>
                                </span>
                            </label>
                            <input type="range" name="movieReviewScore" class="form-range" id="scoreRange" min="0"
                                   max="100"
                                   value="${userReview.score}"
                                   onChange="rangeSlide(this.value)" onmousemove="rangeSlide(this.value)">
                        </div>
                        <div class="mb-3">
                            <label for="reviewTextArea" class="form-label"><fmt:message key="text.yourReview"/></label>
                            <textarea name="movieReviewText" class="form-control" id="reviewTextArea" minlength="100"
                                      maxlength="10000"
                                      rows="7">${userReview.text}</textarea>
                        </div>
                        <button type="submit"><fmt:message key="button.submit"/></button>
                        <c:if test="${not empty userReview}">
                            <a href="${pageContext.request.contextPath}/controller?command=delete_movie_review&movieReviewId=${userReview.id}&previousPage=${currentPage}">
                                <fmt:message key="button.delete"/>
                            </a>
                        </c:if>
                    </form>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    <c:if test="${not empty reviewsOnMoviePage}">
        <div class="container mt-5">
            <c:forEach var="review" items="${reviewsOnMoviePage}">
                <div class="row mt-4">
                    <div class="col-1">
                        <a href="${pageContext.request.contextPath}/controller?command=open_user_profile&userId=${review.userId}">
                            <img class="img-thumbnail"
                                 src="${pageContext.request.contextPath}/picture?currentPicture=${review.imagePath}"
                                 alt="${review.title}">
                        </a>
                    </div>
                    <div class="col">
                        <strong>${review.title}</strong><br>
                        <fmt:message key="review.score"/>: ${review.score}<br>
                            ${review.text}
                    </div>
                    <c:if test="${user.role eq 'ADMIN'}">
                        <div class="col-1">
                            <a href="${pageContext.request.contextPath}/controller?command=delete_movie_review&movieReviewId=${review.id}&previousPage=${currentPage}">
                                <fmt:message key="button.delete"/>
                            </a>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </div>
        <a href="${pageContext.request.contextPath}/controller?command=open_movie_reviews&movieId=${movie.id}">
            <fmt:message key="button.showMore"/>
        </a>
    </c:if>
</div>
</body>
</html>
<script type="text/javascript">
    function rangeSlide(value) {
        document.getElementById('scoreValue').innerHTML = value;
    }
</script>