<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
			$("#inputForm").validate();
			
			//windows2008R2 没有32位,只有64位.
			setOsBitCheckedByOsType();
			$("#osType").on("change click",function(){
				setOsBitCheckedByOsType();
			});
			
			$("#addESGBtn").click(function() {
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
			
			//遍历页面,将页面存在的esgIds放入临时数组中.
			$("div.resources").each(function() {
				selectedArray.push($(this).find("#esgIds").val());
			});
			
			//遍历挂载Compute的Id,先判断页面是否有选中的computeId,有的话跳过.
			$CheckedIds.each(function(){
				var $this = $(this);
				if ($.inArray($this.val(), selectedArray) == -1) {
					var $td = $this.closest("tr").find("td");
					var esgInfo = $td.eq(1).text() + "(" + $td.eq(2).text() + ")";	
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button type="button" class="close" data-dismiss="alert">×</button>';
					html += '<input type="hidden" value="' + $this.val() + '" id="esgIds" name="esgIds">';
					html += '<dd><em>关联ESG</em>&nbsp;&nbsp;<strong>' + esgInfo + '</strong></dd>';
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
	
	<form id="inputForm" action="${ctx}/resources/update/compute/" method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="id" value="${resources.id }">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${compute.computeType == 1 }">变更实例PCS</c:when>
					<c:otherwise>变更实例ECS</c:otherwise>
				</c:choose>
			</small></legend>
			
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
				<label class="control-label" for="usedby">运维人</label>
				<div class="controls">
					<select id="usedby" name="usedby" class="required">
						<c:forEach var="map" items="${assigneeMap}">
							<option value="${map.key}" 
								<c:if test="${map.key == resources.usedby }">
									selected="selected"
								</c:if>
							>${map.value}</option>
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
					<p class="help-inline plain-text">${compute.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${compute.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="innerIp">IP地址</label>
				<div class="controls">
					<p class="help-inline plain-text">${compute.innerIp}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="osType">操作系统</label>
				<div class="controls">
					<select id="osType" name="osType" class="required">
						<c:forEach var="map" items="${osTypeMap }">
							<option value="${map.key }" 
								<c:if test="${map.key == compute.osType }">
									selected="selected"
								</c:if>
							>${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="osBit">操作位数</label>
				<div class="controls">
					<c:forEach var="map" items="${osBitMap }">
						<label class="radio inline"> 
							<input type="radio" name="osBit" value="${map.key}" <c:if test="${map.key == compute.osBit }">checked="checked"</c:if> >
							<c:out value="${map.value}" />
						</label>
					</c:forEach>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="serverType">规格</label>
				<div class="controls">
					<select id="serverType" name="serverType" class="required">
						<c:choose>
							<c:when test="${compute.computeType == 1 }">
								<c:forEach var="map" items="${pcsServerTypeMap}"><option value="${map.key }" <c:if test="${map.key == compute.serverType }"> selected="selected" </c:if> >${map.value }</option></c:forEach>
							</c:when>
							<c:otherwise>
								<c:forEach var="map" items="${ecsServerTypeMap}"><option value="${map.key }" <c:if test="${map.key == compute.serverType }"> selected="selected" </c:if> >${map.value }</option></c:forEach>
							</c:otherwise>
						</c:choose>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="remark">用途信息</label>
				<div class="controls">
					<input type="text" id="remark" name="remark" value="${compute.remark }" class="required" maxlength="45" placeholder="...用途信息">
				</div>
			</div>
			
			<table class="table table-bordered table-condensed"  >
				<thead><tr><th>应用名称</th><th>应用版本</th><th>部署路径</th><th></th></tr></thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty compute.applications }">
							<c:forEach var="item" items="${compute.applications}">
								<tr class="clone">
									<td><input type="text" name="applicationName" value="${item.name}" class="input-small required" maxlength="45" placeholder="...应用名称"></td>
									<td><input type="text" name="applicationVersion" value="${item.version}" class="input-small required" maxlength="45" placeholder="...应用版本"></td>
									<td><input type="text" name="applicationDeployPath" value="${item.deployPath}" class="input-small required" maxlength="45" placeholder="...部署路径"></td>
									<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr class="clone">
								<td><input type="text" name="applicationName" class="input-small required" maxlength="45" placeholder="...应用名称"></td>
								<td><input type="text" name="applicationVersion" class="input-small required" maxlength="45" placeholder="...应用版本"></td>
								<td><input type="text" name="applicationDeployPath" class="input-small required" maxlength="45" placeholder="...部署路径"></td>
								<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			
			<div class="control-group">
				<div class="controls">
					 <a id="addESGBtn" class="btn" data-toggle="modal" href="#esgModal">ESG相关资源</a>
				</div>
			</div>	
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal">
				<c:forEach var="esg" items="${compute.networkEsgItemList }">
					<div class="resources alert alert-block alert-info fade in">
						<button data-dismiss="alert" class="close" type="button">×</button>
						<input type="hidden" name="esgIds" id="esgIds" value="${esg.id }">
						<dd>
							<em>关联ESG</em>&nbsp;&nbsp;<strong>${esg.identifier}</strong>
						</dd>
						<dd>
							<em>ESG描述</em>&nbsp;&nbsp;<strong>${esg.description}</strong>
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
	
	<!-- ESG选择的Modal -->
	<form id="modalForm" action="#" >
		<div id="esgModal" class="modal container hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>实例</h4></div>
				
			<div class="modal-body">
				<div class="singlePage">
				<table class="table table-striped table-bordered table-condensed">
					<thead><tr>
						<th><input type="checkbox"></th>
						<th>标识符</th>
						<th>安全组描述</th>
					</tr></thead>
					<tbody id="resources-tbody">
						<c:forEach var="item" items="${esgList }">
							<tr>
								<td><input type="checkbox" value="${item.id }"></td>
								<td>${item.identifier}</td>
								<td>${item.description }</td>
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
	</form><!-- ESG选择的Modal End -->
	
</body>
</html>
