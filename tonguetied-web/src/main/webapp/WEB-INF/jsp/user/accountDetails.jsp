<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

    <div class="content">
    <c:if test="${!empty actionMessage}">
    <h4 class="message">
        <img alt="" src="<c:url value="images/tick.png"/>"/>&nbsp;<fmt:message key="${actionMessage}"/>
    </h4>
    </c:if>
    <c:url value="/accountDetails.htm" var="accountUrl" scope="page"/>
    <form:form id="userForm" method="post" action="${accountUrl}" commandName="user">
        <fieldset>
            <legend><fmt:message key="user.details"/></legend>
            <div>
                <label id="username" class="content"><fmt:message key="username"/></label>
                <security:authentication property="principal.username"/>
            </div>
            <div>
            </div>
            <div>
                <form:label path="firstName" cssClass="content"><fmt:message key="first.name"/></form:label>
                <form:input path="firstName"/>
                <form:errors path="firstName" cssClass="error"/>
            </div>
            <div>
                <form:label path="lastName" cssClass="content"><fmt:message key="last.name"/></form:label>
                <form:input path="lastName"/>
                <form:errors path="lastName" cssClass="error"/>
            </div>
            <div>
                <form:label path="email" cssClass="content"><fmt:message key="email"/></form:label>
                <form:input path="email"/>
                <form:errors path="email" cssClass="error"/>
            </div>
        </fieldset>
        <div class="submit">
            <input type="submit" id="save" name="save" value="<fmt:message key="save"/>" class="button"/>
            <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
        </div>
    </form:form>
    </div>
