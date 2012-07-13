<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<%
	response.setStatus(200);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>404 - 页面不存在</title>
<%@ include file="/WEB-INF/layouts/meta.jsp"%>
</head>

<body style="text-align:center;">
	<div class="container" style="margin-top:125px;">
		<div id="content" class="span12">
			<h3>您访问的页面不存在！</h3><br>
			<div>
				<a href="${ctx}" class="btn btn-primary">返回首页</a>
			</div>			
		</div>
	</div>
</body>
</html>