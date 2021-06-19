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
        borderRadius: "5px",
        failure: {background: "#FF416C",},
    });

    Notiflix.Report.Init({
        fontFamily: "Quicksand",
        useGoogleFont: true,
        backgroundColor: "#ffffff",
        failure: {svgColor: "#FF416C", titleColor: "#FF416C", messageColor: "#FF416C", buttonBackground: "#FF416C",},
    });
</script>
<body>
<c:forEach var="validationError" items="${validationErrors}">
    <script>Notiflix.Notify.Failure('${validationError}')</script>
</c:forEach>
<c:if test="${not empty serviceError}">
    <script>Notiflix.Notify.Failure('${serviceError}')</script>
</c:if>
<c:if test="${not empty globalError}">
    <script>Notiflix.Report.Failure('${globalError}')</script>
</c:if>
</body>
</html>
<c:remove var="serviceError"/>
<c:remove var="validationErrors"/>
<c:remove var="globalError"/>