<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

<title>审批管理</title>

<script>
	$(document).ready(function() {
		
		$("ul#navbar li#audit").addClass("active");
		
		//点击class为"auditResult" 的button触发审批意见的非空判断
		
		$("button.auditResult").click(function(){
			
			var $this = $(this); 
			
			var $opinion = $("#opinion");
			
			//选择 1.同意 去除class中的required.
			
			$this.val() == 1 ? $opinion.removeClass("required"):$opinion.addClass("required")
			
			$("#result").val($this.val());
			
		});
		
		$("#inputForm").validate({
			errorClass: "help-inline",
			errorElement: "span",
			highlight:function(element, errorClass, validClass) {
		   		$(element).closest('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).closest('.control-group').removeClass('error');
			}
		});
		
		
	});
</script>
</head>

<body>

	<form id="inputForm" action="${ctx}/audit/apply/${apply.id}/" method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${apply.id}">
		<input type="hidden" name="userId" value="${userId }">
		<input type="hidden" id="result" name="result">
		
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
							&nbsp; ${item.identifier}(${item.remark})
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
			
			<hr>
			
			<!-- 审批意见 -->
			
			<c:if test="${not empty audits  }">
				<table class="table">
					<thead>
						<tr>
							<th>审批人</th>
							<th>审批决定</th>
							<th>审批意见</th>
							<th>审批时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${audits}" var="item">
							<c:if test="${item.status != 0}">	
							<tr>
								<td>${item.auditFlow.user.name}</td>
								<td>
									<c:forEach var="map" items="${auditResultMap}">
										<c:if test="${item.result == map.key}">
											<span class="label label-success">${map.value }</span>
										</c:if>
									</c:forEach>
								</td>
								<td>${item.opinion}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
							</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
			
			
			<form id="inputForm" action="." method="post">
			
				<div class="control-group">
					<label class="control-label" for="opinion">审批意见</label>
					<div class="controls">
						<textarea rows="3" id="opinion" name="opinion" placeholder="...审批意见" maxlength="45"></textarea>
					</div>
				</div>
			
				<div class="form-actions">
					<input class="btn" type="button" value="返回" onclick="history.back()">
					<c:forEach var="map" items="${auditResultMap}">
						<button class="auditResult btn btn-info" value="${map.key}">${map.value}</button>
					</c:forEach>
				</div>
				
			</form>
			
		
		</fieldset>
		
	</form>
	
</body>
</html>
