<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
    <c:url value="/user.htm" var="userUrl" scope="page"/>
    <form:form id="userForm" method="post" action="${userUrl}" commandName="userForm">
        <%-- We need to bind the id as the form is in the request and would be lost when submitted --%>
        <fieldset>
            <legend><fmt:message key="user.details"/></legend>
            <div>
                <span class="contentLabel"><fmt:message key="username"/>:</span>
                <span class="contentValue"><c:out value="${userForm.user.username}"/></span>
            </div>
            <div>
                <span class="contentLabel"><fmt:message key="first.name"/>:</span>
                <span class="contentValue"><c:out value="${userForm.user.firstName}"/></span>
            </div>
            <div>
                <span class="contentLabel"><fmt:message key="last.name"/>:</span>
                <span class="contentValue">${userForm.user.lastName}</span>
            </div>
            <div>
                <span class="contentLabel"><fmt:message key="email"/>:</span>
                <span class="contentValue">${userForm.user.email}</span>
            </div>
            <div>
                <span class="contentLabel"><fmt:message key="account.enabled"/>:</span>
                <span class="contentValue"><fmt:message key="enabled.${userForm.user.enabled}"/></span>
            </div>
        </fieldset>
        <fieldset>
            <legend><fmt:message key="authorities"/></legend>
            <c:forEach items="${userForm.user.userRights}" var="userRight" varStatus="index">
            <div>
                <fmt:message key="user.role"/>&nbsp;:&nbsp;<fmt:message key="${userRight.permission}"/>
            </div>
            </c:forEach>
        </fieldset>
        <div class="submit">
            <input type="submit" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" class="button"/>
        </div>
    </form:form>
    </div>
