<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>


<@c.page>
    <#if message??>${message}</#if>
    <@l.login "/login" false />
</@c.page>