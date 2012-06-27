<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
