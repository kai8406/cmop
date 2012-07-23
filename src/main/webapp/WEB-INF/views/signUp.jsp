<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Sobey云平台服务 &mdash; 登录页</title>
	
	<%@ include file="/WEB-INF/layouts/meta.jsp"%>
 
	<script>
		$(document).ready(function() {
			
			$("#username").focus();
			
			$("#message").fadeOut(3000);
			
			$("#loginForm").validate();
		});
	</script>
</head>

<body>
<div class="container">


	<%@ include file="/WEB-INF/layouts/header.jsp"%>
	
	<div class="tab-content">
		<div class="span6 offset2">
		
			<fieldset>
			
				<legend>登录</legend>
				
				<%
					String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
					if(error != null){
				%>
						<div class="control-group"><div class="controls "><div id="message" class="alert alert-error"><button class="close" data-dismiss="alert">×</button>登录失败，请重试.</div></div></div>
				<%
					}
				%>
				
				<form:form id="loginForm" action="${ctx}/login" method="post" cssClass="form-horizontal">
							
					<div class="control-group">
						<label for="username" class="control-label">登录邮箱</label>
						<div class="controls">
							<input type="text" id="username" name="username" placeholder="Email"  size="50"  class="required input-large"/>
						</div>
					</div>
					<div class="control-group">
						<label for="password" class="control-label">密码</label>
						<div class="controls">
							<input type="password" id="password" name="password" placeholder="Password"  size="50"  class="required input-large"/>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
						<label class="checkbox inline" for="rememberMe"> <input type="checkbox" checked="checked" id="rememberMe" name="rememberMe"/> 记住我</label>
						</div>
					</div>
					
					<div class="form-actions">
						<input id="submit" class="btn-large btn-primary" type="submit" value="登录"/> 
					</div>
				</form:form>
			</fieldset>
		</div>
	</div>
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
</div>
</body>
</html>
