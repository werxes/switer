<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>


<@c.page>
    <form action="/user" method="post" xmlns="http://www.w3.org/1999/html">

        </br>
        <label>Username:</label>
        <input type="text" value="${user.username}" name="username"/>

        </br>
        <label>Password:</label>
        <input type="text" value="${user.password}" name="password"/>

        </br>
        <#list roles as role>
            <div>
                <label>
                    <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}/>
                    ${role}
                </label>
            </div>
        </#list>


        </br>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="userId" value="${user.id}"/>
        <input type="submit" value="Save"/>

    </form>

</@c.page>