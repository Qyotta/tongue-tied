
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>

<div class="content">
    <div id="left">
        <fmt:message key="search" var="searchKey" scope="page"/>
        <div class="side-panel-header-bar">
            <div>${searchKey}</div>
            <a id="showSearchParamPanel" href="#" class="hidden">
                <img src="<c:url value="images/bullet_toggle_plus.png"/>" alt="<fmt:message key="show.panel"><fmt:param value="${searchKey}"/></fmt:message>" title="<fmt:message key="show.panel"><fmt:param value="${searchKey}"/></fmt:message>" class="img-link"/>
            </a>
            <a id="hideSearchParamPanel" href="#">
                <img src="<c:url value="images/bullet_toggle_minus.png"/>" alt="<fmt:message key="hide.panel"><fmt:param value="${searchKey}"/></fmt:message>" title="<fmt:message key="hide.panel"><fmt:param value="${searchKey}"/></fmt:message>" class="img-link"/>
            </a>
        </div>
        <div id="searchParamPanel" class="sidepanel">
            <c:url value="/keywordSearch.htm" var="searchAction" scope="page"/>
            <form:form id="searchParameters" method="post" action="${searchAction}" commandName="searchParameters">
                <fieldset>
                    <div>
                        <form:label path="keywordKey" cssClass="sidepanel"><fmt:message key="keyword"/></form:label>
                        <form:input path="keywordKey"/>
                    </div>
                    <div>
                        <form:label path="bundle" cssClass="sidepanel"><fmt:message key="bundle"/></form:label>
                        <form:select path="bundle" size="1" cssClass="sidepanel">
                            <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                            <form:options items="${bundles}" itemValue="id" itemLabel="name"/>
                        </form:select>
                    </div>
                    <div>
                        <form:label path="country" cssClass="sidepanel"><fmt:message key="country"/></form:label>
                        <form:select path="country" size="1" cssClass="sidepanel">
                            <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                            <form:options items="${countries}" itemValue="id" itemLabel="name"/>
                        </form:select>
                    </div>
                    <div>
                        <form:label path="language" cssClass="sidepanel"><fmt:message key="language"/></form:label>
                        <form:select path="language" size="1" cssClass="sidepanel">
                            <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                            <form:options items="${languages}" itemValue="id" itemLabel="name"/>
                        </form:select>
                    </div>
                    <div>
                        <form:label path="translationState" cssClass="sidepanel"><fmt:message key="translation.state"/></form:label>
                        <form:select path="translationState" size="1" cssClass="sidepanel">
                            <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                            <c:forEach items="${states}" var="state">
                                <c:if test="${translationState == state}">
                                    <c:set var="selected" value="true"/>
                                </c:if>
                                <form:option value="${state}"><fmt:message key="${state}"/></form:option>
                                <c:remove var="selected"/>
                            </c:forEach>
                        </form:select>
                    </div>
                    <div>
                        <form:label path="translatedText" cssClass="sidepanel"><fmt:message key="translated.text"/></form:label>
                        <form:input path="translatedText"/>
                    </div>
                    <div class="checkbox">
                        <form:checkbox path="ignoreCase" id="ignoreCase"/>
                        <form:label path="ignoreCase"><fmt:message key="ignore.case"/></form:label>
                    </div>
                </fieldset>
                <div class="submit">
                    <input type="submit" id="searchBtn" name="searchBtn" value="<fmt:message key="search"/>" class="button"/>
                    <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
                </div>
            </form:form>
        </div>
        <fmt:message key="preferences" var="preferencesKey" scope="page"/>
        <div class="side-panel-header-bar">
            <div>${preferencesKey}</div>
            <a id="showPreferencesPanel" href="#">
                <img src="<c:url value="images/bullet_toggle_plus.png"/>" alt="<fmt:message key="show.panel"><fmt:param value="${preferencesKey}"/></fmt:message></img>" title="<fmt:message key="show.panel"><fmt:param value="${preferencesKey}"/></fmt:message>" class="img-link"/>
            </a>
            <a id="hidePreferencesPanel" href="#">
                <img src="<c:url value="images/bullet_toggle_minus.png"/>" alt="<fmt:message key="hide.panel"><fmt:param value="${preferencesKey}"/></fmt:message>" title="<fmt:message key="hide.panel"><fmt:param value="${preferencesKey}"/></fmt:message>" class="img-link"/>
            </a>
        </div>
        <div id="preferencesPanel" class="sidepanel">
            <form id="viewPreferences" method="post" action="<c:url value="/preferences.htm"/>">
                <fieldset>
                    <div>
                        <spring:bind path="viewPreferences.maxResults">
                        <label for="${status.expression}"><fmt:message key="max.results"/></label>
                        <select name="<c:out value="${status.expression}"/>" id="<c:out value="${status.expression}"/>" size="1">
                        <c:forEach items="${pageSizes}" var="size">
                            <option <c:if test="${size == status.value}">selected="selected"</c:if> value="${size}">${size}</option>
                        </c:forEach>
                        </select>
                        </spring:bind>
                    </div>
                    <h4 class="preference"><fmt:message key="translation.view.filter"/></h4>
                    <div id="languages">
                        <h3><fmt:message key="languages"/></h3>
                        <spring:bind path="viewPreferences.selectedLanguages">
                            <c:forEach items="${languages}" var="language" varStatus="langIndex">
                                <c:forEach items="${status.value}" var="currentLanguage">
                                   <c:if test="${currentLanguage.id == language.id}">
                                       <c:set var="selected" value="true"/>
                                   </c:if>
                               </c:forEach>
                        <div class="checkbox">
                            <input type="checkbox" id="${status.expression}${langIndex.index}" name="${status.expression}" value="${language.id}" <c:if test="${selected}">checked="checked"</c:if>/>
                            <input type="hidden" name="_${status.expression}"/>
                            <label for="${status.expression}${langIndex.index}"><c:out value="${language.name}"/></label>
                        </div>
                                <c:remove var="selected"/>
                            </c:forEach>
                        </spring:bind>
                    </div>
                    <div id="countries">
                        <h3><fmt:message key="countries"/></h3>
                        <spring:bind path="viewPreferences.selectedCountries">
                            <c:forEach items="${countries}" var="country" varStatus="countryIndex">
                                <c:forEach items="${status.value}" var="currentCountry">
                                   <c:if test="${currentCountry.id == country.id}">
                                       <c:set var="selected" value="true"/>
                                   </c:if>
                               </c:forEach>
                        <div class="checkbox">
                            <input type="checkbox" id="${status.expression}${countryIndex.index}" name="${status.expression}" value="${country.id}" <c:if test="${selected}">checked="checked"</c:if>/>
                            <input type="hidden" name="_${status.expression}"/>
                            <label for="${status.expression}${countryIndex.index}">
                            <c:if test="${not empty country.code and country.code != \"DEFAULT\"}">
                            <img src="<c:url value="/images/flags/${fn:toLowerCase(country.code)}.png"/>" alt="" title="${country.name}"/>
                            </c:if>
                            <c:out value="${country.name}"/>
                            </label>
                        </div>
                                <c:remove var="selected"/>
                            </c:forEach>
                        </spring:bind>
                    </div>
                    <div id="bundles">
                        <h3><fmt:message key="bundles"/></h3>
                        <spring:bind path="viewPreferences.selectedBundles">
                            <c:forEach items="${bundles}" var="bundle" varStatus="bundleIndex">
                                <c:forEach items="${status.value}" var="currentBundle">
                                   <c:if test="${currentBundle.id == bundle.id}">
                                       <c:set var="selected" value="true"/>
                                   </c:if>
                               </c:forEach>
                        <div class="checkbox">
                            <input type="checkbox" id="${status.expression}${bundleIndex.index}" name="${status.expression}" value="${bundle.id}" <c:if test="${selected}">checked="checked"</c:if>/>
                            <input type="hidden" name="_${status.expression}"/>
                            <label for="${status.expression}${bundleIndex.index}"><c:out value="${bundle.name}"/></label>
                        </div>
                                <c:remove var="selected"/>
                            </c:forEach>
                        </spring:bind>
                    </div>
                </fieldset>
                <div class="submit">
                    <input type="submit" id="submitBtn" name="submitBtn" value="<fmt:message key="submit"/>" class="button"/>
                </div>
            </form>
        </div>
    </div>

    <div class="contentPanel">
        <a href="<c:url value="keywords.htm"><c:param name="showAllKeywords" value="true"/></c:url>" title="<fmt:message key="get.all.keywords"/>"><fmt:message key="all.keywords"/></a>
        <display:table name="keywords" htmlId="keywordsTable" id="keyword" sort="external" pagesize="${viewPreferences.maxResults}" partialList="true" size="maxListSize" requestURI="">
            <display:column titleKey="action" group="1" class="actions">
                <c:url value="deleteKeyword.htm" var="deleteKeywordUrl" scope="page"><c:param name="keywordId" value="${keyword.id}"/></c:url>
                <fmt:message key="confirm.keyword.delete" var="confirmDeleteKeywordMsg" scope="page" ><fmt:param value="${keyword.keyword}"/></fmt:message>
                <a href="${deleteKeywordUrl}" onclick="return confirm('${fn:escapeXml(confirmDeleteKeywordMsg)}')">
                    <img src="<c:url value="images/delete.png"/>" alt="<fmt:message key="delete"/>" title="<fmt:message key="delete"/>" class="img-link"/>
                </a>
            </display:column>
            <display:column titleKey="keyword" group="2" class="keyword" sortable="true">
                <a href="<c:url value="keyword.htm"><c:param name="keywordId" value="${keyword.id}"/></c:url>">
                    <c:out value="${keyword.keyword}"/>
                </a>
            </display:column>
            <display:column property="context" maxLength="60" titleKey="context" group="3" class="context"/>
            <c:set var="translations" value="keywords.result[${keyword_rowNum-1}].translations"/>
            <display:column titleKey="translations" class="sublist">
                <display:table name="${translations}" id="translation" htmlId="translationTable${keyword_rowNum}" class="sublist">
                    <c:choose>
                        <c:when test="${applyFilter}">
                            <fmt:message key="translation.filtered.sublist.msg.empty.list" var="emptyListMsg" scope="page"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="translation.sublist.msg.empty.list" var="emptyListMsg" scope="page"/>
                        </c:otherwise>
                    </c:choose>
                    <display:setProperty name="basic.msg.empty_list" value="${emptyListMsg}"/>
                    <display:column property="bundle.name" titleKey="bundle" class="bundle" escapeXml="true"/>
                    <display:column property="language.name" titleKey="language" class="language" escapeXml="true"/>
                    <display:column titleKey="country" class="country">
                        <c:if test="${not empty translation.country.code and translation.country.code != \"DEFAULT\"}">
                        <img src="<c:url value="/images/flags/${fn:toLowerCase(translation.country.code)}.png"/>" alt="" title="${translation.country.name}"/>
                        </c:if>
                        <c:out value="${translation.country.name}"/>
                    </display:column>
                    <display:column titleKey="translation" headerClass="translation">
                        <c:out value="${translation.value}"/>
                    </display:column>
                    <display:column titleKey="state" headerClass="state">
                        <fmt:message key="${translation.state}"/>
                    </display:column>
                </display:table>
            </display:column>
            <display:setProperty name="basic.empty.showtable" value="true" />
        </display:table>
    </div>
</div>

<script type="text/javascript" src="<c:url value="/scripts/jquery-1.3.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery.cookie.1.0.js"/>"></script>

<script>
//<![CDATA[
  $(document).ready(function(){
    $("#hideSearchParamPanel").data("panelName","#searchParamPanel");
    $("#showSearchParamPanel").data("panelName","#searchParamPanel");
    $("#hidePreferencesPanel").data("panelName","#preferencesPanel");
    $("#showPreferencesPanel").data("panelName","#preferencesPanel");

    $(window).load(function()
    {
        if ($.cookie("#searchParamPanel") == "hide")
        {
            $("#searchParamPanel").hide();
        }
        if ($.cookie("#preferencesPanel") == "hide")
        {
            $("#preferencesPanel").hide();
        }
    });

    $("#hideSearchParamPanel,#hidePreferencesPanel").click(function ()
    {
        var panelName = $(this).data("panelName");
        $(panelName).slideUp("slow");
        $.cookie(panelName, "hide");
        return false;
    });
    
    $("#showSearchParamPanel,#showPreferencesPanel").click(function ()
     {
        var panelName = $(this).data("panelName");
        $(panelName).slideDown("slow");
        $.cookie(panelName, null);
        return false;
    });
  });
//]]>
</script>

