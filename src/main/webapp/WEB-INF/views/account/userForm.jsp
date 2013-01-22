<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
	<title>帐号管理</title>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#email").focus();
			
	 		//active tab
	 		$("#user-tab").addClass("active");
			
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					email: {
						remote: "${ctx}/ajax/account/checkEmail?oldEmail=" + encodeURIComponent('${user.email}')
					},
					groupList:"required"
				},
				messages: {
					email: {
						remote: "登录邮箱已存在"
					},
					confirmPassword: {
						equalTo: "输入与上面相同的密码"
					}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					if ( element.is(":checkbox") )
						error.appendTo ( element.parent().next() );
					else
						error.insertAfter( element );
				}
			});
		});
	</script>
</head>

<body>
	<form:form id="inputForm" modelAttribute="user" action="." method="post" cssClass="form-horizontal">
	
		<input type="hidden" name="id" value="${user.id}"/>
		
		<div class="tab-content">
			<div class="span6 offset2">
				<fieldset>
					<legend><small>管理账号</small></legend>
					
					<div id="messageBox" class="alert alert-error" style="display:none">输入有误，请先更正。</div>
					<input name="status" type="hidden" value="1">
					<div class="control-group">
						<label for="email" class="control-label">登录邮箱</label>
						<div class="controls">
							<input type="text" id="email" name="email" size="50" value="${user.email}" class="required email"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="name" class="control-label">用户真实姓名</label>
						<div class="controls">
							<input type="text" id="name" name="name" size="50" value="${user.name}" class="required"/>
						</div>
					</div>
					
					
					<div class="control-group">
						<label for="name" class="control-label">登录名</label>
						<div class="controls">
							<input type="text" id="loginName" name="loginName" size="50" value="${user.loginName}" class="required"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="password" class="control-label">密码</label>
						<div class="controls">
							<input type="password" id="plainPassword" name="plainPassword" size="50" value="${user.plainPassword}" class="required" minlength="3"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="confirmPassword" class="control-label">确认密码</label>
						<div class="controls">
							<input type="password" id="confirmPassword" name="confirmPassword" size="50" value="${user.plainPassword}" equalTo="#plainPassword"/>
						</div>
					</div>
					<div class="control-group">
						<label for="groupList" class="control-label ">权限组</label>
						<div class="controls">
							<form:checkboxes path="groupList" items="${allGroups}" itemLabel="name" itemValue="id" />
						</div>
					</div>
					
					<div class="form-actions">
						<button class="btn btn-primary">保存</button>&nbsp;	
						<input id="cancel" class="btn" type="button" value="返回" onclick="history.back()"/>
					</div>
	
				</fieldset>
			</div>
		</div>
	</form:form>
</body>
</html>
