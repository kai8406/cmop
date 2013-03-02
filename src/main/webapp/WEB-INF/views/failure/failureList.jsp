<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>故障申报</title>
	
	<script>
		$(document).ready(function() {
			$("ul#navbar li#failure").addClass("active");
		});
	</script>

</head>

<body>

	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>

	<form class="form-inline well well-small" action="#">

		<div class="row">

			<div class="span3">
				<label class="control-label search-text">优先级</label> 
				<select name="search_EQ_level" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${priorityMap }">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_level }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span3">
				<label class="control-label search-text">故障类型</label> 
				<select name="search_EQ_faultType" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${applyServiceTypeMap }">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_faultType }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span3">
				<label class="control-label search-text">Status</label> 
				<select name="search_EQ_redmineIssue.status" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${operateStatusMap}">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_redmineIssue.status }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
			</div>

		</div>

	</form>
	
	<div class="row">
		<div class="span4"><a class="btn" href="save/">故障申报</a></div>
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>

	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>优先级</th>
				<th>故障类型</th>
				<th>申报时间</th>
				<th>状态</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td><a href="detail/${item.id}">${item.title}</a></td>
					<td><c:forEach var="map" items="${priorityMap }"><c:if test="${map.key == item.level }">${map.value }</c:if></c:forEach></td>
					<td><c:forEach var="map" items="${applyServiceTypeMap }"><c:if test="${map.key == item.faultType }">${map.value }</c:if></c:forEach></td>
					<td><fmt:formatDate value="${item.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
					<td><c:forEach var="map" items="${operateStatusMap}"><c:if test="${item.redmineIssue.status==map.key}">${map.value}</c:if></c:forEach></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

</body>
</html>
