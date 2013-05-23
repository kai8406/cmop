<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源汇总</title>
	
	<script>
		$(document).ready(function() {
			$("ul#navbar li#summary").addClass("active");
		});
	</script>
	
</head>

<body>

	<!-- 资源汇总 -->
	<div class="widget-box">
		<div class="widget-title"><span class="icon"><i class="icon-signal"></i></span><h5>Summary</h5></div>
		<div class="widget-content">
			<div class="row">
			
				<div class="span3">
					<ul class="site-stats">
						<a href="${ctx}/summary/?search_EQ_serviceType=1"><li><i class="icon-calendar"></i> <strong>${PCSCOUNT}</strong> <small>PCS 物理机</small></li></a>
						<a href="${ctx}/summary/?search_EQ_serviceType=2"><li><i class="icon-tasks"></i> <strong>${ECSCOUNT}</strong> <small>ECS 虚拟机(实例)</small></li></a>
						<a href="${ctx}/summary/?search_EQ_serviceType=3"><li><i class="icon-hdd"></i> <strong>${ES3COUNT}</strong> <small>ES3 存储卷</small></li></a>
					</ul>
				</div>
				
				<div class="span3">
					<ul class="site-stats">
						<a href="${ctx}/summary/?search_EQ_serviceType=4"><li><i class="icon-random"></i> <strong>${ELBCOUNT}</strong> <small>ELB 负载均衡</small></li></a>
						<a href="${ctx}/summary/?search_EQ_serviceType=5"><li><i class="icon-screenshot"></i> <strong>${EIPCOUNT}</strong> <small>EIP 公网IP及端口映射</small></li></a>
						<a href="${ctx}/summary/?search_EQ_serviceType=6"><li><i class="icon-list"></i> <strong>${DNSCOUNT}</strong> <small>DNS 域名映射</small></li></a>
					</ul>
				</div>
				
				<div class="span3">
					<ul class="site-stats">
						<a href="${ctx}/summary/?search_EQ_serviceType=9"><li><i class="icon-eye-open"></i> <strong>${MONITOR_COMPUTECOUNT}</strong> <small>实例监控</small></li></a>
						<a href="${ctx}/summary/?search_EQ_serviceType=10"><li><i class="icon-eye-close"></i> <strong>${MONITOR_ELBCOUNT}</strong> <small>ELB监控</small></li></a>
					</ul>
				</div>
				
				<div class="span2">
					<ul class="site-stats">
						<a href="${ctx}/summary/?search_EQ_serviceType=8"><li><i class="icon-globe"></i> <strong>${MDNCOUNT}</strong> <small>MDN</small></li></a>
						<a href="${ctx}/summary/?search_EQ_serviceType=11"><li><i class="icon-inbox"></i> <strong>${CPCOUNT}</strong> <small>CP 云生产</small></li></a>
					</ul>
				</div>
				
			</div>							
		</div>
	</div>

	<form class="form-inline well well-small" action=".">

		<div class="row">

			<div class="span3">
				<label class="search-text">服务标签</label> 
				<input type="text" name="search_LIKE_serviceTag.name" class="span2" maxlength="45" 
					value="${param.search_LIKE_serviceTag.name}">
			</div>
			
			<div class="span3">
				<label class="search-text">状态</label> 
				<select name="search_EQ_status" class="span2">
					<option value="" >Choose...</option>
					<c:forEach var="map" items="${resourcesStatusMap}">
						<option value="${map.key}" 
							<c:if test="${map.key == param.search_EQ_status && param.search_EQ_status != ''}">
								selected="selected"
							</c:if>
						>${map.value}</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span3">
				<label class="search-text">IP地址</label> 
				<input type="text" name="search_EQ_ipAddress" class="span2" maxlength="45" 
					value="${param.search_EQ_ipAddress}">
			</div>
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
			</div>

		</div>
		
		<!-- 多个搜索条件的话,启用 div.options -->
		<div class="row options">
		
			<div class="span3">
				<label class="search-text">标识符</label> 
				<input type="text" name="search_LIKE_serviceIdentifier" class="span2" maxlength="45" 
					value="${param.search_LIKE_serviceIdentifier}">
			</div>
			
			<div class="span3">
				<label class="search-text">服务类型</label> 
				<select name="search_EQ_serviceType" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${resourcesServiceTypeMap}">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_serviceType}">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span3">
				<label class="search-text">申请人</label> 
				<input type="text" name="search_LIKE_user.name" class="span2" maxlength="45" 
					value="${param.search_LIKE_user.name}">
			</div>
			
		</div>

	</form>
	
	<div class="row">
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>

	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标识符</th>
				<th>服务标签</th>
				<th>服务类型</th>
				<th>IP地址</th>
				<th>状态</th>
				<th>申请时间</th>
				<th>申请人</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td><a href="detail/${item.id}">${item.serviceIdentifier}</a></td>
					<td>${item.serviceTag.name}</td>
					<td>
						<c:forEach var="map" items="${resourcesServiceTypeMap}">
							<c:if test="${map.key == item.serviceType}">${map.value}</c:if>
						</c:forEach>
					</td>
					<td>${item.ipAddress}</td>
					<td>
						<c:forEach var="map" items="${resourcesStatusMap }">
							<c:if test="${map.key == item.status }">
								<c:choose>
									<c:when test="${item.status == -1 }">
										<span class="label" style="background-color: #bbbbbb;">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 0 }">
										<span class="label">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 1 }">
										<span class="label label-warning tip-right" title="审批人: ${item.serviceTag.auditFlow.user.name }">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 2 }">
										<span class="label label-important tip-right" title="审批人: ${item.serviceTag.auditFlow.user.name }">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 3 }">
										<span class="label label-inverse">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 4 }">
										<span class="label status4">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 5 }">
										<span class="label status5">${map.value }</span>
									</c:when>
									
									<c:otherwise>
										<span class="label label-success">${map.value }</span>
									</c:otherwise>
									
								</c:choose>
							</c:if>
						</c:forEach>
						
					</td>
					<td><fmt:formatDate value="${item.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
					<td>${item.user.name}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

</body>
</html>
