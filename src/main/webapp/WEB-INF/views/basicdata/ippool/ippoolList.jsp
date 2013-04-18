<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	
	<title>基础数据-IP管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#basicdata, li#ip").addClass("active");
			
		});
	</script>
	
</head>

<body>

	<%@ include file="/WEB-INF/layouts/basicdataTab.jsp"%>

	<form class="form-inline well well-small" action="#">
	
		<div class="row">
		
			<div class="span3">
				<label class="control-label search-text">IP地址</label> 
				<input type="text" name="search_EQ_ipAddress" class="span2" maxlength="45" value="${param.search_EQ_ipAddress}">
			</div>
			
			<div class="span3">
				<label class="control-label search-text">IP池类型</label> 
				<select name="search_EQ_poolType" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${poolTypeMap}">
						<option value="${map.key }" 
							<c:if test="${map.key == param.search_EQ_poolType && param.search_EQ_poolType != '' }">
								selected="selected"
							</c:if>
						>${map.value }</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="span3">
				<label class="control-label search-text">IP状态</label> 
				<select name="search_EQ_status" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="map" items="${ipStausMap}">
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
				<!-- <button class="btn tip-bottom options"  title="更多搜索条件" type="button"><i class="icon-resize-small"></i></button> -->
			</div>
			
		</div>
		
		<!-- 多个搜索条件的话,启用 div.options -->
		<div class="row options">
			<div class="span3">
				<label class="control-label search-text">Vlan</label> 
				<select name="search_EQ_vlan.id" class="span2">
					<option value="" selected="selected">Choose...</option>
					<c:forEach var="item" items="${vlanList }">
						<option value="${item.id}">${item.name }</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</form>
	
	<div class="row">
		<div class="span4">	<a class="btn" href="${ctx}/basicdata/ippool/save/">创建IP</a></div>
		<div class="pull-right"><tags:singlePage page="${page}" /></div>
	</div>

	<div class="singlePage">
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>IP地址</th>
				<th>IP池类型</th>
				<th>IP状态</th>
				<th>Vlan</th>
				<th>IDC</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content}" var="item">
				<tr>
					<td>${item.ipAddress}</td>
					<td>
						<c:forEach var="map" items="${poolTypeMap }">
							<c:if test="${map.key == item.poolType}">${map.value }</c:if>
						</c:forEach>
					</td>
					
					<td>
						<c:if test="${item.status == 1 }">
							<span class="label label-success">未使用</span>
						</c:if> 
						<c:if test="${item.status == 2 }">
							<span class="label label-important">已使用</span>
						</c:if> 
					</td>
					
					<td>
						<c:forEach var="vlan" items="${vlanList}"><c:if test="${vlan.id == item.vlan.id}">${vlan.name }</c:if></c:forEach>
					</td>
	
					<td>
						<c:forEach var="location" items="${locationList }"><c:if test="${location.id == item.vlan.location.id}">${location.name }</c:if></c:forEach>
					</td>
						
					<td>
						<a	href="update/${item.id}">修改</a>
						
						<c:if test="${1 == item.status}">
						
							<a data-toggle="modal" href="#deleteModal${item.id}">删除</a>
							<div id="deleteModal${item.id }" class="modal hide fade form-horizontal">
								<div class="modal-header">
									<button data-dismiss="modal" class="close" type="button">×</button>
									<strong>提示</strong>
								</div>
								<div class="modal-body" >是否删除?</div>
								<div class="modal-footer">
									<a class="btn" data-dismiss="modal" href="#">关闭</a>
									<a href="delete/${item.id}" class="btn btn-primary loading">确定</a>
								</div>
							</div>
							
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
		
</body>
</html>
