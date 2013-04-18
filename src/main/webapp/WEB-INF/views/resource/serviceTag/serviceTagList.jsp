<%@ page contentType="text/html;charset=UTF-8" %>
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

	<form class="form-inline well well-small" action="#">

		<div class="row">
		
			<div class="span3 ">
				<label class="control-label search-text">标签名</label> 
				<input type="text" name="search_LIKE_name" class="span2" maxlength="45" value="${param.search_LIKE_name}">
			</div>
			
			<div class="span3">
				<label class="control-label search-text">优先级</label> 
				<select name="search_EQ_priority" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${priorityMap }">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_priority }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
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
			
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
			</div>
			
		</div>

	</form>
	
	<div class="row">
		<div class="span4"><a class="btn" href="save/">创建服务标签</a>&nbsp;<a class="btn" href="${ctx}/resources/">返回</a></div>
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>
	
	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标签名</th>
				<th>优先级</th>
				<th>创建时间</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td><a href="detail/${item.id}">${item.name}</a></td>
					<td>
						<c:forEach var="map" items="${priorityMap }">
							<c:if test="${map.key == item.priority }">
								${map.value }
							</c:if>
						</c:forEach>
					</td>
					<td><fmt:formatDate value="${item.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
					<td>
						<c:forEach var="map" items="${resourcesStatusMap }">
							<c:if test="${map.key == item.status }">
								<c:choose>
									<c:when test="${item.status == -1 }">
										<span class="label" style="background-color: #bbbbbb;">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 0 }">
										<span class="label">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 1 }">
										<span class="label label-warning tip-right" title="审批人: ${item.auditFlow.user.name }">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 2 }">
										<span class="label label-important tip-right" title="审批人: ${item.auditFlow.user.name }">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 3 }">
										<span class="label label-inverse">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 4 }">
										<span class="label status4">${map.value }</span>
									</c:when>
									
									<c:when test="${item.status == 5 }">
										<span class="label status5">${map.value }</span>
									</c:when>
									
									<c:otherwise>
										<span class="label label-success">${map.value }</span>
									</c:otherwise>
									
								</c:choose>
							</c:if>
						</c:forEach>
						
					</td>
					<td>
						<c:forEach var="allowStatus" items="${allowResourcesStatus }">
						<c:if test="${ item.status == allowStatus }">
							<a href="update/${item.id}">修改</a>
							<a href="#deleteModal${item.id}" data-toggle="modal">回收</a>
							<div id="deleteModal${item.id }" class="modal hide fade " tabindex="-1" data-width="250">
								<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
								<div class="modal-body">是否回收该服务标签下所有的资源?</div>
								<div class="modal-footer">
									<button class="btn" data-dismiss="modal">关闭</button>
									<a href="delete/${item.id}" class="btn btn-primary loading">确定</a>
								</div>
							</div>
						</c:if>
						</c:forEach>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

</body>
</html>
