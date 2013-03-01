<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>审批管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resourceAudit").addClass("active");
			
			//点击class为"auditResult" 的button触发审批意见的非空判断
			
			$("button.auditResult").click(function(){
				
				var $this = $(this); 
				
				var $opinion = $("#opinion");
				
				//审批时,选择 1.同意 去除class中的required.
				
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
	
	<style>body{background-color: #f5f5f5;}</style>

	<form   action="#" method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${apply.id}">
		<input type="hidden" name="userId" value="${userId }">
		<input type="hidden" id="result" name="result">
		
		<fieldset>
			<legend><small>资源变更详情</small></legend>
			
			<dl class="dl-horizontal">
			
				<dt>标签名</dt>
				<dd>${serviceTag.name}&nbsp;</dd>
				
				<dt>状态</dt>
				<dd>
					<c:forEach var="map" items="${resourcesStatusMap }">
					 	<c:if test="${map.key == serviceTag.status }">${map.value }</c:if>
					</c:forEach>&nbsp;
				</dd>
				
				<dt>优先级</dt>
				<dd>
					<c:forEach var="map" items="${priorityMap }">
					 	<c:if test="${map.key == serviceTag.priority }">${map.value }</c:if>
					</c:forEach>&nbsp;
				</dd>
				
				<dt>服务开始时间</dt>
				<dd>${serviceTag.serviceStart}&nbsp;</dd>
				
				<dt>服务结束时间</dt>
				<dd>${serviceTag.serviceEnd}&nbsp;</dd>
				
				<dt>用途描述</dt>
				<dd>${serviceTag.description}&nbsp;</dd>
				
				<dt>创建日期</dt>
				<dd><fmt:formatDate value="${serviceTag.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />&nbsp;</dd>
				</dl>
		
				<c:forEach var="resource" items="${resourcesList }">
					
					<c:forEach var="change" items="${resource.changes }">
					
						<p>变更资源标识符&nbsp;<strong>${resource.serviceIdentifier}</strong> &nbsp;&nbsp; 变更描述&nbsp;${change.description }</p>
						
						<table class="table table-bordered">
				            <thead><tr><th class="span2">变更项</th><th class="span2">旧值</th><th class="span2">新值</th></tr></thead>
				            <tbody>
				            
				            <c:forEach var="item" items="${change.changeItems }">
				            
								<tr>
								
									<td>${item.fieldName}</td>
									
									<!-- 实例Compute -->
	            					<c:if test="${resource.serviceType == 1 || resource.serviceType == 2 }">
									
										<td class="is-hidden">
											<c:choose>
												<c:when test="${item.fieldName == '操作系统'}">
													<c:forEach var="map" items="${osTypeMap }"><c:if test="${map.key == item.oldValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '操作位数'}">
													<c:forEach var="map" items="${osBitMap }"><c:if test="${map.key == item.oldValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '规格' && resource.serviceType == 1}">
													<c:forEach var="map" items="${pcsServerTypeMap }"><c:if test="${map.key == item.oldValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '规格' && resource.serviceType == 2}">
													<c:forEach var="map" items="${ecsServerTypeMap }"><c:if test="${map.key == item.oldValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:otherwise>${item.oldValue}</c:otherwise>
											</c:choose>
										</td>
										
										<td class="is-visible">
											<c:choose>
												<c:when test="${item.fieldName == '操作系统'}">
													<c:forEach var="map" items="${osTypeMap }"><c:if test="${map.key == item.newValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '操作位数'}">
													<c:forEach var="map" items="${osBitMap }"><c:if test="${map.key == item.newValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '规格' && resource.serviceType == 1}">
													<c:forEach var="map" items="${pcsServerTypeMap }"><c:if test="${map.key == item.newValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '规格' && resource.serviceType == 2}">
													<c:forEach var="map" items="${ecsServerTypeMap }"><c:if test="${map.key == item.newValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:otherwise>${item.newValue}</c:otherwise>
											</c:choose>
										</td>
									
									</c:if><!-- 实例Compute End -->
									
									<!-- 存储 storage  -->
					            	<c:if test="${resource.serviceType == 3 }">
					            		<td class="is-hidden">
					            			<c:choose>
												<c:when test="${item.fieldName == '存储类型'}">
													<c:forEach var="map" items="${storageTypeMap }"><c:if test="${map.key == item.oldValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '容量空间'}">${item.oldValue}GB</c:when>
												<c:otherwise>${item.newValue}</c:otherwise>
											</c:choose>
										</td>
										
										<td class="is-visible">
											<c:choose>
												<c:when test="${item.fieldName == '存储类型'}">
													<c:forEach var="map" items="${storageTypeMap }"><c:if test="${map.key == item.newValue }">${map.value }</c:if></c:forEach>
												</c:when>
												<c:when test="${item.fieldName == '容量空间'}">${item.newValue}GB</c:when>
												<c:otherwise>${item.newValue}</c:otherwise>
											</c:choose>
										</td>
					            	</c:if><!-- 存储 storage  End -->
									
								</tr>
								
							</c:forEach>
								
				            </tbody>
			            </table>
					 
					</c:forEach>
					
					<hr>
					
				</c:forEach>
				
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
			
			<form id="inputForm" action="${ctx}/audit/apply/${apply.id}" method="post">
			
				<div class="control-group">
					<label class="control-label" for="opinion">审批意见</label>
					<div class="controls">
						<textarea rows="3" id="opinion" name="opinion" placeholder="...审批意见" maxlength="45"></textarea>
					</div>
				</div>
			
				<div class="form-actions">
					<input class="btn" type="button" value="返回" onclick="history.back()">
					<c:forEach var="map" items="${auditResultMap}">
						<button class="auditResult btn btn-primary" value="${map.key}">${map.value}</button>
					</c:forEach>
				</div>
				
			</form>
		
		</fieldset>
		
	</form>
	
</body>
</html>
