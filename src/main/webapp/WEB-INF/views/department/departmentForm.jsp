<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
	<title>部门管理</title>
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#department").addClass("active");
			
			$("#name").focus();
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/checkDepartmentName?oldName=${department.name}"
					},
					permissionArray:"required"
				},
				messages:{
					name:{remote:"部门名已存在"}
				},
				errorClass: "help-inline",
				errorElement: "span",
				highlight:function(element, errorClass, validClass) {
					$(element).closest('.control-group').addClass('error');
				},
				unhighlight: function(element, errorClass, validClass) {
					$(element).closest('.control-group').removeClass('error');
				}
			});
		});
	</script>
</head>

<body>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${department.id}"/>
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${not empty department.id }">修改部门信息</c:when>
					<c:otherwise>创建部门信息</c:otherwise>
				</c:choose>
			</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="serviceTag">部门名称</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${department.name }" class="required" maxlength="45" placeholder="...部门名称">
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
