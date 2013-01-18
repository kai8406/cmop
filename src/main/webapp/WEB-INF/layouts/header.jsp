<%@ page language="java" pageEncoding="UTF-8"%>
<div id="header">
	<div id="logo" class="page-header">
		<h1>
			<a href="${ctx}/index/">Sobey<small>&mdash;云平台管理系统</small></a>
		</h1>
		<shiro:user>
			<div class="btn-group pull-right">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-user"></i> <shiro:principal property="name" /> <span
					class="caret"></span>
				</a>

				<ul class="dropdown-menu">
					<shiro:hasRole name="admin">
						<li><a href="${ctx}/admin/user">Admin Users</a></li>
						<li class="divider"></li>
					</shiro:hasRole>
					<li><a href="${ctx}/profile">Edit Profile</a></li>
					<li><a href="${ctx}/logout">Logout</a></li>
				</ul>
			</div>
		</shiro:user>
	</div>

	<div id="menu">
		<ul class="nav nav-tabs">

			<li id="index-tab"><a href="${ctx}/">首页</a></li>
			<li id="user-tab"><a href="${ctx}/account/user/">用户列表</a></li>
			<li id="group-tab"><a href="${ctx}/account/group/">权限组</a></li>

			<shiro:user>
				<shiro:hasPermission name="user:view">
				</shiro:hasPermission>
				<shiro:hasPermission name="group:view">
				</shiro:hasPermission>
			</shiro:user>

			<shiro:guest>
				<li class="active"><a href="${ctx}/">登录/注册</a></li>
			</shiro:guest>
		</ul>


	</div>
</div>