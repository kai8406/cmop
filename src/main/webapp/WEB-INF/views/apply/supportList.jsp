<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>服务申请管理</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#support-tab").addClass("active");

		$("#message").fadeOut(3000);
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
						<a href="${ctx }/apply/support/ecs/">ECS (${ecsCount})</a>
					</div>
					<div class="span2">
						<a href="${ctx }/apply/support/es3/">ES3 (${es3Count})</a>
					</div>
				</div>
			</div>

			<form class="well well-small form-search"
				action="${ctx }/apply/support/">

				<div class="row-fluid show-grid">

					<div class="span4">
						<label class="control-label">主题:</label> <input type="text"
							id="title" name="title" class="input-medium" />
					</div>

					<div class="span4">
						<label class="control-label">审核状态:</label> <select id="status"
							name="status" class="input-medium">
							<option value="0"></option>
							<c:forEach var="map" items="${applyStatusMap }">
							
								<option value="	<c:out value='${map.key}' />">
									<c:out value="${map.value}" />
								</option>
							</c:forEach>

						</select>
					</div>
					<div class="span2">
						<button class="btn" type="submit">Search</button>
					</div>
				</div>
			</form>


			<table class="table table-striped table-bordered table-condensed">
				<colgroup>
					<col class="span4">
					<col class="span2">
					<col class="span2">
					<col class="span2">
				</colgroup>
				<thead>
					<tr>
						<th>主题</th>
						<th>创建时间</th>
						<th>审核状态</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${page.content}" var="item">
						<tr>
							<td>${item.title}</td>
							<td>${item.createTime}</td>
							<td><c:if test="${item.status == 1 }">
									<span class="label label-important">待审核</span>
								</c:if> <c:if test="${item.status == 2 }">
									<span class="label label-warning">审核中</span>
								</c:if> <c:if test="${item.status == 4 }">
									<span class="label label-success">已审核</span>
								</c:if></td>
							<td><c:if test="${item.serviceType == 'ECS' }">
									<a href="${ctx }/apply/support/ecs/detail/${item.id}">查看</a>
									<a href="${ctx }/apply/support/ecs/update/${item.id}">修改</a>
								</c:if> <c:if test="${item.serviceType == 'ES3' }">
									<a href="${ctx }/apply/support/es3/detail/${item.id}">查看</a>
									<a href="${ctx }/apply/support/es3/update/${item.id}">修改</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
		</div>

	</div>

</body>
</html>
