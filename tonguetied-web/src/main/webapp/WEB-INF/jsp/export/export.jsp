<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
    <c:url value="/export.htm" var="exportAction" scope="page"/>
    <form:form id="exportForm" method="post" action="${exportAction}" commandName="export">
        <fieldset>
            <legend><fmt:message key="export.details"/></legend>
            <div>
                <form:label path="countries" cssClass="content"><fmt:message key="country"/></form:label>
                <form:select path="countries" multiple="true" size="5">
                    <form:options items="${countries}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="countries" cssClass="error"/>
            </div>
            <div>
                <form:label path="languages" cssClass="content"><fmt:message key="language"/></form:label>
                <form:select path="languages" multiple="true" size="5">
                    <form:options items="${languages}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="languages" cssClass="error"/>
            </div>
            <div>
                <form:label path="bundles" cssClass="content"><fmt:message key="bundle"/></form:label>
                <form:select path="bundles" multiple="true" size="5">
                    <form:options items="${bundles}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="bundles" cssClass="error"/>
            </div>
            <div>
                <form:label path="translationStates" cssClass="content"><fmt:message key="translation.state"/></form:label>
                <form:select path="translationStates" multiple="true" size="4">
                    <c:forEach items="${states}" var="state">
                       
                        <form:option value="${state}"><fmt:message key="${state}"/></form:option>
                        <c:remove var="selected"/>
                    </c:forEach>
                </form:select>
                <form:errors path="translationStates" cssClass="error"/>
            </div>
            <div>
                <form:label path="formatType" cssClass="content"><fmt:message key="export.type"/></form:label>
                <form:select path="formatType" size="1">
                    <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                    <c:forEach items="${formatTypes}" var="type">
                        <c:if test="${formatType == type}">
                            <c:set var="selected" value="true"/>
                        </c:if>
                        <form:option value="${type}"><fmt:message key="${fn:toLowerCase(type)}.file"/></form:option>
                        <c:remove var="selected"/>
                    </c:forEach>
                </form:select>
                <form:errors path="formatType" cssClass="error"/>
            </div>
            <div>
                <form:label path="resultPackaged" cssClass="content"><fmt:message key="package.result"/></form:label>
                <form:checkbox path="resultPackaged" id="resultPackaged"/>
            </div>
			<div>
                <form:label path="placeholder" cssClass="content"><fmt:message key="placeholder"/></form:label>
                <form:checkbox path="placeholder" id="placeholder"/>
            </div>
            <div id="globalsSection" style="float: left; clear: left">
                <form:label path="globalsMerged" cssClass="content"><fmt:message key="merge.global.bundles"/></form:label>
                <form:checkbox path="globalsMerged" id="globalsMerged"/>
            </div>
        </fieldset>
        <div class="submit">
            <input type="submit" id="export" name="export" value="<fmt:message key="export"/>" class="button"/>
            <input type="submit" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" class="button"/>
            <input type="reset" id="reset" name="reset" value="<fmt:message key="reset"/>" class="button"/>
        </div>
    </form:form>
    </div>
    
<script type="text/javascript" src="<c:url value="/scripts/jquery-1.3.2.min.js"/>"></script>

<script>
//<![CDATA[
  $(document).ready(function()
  {
      $(window).load(function()
          {
              if ($("#formatType").val() != "JAVA_PROPERTIES" && $("#formatType").val() != "NLS_PROPERTIES")
              {
                  $("#globalsSection").hide();
              }
          }
      );

      $("#formatType").change(function()
          {
              if ($(this).val() == "JAVA_PROPERTIES" || $(this).val() == "NLS_PROPERTIES")
              {
                  $("#globalsSection").slideDown("slow");
              }
              else
              {
                  $("#globalsSection").slideUp("slow");
              }
          }
      );
  });
//]]>
</script>
  