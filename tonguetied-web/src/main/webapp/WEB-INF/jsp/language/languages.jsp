<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

        <display:table name="languages" id="language" sort="page" requestURI="">
            <display:column sortable="true" titleKey="language.name" url="/language.htm" paramId="languageId" paramProperty="id">
                <c:out value="${language.name}"/>
            </display:column>
            <display:column property="code" titleKey="language.code" sortable="true"/>
            <display:column titleKey="action">
                <c:url value="deleteLanguage.htm" var="deleteLanguageUrl" scope="page"><c:param name="languageId" value="${language.id}"/></c:url>
                <fmt:message key="confirm.language.delete" var="confirmDeleteLanguageMsg" scope="page" ><fmt:param value="${language.name}"/></fmt:message>
                <a href="${deleteLanguageUrl}" onclick="return confirm('${fn:escapeXml(confirmDeleteLanguageMsg)}')">
                    <img src="<c:url value="images/delete.png"/>" alt="<fmt:message key="delete"/>" title="<fmt:message key="delete"/>" class="img-link"/>
                </a>
            </display:column>
        </display:table>
    </div>
