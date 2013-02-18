<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>Vlan管理</title>
	<script>
		$(document).ready(function() {
			$("ul#navbar li#basicdata").addClass("active");
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/checkVlan?oldName=${vlan.name}"
					},
					permissionArray:"required"
				},
				messages:{
					name:{remote:"VLAN名已存在"}
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
		<input type="hidden" name="id" value="${vlan.id}"/>
		<input type="hidden" name="alias" value="${vlan.alias}"/>
		
		<fieldset>
			<legend><small>VLAN<c:if test="${not empty vlan.id}">修改</c:if><c:if test="${ empty vlan.id}">新增</c:if></small></legend>
			
			<div class="control-group">
				<label for="name" class="control-label">Vlan名称</label>
				<div class="controls">
					<input type="text" id="name" name="name" size="45" class="required" value="${vlan.name}" placeholder="请输入Vlan名称" />
				</div>
			</div>
			
			<div class="control-group">
				<label for="description" class="control-label">说明</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" maxlength="255" class="required input-large" placeholder="请输入Vlan说明" >${vlan.description}</textarea>
				</div>
			</div>
		
			<div class="control-group">
				<label for="locationId" class="control-label">IDC</label>
				<div class="controls">
					<select name="locationId" class="required">
						<c:forEach var="item" items="${locationList}">
							<option value="${item.id }"
								<c:if test="${item.id == vlan.location.id }"> selected="selected"	</c:if> >
								${item.name }
							</option>
						</c:forEach>
					</select>
					<span class="help-inline">请选择IDC</span>
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
