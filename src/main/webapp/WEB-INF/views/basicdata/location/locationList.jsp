<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>基础数据-IDC管理</title>
<script>
	$(document).ready(function() {
		$("ul#navbar li#basicdata").addClass("active");
		$("div#leftbar li span").tooltip();
		$("#location-bar").addClass("active").find("span").addClass("txt-color");
	});
</script>
</head>

<body>

	<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
	<c:if test="${not empty errorMessage}"><div id="errorMessage" class="alert alert-danger fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${errorMessage}</span></div></c:if>
	<div class="row" >
	
		<div id="leftbar" class="span2">
			<ul class="nav nav-list" style="width: 120px;">
				<li id="location-bar"><a href="${ctx}/basicdata/location/"><i class="icon-inbox"></i><span data-placement="right" title="IDC管理">IDC管理</span></a></li>
				<li id="vlan-bar"><a href="${ctx}/basicdata/vlan/"><i class="icon-inbox"></i><span data-placement="right" title="VLAN管理">VLAN管理</span></a></li>
				<li id="ip-bar"><a href="${ctx}/basicdata/ippool"><i class="icon-list" ></i><span data-placement="right" title="IP创建、查询、修改、删除">IP管理</span></a></li>
				<li id="excel-bar"><a href="${ctx}/basicdata/import"><i class="icon-edit" ></i><span data-placement="right" title="已用数据初始化导入">Excel导入</span></a></li>
				<li id="hostServer-bar"><a href="${ctx}/basicdata/host/"><i class="icon-inbox" ></i><span data-placement="right" title="宿主机及物理机的创建、查询及管理">服务器管理</span></a></li>
			</ul>
		</div>

		<div id="main" class="span10">

			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>IDC名称</th>
						<th>城市</th>
						<th>地址</th>
						<th>邮编</th>
						<th>电话</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.content}" var="item">
						<tr>
							<td>${item.name}</td>
							<td>${item.city}</td>
							<td>${item.address}</td>
							<td>${item.postcode}</td>
							<td>${item.telephone}</td>
							<td>
								<a href="${ctx }/basicdata/location/update/${item.id}">修改</a>
							 	<a data-toggle="modal" href="#deleteModal${item.id}">删除</a>
								<div id="deleteModal${item.id }" class="modal hide fade form-horizontal">
									<div class="modal-header">
										<button data-dismiss="modal" class="close" type="button">×</button>
										<strong>提示</strong>
									</div>
									<div class="modal-body">是否删除?</div>
									<div class="modal-footer">
										<a class="btn" data-dismiss="modal" href="#">关闭</a> 
										<a href="${ctx}/basicdata/location/delete/${item.id}" class="btn btn-primary">确定</a>
									</div>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		
			<tags:pagination page="${page}" />
		
			<a class="btn" href="${ctx}/basicdata/location/save/">创建IDC</a>
		
		</div>
	</div>

</body>
</html>
