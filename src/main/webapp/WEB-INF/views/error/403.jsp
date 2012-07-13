<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>403 - 用户权限不足</title>
</head>

<body>
	<c:url value="/" var="homeUrl" />

	<div class="row span6 offset3">
		<div class="page-header">
			<h1>抱歉你没有访问该页面的权限!</h1>
		</div>

		<div>
			<a href="${homeUrl}" class="btn btn-primary">返回首页</a>
		</div>

	</div>

</body>
</html>
