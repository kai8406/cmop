<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="description" content="cmop v2.0">
<meta name="author" content="liukai">

<!-- Le Mobile Specific -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<!-- Le favicon and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="${ctx}/static/common/img/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="${ctx}/static/common/img/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="${ctx}/static/common/img/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="${ctx}/static/common/img/ico/apple-touch-icon-57-precomposed.png">
<link rel="shortcut icon" href="${ctx}/static/common/img/ico/favicon.png">

<!-- Le css styles ==================================================== -->
<link href="${ctx}/static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<style type="text/css">
	body {
	  padding-top: 60px;
	  padding-bottom: 40px;
	}
</style>
<link href="${ctx}/static/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="${ctx}/static/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet">
<link href="${ctx}/static/jquery-uniform/css/uniform.default.min.css" rel="stylesheet">
<link href="${ctx}/static/jquery-ui/css/jquery-ui-1.10.1.custom.min.css" rel="stylesheet">
<link href="${ctx}/static/common/css/style.css" rel="stylesheet">

<!--[if lt IE 7 ]><![endif]-->
<!--[if IE 8 ]><![endif]-->
<!--[if IE 9 ]><![endif]-->

 <!-- Le javascript  ================================================== -->
 <script src="${ctx}/static/jquery/jquery.min.js"></script>
 <script src="${ctx}/static/jquery-uniform/jquery.uniform.min.js"></script>
 <script src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>
 <script src="${ctx}/static/bootstrap-modal/js/bootstrap-modalmanager.js"></script>
 <script src="${ctx}/static/bootstrap-modal/js/bootstrap-modal.js"></script>
 <script src="${ctx}/static/jquery-ui/js/jquery-ui-1.10.1.custom.min.js"></script>
 <script src="${ctx}/static/jquery-ui/js/jquery.ui.datepicker-zh-CN.js"></script>
 <script src="${ctx}/static/jquery-validation/jquery.validate.js"></script>
 <script src="${ctx}/static/jquery-validation/messages_zh.js"></script>
 <script src="${ctx}/static/common/js/custom.js"></script>
 <script src="${ctx}/static/common/js/custom.wizard.js"></script>

<!-- head Tag`s Template -->
<sitemesh:head/>

<!-- title Tag`s Template -->
<title>Sobey云平台服务${ctx }&mdash; <sitemesh:title/></title>

</head>

<body>
	
	<%@ include file="/WEB-INF/layouts/navbar.jsp"%>
	
	<div class="container">
		<div id="content">
			<sitemesh:body/>
		</div>
	</div>
	
	<footer style="text-align: center;">
		<hr>
		<p>Copyright &copy; 2013 Sobey</p>
	</footer>
	
</body>
</html>