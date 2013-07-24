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

	<form id="inputForm" action="${ctx}/summary/migrate" method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="resourceId" value="${resources.id }">
		
		<fieldset>
			<legend><small>资源详情</small></legend>
			
			 <div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${compute.apply.title}</p>
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
					<p class="help-inline plain-text">${compute.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ipAddress">IP地址</label>
				<div class="controls">
					<p class="help-inline plain-text">${resources.ipAddress}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="osType">操作系统</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${osTypeMap}"><c:if test="${map.key == compute.osType }">${map.value}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="osBit">操作位数</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${osBitMap}"><c:if test="${map.key == compute.osBit }">${map.value}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="serverType">规格</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:choose>
							<c:when test="${compute.computeType == 1 }">
								<c:forEach var="map" items="${pcsServerTypeMap}"><c:if test="${map.key == compute.serverType }">${map.value}</c:if></c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach var="map" items="${ecsServerTypeMap}"><c:if test="${map.key == compute.serverType }">${map.value}</c:if></c:forEach>
							</c:otherwise>
						</c:choose>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="mountESG">关联ESG</label>
				<div class="controls">
					<p class="help-inline plain-text">${compute.mountESG}</p>	
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="remark">用途信息</label>
				<div class="controls">
					<p class="help-inline plain-text">${compute.remark}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="application">应用信息</label>
				<div class="controls">
					<table class="table table-bordered table-condensed"  >
						<thead><tr><th>应用名称</th><th>应用版本</th><th>部署路径</th></tr></thead>
						<tbody>
							<c:forEach var="item" items="${compute.applications}">
								<tr>
									<td>${item.name }</td>
									<td>${item.version }</td>
									<td>${item.deployPath }</td>
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
			
			<c:if test="${not empty migrate }">
				<div class="control-group">
					<label class="control-label" for="userId">资源所属人</label>
					<div class="controls">
						<select name="userId">
							<c:forEach var="item" items="${users }">
								<option value="${item.id }" 
									<c:if test="${item.id == resources.user.id }">selected="selected"</c:if>	
								>${item.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</c:if>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<c:if test="${not empty migrate }"><input class="btn btn-primary" type="submit" value="提交"></c:if>
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
