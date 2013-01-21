<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container-fluid">
		
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a> 
			
			<a class="brand" href="${ctx}/">Sobey`CMOP</a>

			<div class="nav-collapse collapse">

				<shiro:user>
				
					<ul class="nav">
						<shiro:hasPermission name="user:view">
							<li class="active"><a href="${ctx}/account/user/">用户管理</a></li>
						</shiro:hasPermission>

						<shiro:hasPermission name="group:view">
							<li class="active"><a href="${ctx}/account/group/">权限管理</a></li>
						</shiro:hasPermission>
					</ul>

					<ul class="nav pull-right">
						<li class="divider-vertical"></li>
						<li class="dropdown">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#"><shiro:principal property="name"/><b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="${ctx}/profile/">个人信息</a></li>
								<li class="divider"></li>
								<li><a href="${ctx}/logout">退出</a></li>
							</ul>
						</li>
					</ul>
					
				</shiro:user>

				<shiro:guest>
					<form class="navbar-form pull-right" action="${ctx}/login" method="post">
						<input class="span2" id="username" name="username" type="text" placeholder="Login name"> 
						<input class="span2" id="password" name="password" type="password" placeholder="Password">
						<button type="submit" class="btn">登录</button>
						<a type="submit" class="btn" href="${ctx}/register/">注册</a>
					</form>
				</shiro:guest>

			</div><!--/.nav-collapse -->

		</div>
	</div>
</div>