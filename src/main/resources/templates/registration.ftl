<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>


<@c.page>
    <div class="mb-1">Registration Page</div>
    <#if message??>${message}</#if>
    <@l.login "/registration" true/>
</@c.page>
