<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
			$("#inputForm").validate();
			
			$("#addComputeBtn").click(function() {
				if (!$("#inputForm").valid()) {
					return false;
				}
			});
			
		});
		
		/*点击弹出窗口保存时,生成Compute标识符信息HTML代码插入页面.*/
		$(document).on("click", "#ModalSave", function() {
			
			var selectedArray = [],
				html = "";
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//遍历页面,将页面存在的computeId放入临时数组中.
			$("div.resources").each(function() {
				selectedArray.push($(this).find("#computeIds").val());
			});
			
			//遍历挂载Compute的Id,先判断页面是否有选中的computeId,有的话跳过.
			$CheckedIds.each(function(){
				var $this = $(this);
				if ($.inArray($this.val(), selectedArray) == -1) {
					var $td = $this.closest("tr").find("td");
					var computeInfo = $td.eq(1).text() + "(" + $td.eq(3).text() + "&nbsp;-&nbsp;" + $td.eq(4).text() + ")";
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button type="button" class="close" data-dismiss="alert">×</button>';
					html += '<input type="hidden" value="' + $this.val() + '" id="computeIds" name="computeIds">';
					html += '<dd><em>挂载实例</em>&nbsp;&nbsp;<strong>' + computeInfo + '</strong></dd>';
					html += '</div> ';
				}
			});
			
			//初始化
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			 
			//插入HTML文本
			$("#resourcesDIV dl").append(html);
			
		}); 
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="${ctx}/resources/update/storage/" method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="id" value="${resources.id }">
		
		<fieldset>
			<legend><small>变更ES3存储空间</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="serviceTagId">服务标签</label>
				<div class="controls">
					<select id="serviceTagId" name="serviceTagId" class="required">
						<c:forEach var="item" items="${tags}">
							<option value="${item.id }" 
								<c:if test="${item.id == resources.serviceTag.id }">
									selected="selected"
								</c:if>
							>${item.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="changeDescription">变更描述</label>
				<div class="controls">
					<textarea rows="3" id="changeDescription" name="changeDescription" placeholder="...变更描述"
						maxlength="200" class="required">${change.description}</textarea>
				</div>
			</div>
			
			<hr>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${storage.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${storage.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="storageType">存储类型</label>
				<div class="controls">
					<select id="storageType" name="storageType" class="required">
						<c:forEach var="map" items="${storageTypeMap }">
							<option value="${map.key }" 
								<c:if test="${map.key == storage.storageType }">
									selected="selected"
								</c:if>
							>${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="space">容量空间(GB)</label>
				<div class="controls">
					<input type="text" id="space" name="space" value="${storage.space}" class="required digits" maxlength="6" placeholder="...容量空间">
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					 <a id="addComputeBtn" class="btn" data-toggle="modal" href="#computeModal" >实例相关资源</a>
				</div>
			</div>
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal">
				<c:forEach var="compute" items="${storage.computeItemList }">
					<div class="resources alert alert-block alert-info fade in">
						<button data-dismiss="alert" class="close" type="button">×</button>
						<input type="hidden" name="computeIds" id="computeIds" value="${compute.id }">
						<dd>
							<em>挂载实例</em>&nbsp;&nbsp;<strong>${compute.identifier}(${compute.remark} - ${compute.innerIp})</strong>
						</dd>
					</div>
				</c:forEach>
			</dl></div> 
			
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
						<c:forEach var="item" items="${computeResources}">
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
