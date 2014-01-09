<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>

    <security:authentication property="principal.username" var="currentUsername" scope="page"/>
    <div class="content">
        <div id="left">
            <div class="sidepanel">
                <c:url value="/userSearch.htm" var="searchAction" scope="page"/>
                <form:form id="searchParameters" method="post" action="${searchAction}" commandName="userSearch">
                    <fieldset>
                        <legend><fmt:message key="search"/></legend>
                        <div>
                            <form:label path="username" cssClass="sidepanel"><fmt:message key="username"/></form:label>
                            <form:input path="username"/>
                        </div>
                        <div>
                            <form:label path="email" cssClass="sidepanel"><fmt:message key="email"/></form:label>
                            <form:input path="email"/>
                        </div>
                        <div>
                            <form:label path="firstName" cssClass="sidepanel"><fmt:message key="first.name"/></form:label>
                            <form:input path="firstName"/>
                        </div>
                        <div>
                            <form:label path="lastName" cssClass="sidepanel"><fmt:message key="last.name"/></form:label>
                            <form:input path="lastName"/>
                        </div>
                    </fieldset>
                    <div class="submit">
                        <input type="submit" id="searchBtn" name="searchBtn" value="<fmt:message key="search"/>" class="button"/>
                        <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
                    </div>
                </form:form>
            </div>
        </div>
        
        <div class="contentPanel">
            <a href="<c:url value="users.htm"><c:param name="showAllUsers" value="true"/></c:url>" title="<fmt:message key="get.all.users"/>"><fmt:message key="all.users"/></a>
            <display:table name="users" htmlId="userTable" id="user" sort="external" pagesize="${userSize}" partialList="true" size="maxListSize" requestURI="" keepStatus="true">
                <display:column sortable="true" titleKey="username">
                    <c:choose>
                        <c:when test="${currentUsername != user.username}">
                            <c:url value="/user.htm" var="userUrl"><c:param name="id" value="${user.id}"/></c:url>
                        </c:when>
                        <c:otherwise>
                            <c:url value="/readOnlyUser.htm" var="userUrl"><c:param name="id" value="${user.id}"/></c:url>
                        </c:otherwise>
                    </c:choose>
                    <a href="${userUrl}">
                    <img src="<c:url value="/images/user.png"/>" alt="" title="${user.username}" class="img-link"/>
                    <c:out value="${user.username}"/>
                    </a>
                </display:column>
                <display:column titleKey="roles" sortable="false">
                    <c:forEach items="${user.userRights}" var="userRight" varStatus="index">
                    <p><fmt:message key="${userRight.permission}"/></p>
                    </c:forEach>
                </display:column>
                <display:column titleKey="account.enabled" sortable="false">
                    <fmt:message key="enabled.${user.enabled}"/>
                </display:column>
            </display:table>
        </div>
    </div>
