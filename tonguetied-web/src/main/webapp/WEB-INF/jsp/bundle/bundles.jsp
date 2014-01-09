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

        <display:table name="bundles" id="bundle" sort="page" requestURI="">
            <display:column sortable="true" titleKey="bundle.name" url="/bundle.htm" paramId="bundleId" paramProperty="id">
                <c:out value="${bundle.name}"/>
                <c:if test="${bundle.default}">
                <img src="<c:url value="/images/asterisk_yellow.png"/>" alt="*" title="<fmt:message key="bundle.default"/>" class="img-link"/>
                </c:if>
            </display:column>
            <display:column property="resourceName" titleKey="bundle.resource.name" sortable="true"/>
            <display:column titleKey="action">
                <c:url value="deleteBundle.htm" var="deleteBundleUrl" scope="page"><c:param name="bundleId" value="${bundle.id}"/></c:url>
                <fmt:message key="confirm.bundle.delete" var="confirmDeleteBundleMsg" scope="page" ><fmt:param value="${bundle.name}"/></fmt:message>
                <a href="${deleteBundleUrl}" onclick="return confirm('${fn:escapeXml(confirmDeleteBundleMsg)}')">
                    <img src="<c:url value="images/delete.png"/>" alt="<fmt:message key="delete"/>" title="<fmt:message key="delete"/>" class="img-link"/>
                </a>
            </display:column>
        </display:table>
    </div>
