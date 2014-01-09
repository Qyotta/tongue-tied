<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:bundle basename="tonguetied">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" media="all" type="text/css" href="<c:url value="/css/tonguetied.css"/>"/>
<!--[if IE]>
<link rel="stylesheet" media="all" type="text/css" href="<c:url value="/css/ie.css"/>"/>
<link rel="shortcut icon" type="image/vnd.microsoft.icon" href="<c:url value="/images/favicon.ico"/>"/>
<![endif]-->
<link rel="icon" type="image/vnd.microsoft.icon" href="<c:url value="/images/favicon.ico"/>"/>
<title><fmt:message key="application.short.name"/></title>
</head>
<body>
    <h1><fmt:message key="error.system.error"/></h1>
    <p/>
        <dl id="errorList">
            <dt class="error"><fmt:message key="error.page.message"/></dt>
            <dd>
                <p class="error"><c:out value="${exception}"/></p>
                <code>
                    <c:forEach items="${exception.stackTrace}" var="stackTraceElement">
                    <c:out value="${stackTraceElement}"/>
                    </c:forEach>
                </code>
            </dd>
        </dl>
</body>
</fmt:bundle>
</html>