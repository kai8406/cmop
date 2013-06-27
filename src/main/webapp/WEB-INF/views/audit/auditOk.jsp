<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

<title>审批通过</title>

<script>

$(document).ready(function() {
	
	alert('${message}');
	window.opener = null; 
	window.open("","_self");  
	window.close(); 
	
});
</script>
</head>
<!-- 
在Firefox地址栏里输入 about:config
在配置列表中找到dom.allow_scripts_to_close_windows
点右键的选切换把上面的false修改为true即可。  
注：默认是false，是为了防止脚本乱关窗口
参考：http://blog.csdn.net/bdstjk/article/details/7473748
-->
<body>
</body>
</html>
