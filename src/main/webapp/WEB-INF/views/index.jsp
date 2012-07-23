<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>首页</title>
<script>
$(document).ready(function() {
	//聚焦指定的Tab
	$("#index-tab").addClass("active");
	
	$("#message").fadeOut(5000);
});
</script>
</head>

<body>
	
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
   各种服务介绍
</body>
</html>
