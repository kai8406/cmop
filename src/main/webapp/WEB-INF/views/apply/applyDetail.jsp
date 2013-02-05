<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

<title>申请管理</title>

<script>
	$(document).ready(function() {
		
		$("ul#navbar li#apply").addClass("active");
		
	});
</script>
</head>

<body>

	<form id="inputForm" action="#" method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${apply.id}">
		
		<fieldset>
			<legend><small>服务申请单详情</small></legend>
			
			<dl class="dl-horizontal">
			
				<dt>标题</dt>
				<dd>${apply.title}&nbsp;</dd>
				
				<dt>申请日期</dt>
				<dd><fmt:formatDate value="${apply.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />&nbsp;</dd>
				
				<dt>状态</dt>
				<dd>
					<c:forEach var="map" items="${applyStatusMap }">
					 	<c:if test="${map.key == apply.status }">${map.value }</c:if>
					</c:forEach>&nbsp;
				</dd>
				
				<dt>服务标签</dt>
				<dd>${apply.serviceTag}&nbsp;</dd>
				
				<dt>优先级</dt>
				<dd>
					<c:forEach var="map" items="${priorityMap }">
					 	<c:if test="${map.key == apply.priority }">${map.value }</c:if>
					</c:forEach>&nbsp;
				</dd>
				
				<dt>服务开始时间</dt>
				<dd>${apply.serviceStart}&nbsp;</dd>
				
				<dt>服务结束时间</dt>
				<dd>${apply.serviceEnd}&nbsp;</dd>
				
				<dt>用途描述</dt>
				<dd>${apply.description}&nbsp;</dd>
		
				<!-- 实例Compute -->
				<c:if test="${not empty apply.computeItems}">
					<hr>
					<dt>PCS、ECS实例:</dt>
					<c:forEach var="item" items="${apply.computeItems}">
					
						<dd><em>标识符</em>
							&nbsp; ${item.identifier}
						</dd>
						
						<dd><em>用途信息</em>
							&nbsp; ${item.remark}
						</dd>
						
						<dd>
							<em>基本信息</em>
							&nbsp; <c:forEach var="map" items="${osTypeMap}"><c:if test="${item.osType == map.key}">${map.value}</c:if></c:forEach>
							&nbsp; <c:forEach var="map" items="${osBitMap}"><c:if test="${item.osBit == map.key}">${map.value}</c:if></c:forEach>
							&nbsp;
							<c:if test="${item.computeType == 1}"><c:forEach var="map" items="${pcsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:if>
							<c:if test="${item.computeType == 2}"><c:forEach var="map" items="${ecsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:if>
						</dd>
						
						<dd>
							<em>关联ESG</em> &nbsp; ${item.networkEsgItem.identifier}(${item.networkEsgItem.description})
						</dd>
						
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- 存储空间ES3 -->
				<c:if test="${not empty apply.storageItems}"></c:if>
				
				<!-- 负载均衡器ELB -->
				<c:if test="${not empty apply.networkElbItems}"></c:if>
				
				<!-- IP地址EIP -->
				<c:if test="${not empty apply.networkEipItems}"></c:if>
				
				<!-- DNS -->
				<c:if test="${not empty apply.networkDnsItems}"></c:if>
				
				<!-- 服务器监控monitorCompute -->
				<c:if test="${not empty apply.monitorComputes}"></c:if>
				
				<!-- ELB监控monitorElb -->
				<c:if test="${not empty apply.monitorElbs}"></c:if>
				
			</dl>
			 
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				
				<a class="btn btn-primary" href="#auditModal" data-toggle="modal">提交审批</a>
				<div id="auditModal" class="modal hide fade" tabindex="-1" data-width="250">
					<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
					<div class="modal-body">是否提交审批?</div>
					<div class="modal-footer">
						<button class="btn" data-dismiss="modal">关闭</button>
						<a href="${ctx}/apply/audit/${apply.id}/" class="btn btn-primary">确定</a>
					</div>
				</div>
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
