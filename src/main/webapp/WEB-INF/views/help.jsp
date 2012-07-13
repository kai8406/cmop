<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sobey--云平台服务-首页</title>
<script>
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#help-tab").addClass("active");
	});
</script>
</head>

<body>
	<div class="container">
		<div class="row">
			<!-- Support -->
			<div class="span3">
				<h2>服务申请</h2>
				<p>
				<ul>
					<li>点击主页上 <span class="label label-info">服务申请</span>
						标签可以进入服务申请列表.
					</li>
					<li>可以根据自身的需求情况创建一个服务申请.</li>
					<li>点击 <span class="label">查看</span>,可查看指定服务申请的详细信息.
					</li>
					<li>可通过页面的 <span class="label label-important">待审核</span>,<span
						class="label label-warning">审核中</span>, <span
						class="label label-success">已审核</span> 等申请流程状态,判断服务申请流程的情况.
					</li>
					<li>点击申请流程的状态标签,可查看具体的审核进度.</li>
					<li>可根据条件进行查询.</li>
				</ul>
				</p>
				<p>
					<a href="./SupportList.html" class="btn">更多 »</a>
				</p>
			</div>


			<!-- Feature -->
			<div class="span3">
				<h2>服务变更</h2>
				<p>
				<ul>
					<li>点击主页上 <span class="label label-info">服务变更</span>
						标签可以进入服务变更列表.
					</li>
					<li>可以根据自身的需求在服务申请列表对指定的服务申请进行服务变更.</li>
					<li>点击 <span class="label">查看</span>,可查看指定服务变更的详细信息.
					</li>
					<li>可通过页面的 <span class="label label-important">待审核</span>,<span
						class="label label-warning">审核中</span>, <span
						class="label label-success">已审核</span> 等服务变更流程的状态,判断服务的情况.
					</li>
					<li>点击变更流程的状态,可查看具体的审核进度.</li>
					<li>可根据条件进行查询.</li>
				</ul>
				</p>
				<p>
					<a href="./FeatureList.html" class="btn">更多 »</a>
				</p>
			</div>

			<!-- Bug -->
			<div class="span3">
				<h2>故障申报</h2>
				<p>
				<ul>
					<li>点击主页上 <span class="label label-info">故障申报</span>
						标签可以进入故障申报列表.
					</li>
					<li>可以对发现的故障进行申报.系统管理员会第一时间进行处理.</li>
					<li>点击 <span class="label">查看</span>,可查看指定故障申报的详细信息.
					</li>
					<li>可根据条件进行查询.</li>
				</ul>
				</p>

				<p>
					<a href="./BugList.html" class="btn">更多 »</a>
				</p>
			</div>

			<!-- Audit -->
			<div class="span3">
				<h2>服务审核</h2>
				<p>
					您的需要审核的申请有 <span class="label label-inverse">2</span> 条.
				</p>
				<p>
				<ul>
					<li>点击主页上 <span class="label label-info">审核</span>
						标签可以进入故障申报列表.
					</li>
					<li>点击 <span class="label">审核</span>,可进入指定服务申请或服务变更的审核页面中.
					</li>
					<li>填写审核意见,再选择是否审核通过.
						<ul>
							<li>审核通过,服务申请或服务变更进入下一步审核流程,直到审核通过.</li>
							<li>审核未通过,将终止审核流程.</li>
						</ul>
					</li>
					<li>可根据条件进行查询.</li>
				</ul>
				</p>
				<p>
					<a href="./AuditList.html" class="btn">更多 »</a>
				</p>

			</div>
		</div>
	</div>
</body>
</html>