<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>审批通过</title>
<script>
$(document).ready(function() {
	//聚焦指定的Tab
	$("#audit-tab").addClass("active");
	alert('${message}');
	window.opener = null; 
	window.open("","_self");  
	window.close(); 
});
</script>
</head>

<body>
</body>
</html>
