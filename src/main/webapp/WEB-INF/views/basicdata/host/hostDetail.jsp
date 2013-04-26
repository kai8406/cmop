<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务器管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata").addClass("active");
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="#" method="post" class="form-horizontal input-form">
		
		<fieldset>
			<legend><small>服务器详情</small></legend>
			
			<dl class="dl-horizontal">
			
				<dt>服务器名称</dt>
				<dd>${hostServer.displayName}&nbsp;</dd>
				
				<dt>服务器型号</dt>
				<dd>${hostServer.serverModel.name}&nbsp;</dd>
				
				<dt>Rack(机柜位置)</dt>
				<dd>${hostServer.rack}&nbsp;</dd>
				
				<dt>Site(模块位置)</dt>
				<dd>${hostServer.site}&nbsp;</dd>
				
				<dt>交换机</dt>
				<dd><c:forEach var="map" items="${switchMap}"><c:if test="${map.key == hostServer.switchAlias }">${map.value}</c:if></c:forEach>&nbsp;</dd>
				
				<dt>交换机口</dt>
				<dd>${hostServer.switchSite}&nbsp;</dd>
				
				<dt>网卡号</dt>
				<dd>${hostServer.nicSite}&nbsp;</dd>
				
				<dt>Mac</dt>
				<dd>${hostServer.mac}&nbsp;</dd>
				
				<dt>高度</dt>
				<dd>${hostServer.height}&nbsp;</dd>
				
				<dt>IDC</dt>
				<dd><c:forEach var="location" items="${locationList}"><c:if test="${location.alias == hostServer.locationAlias }">${location.name }</c:if></c:forEach>&nbsp;</dd>
				
				<dt>IP地址</dt>
				<dd>${hostServer.ipAddress}&nbsp;</dd>
				
				<dt>用途描述</dt>
				<dd>${hostServer.description}&nbsp;</dd>
				
				<dt>创建时间</dt>
				<dd><fmt:formatDate value="${hostServer.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />&nbsp;</dd>
				 
			</dl>
			 
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
