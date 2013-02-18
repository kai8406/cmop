<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>提交变更</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
		});
	</script>
	
</head>

<body>

	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
	
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
			
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
			</div>
			
		</div>

	</form>
	
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标签名</th>
				<th>优先级</th>
				<th>创建时间</th>
				<th>管理</th>
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
						<a href="#commitModal${item.id}" data-toggle="modal">提交变更</a>
						<div id="commitModal${item.id }" class="modal hide fade " tabindex="-1" data-width="250">
							<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
							<div class="modal-body">是否提交变更?</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal">关闭</button>
								<a href="commit/${item.id}" class="btn btn-primary">确定</a>
							</div>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${page}" />

	<a class="btn" href="#" onclick="history.back()">返回</a>

</body>
</html>
