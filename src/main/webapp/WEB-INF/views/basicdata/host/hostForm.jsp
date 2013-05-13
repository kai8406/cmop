<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>服务器管理</title>

	<script>
	$(document).ready(function() {
		
		$("ul#navbar li#basicdata").addClass("active");
		
		$("#serverModelId").on("change", function() {
			getServerModel();
		});
		
		if ($("#hostServerId").val() == "") {
			getServerModel();
			changeLocation();
		}
		
	});
	
	$(document).on("change", "select.vlan", function() {
		changeVlan($(this));
	});
	
	$(document).on("change", "select.ipAddress", function() {
		changeIP($(this));
	});
	
	//根据服务器规格生成网卡信息.
	function getServerModel() {
		$.ajax({
			type: "GET",
			url: "${ctx}/ajax/getServerModel?id=" + $('#serverModelId').val(),
			dataType: "json",
			success: function(data) {
				var count = data.port;
				$("#NICDIV").empty();
				if (count > 0) {
					var html = "";
					for (var i = 0; i < count; i++) {
						html += '<hr>';
						html += '<div class="control-group">';
						html += '<label class="control-label">网卡号</label>';
						html += '<div class="controls">';
						html += '<input type="text" name="nicSite" class="required" maxlength="45" placeholder="..网卡号">';
						html += '</div>';
						html += '</div>';
						html += '<div class="control-group">';
						html += '<label class="control-label">Mac</label>';
						html += '<div class="controls">';
						html += '<input type="text" name="nicMac" class="required" maxlength="45" placeholder="..Mac地址">';
						html += '</div>';
						html += '</div>';
						html += '<div class="control-group">';
						html += '<label class="control-label">网卡IP</label>';
						html += '<div class="controls">';
						html += '<select class="span1 vlan"></select>';
						html += '<select class="span1 ipAddress"></select>';
						html += '<input type="text" id="nicIpAddress' + i + '" readonly="readonly" name="nicIpAddress" class="required span2 ipAddress">';
						html += '</div>';
						html += '</div> ';
					}
					$("#NICDIV").append(html);
				}
			}
		});
	}

	function changeLocation() {
		$.ajax({
			type: "GET",
			url: "${ctx}/ajax/getVlanByLocationAlias?locationAlias=" + $('#locationAlias').val(),
			dataType: "json",
			success: function(data) {
				var $vlan = $("select.vlan");
				$vlan.empty();
				var html = "";
				for (var key in data) {
					html += ("<option value='" + key + "'>" + data[key] + "</option>");
				}
				$vlan.append(html);
				changeVlan($vlan);
			}
		});
	}

	function changeVlan(obj) {
		$.ajax({
			type: "GET",
			url: "${ctx}/ajax/getIpPoolByVlan?vlanAlias=" + obj.val(),
			dataType: "json",
			success: function(data) {
				var $ipAddress = obj.next("select.ipAddress");
				var html = "";
				$ipAddress.empty();
				for (var i = 0; i < data.length; i++) {
					html += "<option value='" + data[i].ipAddress + "'>" + data[i].ipAddress + "</option>";
				}
				$ipAddress.append(html);
			}
		});
	}

	function changeIP(obj) {
		var $inputIp = obj.next("input.ipAddress");
		var selectedArray = [];
		$("input.ipAddress").each(function() {
			var $this = $(this);
			var $ip = $this.val();
			if ($ip != "") {
				selectedArray.push($ip);
			}
		});
		$inputIp.val(obj.val());
		//IP只能被使用一次.
		if ($.inArray($inputIp.val(), selectedArray) > -1) {
			alert("该IP已使用,请选择其他IP.");
			$inputIp.val("");
		}
		selectedArray = [];
	}
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" id="hostServerId" name="id" value="${hostServer.id}">
		
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
				<label class="control-label" for="ipAddress">IP地址</label>
				<div class="controls">
					<select class="span1 vlan"></select>
					<select class="span1 ipAddress"></select>
					<input type="text" readonly="readonly" name="ipAddress" value="${hostServer.ipAddress }" class="required span2 ipAddress" >
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="description">用途描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...用途描述"
						maxlength="100" class="required ">${hostServer.description }</textarea>
				</div>
			</div>	
			
			<div class="control-group">
				<label class="control-label" for="managementMac">Mac</label>
				<div class="controls">
					<input type="text" id="managementMac" name="managementMac" value="${hostServer.managementMac}"  class="required" maxlength="45" placeholder="..Mac地址">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="managementIp">管理口IP</label>
				<div class="controls">
					<select class="span1 vlan"></select>
					<select class="span1 ipAddress"></select>
					<input type="text" readonly="readonly" name="managementIp" value="${hostServer.managementIp }" class="required span2 ipAddress" >
				</div>
			</div>
			
			<div id="NICDIV">
				<c:forEach var="nic" items="${hostServer.nics }">
					<hr>
					<div class="control-group">
						<label class="control-label">网卡号</label>
						<div class="controls">
							<input type="text" placeholder="..网卡号" maxlength="45" value="${nic.site }" class="required" name="nicSite">
						</div>
					</div> 
					<div class="control-group">
						<label class="control-label">Mac</label>
						<div class="controls">
							<input type="text" placeholder="..Mac地址" maxlength="45" value="${nic.mac }" class="required" name="nicMac">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">网卡IP</label>
						<div class="controls">
							<select class="span1 vlan"></select>
							<select	class="span1 ipAddress"></select>
							<input type="text" readonly="readonly" name="nicIpAddress" value="${nic.ipAddress }" class="required span2 ipAddress" >
						</div>
					</div>
				</c:forEach>
			</div>


			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
