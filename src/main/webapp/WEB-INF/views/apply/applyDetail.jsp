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
	
	<style>body{background-color: #f5f5f5;}</style>

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
					<dt>PCS & ECS实例</dt>
					<c:forEach var="item" items="${apply.computeItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>用途信息</em>&nbsp;&nbsp;${item.remark}</dd>
						
						<dd>
							<em>基本信息</em>
							&nbsp;&nbsp;<c:forEach var="map" items="${osTypeMap}"><c:if test="${item.osType == map.key}">${map.value}</c:if></c:forEach>
							&nbsp;&nbsp;<c:forEach var="map" items="${osBitMap}"><c:if test="${item.osBit == map.key}">${map.value}</c:if></c:forEach>
							&nbsp;&nbsp;
							<c:choose>
								<c:when test="${item.computeType == 1}"><c:forEach var="map" items="${pcsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:when>
								<c:otherwise><c:forEach var="map" items="${ecsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:otherwise>
							</c:choose>
						</dd>
						
						<dd><em>关联ESG</em>&nbsp;&nbsp;${item.networkEsgItem.identifier}(${item.networkEsgItem.description})</dd>
						
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- 存储空间ES3 -->
				<c:if test="${not empty apply.storageItems}">
					<hr>
					<dt>ES3存储空间</dt>
					<c:forEach var="item" items="${apply.storageItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>存储类型</em>&nbsp;&nbsp;<c:forEach var="map" items="${storageTypeMap}"><c:if test="${item.storageType == map.key}">${map.value}</c:if></c:forEach></dd>
						
						<dd><em>容量空间</em>&nbsp;&nbsp;${item.space}&nbsp;GB</dd>
						
						<dd><em>挂载实例</em>&nbsp;&nbsp;${item.mountComputes}</dd>
						
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- 负载均衡器ELB -->
				<c:if test="${not empty apply.networkElbItems}">
					<hr>
					<dt>负载均衡器ELB</dt>
					<c:forEach var="item" items="${apply.networkElbItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>是否保持会话</em>&nbsp;<c:forEach var="map" items="${keepSessionMap}"><c:if test="${item.keepSession == map.key }">${map.value}</c:if></c:forEach></dd>
						
						<dd><em>关联实例</em>&nbsp; 
							<c:forEach var="compute" items="${allComputes}">
								<c:if test="${compute.networkElbItem.id == item.id }">${compute.identifier}
									<c:if test="${not empty compute.innerIp }">(${compute.innerIp})</c:if>&nbsp;&nbsp;
								</c:if>
							</c:forEach>
						</dd>
						
						<dd><em>端口映射（协议、源端口、目标端口）</em></dd>
						
						<c:forEach var="port" items="${item.elbPortItems }">
							<dd>&nbsp;&nbsp;${port.protocol}&nbsp;,&nbsp;${port.sourcePort}&nbsp;,&nbsp;${port.targetPort}</dd>
						</c:forEach>
							
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- IP地址EIP -->
				<c:if test="${not empty apply.networkEipItems}">
				
					<hr>
					<dt>EIP</dt>
					<c:forEach var="item" items="${apply.networkEipItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>ISP运营商</em>&nbsp;&nbsp;<c:forEach var="map" items="${ispTypeMap}"><c:if test="${item.ispType == map.key }">${map.value}</c:if></c:forEach></dd>
						
						<dd>
							<c:choose>
								<c:when test="${not empty item.computeItem }"><em>关联实例</em>&nbsp;&nbsp;${item.computeItem.identifier }(${item.computeItem.innerIp })</c:when>
								<c:otherwise><em>关联ELB</em>&nbsp;&nbsp;${item.networkElbItem.identifier }(${item.networkElbItem.virtualIp })</c:otherwise>
							</c:choose>
						</dd>
						
						<dd><em>端口映射（协议、源端口、目标端口）</em></dd>
						
						<c:forEach var="port" items="${item.eipPortItems }">
							<dd>&nbsp;&nbsp;${port.protocol}&nbsp;,&nbsp;${port.sourcePort}&nbsp;,&nbsp;${port.targetPort}</dd>
						</c:forEach>
							
						<br>
						
					</c:forEach>
				
				</c:if>
				
				<!-- DNS -->
				<c:if test="${not empty apply.networkDnsItems}">
				
					<hr>
					<dt>DNS域名映射</dt>
					<c:forEach var="item" items="${apply.networkDnsItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>域名</em>&nbsp;&nbsp;${item.domainName }</dd>
						
						<dd><em>域名类型</em>&nbsp;&nbsp;<c:forEach var="map" items="${domainTypeMap}"><c:if test="${item.domainType == map.key }">${map.value}</c:if></c:forEach></dd>
						
						<dd>
							<c:choose>
								<c:when test="${item.domainType != 3 }"><em>目标IP</em>&nbsp;&nbsp;${item.mountElbs }</c:when>
								<c:otherwise><em>CNAME域名</em>&nbsp;&nbsp;${item.cnameDomain }</c:otherwise>
							</c:choose>
						</dd>
						
						<br>
						
					</c:forEach>
					
				</c:if>
				
				<!-- 监控邮件列表 -->
				<c:if test="${not empty apply.monitorMails}">
					<hr>
					<dt>监控邮件列表</dt>
					<c:forEach var="item" items="${apply.monitorMails}"><dd>${item.email }</dd>	</c:forEach>
				</c:if>
				
				<!-- 监控手机列表 -->
				<c:if test="${not empty apply.monitorPhones}">
					<hr>
					<dt>监控手机列表</dt>
					<c:forEach var="item" items="${apply.monitorPhones}"><dd>${item.telephone }</dd></c:forEach>
				</c:if>
				
				<!-- 服务器监控monitorCompute -->
				<c:if test="${not empty apply.monitorComputes}"></c:if>
				
				<!-- ELB监控monitorElb -->
				<c:if test="${not empty apply.monitorElbs}">
				
					<hr>
					<dt>ELB监控</dt>
					<c:forEach var="item" items="${apply.monitorElbs}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>监控ELB</em>&nbsp;&nbsp;${item.networkElbItem.identifier }(${item.networkElbItem.virtualIp})</dd>
						
						<br>
						
					</c:forEach>
				</c:if>
				
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
