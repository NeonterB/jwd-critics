<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/scripts/Notiflix-2.7.0/dist/notiflix-2.7.0.min.css"/>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/scripts/Notiflix-2.7.0/dist/notiflix-2.7.0.min.js"></script>
</head>
<script>
    Notiflix.Notify.Init({
        useGoogleFont: true,
        fontFamily: "Quicksand",
        fontSize: "14px",
        position: "right-bottom",
        closeButton: true,
        borderRadius: "20px",
        failure: {background: "#FF416C",},
    });
</script>
<body>
<c:forEach var="validationError" items="${sessionScope.validationErrors}">
    <script>Notiflix.Notify.Failure('${validationError}')</script>
</c:forEach>
<c:if test="${not empty sessionScope.serviceError}">
    <script>Notiflix.Notify.Failure('${sessionScope.serviceError}')</script>
</c:if>
</body>
</html>
