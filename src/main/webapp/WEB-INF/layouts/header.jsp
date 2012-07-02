<%@ page language="java" pageEncoding="UTF-8"%>
<div id="header">

	<div id="logo" class="page-header">
		<h1>
			Sobey<small>&mdash;云平台服务</small>
		</h1>
		<shiro:user>
			<span class="pull-right">
				Hello, <shiro:principal	property="name" />!
			</span>
		</shiro:user>
	</div>

	<div id="menu">
		<ul class="nav nav-tabs">
			<shiro:user>
					<li id="support-tab"><a href="${ctx }/apply/support/">服务申请</a></li>
				<shiro:hasPermission name="user:view">
					<li id="user-tab"><a href="${ctx}/account/user/">用户列表</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="group:view">
					<li id="group-tab"><a href="${ctx}/account/group/">权限组列表</a></li>
				</shiro:hasPermission>
				<li><a href="${ctx}/logout">退出登录</a></li>
			</shiro:user>
			<shiro:guest>
				<li class="active"><a href="${ctx}/login">登录</a></li>
			</shiro:guest>
		</ul>

	</div>
</div>