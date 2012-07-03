<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>用户管理</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#feature-tab").addClass("active");
		
		$("#message").fadeOut(5000);
		
	});
</script>
</head>

<body>
	
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
	<div class="row">

			<div id="main" class="span10">

				<!-- Search -->
				<form class="well form-search" action="${ctx }/apply/support">

					<div class="row-fluid show-grid">

						<div class="span4">
							<label class="control-label">主题:</label> 
							<input type="text" id="title" name="title"	placeholder="服务申请主题" class="input-medium">
						</div>

						<div class="span4">
							<label class="control-label">审核状态:</label> <select id="status" name="status"
								class="input-mini">
								<option></option>
								<option value="1">待审核</option>
								<option value="2">审核中</option>
								<option value="3">已审核</option>
								<option value="4">已退回</option>
							</select>
						</div>
						<div class="span2">
							<button class="btn" type="submit">Search</button>
						</div>
					</div>
				</form>


				<!-- Table -->
				<table class="table table-striped table-bordered table-condensed">
					<colgroup>
						<col class="span6">
						<col class="span2">
						<col class="span2">
						<col class="span2">
					</colgroup>
					<thead>
						<tr>
							<th>服务变更主题</th>
							<th>创建日期</th>
							<th>审核状态</th>
							<th>操作</th>
						</tr>
					</thead>
					
					<tbody>
						<c:forEach items="${page.content}" var="apply">
							<tr>
								<td>${apply.title}</td>
								<td>${apply.createTime}</td>
								<td>
								<c:if test="${apply.status == 1 }">
								<span class="label label-important">待审核</span>
								</c:if>
								<c:if test="${apply.status == 2 }">
								<span class="label label-warning">审核中</span>
								</c:if>
								<c:if test="${apply.status == 3 }">
								<span class="label label-success">已审核</span>
								</c:if>
								</td>
								<td> 
									<a class="btn" href="./SupportDetail.html">查看</a> 
									<a class="btn btn-primary" href="update/${apply.id}">修改</a>
								</td>	
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<!-- Pagination -->
				<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
			</div>

		</div>
        

</body>
</html>
