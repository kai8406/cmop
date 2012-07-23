<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>登录</title>

<%@ include file="/WEB-INF/layouts/meta.jsp"%>

<script>
	$(document).ready(function() {
		$("#username").focus();
	});
</script>
</head>

<body>

	<div class="container">

		<!-- Header -->
		<div id="logo" class="page-header row">
			<div class="span4">
				<h1>
					Sobey<small>--云平台管理系统</small>
				</h1>
			</div>

			<!-- 登录框 -->
			<form:form id="loginForm" action="${ctx}/login" method="post"
				class="well well-small form-inline pull-right">
				<input type="text" id="username" name="username" size="50"
					class="input-small" placeholder="Email" />

				<input type="password" id="password" name="password" size="50"
					class="input-small" placeholder="Password" />

				<label class="checkbox"> <input type="checkbox"
					checked="checked" id="rememberMe" name="rememberMe" /> 记住我
				</label>

				<button type="submit" class="btn">登录</button>

				<a class="btn btn-small btn-primary "
					href="${ctx }/account/user/regist">注册</a>
			</form:form>
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
		<div class="row">
			<div class="span4">
				<h2>说明1</h2>
				<p>The best part about forms in Bootstrap is that all your
					inputs and controls look great no matter how you build them in your
					markup. No superfluous HTML is required, but we provide the
					patterns for those who require it.</p>
				<p>More complicated layouts come with succinct and scalable
					classes for easy styling and event binding, so you're covered at
					every step.</p>
			</div>
			<div class="span4">
				<h2>说明2</h2>
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
				<h2>说明3</h2>
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

		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>

</body>
</html>
