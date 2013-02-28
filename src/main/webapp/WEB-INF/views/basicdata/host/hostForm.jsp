<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>服务器管理</title>
<script>
$(document).ready(function() {
	$("ul#navbar li#basicdata").addClass("active");

	$("#inputForm").validate({
		rules:{
			name:{
				remote: "${ctx}/ajax/checkDisplayName?oldDisplayName=${hostServer.displayName}"
			},
			permissionArray:"required"
		},
		messages:{
			name:{remote:"服务器名称已存在"}
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
		<input type="hidden" name="id" value="${hostServer.id}"/>
		
		<fieldset>
			<legend><small>服务器<c:if test="${not empty hostServer.id}">修改</c:if><c:if test="${ empty hostServer.id}">新增</c:if></small></legend>
			
			<div class="control-group">
				<label class="control-label">服务器名称</label>
				<div class="controls">
					<input type="text" id="displayName" name="displayName" size="50" class="required" value="${hostServer.displayName}"/>
					<span class="help-inline">组成格式：Company Model Rack-Site.例如:HP DL2000 0416-1-1
					</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">IDC</label>
				<div class="controls">
					<select name="locationAlias" class="required">
						<c:forEach var="item" items="${locationList}">
							<option <c:if test="${item.alias == hostServer.locationAlias }">selected="selected"</c:if>
							value="${item.alias }">${item.name }</option>
						</c:forEach>
					</select>
					<span class="help-inline">请选择IDC</span>
				</div>
			</div>	
			
			<div class="control-group">
				<label class="control-label">服务器类型</label>
				<div class="controls">
					<select name="serverType" class="required">
						<c:forEach var="map" items="${hostServerTypeMap}">
						<option	<c:if test="${map.key == hostServer.serverType}">selected="selected"</c:if>
						 	value="<c:out value='${map.key}'/>"><c:out value='${map.value}'/></option>
						</c:forEach>
					</select>
					<span class="help-inline">请选择服务器类型</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">IP</label>
				<div class="controls">
					<input type="text" id="ipAddress" name="ipAddress" size="50" class="required ipAddressValidate" />
					<span class="help-inline">请输入可用IP</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">IP池类型</label>
				<div class="controls">
					<select name="poolType" class="required">
						<c:forEach var="map" items="${poolTypeMap}">
						<option	<c:if test="${map.key == ipPool.poolType}">selected="selected"</c:if>
						 	value="<c:out value='${map.key}'/>"><c:out value='${map.value}'/></option>
						</c:forEach>
					</select>
					<span class="help-inline">请选择IP池类型</span>
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
