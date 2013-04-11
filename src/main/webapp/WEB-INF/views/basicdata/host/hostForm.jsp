<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>服务器管理</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata").addClass("active");
			
			changeLocation();
		
			$("#inputForm").validate({
				rules:{
					displayName:{
						remote: "${ctx}/ajax/checkDisplayName?oldDisplayName=${hostServer.displayName}"
					}
				},
				messages:{
					displayName:{remote:"服务器名称已存在"}
				}
			});
			
		});
		
		function changeLocation() {
			$.ajax({
				type: "GET",
				url: "${ctx}/ajax/getVlanByLocationAlias?locationAlias=" + $('#locationAlias').val(),
				dataType: "json",
				success: function(data) {
					$("#vlan").empty();
					var html = "";
					for (var key in data) {
						html += ("<option value='" + key + "'>" + data[key] + "</option>");
					}
					$("#vlan").append(html);
					changeVlan();
				}
			});
		}
		
		function changeVlan() {
			$.ajax({
				type: "GET",
				url: "${ctx}/ajax/getIpPoolByVlan?vlanAlias=" + $("#vlan").val(),
				dataType: "json",
				success: function(data) {
					$("#ipAddress").empty();
					var html = "<option value=''></option>";
					var ip = $("#hostIpAddress").val();
					for (var i = 0; i < data.length; i++) {
						html += "<option value='" + data[i].ipAddress + "'>" + data[i].ipAddress + "</option>";
					}
					
					//如果ip不为"",插入hostServer本身的ip.
					if (ip != "") {
						html += "<option selected='selected' value='" + ip + "'>" + ip + "</option>";
					}
					$("#ipAddress").append(html);
				}
			});
		}
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${hostServer.id}">
		<input type="hidden" id="hostIpAddress" value="${hostServer.ipAddress}">
		
		<fieldset>
		
			<legend><small>
				<c:choose><c:when test="${not empty hostServer.id }">修改</c:when><c:otherwise>创建</c:otherwise></c:choose>服务器
			</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="displayName">服务器名称</label>
				<div class="controls">
					<input type="text" id="displayName" name="displayName" value="${hostServer.displayName}" 
					class="required hostNameValidate" maxlength="45" placeholder="..格式如:HP DL2000 0416-1-1">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="serverType">服务器类型</label>
				<div class="controls">
					<select id="serverType" name="serverType" class="required">
						<c:forEach var="map" items="${hostServerTypeMap}">
						<option <c:if test="${map.key == hostServer.serverType}">selected="selected"</c:if>
						 	value="${map.key}">${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="locationAlias">IDC</label>
				<div class="controls">
					<select id="locationAlias" name="locationAlias" class="required" onchange="changeLocation()">
						<c:forEach var="item" items="${locationList}">
							<option <c:if test="${item.alias == hostServer.locationAlias }">selected="selected"</c:if>
								value="${item.alias }">${item.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>	
			
			<div class="control-group">
				<label class="control-label" for="vlan">Vlan</label>
				<div class="controls">
					<select id="vlan" class="required" onchange="changeVlan()"></select>
				</div>
			</div>	
			
			<div class="control-group">
				<label class="control-label" for="ipAddress">IP地址</label>
				<div class="controls">
					<select id="ipAddress" name="ipAddress" class="required">
						<option value="${hostServer.ipAddress }">${hostServer.ipAddress }</option>
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
