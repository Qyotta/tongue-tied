<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
    <c:url value="/user.htm" var="userUrl" scope="page"/>
    <form:form id="userForm" method="post" action="${userUrl}" commandName="userForm">
        <%-- We need to bind the id as the form is in the request and would be lost when submitted --%>
        <fieldset>
            <form:hidden path="user.id"/>
            <form:hidden path="user.version"/>
            <legend><fmt:message key="user.details"/></legend>
            <div>
                <form:label path="user.username" cssClass="content"><fmt:message key="username"/></form:label>
                <form:input path="user.username"/>
                <form:errors path="user.username" cssClass="error"/>
            </div>
            <c:choose>
                <c:when test="${userForm.user.id eq null}">
                    <div>
                        <form:label path="user.password" cssClass="content"><fmt:message key="password"/></form:label>
                        <form:password path="user.password"/>
                        <form:errors path="user.password" cssClass="error"/>
                    </div>
                    <div>
                        <form:label path="user.repeatedPassword" cssClass="content"><fmt:message key="repeated.password"/></form:label>
                        <form:password path="user.repeatedPassword"/>
                        <form:errors path="user.repeatedPassword" cssClass="error"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <div>
                        <span class="contentLabel"><fmt:message key="password"/></span>
                        <a href="<c:url value="resetPassword.htm"><c:param name="userId" value="${userForm.user.id}"/></c:url>"><fmt:message key="change.password"/></a>
                        <form:hidden path="user.password"/>
                    </div>
                </c:otherwise>
            </c:choose>
            <div>
                <form:label path="user.firstName" cssClass="content"><fmt:message key="first.name"/></form:label>
                <form:input path="user.firstName"/>
                <form:errors path="user.firstName" cssClass="error"/>
            </div>
            <div>
                <form:label path="user.lastName" cssClass="content"><fmt:message key="last.name"/></form:label>
                <form:input path="user.lastName"/>
                <form:errors path="user.lastName" cssClass="error"/>
            </div>
            <div>
                <form:label path="user.email" cssClass="content"><fmt:message key="email"/></form:label>
                <form:input path="user.email"/>
                <form:errors path="user.email" cssClass="error"/>
            </div>
            <div>
                <form:label path="user.enabled" cssClass="content"><fmt:message key="is.enabled"/></form:label>
                <form:checkbox path="user.enabled" id="enabled"/>
            </div>
        </fieldset>
        <fieldset>
            <legend><fmt:message key="authorities"/></legend>
            <c:forEach items="${permissionsMap}" var="permission" varStatus="permissionIndex">
                <div class="checkbox">
                    <form:checkbox path="permissions[${permission.key}]" id="permission${permissionIndex.index}"/>
                    <form:label for="permission${permissionIndex.index}" path="permissions[${permission.key}]"><fmt:message key="${permission.key}"/></form:label>
                </div>
            </c:forEach>
        </fieldset>
        <div class="submit">
            <input type="submit" id="save" name="save" value="<fmt:message key="save"/>" class="button"/>
            <input type="submit" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" class="button"/>
            <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
        </div>
    </form:form>
    </div>
