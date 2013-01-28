<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>服务申请管理</title>
<script>
	$(document).ready(function() {
		$("ul#navbar li#apply").addClass("active");
		
		//隐藏资源创建列表
		$("#thumbnailsBtn").on('click', function(){
		 		
			$(this).find("i").toggleClass("icon-resize-small icon-resize-full").end().parents().find("div.quick-actions").toggle(300);
		 		
	 	});
		 
	});
</script>
</head>

<body>

	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
	
	<p><h3>Apply</h3>创建任何资源前,必须有一个服务申请单</p>
	<p>
		<a class="btn btn-large btn-primary" href="save/">创建申请单 &raquo;</a>
		<a class="btn btn-large" href="#" id="thumbnailsBtn">申请单列表 <i class="icon-resize-full"></i></a>
	</p>
	
	<div class="row">
		<div class="span12 quick-actions">
			<a href="#" class="btn span1">PCS &raquo;</a>
			<a href="#" class="btn span1">ECS &raquo;</a>
			<a href="#" class="btn span1">ES3 &raquo;</a>
			<a href="#" class="btn span1">ELB &raquo;</a>
			<a href="#" class="btn span1">EIP &raquo;</a>
			<a href="#" class="btn span1">DNS &raquo;</a>
			<a href="#" class="btn span1">ESG &raquo;</a>
		</div>

		<div class="span12 quick-actions">
			<a href="#" class="btn span1">MDN &raquo;</a>
			<a href="#" class="btn span1">CP &raquo;</a>
		</div>
	</div>

	<form class="form-inline well well-small" action="#">

		<div class="row">

			<div class="span3">
				<label class="control-label search-text">标题</label> <input type="text" name="search_LIKE_title" class="input-small" maxlength="45" 
					value="${param.search_LIKE_title}">
			</div>
			
			<div class="span3">
				<label class="control-label search-text">服务标签</label> <input type="text" name="search_LIKE_serviceTag" class="input-small" maxlength="45" 
					value="${param.search_LIKE_serviceTag}">
			</div>
			
			<div class="span3">
				<label class="control-label search-text">状态</label> 
				<select name="search_EQ_status" class="input-small">
					<option value="">...Choose</option>
					<c:forEach var="map" items="${applyStatusMap }">
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
				<button class="btn tip-bottom options"  title="更多搜索条件" type="button"><i class="icon-resize-small"></i></button>
			</div>

		</div>
		
		<!-- 多个搜索条件的话,启用 div.options -->
		<div class="row options">
			<div class="span3">
				<label class="control-label search-text">优先级</label> 
				<select name="search_EQ_priority" class="input-small">
					<option value="">...Choose</option>
					<c:forEach var="map" items="${applyPriorityMap }">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_priority }">
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
				<th>标题</th>
				<th>服务标签</th>
				<th>优先级</th>
				<th>状态</th>
				<th>申请时间</th>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td><a href="update/${item.id}">${item.title}</a></td>
					<td>${item.serviceTag}</td>
					<td>${item.priority}</td>
					<td>
						<c:forEach var="map" items="${applyStatusMap }">
							<c:if test="${map.key == item.status }">
								${map.value }
							</c:if>
						</c:forEach>
					</td>
					<td><fmt:formatDate value="${item.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
					<td>
					
						<a href="#deleteModal${item.id}" data-toggle="modal">删除</a>
						<div id="deleteModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
							<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
							<div class="modal-body">是否删除?</div>
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

</body>
</html>
