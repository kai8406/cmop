<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>基础数据-服务器管理</title>
<script>
	$(document).ready(function() {
		$("ul#navbar li#basicdata, li#hostServer").addClass("active");
	});
</script>
</head>

<body>
	<%@ include file="/WEB-INF/layouts/basicdataTab.jsp"%>

	<strong>${hostServer.displayName}下的虚拟机：</strong>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标识符</th>
				<th>基本信息(操作系统,位数,规格)</th>
				<th>备注</th>
				<th>IP地址</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ecsList}" var="item">
				<tr>
					<td>${item.identifier}</td>
					<td><c:forEach var="map" items="${osTypeMap}"><c:if test="${item.osType == map.key}">${map.value}</c:if></c:forEach>&nbsp;&nbsp;&nbsp;
						<c:forEach var="map" items="${osBitMap}"><c:if test="${item.osBit == map.key}">${map.value}</c:if></c:forEach>&nbsp;&nbsp;&nbsp;
						<c:choose>
							<c:when test="${item.computeType == 1}">
								<c:forEach var="map" items="${pcsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach var="map" items="${ecsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach>
							</c:otherwise>
						</c:choose>
					</td>
					<td>${item.remark}</td>
					<td>${item.innerIp}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="form-actions">
		<input class="btn" type="button" value="返回" onclick="history.back()">
	</div>

</body>
</html>
