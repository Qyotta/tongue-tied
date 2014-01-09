<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
    <c:url value="/bundle.htm" var="bundleAction" scope="page"/>
    <form:form id="bundleForm" method="post" action="${bundleAction}" commandName="bundle">
        <fieldset>
            <%-- We need to bind the id as the form is in the request and would be lost when submitted --%>
            <form:hidden path="id"/>
            <legend><fmt:message key="bundle.details"/></legend>
            <div>
                <form:label path="name" cssClass="content"><fmt:message key="bundle.name"/></form:label>
                <form:input path="name"/>
                <form:errors path="name" cssClass="error"/>
            </div>
            <div>
                <form:label path="description" cssClass="content"><fmt:message key="bundle.description"/></form:label>
                <form:input path="description"/>
                <form:errors path="description" cssClass="error"/>
            </div>
            <div>
                <form:label path="resourceName" cssClass="content"><fmt:message key="bundle.resource.name"/></form:label>
                <form:input path="resourceName"/>
                <form:errors path="resourceName" cssClass="error"/>
            </div>
            <div>
                <form:label path="default" cssClass="content"><fmt:message key="bundle.default"/></form:label>
                <form:checkbox path="default" id="default"/>
                <form:errors path="default" cssClass="error"/>
            </div>
            <div>
                <form:label path="global" cssClass="content"><fmt:message key="bundle.global"/></form:label>
                <form:checkbox path="global" id="global"/>
                <form:errors path="global" cssClass="error"/>
            </div>
        </fieldset>
        <div class="submit">
            <input type="submit" id="save" name="save" value="<fmt:message key="save"/>" class="button"/>
            <input type="submit" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" class="button"/>
            <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
        </div>
    </form:form>
    </div>
