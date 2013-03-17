<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>用户注册</title>
	
	<script>
		$(document).ready(function() {
			
			$("#loginName").focus();
			
			$("#inputForm").validate({
				rules: {
					loginName: {
						remote: "${ctx}/ajax/checkLoginName"
					},
					email: {
						remote: "${ctx}/ajax/checkEmail"
					}
				},
				messages: {
					loginName: {
						remote: "用户登录名已存在"
					},
					email: {
						remote: "邮箱已存在"
					}
				} 
			});
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="${ctx}/register" method="post" class="form-horizontal input-form">
	
		<fieldset>
		
			<legend><small>注册<span class="pull-right">已有账号?<a href="${ctx}/login">登录</a></span></small></legend>
			
			<div class="control-group">
				<label class="control-label" for="loginName">登录名</label>	 
				<div class="controls">
					<input type="text" id="loginName" name="loginName" class="required" maxlength="45" placeholder="...Login name">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="email">Email地址</label>	 
				<div class="controls">
					<input type="text" id="email" name="email" class="required email" maxlength="45" placeholder="...Email address">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="plainPassword">密码</label>
				<div class="controls">
					<input type="password" id="plainPassword" name="plainPassword" class="required" minlength="6" maxlength="16" placeholder="...Password">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="email">确认密码</label>
				<div class="controls">
					<input type="password" id="confirmPassword" class="required" minlength="6" maxlength="16" equalTo="#plainPassword" placeholder="...Confirm password">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="phonenum">联系电话</label>
				<div class="controls">
					<input type="text" id="phonenum" name="phonenum" class="required" maxlength="45" placeholder="...联系电话">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="name">真实姓名</label>
				<div class="controls">
					<input type="text" id="name" name="name" class="required" maxlength="45" placeholder="...真实姓名">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="departmentId">所属部门</label>
				<div class="controls">
					<select id="departmentId" name="departmentId" class="required">
						<c:forEach var="item" items="${allDepartments}">
							<option value="${item.id }" <c:if test="${user.department.id == item.id}">selected="selected"</c:if> >${item.name }</option>							
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="leaderId">所属领导</label>
				<div class="controls">
					<select id="leaderId" name="leaderId" class="required">
						<c:forEach var="item" items="${leaders}">
							<option value="${item.id }" <c:if test="${user.leaderId == item.id}">selected="selected"</c:if> >${item.name }</option>							
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
	</form>
</body>
</html>
