<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
    <c:url value="/resetPassword.htm" var="changePasswordUrl" scope="page"/>
    <form:form id="changePasswordForm" method="post" action="${changePasswordUrl}" commandName="changePasswordForm">
        <fieldset>
            <form:hidden path="userId"/>
            <legend><fmt:message key="change.password"/></legend>
            <div>
            </div>
            <div>
                <form:label path="newPassword" cssClass="content"><fmt:message key="new.password"/></form:label>
                <form:password path="newPassword"/>
                <form:errors path="newPassword" cssClass="error"/>
            </div>
            <div>
                <form:label path="newRepeatedPassword" cssClass="content"><fmt:message key="new.repeat.password"/></form:label>
                <form:password path="newRepeatedPassword"/>
                <form:errors path="newRepeatedPassword" cssClass="error"/>
            </div>
        </fieldset>
        <div class="submit">
            <input type="submit" id="save" name="save" value="<fmt:message key="save"/>" class="button"/>
            <input type="submit" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" class="button"/>
            <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
        </div>
    </form:form>
    </div>