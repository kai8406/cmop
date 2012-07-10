<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>用户管理</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#support-tab").addClass("active");

	});
</script>
</head>

<body>

	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>
			${message}
		</div>
	</c:if>

	<div class="row">

		<%@ include file="/WEB-INF/layouts/left.jsp"%>

		<div id="main" class="span10">

			<div id="myResources">
				<h3>我的资源</h3>
				<div class="row page-header">
					<div class="span2">
						<a href="#">ECS 2 </a>
					</div>
					<div class="span2">
						<a href="#">ES3 5</a>
					</div>
				</div>
			</div>

			<!-- Search -->
			<form class="well well-small form-search"
				action="${ctx }/apply/support">

				<div class="row-fluid show-grid">

					<div class="span4">
						<label class="control-label">主题:</label> <input type="text"
							id="title" name="title" placeholder="服务申请主题" class="input-medium">
					</div>

					<div class="span4">
						<label class="control-label">审核状态:</label> <select id="status"
							name="status" class="input-mini">
							<option></option>
							<option value="1">待审核</option>
							<option value="2">审核中</option>
							<option value="3">已审核</option>
							<option value="4">已退回</option>
						</select>
					</div>
					<div class="span2">
						<button class="btn" type="submit">Search</button>
					</div>
				</div>
			</form>


			<!-- Table -->
			<table class="table table-striped table-bordered table-condensed">
				<colgroup>
					<col class="span4">
					<col class="span2">
					<col class="span2">
					<col class="span2">
				</colgroup>
				<thead>
					<tr>
						<th>服务申请主题</th>
						<th>创建时间</th>
						<th>审核状态</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${page.content}" var="apply">
						<tr>
							<td>${apply.title}</td>
							<td>${apply.createTime}</td>
							<td><c:if test="${apply.status == 1 }">
									<span class="label label-important">待审核</span>
								</c:if> <c:if test="${apply.status == 2 }">
									<span class="label label-warning">审核中</span>
								</c:if> <c:if test="${apply.status == 3 }">
									<span class="label label-success">已审核</span>
								</c:if></td>
							<td><a href="./SupportDetail.html">查看</a> <c:if
									test="${apply.serviceType == 'ECS' }">
									<a href="${ctx }/apply/support/ecs/update/${apply.id}">修改</a>
								</c:if> <c:if test="${apply.serviceType == 'ES3' }">
									<a href="${ctx }/apply/support/es3/update/${apply.id}">修改</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<!-- Pagination -->
			<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
		</div>

	</div>


</body>
</html>
