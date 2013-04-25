<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>服务器管理</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata").addClass("active");
			
			changeLocation();
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
		
			<legend>
				<small><c:choose><c:when test="${not empty hostServer.id }">修改</c:when><c:otherwise>创建</c:otherwise></c:choose>服务器</small>
			</legend>
			
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
				<label class="control-label" for="serverModelId">服务器型号</label>
				<div class="controls">
					<select name="serverModelId" id="serverModelId" class="required">
						<c:forEach var="item" items="${serverModelList }">
							<option value="${item.id}" <c:if test="${item.id == hostServer.serverModel.id }"> selected="selected"</c:if>
							>${item.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="rack">Rack(机柜位置)</label>
				<div class="controls">
					<select id="rack" name="rack" class="required">
						<c:forEach var="map" items="${rackMap }">
							<option <c:if test="${map.key == hostServer.rackAlias }">selected="selected"</c:if>
							 value="${map.key }&${map.value}">${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="site">Site(模块位置)</label>
				<div class="controls">
					<input type="text" id="site" name="site" value="${hostServer.site}" class="required" maxlength="45" placeholder="..模块位置.eg:1-1,4-2">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="switchs">交换机</label>
				<div class="controls">
					<select id="switchs" name="switchs" class="required">
						<c:forEach var="map" items="${switchMap }">
							<option <c:if test="${map.key == hostServer.switchAlias }">selected="selected"</c:if>
							 value="${map.key }&${map.value}">${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="switchSite">交换机口</label>
				<div class="controls">
					<input type="text" id="switchSite" name="switchSite" value="${hostServer.switchSite}" class="required" maxlength="45" placeholder="..交换机口">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="nicSite">网卡号</label>
				<div class="controls">
					<input type="text" id="nicSite" name="nicSite" value="${hostServer.nicSite}" class="required" maxlength="45" placeholder="..网卡号">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="mac">Mac</label>
				<div class="controls">
					<input type="text" id="mac" name="mac" value="${hostServer.mac}"  class="required" maxlength="45" placeholder="..Mac地址">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="height">高度</label>
				<div class="controls">
					<input type="text" id="height" name="height" value="${hostServer.height}"  class="required" maxlength="45" placeholder="..格式如:1U,2U">
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
			
			<div class="control-group">
				<label class="control-label" for="description">用途描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...用途描述"
						maxlength="100" class="required ">${hostServer.description }</textarea>
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
