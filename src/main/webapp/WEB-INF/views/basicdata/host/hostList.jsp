<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>基础数据-服务器管理</title>
<script>
	$(document).ready(function() {
		$("ul#navbar li#basicdata, li#hostServer").addClass("active");

		$("#synBtn").click(function(){
			$(this).modalmanager('loading');
		});
		$("#exportBtn").click(function(){
			$(this).modalmanager('loading');
		});
	});
</script>
</head>

<body>

	<%@ include file="/WEB-INF/layouts/basicdataTab.jsp"%>

	<form class="form-inline well well-small" action="#">
		<div class="row">
			<div class="span3">
				<label class="search-text">IP地址</label> 
				<input type="text" name="search_EQ_ipAddress" class="span2" maxlength="45" 
					value="${param.search_EQ_ipAddress}">
			</div>
			
			<div class="span3">
				<label class="control-label">服务器类型:</label> 
				<select name="search_EQ_serverType" class="input-medium">
					<option value="">全部</option>
					<c:forEach var="map" items="${hostServerTypeMap }">
						<option 
							<c:if test="${map.key == param.search_EQ_serverType }"> selected="selected"</c:if>
								value="${map.key }"><c:out value="${map.value }" />
						</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span2 pull-right">
				<button class="btn" type="submit"><i class="icon-search"></i>查询</button>
				<!-- 
				<a href="${ctx}/basicdata/host/syn" title="同步Vcenter数据" class="btn tip-bottom" id="synBtn"><i class="icon-refresh"></i>同步</a>
				<a href="${ctx}/basicdata/host/export" title="导出数据到Excel" class="btn tip-bottom" id="exportBtn"><i class="icon-refresh"></i>导出</a>
				<a href="${ctx}/basicdata/host/write" title="写入OneCMDB" class="btn tip-bottom" id="writeBtn"><i class="icon-refresh"></i>写入</a> 
				 --> 
			</div>
		</div>
	</form>
	
	<div class="row">
		<div class="span4"><a class="btn" href="${ctx}/basicdata/host/save/">创建服务器</a></div>
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>

	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>服务器名称</th>
				<th>服务器类型</th>
				<th>IP池类型</th>
				<th>IP地址</th>
				<th>IDC</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td><a href="detail/${item.id}">${item.displayName}</a></td>
					<td>
						<c:forEach var="map" items="${hostServerTypeMap}"><c:if test="${map.key == item.serverType}">${map.value }</c:if></c:forEach>
					</td>
					<td>
						<c:forEach var="map" items="${poolTypeMap }"><c:if test="${map.key == item.poolType }">${map.value }</c:if></c:forEach>
					</td>
					<td>${item.ipAddress}</td>
					<td>
						<c:forEach var="location" items="${locationList}"><c:if test="${location.alias == item.locationAlias }">${location.name }</c:if></c:forEach>
				 	</td>
					
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
								<a href="${ctx}/basicdata/host/delete/${item.id}" class="btn btn-primary loading">确定</a>
							</div>
						</div>
						
						<a href="${ctx}/basicdata/host/hostTree/${item.id}">虚拟机[${fn:length(item.ipPools)}]</a>
						
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>

</body>
</html>
