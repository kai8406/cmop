<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>服务申请管理</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#support-tab").addClass("active");
		/* 
		$("#es3-bar").addClass("active");
		$("#es3-icon").addClass("icon-white");
		 */
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

			<form class="well well-small form-search"
				action="${ctx }/apply/support/es3/">

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
					<col class="span4">
				</colgroup>
				<thead>
					<tr>
						<th>标识符</th>
						<th>存储空间(GB)</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${page.content}" var="item">
						<tr>
							<td>${item.identifier}</td>
							<td>${item.storageSpace}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
		</div>

	</div>

</body>
</html>
