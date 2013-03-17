<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#inputForm").validate();
			
			/*禁用回车提交form表单.*/
			$("#inputForm").keypress(function(e) {
				if (e.which == 13) {return false;}
			});
			
			$("#addComputeBtn").click(function() {
				if (!$("#inputForm").valid()) {
					return false;
				}
			});
			
		});
		
		/*点击弹出窗口保存时,连同ES3的信息生成HTML代码插入页面.*/
		$(document).on("click", "#ModalSave", function() {
			
			var html = "",
				computeIds = "",
				computeInfo = "";
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			var storageType = $("#storageType").val();
			var storageTypeText = $("#storageType>option:selected").text();
			var space = $("#space").val();
			
			//遍历挂载Compute的Id和identifier.
			$CheckedIds.each(function() {
				var $this = $(this);
				var $td = $this.closest("tr").find("td");
				computeIds += $this.val() + "-";
				computeInfo += "<br>" + $td.eq(1).text() + "(" + $td.eq(4).text() + ")";
			});
			
			//判断是否选中实例,拼装HTML文本
			if ($CheckedIds.length > 0) {
				html += '<div class="resources alert alert-block alert-info fade in">';
				html += '<button type="button" class="close" data-dismiss="alert">×</button>';
				html += '<input type="hidden" value="' + computeIds + '" name="computeIds">';
				html += '<input type="hidden" value="' + space + '" name="spaces">';
				html += '<input type="hidden" value="' + storageType + '" name="storageTypes">';
				html += '<dd><em>存储类型</em>&nbsp;&nbsp;<strong>' + storageTypeText + '</strong></dd>';
				html += '<dd><em>容量空间(GB)</em>&nbsp;&nbsp;<strong>' + space + '</strong></dd>';
				html += '<dd><em>挂载实例</em>&nbsp;&nbsp;<strong>' + computeInfo + '</strong></dd>';
				html += '</div> ';
			}
			
		    //初始化
		    $("input[type=checkbox]").removeAttr('checked');
		    $ModalDiv.find(".checker > span").removeClass("checked"); //uniform checkbox的处理
		    
		    //插入HTML文本
		    $("#resourcesDIV dl").append(html);
			
		}); 
		 
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建ES3存储空间</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="applyId">所属服务申请</label>
				<div class="controls">
					<select id="applyId" name="applyId" class="required">
						<c:forEach var="item" items="${baseStationApplys}"><option value="${item.id }">${item.title} ( ${item.description} )</option></c:forEach>
					</select>
				</div>
			</div>
			
			<hr> 
			
			<div class="control-group">
				<label class="control-label" for="storageType">存储类型</label>
				<div class="controls">
					<select id="storageType" class="required">
						<c:forEach var="map" items="${storageTypeMap}">
							<option value="${map.key}">${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="space">容量空间(GB)</label>
				<div class="controls">
					<input type="text" id="space" class="required digits" maxlength="6" placeholder="...容量空间(GB)">
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					 <a id="addComputeBtn" class="btn" data-toggle="modal" href="#computeModal" >实例相关资源</a>
				</div>
			</div>
				
			<hr>
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal"></dl></div>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
	<!-- 实例选择的Modal -->
	<form id="modalForm" action="#" >
		<div id="computeModal" class="modal container hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>实例</h4></div>
				
			<div class="modal-body">
				<div class="singlePage">
				<table class="table table-striped table-bordered table-condensed">
					<thead><tr>
						<th><input type="checkbox"></th>
						<th>实例标识符</th>
						<th>基本信息(操作系统,位数,规格)</th>
						<th>用途信息</th>
						<th>IP地址</th>
					</tr></thead>
					<tbody id="resources-tbody">
						<c:forEach var="item" items="${allComputes }">
							<tr>
								<td><input type="checkbox" value="${item.id }"></td>
								<td>${item.identifier}</td>
								<td><c:forEach var="map" items="${osTypeMap}"><c:if test="${item.osType == map.key}">${map.value}</c:if></c:forEach>&nbsp;&nbsp;&nbsp;
									<c:forEach var="map" items="${osBitMap}"><c:if test="${item.osBit == map.key}">${map.value}</c:if></c:forEach>
									<c:choose>
										<c:when test="${item.computeType == 1}"><c:forEach var="map" items="${pcsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:when>
										<c:otherwise><c:forEach var="map" items="${ecsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:otherwise>
									</c:choose>
								</td>
								<td>${item.remark }</td>
								<td>${item.innerIp }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
			</div>
				
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
				<a id="ModalSave" href="#" class="btn btn-primary" data-dismiss="modal" >确定</a>
			</div>
		</div>
	</form><!-- 实例规格选择的Modal End -->
	
</body>
</html>
