<#macro quote value>"${value?trim?replace("\"", "\"\"")}"</#macro>
<#list translations as translation>
${translation.keyword.keyword},<@quote value="${translation.keyword.context!}"/>,${translation.language.code},${translation.language.name},${translation.country.code},${translation.country.name},${translation.bundle.name},${translation.state},<@quote value="${translation.correctedValue!}"/>
</#list>