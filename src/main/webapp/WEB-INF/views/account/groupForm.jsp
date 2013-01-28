<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>权限管理</title>
	
	<script>
		$(document).ready(function() {
			
			
			$("ul#navbar li#group").addClass("active");
			
			$("#name").focus();
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/account/checkGroupName?oldName=${group.name}"
					},
					permissionArray:"required"
				},
				messages:{
					name:{remote:"权限角色已存在"}
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

<link href="${ctx}/static/common/css/inputForm.css" rel="stylesheet">

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${group.id}">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${not empty group }">修改权限</c:when>
					<c:otherwise>创建权限</c:otherwise>
				</c:choose>
			</small></legend>
			
			
			<div class="control-group">
				<label class="control-label" for="name">权限角色</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${group.name}" class="required" maxlength="45"  placeholder="...权限角色">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="permissionArray" >授权列表</label>
				<div class="controls">
					<c:forEach var="item" items="${allPermissions }" >
						<label class="checkbox">
					  		<input type="checkbox" id="permissionArray" name="permissionArray" value="${item.value }"
								 <c:forEach var="permission" items="${permissions }" >
									<c:if test="${permission ==  item.value}"> 
										checked="checked" 
									</c:if>
								</c:forEach>
							>
						  ${item.displayName }
						</label>
					</c:forEach>
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
