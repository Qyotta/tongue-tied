<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
        <div class="dir-breadcrumbs">
            <p><fmt:message key="listing.for"/></p>
            <span>
        <c:set value="" var="urlString" scope="page"/>
        <c:forEach items="${parents}" var="parent">
            <c:set value="${urlString}/${parent}" var="urlString" scope="page"/>
            <c:url value="${urlString}${suffix}" var="parentUrl" scope="page"/>
            /<a href="${parentUrl}" title="<fmt:message key="go.to"><fmt:param value="${parent}"/></fmt:message>">${parent}</a>
        </c:forEach>
            /${baseDir.name}
            </span>
        </div>
        <div class="dir-listing">
            <ul>
            <c:if test="${parentUrl ne null}">
                <li class="up-one-level"><a href="${parentUrl}" title="<fmt:message key="go.up.one.level"/>" class="file-link">..</a></li>
            </c:if>
            <c:forEach items="${baseDir.files}" var="child">
                <c:choose>
                    <c:when test="${child.directory}">
                        <c:url value="${baseDir.name}/${child.name}${suffix}" var="fileUrl"/>
                        <fmt:message key="go.to" var="itemTitle" scope="page">
                            <fmt:param value="${child.name}"/>
                        </fmt:message>
                    </c:when>
                    <c:otherwise>
                        <c:url value="${baseDir.name}/${child.name}" var="fileUrl"/>
                        <fmt:message key="download.file" var="itemTitle" scope="page"/>
                    </c:otherwise>
                </c:choose>
                <li class="file-item">
                    <a href="${fileUrl}" title="${itemTitle}" class="file-link">${child.name}</a>
                    <span class="file-attributes">
                        <c:if test="${child.file}">
                            <c:choose>
                                <c:when test="${child.size < 1000}">
                            <c:out value="${child.size}"/><fmt:message key="byte.suffix"/>&nbsp
                                </c:when>
                                <c:otherwise>
                            <fmt:formatNumber type="number" value="${child.size div 1000}" maxFractionDigits="1" /><fmt:message key="kilo.byte.suffix"/>&nbsp
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <fmt:formatDate value="${child.lastModifiedDate}" pattern="EEE dd MMM yyyy hh:mm a"/>
                    </span>
                </li>
            </c:forEach>
            </ul>
        </div>
    </div>
