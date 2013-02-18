<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>服务器数据</title>
<script>
	$(document).ready(
			function() {
				$("div#leftbar li span").tooltip();
				$("#basicData-tab").addClass("active");
				$("#hostServer-bar").addClass("active").find("span").addClass(
						"txt-color");
				
				$("#saveMessage,#errorMessage").fadeOut(3000);
			});
</script>
</head>

<body>

	<div class="row">
		<div id="leftbar" class="span2">
			<ul class="nav nav-list" style="width: 120px;">
				<li class="nav-header">基础数据管理</li>
				<li id="ip-bar"><a href="${ctx}/basicData/"><i class="icon-list" ></i><span data-placement="right" title="IP创建、查询、修改、删除">IP管理</span></a></li>
				<li id="excel-bar"><a href="${ctx}/basicData/import"><i class="icon-edit" ></i><span data-placement="right" title="已用数据初始化导入">Excel导入</span></a></li>
				<li id="hostServer-bar"><a href="${ctx}/hostServer/"><i class="icon-inbox" ></i><span data-placement="right" title="宿主机及物理机的创建、查询及管理">服务器管理</span></a></li>
				<li id="location-bar"><a href="${ctx}/location/"><i class="icon-inbox"></i><span data-placement="right" title="IDC管理">IDC管理</span></a></li>
				<li id="vlan-bar"><a href="${ctx}/vlan/"><i class="icon-inbox"></i><span data-placement="right" title="VLAN管理">VLAN管理</span></a></li>
			</ul>
		</div>

		<div id="main" class="span10">

			<c:if test="${not empty saveMessage}">
				<div id="saveMessage" class="alert alert-success">
					<button data-dismiss="alert" class="close">×</button>
					${saveMessage}
				</div>
			</c:if>

			<c:if test="${not empty errorMessage}">
				<div id="errorMessage" class="alert alert-danger">
					<button data-dismiss="alert" class="close">×</button>
					${errorMessage}
				</div>
			</c:if>

			<a class="btn btn-info pager" href="${ctx}/hostServer/save/">创建服务器</a>

			<form class="well well-small form-search" action="${ctx}/hostServer/">

				<div class="row-fluid rowshow-grid">

					<div class="span3">
						<label class="control-label">服务器类型:</label> <select
							id="serverType" name="serverType" class="input-small">
							<option value="999"></option>

							<c:forEach var="map" items="${hostServerTypeMap}">
								<option
									<c:if test="${map.key == serverType}">selected="selected"</c:if>
									value="<c:out value='${map.key}'/>">
									<c:out value='${map.value}' />
								</option>
							</c:forEach>

						</select>
					</div>

					<div class="span1">
						<button class="btn" type="submit">Search</button>
					</div>

				</div>
			</form>
			<c:if test="${not empty page.content}">
				<table id="contentTable"
					class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<th>服务器类型</th>
							<th>IP池类型</th>
							<th>服务器名</th>
							<th>IDC</th>
							<th>操作</th>
						</tr>
					</thead>
					<c:forEach items="${page.content}" var="item">
						<tr>
							<td><c:forEach var="map" items="${hostServerTypeMap}">
									<c:if test="${map.key == item.serverType}">
										<c:out value="${map.value }" />
									</c:if>
								</c:forEach></td>

							<td><c:forEach var="map" items="${poolTypeMap }">
									<c:if test="${map.key == item.poolType }">
										<c:out value="${map.value }" />
									</c:if>
								</c:forEach></td>
							<td>${item.displayName}</td>
							
							<td>
								<c:forEach var="location" items="${locationList}">
									<c:if test="${location.alias == item.locationAlias }">
										${location.name }
									</c:if>
								</c:forEach>
						 </td>
							
							<td><a data-toggle="modal" href="#deleteModal${item.id}">删除</a>
								<div id="deleteModal${item.id }"
									class="modal hide fade form-horizontal">
									<div class="modal-header">
										<button data-dismiss="modal" class="close" type="button">×</button>
										<strong>提示</strong>
									</div>
									<div class="modal-body">是否删除?</div>
									<div class="modal-footer">
										<a class="btn" data-dismiss="modal" href="#">关闭</a> <a
											href="${ctx}/hostServer/delete/${item.id}"
											class="btn btn-primary">确定</a>
									</div>
								</div></td>
						</tr>
					</c:forEach>
				</table>

				<tags:pagination page="${page}" />

			</c:if>

			<c:if test="${ empty page.content}">
				<h4>未找到符合条件的结果</h4>
			</c:if>
		</div>

	</div>

</body>
</html>
