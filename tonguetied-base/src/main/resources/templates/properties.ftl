<#list translations as translation>
    <#if (translation.value!)?length gt 0>
<@native2ascii iskey=true>${translation.keyword.keyword}</@native2ascii>=<@native2ascii>${translation.correctedValue}</@native2ascii>
    </#if>
</#list>
