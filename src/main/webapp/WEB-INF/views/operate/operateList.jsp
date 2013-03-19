<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>工单管理</title>
<script>
$(document).ready(function() {
	
	$("ul#navbar li#operate").addClass("active");
	
});
</script>
</head>

<body>
	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
	
	<form class="form-inline well well-small" action="#">
		<div class="row">
		
			<div class="span3">
				<label class="control-label search-text">Subject</label> <input type="text" name="search_LIKE_subject" class="span2" maxlength="45"  value="${param.search_LIKE_subject}">
			</div>
			
			<div class="span3">
				<label class="control-label search-text">Tracker</label> 
				<select name="search_EQ_trackerId" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${trackerMap}">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_trackerId && param.search_EQ_trackerId != '' }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span3">
				<label class="control-label search-text">Status</label> 
				<select name="search_EQ_status" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${operateStatusMap}">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_status }">
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
		<c:choose>
			<c:when test="${not empty toReported }">
				<div class="span5"><a class="btn tip-bottom" title="跳转到所有工单列表" href="${ctx}/operate/reported/">所有工单</a></div>
				<div class="span4" style="padding-top: 5px;"><b>Assigned Issues</b></div>
			</c:when>
			<c:otherwise>
				<div class="span5"><a class="btn tip-bottom" title="跳转到分配给自己的工单列表"" href="${ctx}/operate/">分配给自己的工单</a></div>
				<div class="span4" style="padding-top: 5px;"><b>All Reported Issues</b></div>
			</c:otherwise>
		</c:choose>	
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>
	
	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<th>ID</th>
				<th>Subject</th>
				<th>Project</th>
				<th>Tracker</th>
				<th>Status</th>
			</tr>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td>
						<a href="${not empty toReported ? '' : '../'}update/${item.issueId}">${item.issueId}</a>
					</td>
					<td>${item.subject}</td>
					<td><c:forEach var="map" items="${projectMap}"><c:if test="${item.projectId==map.key}">${map.value}</c:if></c:forEach></td>
					<td><c:forEach var="map" items="${trackerMap}"><c:if test="${item.trackerId==map.key}">${map.value}</c:if></c:forEach></td>
					<td><c:forEach var="map" items="${operateStatusMap}"><c:if test="${item.status==map.key}">${map.value}</c:if></c:forEach></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

</body>
</html>
