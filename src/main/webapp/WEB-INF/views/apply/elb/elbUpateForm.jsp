<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
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
			
			// 遍历页面,将存在于页面的computeId放入临时数组selectedArray中
			$("div.resources").each(function() {
				selectedArray.push($(this).find("#computeIds").val());
			});
			
			//遍历挂载Compute的Id,先判断页面是否有选中的computeId,有的话跳过.
			$CheckedIds.each(function() {
				var $this = $(this);
				if ($.inArray($this.val(), selectedArray) == -1) {
					var $td = $this.closest("tr").find("td");
					var computeInfo = $td.eq(1).text() + "(" + $td.eq(4).text() + ")";
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
			$ModalDiv.find(".checker > span").removeClass("checked");//uniform checkbox的处理
			
			//插入HTML文本
			$("#resourcesDIV dl").append(html);
			
		}); 
	</script>
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="." method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="applyId" value="${elb.apply.id }">
		
		<fieldset>
			<legend><small>修改负载均衡器ELB</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${elb.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${elb.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="keepSession">是否保持会话</label>
				<div class="controls">
					<c:forEach var="map" items="${keepSessionMap}">
						<label class="radio inline"> 
							<input type="radio" name="keepSession" value="${map.key}" <c:if test="${elb.keepSession == map.key }"> checked="checked"</c:if>>${map.value}
						</label>
					</c:forEach>
				</div>
			</div>
			
			<table class="table table-bordered table-condensed"  >
				<thead><tr><th>Protocol</th><th>SourcePort</th><th>TargetPort</th><th></th></tr></thead>
				<tbody>
					<c:forEach var="item" items="${elb.elbPortItems}">
						<tr class="clone">
							<td>
								<select id="protocol" name="protocols" class="input-small required">
									<c:forEach var="map" items="${protocolMap}">
										<option value="${map.key }" <c:if test="${item.protocol == map.value }">selected="selected"</c:if>	
										>${map.value }</option>
									</c:forEach>
								</select>
							</td>
							<td><input type="text" id="sourcePort" name="sourcePorts" value="${item.sourcePort }" class="input-small required" maxlength="45" placeholder="...SourcePort"></td>
							<td><input type="text" id="targetPort" name="targetPorts" value="${item.targetPort }" class="input-small required" maxlength="45" placeholder="...TargetPort"></td>
							<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="control-group">
				<div class="controls">
					 <a id="addComputeBtn" class="btn" data-toggle="modal" href="#computeModal" >实例相关资源</a>
				</div>
			</div>
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal">
				<c:forEach var="compute" items="${relationComputes }">
					<div class="resources alert alert-block alert-info fade in">
						<button data-dismiss="alert" class="close" type="button">×</button>
						<input type="hidden" name="computeIds" id="computeIds" value="${compute.id }">
						<dd>
							<em>挂载实例</em>&nbsp;&nbsp;<strong>${compute.identifier}(${compute.remark})</strong>
						</dd>
					</div>
				</c:forEach>
			</dl></div>
			
			<div class="form-actions">
				<a href="${ctx}/apply/update/${elb.apply.id}/" class="btn">返回</a>
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
						<c:forEach var="item" items="${computeByElbIsNullList }">
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
