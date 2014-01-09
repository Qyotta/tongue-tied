<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
    <c:url value="/language.htm" var="languageUrl" scope="page"/>
    <form:form id="languageForm" method="post" action="${languageUrl}" commandName="language">
        <fieldset>
            <%-- We need to bind the id as the form is in the request and would be lost when submitted --%>
            <form:hidden path="id"/>
            <legend><fmt:message key="language.details"/></legend>
            <div>
                <form:label path="code" cssClass="content"><fmt:message key="language.code"/></form:label>
                <form:select path="code" size="1">
                    <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                    <form:options items="${languageCodes}"/>
                </form:select>
                <form:errors path="code" cssClass="error"/>
            </div>
            <div>
                <form:label path="name" cssClass="content"><fmt:message key="language.name"/></form:label>
                <form:input path="name"/>
                <form:errors path="name" cssClass="error"/>
            </div>
        </fieldset>
        <div class="submit">
            <input type="submit" id="save" name="save" value="<fmt:message key="save"/>" class="button"/>
            <input type="submit" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" class="button"/>
            <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
        </div>
    </form:form>
    </div>
