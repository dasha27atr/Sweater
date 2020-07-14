<#import "parts/common.ftl" as c>

<@c.page>
    List of users
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td>
                    <#list user.roles as role>${role}
                        <#sep>,
                    </#list>
                </td>
                <td>
                    <a href="/user/${user.id}">edit</a>
                </td>
                <td>
                    <a style="margin-left: 10px" href="/user/${user.id}/delete">
                        <i class="fa fa-trash" aria-hidden="true"></i>
                    </a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>