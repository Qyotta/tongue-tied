<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="content">
    <c:url value="/import.htm" var="importAction" scope="page"/>
    <form:form id="importForm" method="post" action="${importAction}" commandName="import" enctype="multipart/form-data">
        <fieldset>
            <legend><fmt:message key="import.details"/></legend>
            <div>
                <form:label path="fileUploadBean.file" cssClass="content"><fmt:message key="file"/></form:label>
                <input type="file" name="fileUploadBean.file" id="fileUploadBean.file"/>
            </div>
            <div>
                <form:label path="parameters.formatType" cssClass="content"><fmt:message key="file.type"/></form:label>
                <form:select path="parameters.formatType" size="1">
                    <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                    <c:forEach items="${formatTypes}" var="type">
                        <c:if test="${formatType == type}">
                            <c:set var="selected" value="true"/>
                        </c:if>
                        <form:option value="${type}"><fmt:message key="${fn:toLowerCase(type)}.file"/></form:option>
                        <c:remove var="selected"/>
                    </c:forEach>
                </form:select>
                <form:errors path="parameters.formatType" cssClass="error"/>
            </div>
            <div id="bundleSection">
                <form:label path="parameters.bundle" cssClass="content"><fmt:message key="bundle"/></form:label>
                <form:select path="parameters.bundle" id="parameters.bundle">
                    <form:option value="" cssClass="select"><fmt:message key="select.bundle"/></form:option>
                    <form:options items="${bundles}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="parameters.bundle" cssClass="error"/>
            </div>
            <div>
                <form:label path="parameters.translationState" cssClass="content"><fmt:message key="translation.state"/></form:label>
                <form:select path="parameters.translationState" size="1">
                    <form:option value="" cssClass="select"><fmt:message key="please.select"/></form:option>
                    <c:forEach items="${states}" var="state">
                        <c:if test="${translationState == state}">
                            <c:set var="selected" value="true"/>
                        </c:if>
                        <form:option value="${state}"><fmt:message key="${state}"/></form:option>
                        <c:remove var="selected"/>
                    </c:forEach>
                </form:select>
                <form:errors path="parameters.translationState" cssClass="error"/>
            </div>
        </fieldset>
        <div class="submit">
            <input type="submit" id="import" name="import" value="<fmt:message key="import"/>" class="button"/>
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
              if ($("#parameters\\.formatType").val() != "JAVA_PROPERTIES" && $("#parameters\\.formatType").val() != "NLS_PROPERTIES")
                  {
                      $("#bundleSection").hide();
                  }
              }
          );

      $("#parameters\\.formatType").change(function()
          {
              if ($(this).val() == "JAVA_PROPERTIES" || $(this).val() == "NLS_PROPERTIES")
              {
                  $("#bundleSection").slideDown("slow");
              }
              else
              {
                  $("#bundleSection").slideUp("slow");
              }
          }
      );
  });
//]]>
</script>
  