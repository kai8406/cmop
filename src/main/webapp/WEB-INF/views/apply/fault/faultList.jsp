<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>故障申报</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#fault-tab").addClass("active");
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

	<a class="btn btn-info pager" href="${ctx}/apply/fault/save/">创建故障申报</a>


	<form class="well well-small form-search" action="${ctx}/apply/fault/">
		<div class="row-fluid show-grid">

			<div class="span4">
				<label class="control-label">主题:</label> <input type="text"
					id="title" name="title" class="input-medium" />
			</div>

			<div class="span4">
				<label class="control-label ">优先级:</label> <select id="level"
					name="level" class="input-medium">
					<option value="0"></option>
					<c:forEach var="map" items="${faultLevelMap}">
						<option value="<c:out value='${map.value}'/>"><c:out value="${map.value}" /></option>
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
				<th>标题</th>
				<th>创建时间</th>
				<th>优先级</th>
				<th>操作</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td>${item.title}</td>
					<td><fmt:formatDate value="${item.createTime}" pattern ="yyyy-MM-dd HH:mm:ss" /></td>
					<td>
						<c:forEach var="map" items="${faultLevelMap}">
							<c:if test="${item.level== map.key}">
								<span class="label label-important">	<c:out value="${map.value}" /></span>
							</c:if> 
						</c:forEach>
					</td>
					<td><a href="${ctx}/apply/fault/detail/${item.id}">查看</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
</body>
</html>
