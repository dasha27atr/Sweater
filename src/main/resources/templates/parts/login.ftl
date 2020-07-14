<#include "security.ftl">
<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User Name: </label>
            <div class="col-sm-6">
                <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       placeholder="User name"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password: </label>
            <div class="col-sm-6">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}" placeholder="Password"/>
                <#if captchaError??>
                    <div class="invalid-feedback">
                        ${captchaError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Password: </label>
                <div class="col-sm-6">
                    <input type="password" name="confirmPassword"
                           class="form-control ${(confirmPasswordError??)?string('is-invalid', '')}"
                           placeholder="Retype password"/>
                    <#if confirmPasswordError??>
                        <div class="invalid-feedback">
                            ${confirmPasswordError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Email: </label>
                <div class="col-sm-6">
                    <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                           class="form-control${(emailError??)?string('is-invalid', '')}"
                           placeholder="some@some.com"/>
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>
        </#if>
        <div class="col-sm-6">
            <div class="g-recaptcha" data-sitekey="6LeGnL4UAAAAABAoxYL7xZBZAj5bP22HrX0_WPLr"></div>
            <#if message??>
                <div class="alert alert-danger" role="alert">
                    ${message}
                </div>
            </#if>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm>
            <a href="/registration">Add new user</a>
        </#if>
        <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Sign In</#if></button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit"><#if user??>Sign Out<#else>Log In</#if></button>
    </form>
</#macro>