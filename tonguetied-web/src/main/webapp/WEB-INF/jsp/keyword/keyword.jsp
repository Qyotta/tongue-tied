<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

    <div class="content">
    <c:url value="/keyword.htm" var="keywordAction" scope="page"/>
    <form:form id="keywordForm" method="post" action="${keywordAction}" commandName="keyword">
        <%-- We need to bind the id as the form is in the request and would be lost when submitted --%>
        <fmt:message key="confirm.keyword.delete" var="confirmDeleteKeywordMsg" scope="page">
            <fmt:param value="${keyword.keyword}"/>
        </fmt:message>
        <fieldset>
            <form:hidden path="id"/>
            <form:hidden path="version"/>
            <legend><fmt:message key="keyword.details"/></legend>
            <div>
                <form:label path="keyword" cssClass="content"><fmt:message key="keyword.id"/></form:label>
                <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                <form:input path="keyword" cssClass="keyword-entry"/>
                <form:errors path="keyword" cssClass="error"/>
                </security:authorize>
                <security:authorize ifNotGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                ${keyword.keyword}
                </security:authorize>
            </div>
            <div>
                <form:label path="context" cssClass="content"><fmt:message key="keyword.context"/></form:label>
                <form:textarea path="context" rows="2" cols="100%"/>
                <form:errors path="context" cssClass="error"/>
            </div>
            <form:errors path="translations" cssClass="error"/>
            <c:choose>
                <c:when test="${empty keyword.translations && sessionScope.applyFilter}">
                <div><p><fmt:message key="translation.filtered.sublist.msg.empty.list"/></p></div>
                </c:when>
                <c:otherwise>
            <c:if test="${sessionScope.applyFilter}">
            </c:if>
            <table>
                <caption>
                <c:choose>
                    <c:when test="${sessionScope.applyFilter}">
                <img src="<c:url value="images/information.png"/>" alt="<fmt:message key="information"/>" title="<fmt:message key="information"/>"/>
                <fmt:message key="translation.filter.on"/>
                    </c:when>
                    <c:otherwise>
                <fmt:message key="translations"/>
                    </c:otherwise>
                </c:choose>
                </caption>
                <colgroup>
                    <col class="action"/>
                    <col class="language"/>
                    <col class="country"/>
                    <col class="bundle"/>
                    <col class="translation"/>
                    <col class="state"/>
                </colgroup>
                <thead>
                    <tr>
                        <th><fmt:message key="action"/></th>
                        <th><fmt:message key="language"/></th>
                        <th><fmt:message key="country"/></th>
                        <th><fmt:message key="bundle"/></th>
                        <th><fmt:message key="translation"/></th>
                        <th><fmt:message key="state"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${keyword.translations}" var="translation" varStatus="index">
                    <c:choose>
                        <c:when test="${index.count % 2 == 0}">
                    <tr class="even">
                        </c:when>
                        <c:otherwise>
                    <tr class="odd">
                        </c:otherwise>
                    </c:choose>
                        <td>
                            <form:hidden path="translations[${index.index}].id" id="translation${index.index}.id"/>
                            <form:hidden path="translations[${index.index}].version" id="translation${index.index}.version"/>
                            <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            <c:choose>
                                <c:when test="${not empty translation.id}">
                                    <c:set var="translationId" value="${translation.id}" scope="page"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="translationId" value="${index.index}" scope="page"/>
                                </c:otherwise>
                            </c:choose>
                            <fmt:message key="confirm.translation.delete" var="confirmDeleteTranslationMsg" scope="page" >
                                <fmt:param value="${translation.language.name}"/>
                                <fmt:param value="${translation.country.name}"/>
                                <fmt:param value="${translation.bundle.name}"/>
                            </fmt:message>
                            <c:url value="keyword.htm" var="deleteTranslationUrl" scope="page"><c:param name="deleteTranslation" value="${translationId}"/></c:url>
                            <a href="${deleteTranslationUrl}" onclick="return confirm('${fn:escapeXml(confirmDeleteTranslationMsg)}')">
                                <img src="<c:url value="images/delete.png"/>" alt="<fmt:message key="delete"/>" title="<fmt:message key="delete"/>" class="img-link"/>
                            </a>
                            </security:authorize>
                        </td>
                        <td>
                            <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            <form:select path="translations[${index.index}].language" id="translation${index.index}.language" size="1">
                                <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                                <form:options items="${languages}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            </security:authorize>
                            <security:authorize ifNotGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            ${translation.language.name}
                            </security:authorize>
                        </td>
                        <td>
                            <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            <form:select path="translations[${index.index}].country" id="translation${index.index}.country" size="1">
                                <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                                <form:options items="${countries}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            </security:authorize>
                            <security:authorize ifNotGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            ${translation.country.name}
                            </security:authorize>
                        </td>
                        <td>
                            <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            <form:select path="translations[${index.index}].bundle" id="translation${index.index}.bundle" size="1">
                                <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                                <form:options items="${bundles}" itemValue="id" itemLabel="name"/>
                            </form:select>
                            </security:authorize>
                            <security:authorize ifNotGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            ${translation.bundle.name}
                            </security:authorize>
                        </td>
                        <td>
                            <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            <form:textarea path="translations[${index.index}].value" id="translation${index.index}.value" rows="4" cols="100%"/>
                            </security:authorize>
                            <security:authorize ifNotGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
                            <c:out value="${translation.value}" escapeXml="true"/>
                            </security:authorize>
                        </td>
                        <td>
                            <security:authorize ifAnyGranted="ROLE_VERIFIER">
                            <form:select path="translations[${index.index}].state" id="translation${index.index}.state" size="1">
                                <form:option value=""><fmt:message key="please.select"/></form:option>
                                <c:forEach items="${states}" var="state">
                                    <c:if test="${translation.state == state}">
                                        <c:set var="selected" value="true"/>
                                    </c:if>
                                    <form:option value="${state}"><fmt:message key="${state}"/></form:option>
                                    <c:remove var="selected"/>
                                </c:forEach>
                            </form:select>
                            </security:authorize>
                            <security:authorize ifNotGranted="ROLE_VERIFIER">
                            <fmt:message key="${translation.state}"/>
                            </security:authorize>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
                </c:otherwise>
            </c:choose>
        </fieldset>
        <div class="submit">
            <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
            <input type="submit" id="add" name="add" value="<fmt:message key="add.translation"/>" class="button"/>
            </security:authorize>
            <input type="submit" id="save" name="save" value="<fmt:message key="save"/>" class="button"/>
            <input type="submit" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" class="button"/>
            <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
            <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN,ROLE_DEV">
            <input type="submit" id="delete" name="delete" value="<fmt:message key="remove.keyword"/>" class="button" onclick="return confirm('${fn:escapeXml(confirmDeleteKeywordMsg)}')"/>
            </security:authorize>
        </div>
    </form:form>
    </div>
