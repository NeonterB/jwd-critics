<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<div class="header">
    <a href="${pageContext.request.contextPath}/controller" class="logo"><img src="../../assets/camera-icon.svg"
                                                                              width="68" height="68"></a>
    <form action="<c:url value="/controller"/>" method="POST" class="input-line">
        <input type="hidden" name="command" value="find_movies_by_title"/>
        <input type="text" class="text-input" name="movieToFind"
               placeholder="<fmt:message key="label.search"/>">
        <button class="find-btn"><i class="fa fa-search"></i></button>
    </form>

<%--    <form name="langForm" method="POST" action="<c:url value="/controller"/>">--%>
<%--        <input type="hidden" name="command" value="change_language">--%>
<%--        <input type="hidden" name="page" value="${ sessionScope.page }">--%>
<%--        <select name="lang" onchange="submit()">--%>
<%--            <option value="" <c:if test="${empty lang}">selected</c:if>>English</option>--%>
<%--            <option value="ru" <c:if test="${lang eq 'ru'}">selected</c:if>>Русский</option>--%>
<%--        </select>--%>
<%--    </form>--%>

    <div class="header-right">
        <c:choose>
            <c:when test="${empty sessionScope.userRole}">
                <a class="outlined"
                   href="${pageContext.request.contextPath}/controller?command=open_sign_in"><span><fmt:message
                        key="label.signIn"/></span></a>
            </c:when>
            <c:otherwise>
                <a class="outlined"
                   href="${pageContext.request.contextPath}/controller?command=sign_out"><span><fmt:message
                        key="label.signOut"/></span></a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
