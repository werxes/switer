<#macro login path isRegisterForm>

<form action="${path}" method="post">

    <div class="form-group row">
        <label class="col-sm-2 col-form-label">User Name:</label>
        <div class="col-sm-6">
            <input type="text" name="username" class="form-control" placeholder="Username"/>
        </div>
    </div>


    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Password:</label>
        <div class="col-sm-6">
            <input type="text" name="password" class="form-control" placeholder="Password"/>
        </div>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <#if !isRegisterForm>
        <a href="/registration">Go to Register</a>
    </#if>

    <div>
        <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Login</#if> </button>
    </div>
</form>
</#macro>


<#macro logout >
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">Sign Out</button>
    </form>
</#macro>

