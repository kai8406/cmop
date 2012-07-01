<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title>Sobey云平台服务 &mdash; <sitemesh:title/></title>
 
 <!-- 导入JS和CSS -->
<%@ include file="/WEB-INF/layouts/meta.jsp"%>

<sitemesh:head/>
</head>

<body>
	<div class="container">
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
		<div id="content" class="span12">
			<sitemesh:body/>
		</div>
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
</body>
</html>