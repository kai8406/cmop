<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<title>帐号管理</title>
	<script>
		$(document).ready(function() {
			
			$("#group-tab").addClass("active");
			
			$("#message").fadeOut(5000);
			
		});
	</script>
</head>

<body>

	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>	
	</c:if>
	
		<a class="btn btn-info pager" href="${ctx }/account/group/save/">创建权限组</a>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<colgroup>
			<col class="span3">
			<col class="span7">
			<col class="span2">
		</colgroup>
		<thead>
			<tr>
				<th>名称</th>
				<th>授权</th>
				<th>操作</th>
			</tr>
		</thead>
		<c:forEach items="${page.content}" var="group">
			<tr>
				<td>${group.name}</td>
				<td>${group.permissionNames}</td>
				<td>
						<a href="update/${group.id}">修改</a> 
						<a href="delete/${group.id}">删除</a>
					<shiro:hasPermission name="group:edit">
					</shiro:hasPermission>	
				</td>
			</tr>
		</c:forEach>
	</table>

	<%@ include file="/WEB-INF/layouts/pageable.jsp"%>

</body>
</html>
