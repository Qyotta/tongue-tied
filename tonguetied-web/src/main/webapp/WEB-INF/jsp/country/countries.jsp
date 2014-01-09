<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>

    <div class="content listing">
        <c:if test="${not empty error}">
        <h1><img src="<c:url value="/images/error.png"/>" alt="" title="<fmt:message key="warning"/>"/>&nbsp;<fmt:message key="warning"/></h1>
        <div>
            <dl id="errorList">
                <dt class="error">
                    <fmt:message key="error.referenced.translation">
                        <fmt:param value="${error.referencedObjectName}"/>
                        <fmt:param value="${error.totalReferences}"/>
                    </fmt:message>
                </dt>
                <dd>
                    <p>
                    <fmt:message key="error.remove.referenced.translation.solution">
                        <fmt:param value="${error.referencedObjectName}"/>
                    </fmt:message>
                    </p>
                </dd>
            </dl>
        </div>
        </c:if>

        <display:table name="countries" id="country" sort="page" requestURI="">
            <display:column>
                <c:if test="${country.code != \"DEFAULT\"}">
                <img src="<c:url value="/images/flags/${fn:toLowerCase(country.code)}.png"/>" alt="${country.name}" title="${country.name}"/>
                </c:if>
            </display:column>
            <display:column sortable="true" titleKey="country.name" url="/country.htm" paramId="countryId" paramProperty="id">
                <c:out value="${country.name}"/>
            </display:column>
            <display:column property="code" titleKey="country.code" sortable="true"/>
            <display:column titleKey="action">
                <c:url value="deleteCountry.htm" var="deleteCountryUrl" scope="page"><c:param name="countryId" value="${country.id}"/></c:url>
                <fmt:message key="confirm.country.delete" var="confirmDeleteCountryMsg" scope="page" ><fmt:param value="${country.name}"/></fmt:message>
                <a href="${deleteCountryUrl}" onclick="return confirm('${fn:escapeXml(confirmDeleteCountryMsg)}')">
                    <img src="<c:url value="images/delete.png"/>" alt="<fmt:message key="delete"/>" title="<fmt:message key="delete"/>" class="img-link"/>
                </a>
            </display:column>
        </display:table>
    </div>
