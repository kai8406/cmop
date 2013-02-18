<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>IDC管理管理</title>
	<script>
		$(document).ready(function() {
			$("ul#navbar li#basicdata").addClass("active");
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/checkLocation?oldName=${location.name}"
					},
					permissionArray:"required"
				},
				messages:{
					name:{remote:"IDC名已存在"}
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
		<input type="hidden" name="id" value="${location.id}"/>
		<input type="hidden" name="alias" value="${location.alias}"/>
		
		<fieldset>
			<legend><small>IDC<c:if test="${not empty location.id}">修改</c:if><c:if test="${ empty location.id}">新增</c:if></small></legend>
			
			<div class="control-group">
				<label class="control-label" >IDC名称</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${location.name }" class="required" maxlength="45" placeholder="请输入IDC名称">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" >城市</label>
				<div class="controls">
					<input type="text" id="city" name="city" value="${location.city }" class="required" maxlength="45" placeholder="请输入城市">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" >地址</label>
				<div class="controls">
					<input type="text" id="address" name="address" value="${location.address }" class="required" maxlength="45" placeholder="请输入地址">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" >邮编</label>
				<div class="controls">
					<input type="text" id="postcode" name="postcode" value="${location.postcode }" class="required" maxlength="45" placeholder="请输入邮编">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" >电话</label>
				<div class="controls">
					<input type="text" id="telephone" name="telephone" value="${location.telephone }" class="required" maxlength="45" placeholder="请输入电话">
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
