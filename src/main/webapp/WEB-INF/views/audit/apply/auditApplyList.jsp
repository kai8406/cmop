<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>审批管理</title>
<script>
	$(document).ready(function() {
		
		$("ul#navbar li#applyAudit").addClass("active");
		
	});
</script>
</head>

<body>

	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
	

	<form class="form-inline well well-small" action="#">

		<div class="row">

	  	 	<div class="span3">
				<label class="control-label search-text">标题</label> <input type="text" name="search_LIKE_apply.title" class="span2" maxlength="45" 
					value="${param.search_LIKE_apply.title}">
			</div>
			
			<div class="span3">
				<label class="control-label search-text">服务标签</label> <input type="text" name="search_LIKE_apply.serviceTag" class="span2" maxlength="45" 
					value="${param.search_LIKE_apply.serviceTag}">
			</div> 
			
			
			<div class="span3">
				<label class="control-label search-text">审批结果</label> 
				<select name="search_EQ_status" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${auditResultMap }">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_status && param.search_EQ_status != '' }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
				<button class="btn tip-bottom options"  title="更多搜索条件" type="button"><i class="icon-resize-small"></i></button>
			</div>

		</div>
		
		<!-- 多个搜索条件的话,启用 div.options-->
		<div class="row options">
		
			<div class="span3">
				<label class="control-label search-text">优先级</label> 
				<select name="search_EQ_apply.priority" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${priorityMap }">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_apply.priority }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
		</div> 

	</form>
	
	<div class="row">
		<div class="span4"></div>
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>

	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>服务标签</th>
				<th>优先级</th>
				<th>申请时间</th>
				<th>审批结果</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td>${item.apply.title}</td>
					<td>${item.apply.serviceTag}</td>
					<td>
						<c:forEach var="map" items="${priorityMap }">
							<c:if test="${map.key == item.apply.priority }">
								${map.value }
							</c:if>
						</c:forEach>
					</td>
					<td><fmt:formatDate value="${item.apply.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
					<td>
						<c:forEach var="map" items="${auditResultMap}">
							<c:if test="${map.key == item.result}">${map.value}</c:if>
						</c:forEach>
					</td>
					<td>
						
						<!-- 0.待审批 -->
						<c:if test="${ empty item.result }">
							<a href="${ctx}/audit/apply/${item.apply.id}">审批</a>
						</c:if>
						
						
						<c:if test="${not empty item.result }">
							<a href="${ctx}/audit/apply/${item.apply.id}">查看</a>
						</c:if>
						
						
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

</body>
</html>
