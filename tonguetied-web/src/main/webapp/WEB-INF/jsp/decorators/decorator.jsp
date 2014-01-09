<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://struts-menu.sf.net/tag" prefix="menu" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<fmt:bundle basename="tonguetied">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" media="all" type="text/css" href="<c:url value="/css/tonguetied.css"/>"/>
<link rel="stylesheet" media="all" type="text/css" href="<c:url value="/css/displaytag.css"/>"/>
<link rel="stylesheet" media="screen" type="text/css" href="<c:url value="/css/tabs.css"/>"/>
<!--[if IE]>
<link rel="stylesheet" media="all" type="text/css" href="<c:url value="/css/ie.css"/>"/>
<link rel="shortcut icon" type="image/vnd.microsoft.icon" href="<c:url value="/images/favicon.ico"/>"/>
<![endif]-->
<link rel="icon" type="image/vnd.microsoft.icon" href="<c:url value="/images/favicon.ico"/>"/>
<title><fmt:message key="application.short.name"/></title>
</head>

<body>
    <div class="header-container">
        <div class="left-element">
            <img src="<c:url value="/images/application_logo.png"/>" alt="<fmt:message key="application.short.name"/>" title="<fmt:message key="application.short.name"/>" class="logo"/>
        </div>
        <div class="right-element">
            <form id="langForm" method="get" action="${pageContext.request.requestURL}" class="header">
                <fieldset class="header">
                    <label id="siteLanguageLabel" for="siteLanguage" class="header"><fmt:message key="language"/></label>
                    <fmt:bundle basename="language">
                    <select id="siteLanguage" name="siteLanguage" size="1" onchange="document.getElementById('langForm').submit();">
                        <c:forEach items="${applicationScope.supportedLanguages}" var="langCode">
                        <option value="<c:out value="${langCode}"/>" <c:if test="${langCode == rc.locale}">selected="selected"</c:if>><fmt:message key="${langCode}"/></option>
                        </c:forEach>
                    </select>
                    </fmt:bundle>
                </fieldset>
            </form>
            <form id="logoutForm" method="post" action="<c:url value="j_spring_security_logout"/>" class="header">
                <fieldset class="header">
                    <span class="welcome"><fmt:message key="current.user"/>&nbsp;<security:authentication property="principal.username"/></span>
                    <input type="submit" id="logout" name="logout" value="<fmt:message key="logout"/>" class="button"/>
                </fieldset>
            </form>
        </div>
    </div>
    <menu:useMenuDisplayer name="TabbedMenu" bundle="tonguetied" permissions="rolesAdapter">
        <menu:displayMenu name="TabbedKeywords"/>
        <menu:displayMenu name="TabbedCountries"/>
        <menu:displayMenu name="TabbedLanguages"/>
        <menu:displayMenu name="TabbedBundles"/>
        <menu:displayMenu name="TabbedImportExport"/>
        <menu:displayMenu name="TabbedAccountManagement"/>
        <menu:displayMenu name="TabbedUsers"/>
        <menu:displayMenu name="TabbedAuditLog"/>
    </menu:useMenuDisplayer>
    <div class="tabBody">
    <decorator:body />
    </div>
    <div class="pagefooter">
        <fmt:message key="application.short.name"/> 
        (<fmt:bundle basename="buildNumber"><fmt:message key="build.version"/> <fmt:message key="build.date"/></fmt:bundle>)
    </div>
    <script type="text/javascript" src="<c:url value="/scripts/tabs.js"/>"></script>
</body>
</fmt:bundle>
</html>