<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源管理</title>
	
	<script>
		$(document).ready(function() {
			$("ul#navbar li#resource").addClass("active");
		});
	</script>
	
</head>

<body>

	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message}</span></div></c:if>

	<form class="form-inline well well-small" action=".">

		<div class="row">

			<div class="span3">
				<label class="search-text">服务标签</label> 
				<input type="text" name="search_LIKE_serviceTag.name" class="span2" maxlength="45" 
					value="${param.search_LIKE_serviceTag.name}">
			</div>
			
			<div class="span3">
				<label class="search-text">状态</label> 
				<select name="search_EQ_status" class="span2">
					<option value="" >Choose...</option>
					<c:forEach var="map" items="${resourcesStatusMap}">
						<option value="${map.key}" 
							<c:if test="${map.key == param.search_EQ_status && param.search_EQ_status != ''}">
								selected="selected"
							</c:if>
						>${map.value}</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span3">
				<label class="search-text">IP地址</label> 
				<input type="text" name="search_EQ_ipAddress" class="span2" maxlength="45" 
					value="${param.search_EQ_ipAddress}">
			</div>
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
				<button class="btn tip-bottom options"  title="更多搜索条件" type="button"><i class="icon-resize-small"></i></button>
			</div>

		</div>
		
		<!-- 多个搜索条件的话,启用 div.options -->
		<div class="row options">
		
			<div class="span3">
				<label class="search-text">标识符</label> 
				<input type="text" name="search_LIKE_serviceIdentifier" class="span2" maxlength="45" 
					value="${param.search_LIKE_serviceIdentifier}">
			</div>
			
			
			<div class="span3">
				<label class="search-text">服务类型</label> 
				<select name="search_EQ_serviceType" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${resourcesServiceTypeMap}">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_serviceType}">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
		</div>

	</form>

	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标识符</th>
				<th>服务标签</th>
				<th>服务类型</th>
				<th>IP地址</th>
				<th>状态</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td><a href="update/${item.id}">${item.serviceIdentifier}</a></td>
					<td>${item.serviceTag.name}</td>
					<td>
						<c:forEach var="map" items="${resourcesServiceTypeMap}">
							<c:if test="${map.key == item.serviceType}">${map.value}</c:if>
						</c:forEach>
					</td>
					<td>${item.ipAddress}</td>
					<td>
						<c:forEach var="map" items="${resourcesStatusMap}">
							<c:if test="${map.key == item.status}">${map.value}</c:if>
						</c:forEach>
					</td>
					<td>
					
						<a href="#deleteModal${item.id}" data-toggle="modal">回收</a>
						<div id="deleteModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
							<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
							<div class="modal-body">是否回收?</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal">关闭</button>
								<a href="delete/${item.id}" class="btn btn-primary">确定</a>
							</div>
						</div>
						
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<tags:pagination page="${page}" />

	<a class="btn" href="save/">提交审批</a>

</body>
</html>
