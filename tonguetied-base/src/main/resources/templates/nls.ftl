<#list translations as translation>
    <#if (translation.value!)?length gt 0>
<@native2nlsascii iskey=true>${translation.keyword.keyword}</@native2nlsascii>=<@native2nlsascii>${translation.nlsCorrectedValue}</@native2nlsascii>
    </#if>
</#list>
