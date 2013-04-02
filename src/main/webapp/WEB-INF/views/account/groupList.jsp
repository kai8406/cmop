<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>权限管理</title>
	
	<script>
		$(document).ready(function() {
			$("ul#navbar li#group").addClass("active");
		});
	</script>
	
</head>

<body>
	
	<form class="form-inline well well-small" action="#">

		<div class="row">
		
			<div class="span3 ">
				<label class="control-label search-text">权限角色</label> 
				<input type="text" name="search_LIKE_name" class="span2" maxlength="45" value="${param.search_LIKE_name}">
			</div>
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
			</div>
			
		</div>

	</form>
	
	<div class="row">
		<div class="span4"><a class="btn" href="save/">创建权限</a></div>
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>
	
	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>权限角色</th>
				<th>授权</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td>${item.name}</td>
					<td>${item.permissionNames}</td>
					<td>
						<a href="update/${item.id}">修改</a>
						<a href="#deleteModal${item.id}" data-toggle="modal">删除</a>
						<div id="deleteModal${item.id }" class="modal hide fade " tabindex="-1" data-width="250">
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
	</div>	

</body>
</html>
