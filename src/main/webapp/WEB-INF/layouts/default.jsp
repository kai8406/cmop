<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<!-- title Tag`s Template -->
<title>Sobey云平台服务${ctx }&mdash; <sitemesh:title/></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
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
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/static/bootstrap-modal/css/bootstrap-modal.css">
<link rel="stylesheet" href="${ctx}/static/jquery-uniform/css/uniform.default.min.css">
<link rel="stylesheet" href="${ctx}/static/jquery-ui/css/jquery-ui-1.10.1.custom.min.css">
<link rel="stylesheet" href="${ctx}/static/common/css/style.css">

<!-- Le javascript -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="${ctx}/static/jquery/jquery.min.js"><\/script>')</script>

<!-- head Tag`s Template -->
<sitemesh:head/>
</head>
<body>
	<div id="wrap">
		<%@ include file="/WEB-INF/layouts/navbar.jsp"%>
		<div class="container">
			<!--[if lt IE 7]>
        		<p class="chromeframe">您的浏览器不被支持！试试其他的：<a href="http://www.google.com/chrome">Google 浏览器</a>、<a href="http://firefox.com.cn/">火狐浏览器</a>、<a href="http://www.apple.com.cn/safari/">Safari</a>、<a href="http://www.google.com/chromeframe?hl=zh-CN">Google 浏览器框架</a></p>
	     	<![endif]-->
			<sitemesh:body/>
		</div>
		<div id="push"></div>
	</div>
	
	<footer id="footer">
		<div class="container">
			<p class="muted credit">Copyright &copy; 2013 Sobey</p>
		</div>
	</footer>
	
	 <!-- Le javascript -->
	 <script src="${ctx}/static/jquery-uniform/jquery.uniform.min.js"></script>
	 <script src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>
	 <script src="${ctx}/static/bootstrap-modal/js/bootstrap-modalmanager.js"></script>
	 <script src="${ctx}/static/bootstrap-modal/js/bootstrap-modal.js"></script>
	 <script src="${ctx}/static/jquery-ui/js/jquery-ui-1.10.1.custom.min.js"></script>
	 <script src="${ctx}/static/jquery-ui/js/jquery.ui.datepicker-zh-CN.js"></script>
	 <script src="${ctx}/static/jquery-validation/jquery.validate.js"></script>
	 <script src="${ctx}/static/jquery-validation/messages_zh.js"></script>
	 <script src="${ctx}/static/jquery-form/jquery.form.js"></script>
	 <script src="${ctx}/static/common/js/custom.js"></script>
	 <script src="${ctx}/static/common/js/custom.wizard.js"></script>
	
</body>
</html>