<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>登录</title>

	<script>
		$(document).ready(function(){
			 
			$("#inputForm").validate({
				rules:{
					username:{
						required: true,
						maxlength:20
					},
					password:{
						required:true,
					 	minlength:6,
						maxlength:20
					}
				},
				errorClass: "help-inline",
				errorElement: "span",
				highlight:function(element, errorClass, validClass) {
					$(element).closest('.control-group').addClass('error');
				},
				unhighlight: function(element, errorClass, validClass) {
					$(element).closest('.control-group').removeClass('error');
				}
			});
			 
		});
	</script>
 
</head>
<body>

	<form id="inputForm" class="input-form" action="${ctx}/login" method="post" style="max-width: 300px;">
		
		<p>
		
			<c:if test="${not empty message }"><span class="text-success">${message } </span></c:if>
			
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
				<input type="text"  name="username" value="${username}" class="input-block-level" placeholder="Login name">
			</div>
		</div>
		
		<div class="control-group">
			<div class="controls">
				<input type="password" name="password" class="input-block-level" placeholder="Password">
			</div>
		</div>
		
		<a class="btn" onclick="history.back()">返回</a>
		<input type="submit" class="btn btn-primary" value="登录">
		
	</form>
	
</body>
</html>