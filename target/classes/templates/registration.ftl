<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>


<@c.page>
    Registration Page
    <#if message??>${message}</#if>
    <@l.login "/registration"/>
</@c.page>
