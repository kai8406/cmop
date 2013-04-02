<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>基础数据-Vlan管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata, li#vlan").addClass("active");
			
		});
	</script>

</head>

<body>

	<%@ include file="/WEB-INF/layouts/basicdataTab.jsp"%>

	<form class="form-inline well well-small" action="#">

		<div class="row">
			
			<div class="span3">
				<label class="control-label search-text"> Vlan名称</label> 
				<input type="text" name="search_LIKE_name" class="span2" maxlength="45" value="${param.search_LIKE_name}">
			</div>
			
			<div class="span2 pull-right">
				<button class="btn tip-bottom" title="搜索" type="submit"><i class="icon-search"></i></button>
				<button class="btn tip-bottom reset" title="刷新" type="reset"><i class="icon-refresh"></i></button>
			</div>

		</div>

	</form>
	
	<div class="row">
		<div class="span4"><a class="btn" href="${ctx}/basicdata/vlan/save/">创建Vlan</a></div>
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>

	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>Vlan名称</th>
				<th>说明</th>
				<th>IDC</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td>${item.name}</td>
					<td>${item.description}</td>
					<td>${item.location.name}</td>
					<td>
						<a href="update/${item.id}">修改</a>
						<a data-toggle="modal" href="#deleteModal${item.id}">删除</a>
						<div id="deleteModal${item.id }" class="modal hide fade form-horizontal">
							<div class="modal-header">
								<button data-dismiss="modal" class="close" type="button">×</button>
								<strong>提示</strong>
							</div>
							<div class="modal-body">是否删除?</div>
							<div class="modal-footer">
								<a class="btn" data-dismiss="modal" href="#">关闭</a> 
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
