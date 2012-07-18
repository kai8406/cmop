<%@ page language="java" pageEncoding="UTF-8"%>
<div id="header">
	<div id="logo" class="page-header">
		<h1>
			<a href="${ctx}/">Sobey<small>&mdash;云平台服务</small></a>
		</h1>
		<shiro:user>
			<span class="pull-right">
				Hello, <shiro:principal	property="name" />! &nbsp;<a href="${ctx}/logout">退出登录</a>
			</span>
			
		</shiro:user>
	</div>

	<div id="menu">
		<ul class="nav nav-tabs">
			<li id="index-tab"><a href="${ctx}/">首页</a></li>
			<shiro:user>
				<shiro:hasPermission name="apply:view">
					<li id="support-tab"><a href="${ctx}/apply/support/">服务申请</a></li>
					<!-- 
					<li id="feature-tab"><a href="${ctx}/apply/feature/">服务变更</a></li>
					 -->
					<li id="fault-tab"><a href="${ctx}/apply/fault/">故障申报</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="audit:view">
					<li id="audit-tab"><a href="${ctx}/audit/">审核</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="user:view">
					<li id="user-tab"><a href="${ctx}/account/user/">用户列表</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="group:view">
					<li id="group-tab"><a href="${ctx}/account/group/">权限组</a></li>
				</shiro:hasPermission>
				<li id="help-tab"><a href="${ctx}/help">帮助</a></li>
			</shiro:user>
			<shiro:guest>
				<li class="active"><a href="${ctx}/login">登录</a></li>
			</shiro:guest>
		</ul>

	</div>
</div>