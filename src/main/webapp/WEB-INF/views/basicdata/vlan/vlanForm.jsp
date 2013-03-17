<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>基础数据-Vlan管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata").addClass("active");
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/checkVlan?oldName=${vlan.name}"
					}
				},
				messages:{
					name:{remote:"Vlan名已存在"}
				}
			});
			
		});			
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${vlan.id}">
		<input type="hidden" name="alias" value="${vlan.alias}">
		
		<fieldset>
		
			<legend><small>
				<c:choose>
					<c:when test="${not empty vlan.id }">修改</c:when>
					<c:otherwise>创建</c:otherwise>
				</c:choose>Vlan
			</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="name">Vlan名称</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${vlan.name }" class="required" maxlength="45" placeholder="...Vlan名称">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label"  for="description">说明</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" maxlength="255" class="required" placeholder="...Vlan说明" >${vlan.description}</textarea>
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="locationId" >IDC</label>
				<div class="controls">
					<select id="locationId" name="locationId" class="required">
						<c:forEach var="item" items="${locationList}">
							<option value="${item.id }"
								<c:if test="${item.id == vlan.location.id }"> selected="selected"</c:if> >
								${item.name }
							</option>
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
