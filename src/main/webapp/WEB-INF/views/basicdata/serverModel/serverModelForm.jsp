<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>基础数据-服务器型号管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata").addClass("active");
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/checkServerModel?oldName=${serverModel.name}"
					}
				},
				messages:{
					name:{remote:"服务器型名称已存在"}
				}
			});
			
		});
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${serverModel.id}">
		
		<fieldset>
		
			<legend>
				<small><c:choose><c:when test="${not empty serverModel.id }">修改</c:when><c:otherwise>创建</c:otherwise></c:choose>服务器型号</small>
			</legend>
		
			<div class="control-group">
				<label class="control-label" for="name">名称</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${serverModel.name }" class="required" maxlength="45" placeholder="...名称">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="company">所属公司</label>
				<div class="controls">
					<select id="company" name="company"  class="required" >
						<c:forEach var="map" items="${companyMap }">
							<option <c:if test="${map.key == serverModel.companyAlias }">selected="selected"</c:if>
							 value="${map.key}&${map.value}">${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="cpu">CPU个数</label>
				<div class="controls">
					<input type="text" id="cpu" name="cpu" value="${serverModel.cpu }" class="required digits" maxlength="45" placeholder="...CPU个数">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="memory">内存槽数</label>
				<div class="controls">
					<input type="text" id="memory" name="memory" value="${serverModel.memory }" class="required digits" maxlength="45" placeholder="...内存槽数">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="disk">硬盘托架数</label>
				<div class="controls">
					<input type="text" id="disk" name="disk" value="${serverModel.disk }" class="required digits" maxlength="45" placeholder="...硬盘托架数">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pci">PCI插槽数</label>
				<div class="controls">
					<input type="text" id="pci" name="pci" value="${serverModel.pci }" class="required digits" maxlength="45" placeholder="...PCI插槽数">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="port">网卡口数</label>
				<div class="controls">
					<input type="text" id="port" name="port" value="${serverModel.port }" class="required digits" maxlength="45" placeholder="...网卡口数">
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
