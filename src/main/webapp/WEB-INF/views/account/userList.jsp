<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>帐号管理</title>
<script>
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#user-tab").addClass("active");
		
		$("#message").fadeOut(5000);
	});
</script>
</head>

<body>
	
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
 	<shiro:hasPermission name="user:edit">
		<a class="btn btn-info pager" href="create">故障申报</a>
	</shiro:hasPermission>
	
	<!-- Search -->
		<form class="well form-search" action="./FeatureList.html">

			<div class="row-fluid rowshow-grid">

				<div class="span3">
					<label class="control-label">主题:</label> <input type="text" id="title" name="title"
						placeholder="故障申报主题" class="input-medium">
				</div>

				<div class="span3">
					<label class="control-label">创建日期:</label> <input type="text" id="" name=""
						placeholder="创建日期" class="input-medium">
				</div>

				<div class="span3">
					<button class="btn" type="submit">Search</button>
				</div>

			</div>
		</form>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<colgroup>
			<col class="span2">
			<col class="span2">
			<col class="span2">
			<col class="span4">
			<col class="span2">
		</colgroup>
		<thead>
			<tr>
				<th>登录名</th>
				<th>用户名</th>
				<th>邮箱</th>
				<th>权限组
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="user">
				<tr>
					<td>${user.loginName}</td>
					<td>${user.name}</td>
					<td>${user.email}</td>
					<td>${user.groupNames}</td>
					<td><shiro:hasPermission name="user:edit">
							<a class="btn btn-primary" href="update/${user.id}">修改</a>
							<a class="btn" href="delete/${user.id}">删除</a>
						</shiro:hasPermission></td>	
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
        

</body>
</html>
