<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>基础数据-IDC管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata").addClass("active");
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/checkLocation?oldName=${location.name}"
					}
				},
				messages:{
					name:{remote:"IDC名已存在"}
				}
			});
			
		});
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${location.id}">
		<input type="hidden" name="alias" value="${location.alias}">
		
		<fieldset>
		
			<legend><small>
				<c:choose>
					<c:when test="${not empty location.id }">修改</c:when>
					<c:otherwise>创建</c:otherwise>
				</c:choose>IDC
			</small></legend>
		
			<div class="control-group">
				<label class="control-label" for="name">IDC名称</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${location.name }" class="required" maxlength="45" placeholder="...IDC名称">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="city">城市</label>
				<div class="controls">
					<input type="text" id="city" name="city" value="${location.city }" class="required" maxlength="45" placeholder="...城市">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="address">地址</label>
				<div class="controls">
					<input type="text" id="address" name="address" value="${location.address }" class="required" maxlength="45" placeholder="...地址">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="postcode">邮编</label>
				<div class="controls">
					<input type="text" id="postcode" name="postcode" value="${location.postcode }" class="required" maxlength="45" placeholder="...邮编">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="telephone">电话</label>
				<div class="controls">
					<input type="text" id="telephone" name="telephone" value="${location.telephone }" class="required" maxlength="45" placeholder="...电话">
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
