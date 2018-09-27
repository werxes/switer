<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>


<@c.page>
    Login Page
    <@l.login "/login" />
    <a href="/registration">Go to Register</a>
</@c.page>