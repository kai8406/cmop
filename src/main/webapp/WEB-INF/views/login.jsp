<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Sobey--云平台服务-登录</title>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link href="${ctx}/static/bootstrap/2.0.3/css/bootstrap.min.css"
	type="text/css" rel="stylesheet" />
<link
	href="${ctx}/static/bootstrap/2.0.3/css/bootstrap-responsive.min.css"
	type="text/css" rel="stylesheet" />
<link href="${ctx}/static/jquery-validation/1.9.0/validate.css"
	type="text/css" rel="stylesheet" />
<link href="${ctx}/static/mini-web.css" type="text/css" rel="stylesheet" />


<script src="${ctx}/static/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
<script
	src="${ctx}/static/jquery-validation/1.9.0/jquery.validate.min.js"
	type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.9.0/messages_cn.js"
	type="text/javascript"></script>
<script src="${ctx}/static/bootstrap/2.0.3/js/bootstrap.min.js"
	type="text/javascript"></script>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
     <script src="${ctx}/static/bootstrap/2.0.3/js/html5.js"></script>
 <![endif]-->

</head>

<body>

	<div class="container">

		<!-- Header -->
		<div class="page-header row show-grid">
			<div class="span4">
				<h1>
					Sobey<small>--云平台服务</small>
				</h1>
			</div>

			<!-- 登录框 -->
			<div class="span6 offset2">
				<form:form id="loginForm" action="${ctx}/login" method="post"
					class="well form-inline">

					<input type="text" id="username" name="username" size="50"
						value="${username}" class="input-small" placeholder="Email" />

					<input type="password" id="password" name="password" size="50"
						class="input-small" placeholder="Password" />

					<label class="checkbox"> <input type="checkbox"
						id="rememberMe" name="rememberMe" /> 记住我
					</label>

					<button type="submit" class="btn">登录</button>

					<a class="btn btn-small btn-primary " href="./Regist.html">注册</a>
				</form:form>
			</div>
		</div>


		<div class="hero-unit">
			<h1>Sobey 云平台</h1>
			<p>This is a template for a simple marketing or informational
				website. It includes a large callout called the hero unit and three
				supporting pieces of content. Use it as a starting point to create
				something more unique.</p>
			<p>
				<a class="btn btn-primary btn-large">Learn more »</a>
			</p>
		</div>

		<section>
		<div class="page-header">
			<h1>云平台说明</h1>
		</div>
		<div class="row">
			<div class="span4">
				<h2>优点1</h2>
				<p>The best part about forms in Bootstrap is that all your
					inputs and controls look great no matter how you build them in your
					markup. No superfluous HTML is required, but we provide the
					patterns for those who require it.</p>
				<p>More complicated layouts come with succinct and scalable
					classes for easy styling and event binding, so you're covered at
					every step.</p>
			</div>
			<div class="span4">
				<h2>优点2</h2>
				<p>Bootstrap comes with support for four types of form layouts:</p>
				<ul>
					<li>Vertical (default)</li>
					<li>Search</li>
					<li>Inline</li>
					<li>Horizontal</li>
				</ul>
				<p>Different types of form layouts require some changes to
					markup, but the controls themselves remain and behave the same.</p>
			</div>
			<div class="span4">
				<h2>优点3</h2>
				<p>Bootstrap's forms include styles for all the base form
					controls like input, textarea, and select you'd expect. But it also
					comes with a number of custom components like appended and
					prepended inputs and support for lists of checkboxes.</p>
				<p>States like error, warning, and success are included for each
					type of form control. Also included are styles for disabled
					controls.</p>
			</div>
		</div>
		</section>


		<section>
		<div class="page-header">
			<h1>云平台说明2</h1>
		</div>
		<div class="row">
			<div class="span4">
				<h2>优点1</h2>
				<p>The best part about forms in Bootstrap is that all your
					inputs and controls look great no matter how you build them in your
					markup. No superfluous HTML is required, but we provide the
					patterns for those who require it.</p>
				<p>More complicated layouts come with succinct and scalable
					classes for easy styling and event binding, so you're covered at
					every step.</p>
			</div>
			<div class="span4">
				<h2>优点2</h2>
				<p>Bootstrap comes with support for four types of form layouts:</p>
				<ul>
					<li>Vertical (default)</li>
					<li>Search</li>
					<li>Inline</li>
					<li>Horizontal</li>
				</ul>
				<p>Different types of form layouts require some changes to
					markup, but the controls themselves remain and behave the same.</p>
			</div>
			<div class="span4">
				<h2>优点3</h2>
				<p>Bootstrap's forms include styles for all the base form
					controls like input, textarea, and select you'd expect. But it also
					comes with a number of custom components like appended and
					prepended inputs and support for lists of checkboxes.</p>
				<p>States like error, warning, and success are included for each
					type of form control. Also included are styles for disabled
					controls.</p>
			</div>
		</div>
		</section>


		<!-- Footer -->
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>

</body>
</html>
