<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>

<title>登录</title>

<script type="text/javascript">

	$(document).ready(function(){
		 
		$("#signinForm").validate({
			rules:{
				username:{
					required: true,
					maxlength:20
				},
				password:{
					required:true,
				//	minlength:6,
					maxlength:20
				}
			},
			errorClass: "help-inline",
			errorElement: "span",
			highlight:function(element, errorClass, validClass) {
				$(element).parents('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).parents('.control-group').removeClass('error');
			}
		});
		 
	});
	
 </script>
 
</head>
<body>
<style type="text/css">
	body {
	  background-color: #f5f5f5;
	}
</style>

	<form id="signinForm" class="form-signin" action="${ctx}/login" method="post">
		
		<c:if test="${not empty message }">
			<div class="alert fade in">
	            <button data-dismiss="alert" class="close" type="button">×</button>
	            <strong>${username }</strong> 欢迎你,请登录.
	          </div>
		</c:if>
		
		<p>
			<%
				String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
				if(error != null){
			%>
					<span class="text-error">请输入正确的登录名或登录密码</span> 
			<%
				}
			%>
			<span class="pull-right"><a	href="${ctx}/register/">注册</a></span>
		</p>

		<div class="control-group">
			<div class="controls">
				<input type="text"  name="username" value="${username}" class="input-block-level" placeholder="Login name" >
			</div>
		</div>
		
		<div class="control-group">
			<div class="controls">
				<input type="password" name="password" class="input-block-level" placeholder="Password">
			</div>
		</div>
		
		<a class="btn" onclick="history.back()">返回</a>
		<button class="btn btn-primary" type="submit">登录</button>
		
	</form>
	
</body>
</html>