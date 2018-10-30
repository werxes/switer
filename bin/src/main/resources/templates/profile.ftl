<#import "parts/common.ftl" as c>



<@c.page>
    <h4>${username}</h4>
    <#if message??>${message}</#if>


    <form method="post">



        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="text" name="password" class="form-control" placeholder="Password"/>
            </div>
        </div>


        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email:</label>
                <div class="col-sm-6">
                    <input type="text" name="email" class="form-control" value="${email!''}"/>
                </div>
        </div>



        <input type="hidden" name="_csrf" value="${_csrf.token}"/>


        <div>
            <button class="btn btn-primary" type="submit">Save</button>
        </div>
    </form>



</@c.page>