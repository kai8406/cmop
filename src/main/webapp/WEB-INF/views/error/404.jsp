<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	response.setStatus(200);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>404 - 页面不存在</title>
</head>

<body>
	<div>

		<c:url value="/" var="homeUrl" />

		<div class="row span6 offset3">
			<div class="page-header">
				<h1>页面不存在!</h1>
			</div>

			<div>
				<a href="${homeUrl}" class="btn btn-primary">返回首页</a>
			</div>

		</div>
	</div>
</body>
</html>