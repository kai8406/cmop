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
				<th>#</th>
				<th>IP地址</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${ecsList}" var="item">
				<tr>
					<td></td>
					<td>${item}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="form-actions">
		<input class="btn" type="button" value="返回" onclick="history.back()">
	</div>

</body>
</html>
