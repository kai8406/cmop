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

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form" style="max-width: 100%">
		
		<fieldset>
		
			<legend>
				<small><strong>${hostServer.displayName}</strong> 下的虚拟机</small>
			</legend>
			
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>标识符</th>
						<th>基本信息(操作系统,位数,规格)</th>
						<th>备注</th>
						<th>IP地址</th>
						<th>所属宿主机/物理机</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ecsList}" var="item">
					
						<input type="hidden" name="computeIds" value="${item.id}">
						
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
							<td>
								<c:if test="${item.computeType==1}">
									<select name="serverAlias" class="required">
										<c:forEach var="map" items="${server}">
											<option value="<c:out value='${map.key}' />" 
												<c:if test="${hostServer.alias==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
											</option>
										</c:forEach>
									</select> 
								</c:if>
								<c:if test="${item.computeType==2}">
									<select name="serverAlias" class="required">
										<c:forEach var="map" items="${vm}">
											<option value="<c:out value='${map.key}' />" 
												<c:if test="${hostServer.alias==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
											</option>
										</c:forEach>
									</select> 
								</c:if>
							</td>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>

</body>
</html>
