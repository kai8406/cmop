<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>审批</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#audit-tab").addClass("active");
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

	<!-- Search -->
	<form class="well well-small form-search" action="${ctx}/audit/">
		<div class="row-fluid show-grid">
			<div class="span4">
				<label class="control-label">标题:</label> <input type="text"
					id="title" name="title" class="input-medium">
			</div>

			<div class="span4">
				<label class="control-label">审核状态:</label> <select id="status"
					name="status" class="input-medium">
					<option value="0">待审核</option>
					<option value="1">已审核</option>
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
				<th>申请/变更标题</th>
				<th>创建时间</th>
				<th>审批状态</th>
				<th>操作</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td>${item.title}</td>
					<td><fmt:formatDate value="${item.createTime}" pattern ="yyyy-MM-dd HH:mm:ss" /></td>
					<td><c:choose>
							<c:when test="${item.status==4}">
								<span class="label label-success">已审核</span>
							</c:when>
							<c:otherwise>
								<span class="label label-important">待审核</span>
							</c:otherwise>
						</c:choose></td>
					<td><a href="${ctx}/audit/view/${item.id}">查看</a> <c:if
							test="${item.status!=4}">
							<a href="${ctx}/audit/create?applyId=${item.id}&user_id=${user.id}">审批</a>
						</c:if></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
</body>
</html>
