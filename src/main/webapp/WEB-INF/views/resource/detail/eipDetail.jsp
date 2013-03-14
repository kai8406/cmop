<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="#" method="post" class="form-horizontal input-form">
		
		<fieldset>
			<legend><small>资源详情</small></legend>
			
			 <div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="status">资源状态</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${resourcesStatusMap}"><c:if test="${map.key == resources.status }">${map.value}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ipAddress">IP地址</label>
				<div class="controls">
					<p class="help-inline plain-text">${resources.ipAddress}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ispType">ISP运营商</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${ispTypeMap}"><c:if test="${map.key == eip.ispType }">${map.value}</c:if></c:forEach>
					</p>
				</div>
			</div>
			  
			<c:if test="${not empty eip.networkElbItem }">
		 	<div class="control-group">
				<label class="control-label" for="networkElbItem">关联ELB</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.networkElbItem.identifier }(${eip.networkElbItem.virtualIp})</p>
				</div>
			</div>
			</c:if>
			
			<c:if test="${not empty eip.computeItem }">
		 	<div class="control-group">
				<label class="control-label" for="networkElbItem">关联ELB</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.computeItem.identifier }(${eip.computeItem.innerIp})</p>
				</div>
			</div>
			</c:if>
			
			<div class="control-group">
				<label class="control-label" for="eipPortItem">端口映射</label>
				<div class="controls">
					<table class="table table-bordered table-condensed"  >
						<thead><tr><th>协议</th><th>源端口</th><th>目标端口</th></tr></thead>
						<tbody>
							<c:forEach var="item" items="${eip.eipPortItems}">
								<tr>
									<td>${item.protocol }</td>
									<td>${item.sourcePort }</td>
									<td>${item.targetPort }</td>
								</tr>
							</c:forEach>
		 				</tbody>
					</table>	
				</div>
			</div>
			
			<hr>
			
			<div class="control-group">
				<label class="control-label" for="serviceTagId">服务标签</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="item" items="${allTags}"><c:if test="${item.id == resources.serviceTag.id }">${item.name}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="usedby">运维人</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${assigneeMap}"><c:if test="${map.key == resources.usedby }">${map.value}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="changeDescription">变更描述</label>
				<div class="controls">
					<p class="help-inline plain-text">${change.description}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="changeDescription">变更时间</label>
				<div class="controls">
					<p class="help-inline plain-text"><fmt:formatDate value="${change.changeTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></p>
				</div>
			</div>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
