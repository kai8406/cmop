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
					<p class="help-inline plain-text">${monitorCompute.apply.title}</p>
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
					<p class="help-inline plain-text">${monitorCompute.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ipAddress">监控实例</label>
				<div class="controls">
					<p class="help-inline plain-text">${resources.ipAddress}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="monitorMail">监控邮件列表</label>
				<div class="controls">
					<c:forEach var="email" items="${monitorCompute.apply.monitorMails  }">
						<p class="help-inline plain-text">${email.email }</p><br>
					</c:forEach>
				</div>
			</div>
				
			<div class="control-group">
				<label class="control-label" for="monitorPhone">监控手机列表</label>
				<div class="controls">
					<c:forEach var="phone" items="${monitorCompute.apply.monitorPhones  }">
						<p class="help-inline plain-text">${phone.telephone }</p><br>
					</c:forEach>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="monitorParameter">监控参数</label>
				<div class="controls">
					<p class="help-inline plain-text">
				 		CPU占用率
				 		&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${monitorCompute.cpuWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${monitorCompute.cpuCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						<br>
				 		内存占用率
				 		&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${monitorCompute.memoryWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${monitorCompute.memoryCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						<br>
				 		网络丢包率
				 		&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${monitorCompute.pingLossWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${monitorCompute.pingLossCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						<br>
				 		硬盘可用率
				 		&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdLtMap}"><c:if test="${monitorCompute.diskWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdLtMap}"><c:if test="${monitorCompute.diskCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						<br>
				 		网络延时率
				 		&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdNetGtMap}"><c:if test="${monitorCompute.pingDelayWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdNetGtMap}"><c:if test="${monitorCompute.pingDelayCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						<br>
				 		最大进程数
				 		&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${maxProcessMap}"><c:if test="${monitorCompute.maxProcessWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${maxProcessMap}"><c:if test="${monitorCompute.maxProcessCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						<br>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="port">监控端口</label>
				<div class="controls">
					<p class="help-inline plain-text">${monitorCompute.port}</p><br>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="process">监控进程</label>
				<div class="controls">
					<p class="help-inline plain-text">${monitorCompute.process}</p><br>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="mountPoint">挂载路径</label>
				<div class="controls">
					<p class="help-inline plain-text">${monitorCompute.mountPoint}</p><br>
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
