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
					<p class="help-inline plain-text">${dns.apply.title}</p>
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
					<p class="help-inline plain-text">${dns.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="domainName">域名</label>
				<div class="controls">
					<p class="help-inline plain-text">${dns.domainName}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="domainType">域名类型</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${domainTypeMap}"><c:if test="${map.key == dns.domainType }">${map.value}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<c:choose>
				<c:when test="${not empty dns.cnameDomain }">
					<div class="control-group">
						<label class="control-label" for="cnameDomain">CNAME域名</label>
						<div class="controls"><p class="help-inline plain-text">${dns.cnameDomain}</p></div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="control-group">
						<label class="control-label" for="mountElbs">目标IP</label>
						<div class="controls">
							<p class="help-inline plain-text">${dns.mountElbs}</p>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
			
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
