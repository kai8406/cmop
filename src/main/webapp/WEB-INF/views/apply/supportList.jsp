<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>服务申请管理</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#support-tab").addClass("active");

		$("#message").fadeOut(5000);
	});
</script>
</head>

<body>

	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">
			<button data-dismiss="alert" class="close">×</button>
			${message}
		</div>
	</c:if>

	<div class="row">

		<%@ include file="/WEB-INF/layouts/left.jsp"%>

		<div id="main" class="span10">

			<div id="myResources">
				<h3>我的资源</h3>
				<div class="row page-header">
					<div class="span2">
						<a href="${ctx }/apply/support/ecs/">ECS (${ecsCount})</a>
					</div>
					<div class="span2">
						<a href="${ctx }/apply/support/es3/">ES3 (${es3Count})</a>
					</div>
				</div>
			</div>

			<form class="well well-small form-search"
				action="${ctx }/apply/support/">

				<div class="row-fluid show-grid">

					<div class="span4">
						<label class="control-label">主题:</label> <input type="text"
							id="title" name="title" class="input-medium" />
					</div>

					<div class="span4">
						<label class="control-label">审核状态:</label> <select id="status"
							name="status" class="input-medium">
							<option value="0"></option>
							<c:forEach var="map" items="${applyStatusMap }">
							
								<option value="	<c:out value='${map.key}' />">
									<c:out value="${map.value}" />
								</option>
							</c:forEach>

						</select>
					</div>
					<div class="span2">
						<button class="btn" type="submit">Search</button>
					</div>
				</div>
			</form>


			<table class="table table-striped table-bordered table-condensed">
				<colgroup>
					<col class="span4">
					<col class="span2">
					<col class="span2">
					<col class="span2">
				</colgroup>
				<thead>
					<tr>
						<th>主题</th>
						<th>创建时间</th>
						<th>审核状态</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${page.content}" var="item">
						<tr>
							<td>${item.title}</td>
							<td><fmt:formatDate value="${item.createTime}" pattern ="yyyy-MM-dd HH:mm:ss" /></td>
							<td>
								<c:if test="${item.status == 1 }">
									<span class="label label-important">待审核</span>
								</c:if> 
								
								<c:if test="${item.status == 2 }">
									<span class="label label-warning">审核中</span>
								</c:if> 
								
								<c:if test="${item.status == 3 }">
									<span class="label label-inverse">已退回</span>
								</c:if> 
								
								<c:if test="${item.status == 4 }">
									<span class="label label-success">已审核</span>
								</c:if>
							</td>
							<td>
								
								<c:forEach var="map" items="${serviceTypeMap }">
									
									<c:if test="${map.value == item.serviceType }">
									
										<a href="${ctx }/apply/support/${map.value }/detail/${item.id}">申请详情</a>
										
										
										<!-- 待审核状态下不能显示 -->
										<c:if test="${item.status != 1 }">
											<a href="#auditDetail_${item.id }"  data-toggle="modal">审核进度</a>
										</c:if>					
										
										<!-- 只有 待审核 已返回 才能显示 -->
										<c:if test="${item.status == 1 || item.status == 3 }">
											<a href="${ctx }/apply/support/${map.value }/update/${item.id}">修改</a>
										</c:if>										
									
									</c:if>
								
								</c:forEach>
							
								
							</td>
						</tr>
						
						<div id="auditDetail_${item.id}" class="modal hide fade form-horizontal">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h3>审核进度</h3>
								</div>
								<div class="modal-body">
								
									<dl class="dl-horizontal">
										<c:forEach var="audits" items="${item.audits }">
											<dt>审核人:</dt>
											<dd>${audits.apply.user.name }</dd>
											<dt>审核时间:</dt>
											<dd>
												<fmt:formatDate value="${audits.createTime}" pattern ="yyyy-MM-dd HH:mm:ss" />
											</dd>
											<dt>审核结果:</dt>
											<dd>
												<c:forEach var="map" items="${auditResultMap }">
													<c:if test="${map.key == audits.result }">
														<c:out value="${map.value}" />
													</c:if>
												</c:forEach>
											</dd>
											<dt>审核意见:</dt>
											<dd>${audits.opinion}</dd>
											
											<hr>
											
										</c:forEach>
									</dl>
								</div>
								<div class="modal-footer">
									<a href="#" class="btn" data-dismiss="modal">关闭</a>  
								</div>
							</div>
							
					</c:forEach>
					
					
					
							
							
				</tbody>
			</table>

			<%@ include file="/WEB-INF/layouts/pageable.jsp"%>
		</div>

	</div>

</body>
</html>
