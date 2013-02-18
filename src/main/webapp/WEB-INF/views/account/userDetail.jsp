<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>用户管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#user").addClass("active");
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="#" method="post" class="form-horizontal input-form">
		
		<fieldset>
		
			<legend><small>用户详情</small></legend>
			
		 	<div class="control-group">
				<label class="control-label" for="loginName">登录名</label>
				<div class="controls">
					<p class="help-inline plain-text">${user.loginName}</p>
				</div>
			</div>
			
		 	<div class="control-group">
				<label class="control-label" for="email">Email地址</label>
				<div class="controls">
					<p class="help-inline plain-text">${user.email}</p>
				</div>
			</div>
			
		 	<div class="control-group">
				<label class="control-label" for="phonenum">联系电话</label>
				<div class="controls">
					<p class="help-inline plain-text">${user.phonenum}</p>
				</div>
			</div>
			
		 	<div class="control-group">
				<label class="control-label" for="name">真实姓名</label>
				<div class="controls">
					<p class="help-inline plain-text">${user.name}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="departmentId">所属部门</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="item" items="${allDepartments}"><c:if test="${item.id == user.department.id}">${item.name}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="leaderId">所属领导</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="item" items="${leaders}"><c:if test="${item.id == user.leaderId}">${item.name}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="type">用户类型</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${userTypeMap}"><c:if test="${map.key == group.id}">${map.value}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="groupId">权限角色</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="item" items="${allGroups}"><c:if test="${item.id == group.id}">${item.name}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="createTime">注册日期</label>
				<div class="controls">
					<p class="help-inline plain-text"><fmt:formatDate value="${user.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></p>
				</div>
			</div>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
			</div>
			
		</fieldset>
		
	</form>
</body>
</html>
