<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>基础数据</title>
<script>
$(document).ready(function() {
	$("div#leftbar li span").tooltip();
	
	$("#basicData-tab").addClass("active"); 
	$("#excel-bar").addClass("active").find("span").addClass("txt-color");
	
	$("#saveMessage,#errorMessage").fadeOut(3000);
	
	$("#submitBtn").click(function(){
		 loading=new ol.loading({id:"container"}).show();
	});
	
});

</script>
</head>

<body>
<div class="row" >
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
		<div id="saveMessage" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${saveMessage}</div>
	</c:if>

	<c:if test="${not empty errorMessage}">
		<div id="errorMessage" class="alert alert-danger"><button data-dismiss="alert" class="close">×</button>${errorMessage}</div>
	</c:if>
	 
	<form  id="readReportForm" action="${ctx}/basicData/import/save" method="post" enctype="multipart/form-data"  >
		<label for="file">请点击"浏览(Browse)"，选择需要导入的Excel文件：</label>
		<input id="file" type="file" name="file" size="44" class="required" />
		<p style="margin-top: 5px;">
			<button type="submit" id="submitBtn">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
			<button type="reset" style="margin-left: 10px;">&nbsp;&nbsp;重 置&nbsp;&nbsp;</button>
		</p>	
	</form>
</div>
</body>
</html>
