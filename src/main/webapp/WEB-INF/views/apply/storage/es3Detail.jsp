<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>ECS详情</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#support-tab").addClass("active");
	});
</script>

</head>

<body>
	<div class="row">
		<div class=" span8 offset2 form-horizontal">

			<fieldset>
				<legend>ES3资源申请详情</legend>
				<dl class="dl-horizontal">
					<dt>申请类型:</dt>
					<dd>ECS申请</dd>

					<dt>申请时间:</dt>
					<dd>
						<fmt:formatDate value="${apply.createTime}"
							pattern="yyyy-MM-dd HH:mm:ss" />
					</dd>

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

					<c:forEach var="item" items="${checkedComputeItem }">

						<hr>

						<dt>存储空间:</dt>
						<dd>${item[2] }GB</dd>

						<dt>存储类型</dt>
						<dd>
							<c:forEach var="map" items="${storeageTypeMap }">
								<c:if test="${item[3] == map.key }">
									<c:out value="${map.value}" />
								</c:if>
							</c:forEach>
						</dd>


						<dt>Identifier:</dt>
						<dd>${item[1] }</dd>

						<dt>绑定计算资源:</dt>
						<dd>${item[5] }</dd>

					</c:forEach>


				</dl>

			</fieldset>
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()" />
			</div>

		</div>
	</div>

</body>
</html>
