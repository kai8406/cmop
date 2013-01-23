<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>用户管理</title>
<script>
	$(document).ready(function() {
		$("ul#navbar li#user").addClass("active");
	});
</script>
</head>

<body>

	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>

	<form class="form-inline well well-small" action="#">

		<div class="row">

			<div class="span3">
				<label class="control-label search-text">登录名</label> <input type="text" name="search_LIKE_loginName" class="input-small" maxlength="45" 
					value="${param.search_LIKE_loginName}">
			</div>
			
			<div class="span3">
				<label class="control-label search-text">真实姓名</label> <input type="text" name="search_LIKE_name" class="input-small" maxlength="45" 
					value="${param.search_LIKE_name}">
			</div>
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
				<button class="btn tip-bottom options"  title="更多搜索条件" type="button"><i class="icon-resize-small"></i></button>
			</div>

		</div>
		
		<!-- 多个搜索条件的话,启用 div.options -->
		<!-- 
			<div class="row options" style="display: none;"></div>
		 -->

	</form>

	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>登录名</th>
				<th>用户名</th>
				<th>权限角色</th>
				<th>注册时间</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td><a href="update/${item.id}">${item.loginName}</a></td>
					<td>${item.name}</td>
					<td>${item.groupNames}</td>
					<td><fmt:formatDate value="${item.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
					<td><a href="delete/${item.id}">删除</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${page}" />

	<a class="btn" href="save/">创建用户</a>

</body>
</html>
