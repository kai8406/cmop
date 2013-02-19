<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>提交变更</title>
	
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
					
						<p><strong>${resource.serviceIdentifier}</strong> &nbsp;${change.description }</p>
						
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
					            	
					            	</c:if><!-- 存储 storage  End -->
									
								</tr>
								
							</c:forEach>
								
				            </tbody>
			            </table>
					 
					</c:forEach>
					
					<hr>
					
				</c:forEach>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
