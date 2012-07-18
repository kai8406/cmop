<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>服务申请管理</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#support-tab").addClass("active");
 
		$("#ecs-bar").addClass("active");
		$("#ecs-icon").addClass("icon-white");
	 
		$("#message").fadeOut(5000);
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
				
			<a class="btn btn-info pager"  href="${ctx}/apply/support/ecs/save/">创建ECS</a>
				
			<form class="well well-small form-search"
				action="${ctx }/apply/support/ecs/">

				<div class="row-fluid show-grid">

					<div class="span4">
						<label class="control-label">标识符:</label> <input type="text"
							id="identifier" name="identifier" class="input-medium" />
					</div>
					<div class="span2">
						<button class="btn" type="submit">Search</button>
					</div>
				</div>
			</form>


			<table class="table table-striped table-bordered table-condensed">
				<colgroup>
					<col class="span2">
					<col class="span2">
					<col class="span2">
					<col class="span4">
				</colgroup>
				<thead>
					<tr>
						<th>标识符</th>
						<th>操作系统</th>
						<th>位数</th>
						<th>规格</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${page.content}" var="item">
						<tr>
							<td>${item.identifier}</td>

							<td><c:if test="${item.osType == 1 }">Windwos2003R2
								</c:if> <c:if test="${item.osType == 2 }">Windwos2008R2
								</c:if> <c:if test="${item.osType == 3 }">Centos5.6
								</c:if> <c:if test="${item.osType == 4 }">Centos6.3
								</c:if></td>

							<td><c:if test="${item.osBit == 1 }">32 Bit
								</c:if> <c:if test="${item.osBit == 2 }">64 Bit
								</c:if></td>

							<td><c:if test="${item.serverType == 1 }">Small &mdash; CPU[单核] Memory[1GB] Disk[20GB]
					</c:if> <c:if test="${item.serverType == 2 }">Middle &mdash; CPU[双核] Memory[2GB] Disk[20GB]
					</c:if> <c:if test="${item.serverType == 3 }">Large &mdash; CPU[四核] Memory[4GB] Disk[20GB]
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
