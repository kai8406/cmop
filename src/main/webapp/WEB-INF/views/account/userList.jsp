<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>用户管理</title>
<script>
	$(document).ready(function() {
		
		//active tab
		$("#user-tab").addClass("active");
		
		$("#message").fadeOut(5000);
		
	});
</script>
</head>

<body>
	
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
		<a class="btn btn-info pager" href="${ctx }/account/user/save/">创建用户</a>
 	<shiro:hasPermission name="user:edit">
	</shiro:hasPermission>
	
		<form class="well well-small form-search" action="${ctx}/account/user/">

			<div class="row-fluid rowshow-grid">

				<div class="span3">
					<label class="control-label">名称:</label> <input type="text" id="name" name="name"  class="input-medium">
				</div>

				<div class="span3">
					<button class="btn" type="submit">Search</button>
				</div>

			</div>
		</form>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<colgroup>
			<col class="span3">
			<col class="span3">
			<col class="span4">
			<col class="span2">
		</colgroup>
		<thead>
			<tr>
				<th>登录邮箱</th>
				<th>用户名</th>
				<th>权限组
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="user">
				<tr>
					<td>${user.email}</td>
					<td>${user.name}</td>
					<td>${user.groupNames}</td>
					<td>
							<a href="${ctx }/account/user/update/${user.id}">修改</a>
							<a href="delete/${user.id}">删除</a>
						</td>	
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
        
</body>
</html>
