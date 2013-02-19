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
			
			<a class="brand tip-bottom" title="首页" href="${ctx}/">CMOP</a>

			<div class="nav-collapse collapse">

				<shiro:user>
				
					<ul id="navbar" class="nav">
						<shiro:hasPermission name="apply:view">
							<li id="apply" class="tip-bottom" title="服务申请"><a href="${ctx}/apply/">服务申请</a></li>
							<li id="resource" class="tip-bottom" title="资源管理"><a href="${ctx}/resources/">资源管理</a></li>
							<li id="fault" class="tip-bottom" title="故障申报"><a href="${ctx}/failure/">故障申报</a></li>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="user:view">
							<li id="applyAudit" class="tip-bottom" title="申请审批"><a href="${ctx}/audit/apply/">申请审批</a></li>
							<li id="resourceAudit" class="tip-bottom" title="变更审批"><a href="${ctx}/audit/resources/">变更审批</a></li>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="operate:view">
							<li id="operate" class="tip-bottom" title="工单处理"><a href="${ctx}/operate/">工单处理</a></li>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="user:view">
							<li id="user" class="tip-bottom" title="用户管理"><a href="${ctx}/account/user/">用户管理</a></li>
						</shiro:hasPermission>

						<shiro:hasPermission name="group:view">
							<li id="group" class="tip-bottom" title="权限管理"><a href="${ctx}/account/group/">权限管理</a></li>
						</shiro:hasPermission>

						<shiro:hasPermission name="department:view">
							<li id="department" class="tip-bottom" title="部门管理"><a href="${ctx}/department/">部门管理</a></li>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="basicData:view">
							<li id="basicdata" class="tip-bottom" title="基础数据"><a href="${ctx}/basicdata/location/">基础数据</a></li>
						</shiro:hasPermission>
						
					</ul>

					<ul class="nav pull-right" >
						<li class="divider-vertical"></li>
						<li class="dropdown">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#"><shiro:principal property="name"/><b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="${ctx}/profile/">个人信息 </a></li>
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