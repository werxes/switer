<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>


<@c.page>

    <div>
        <@l.logout />
    </div>
    <span><a href="/user">User admin</a></span>

    <div>
        <form method="POST" enctype="multipart/form-data">
            <input type="text", name="text", placeholder="Enter Message"/>
            <input type="text", name="tag", placeholder="Enter TAG" />
            <input type="file" name="file">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Add Message</button>
        </form>
    </div>
    </br>

    <div>Filtering</div>
    </br>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter?if_exists}"/>
        <button type="submit">Filter</button>
    </form>

    <div>List of Messages</div>

    </br>
    <#list messages as message>
         <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
             <div>
                 <#if message.filename??>
                     <img src="img/${message.filename}"
                 </#if>

             </div>

        </div>
    <#else>
    No Messages
    </#list>

</@c.page>