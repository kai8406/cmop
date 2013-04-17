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
			<legend><small>服务标签变更详情</small></legend>
			
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
				
				<hr>
		
				<c:forEach var="resource" items="${resourcesList }">
					
					<c:forEach var="change" items="${resource.changes }">
					
						<p>变更资源标识符&nbsp;<strong>${resource.serviceIdentifier}<c:if test="${resource.ipAddress != DEFAULT_IPADDRESS }">(${ resource.ipAddress })</c:if></strong> &nbsp;&nbsp; 变更描述:&nbsp;${change.description }</p>
						
						<table class="table table-bordered">
				            <thead>
				            	<tr>
					            	<th class="span2">变更项<c:if test="${not empty change.subResourcesId }">(服务子项ID:${change.subResourcesId })</c:if></th>
									<th class="span2">旧值</th>
									<th class="span2">新值</th>
								</tr>
							</thead>
				            <tbody>
					            <c:forEach var="item" items="${change.changeItems }">
									<tr>
										<td>${item.fieldName}</td>
										<td class="is-hidden">${item.oldString}</td>
										<td class="is-visible">${item.newString}</td>
									</tr>
								</c:forEach>
				            </tbody>
			            </table>
					 
					</c:forEach>
					
					<hr>
					
				</c:forEach>
			
				<div class="form-actions">
					<input class="btn" type="button" value="返回" onclick="history.back()">
					
					<c:forEach var="allowStatus" items="${allowResourcesStatus }">
					<c:if test="${ item.status == allowStatus }">
					<a class="btn btn-primary" href="#commitModal${item.id}" data-toggle="modal">提交变更</a>
					<div id="commitModal${item.id }" class="modal hide fade " tabindex="-1" data-width="250">
						<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
						<div class="modal-body">是否提交变更?</div>
						<div class="modal-footer">
							<button class="btn" data-dismiss="modal">关闭</button>
							<a href="${ctx}/serviceTag/commit/${serviceTag.id}" class="btn btn-primary loading">确定</a>
						</div>
					</div>
					</c:if>
					</c:forEach>
					
				</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
