<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>ECS详情</title>
</head>

<body>

	<div class=" span8 offset2 form-horizontal">
		<dl class="dl-horizontal">

			<dt>申请类型:</dt>
			<dd>ECS申请</dd>

			<dt>主题:</dt>
			<dd>${apply.title}</dd>

			<dt>资源类型:</dt>
			<dd>
				<c:forEach var="map" items="${resourceTypeMap }">
					<c:if test="${apply.resourceType == map.key }">
						<c:out value="${map.value}" />
					</c:if>
				</c:forEach>
			</dd>

			<dt>申请起始时间:</dt>
			<dd>${apply.serviceStart } 至 ${apply.serviceEnd }</dd>

			<dt>申请用途:</dt>
			<dd>${apply.description }</dd>

			<hr>
			<dt>存储空间:</dt>
			<dd>${storageItem.storageSpace }GB</dd>

			<dt>Identifier:</dt>
			<dd>${storageItem.identifier}</dd>


			<c:if test="${not empty checkedComputeItem }">
				<hr>
				<dt>挂载实例:</dt>
				<c:forEach var="checked" items="${checkedComputeItem }">
					<dd>
					${checked[2]}
					
					
					<strong>操作系统:</strong>
					<c:forEach var="map" items="${osTypeMap }">
							<c:if test="${checked[3] == map.key }">
								<c:out value="${map.value}" />
							</c:if>
						</c:forEach>
						
						
						<c:forEach var="map" items="${osBitMap }">
							<c:if test="${checked[4] == map.key }">
								<c:out value="${map.value}" />
							</c:if>
						</c:forEach>
					
					
					<strong>规格:</strong>

						<c:forEach var="map" items="${serverTypeMap }">
							<c:if test="${checked[5] == map.key }">
								<c:out value="${map.value}" />
							</c:if>
						</c:forEach>
					
					
					
					</dd>
				</c:forEach>
			</c:if>
		</dl>

		<div class="form-actions">
			<input class="btn btn-large" type="button" value="返回"
				onclick="history.back()" />
		</div>


	</div>

</body>
</html>
