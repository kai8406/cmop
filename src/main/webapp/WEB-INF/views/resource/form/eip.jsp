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
			
			/*关联实例和关联ELB select控件的切换*/
			$("input[name='linkRadio']").click(function() {
				if ($(this).val() == "isCompute") {
					$("#computeSelectDiv").addClass("show").removeClass("hidden");
					$("#elbSelectDiv").addClass("hidden").removeClass("show");
				} else {
					$("#elbSelectDiv").addClass("show").removeClass("hidden");
					$("#computeSelectDiv").addClass("hidden").removeClass("show");
				}
			});
			
		});
		
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="${ctx}/resources/update/eip/" method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="id" value="${resources.id }">
		<input type="hidden" id="linkId" name="linkId">
		<input type="hidden" id="linkType" name="linkType">
		
		<fieldset>
			<legend><small>变更EIP</small></legend>
			
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
					<p class="help-inline plain-text">${eip.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ipAddress">IP地址</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.ipAddress}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ispType">ISP运营商</label>
				<div class="controls">
					<p class="help-inline plain-text"><c:forEach var="map" items="${ispTypeMap }"><c:if test="${map.key == eip.ispType }">${map.value }</c:if></c:forEach></p>
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					<label class="radio inline">
 						<input type="radio" name="linkRadio" value="isCompute"
 						<c:if test="${not empty eip.computeItem }">checked="checked"</c:if>>关联实例
					</label>
					<label class="radio inline">
	 					<input type="radio" name="linkRadio" value="isElb"
	 					<c:if test="${not empty eip.networkElbItem }">checked="checked"</c:if>>关联Elb
					</label>
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
				
					<div id="computeSelectDiv" 
						<c:choose>
						<c:when test="${not empty eip.computeItem }">class="show"</c:when>
						<c:otherwise>class="hidden"</c:otherwise>
						</c:choose>
					>
						<select id="computeSelect">
							<option></option>
							<c:forEach var="item" items="${computeResources }">
								<option value="${item.id }" <c:if test="${not empty eip.computeItem && item.id == eip.computeItem.id }">selected="selected"</c:if>>${item.identifier}(${item.remark} - ${item.innerIp})</option>
							</c:forEach>
						</select>					
					</div>
					
					<div id="elbSelectDiv" 
						<c:choose>
						<c:when test="${not empty eip.networkElbItem }">class="show"</c:when>
						<c:otherwise>class="hidden"</c:otherwise>
						</c:choose>
					>
						<select id="elbSelect">
							<option></option>
							<c:forEach var="item" items="${elbResources }">
								<option value="${item.id }" <c:if test="${not empty eip.networkElbItem && item.id == eip.networkElbItem.id }">selected="selected"</c:if>>${item.identifier}(${item.virtualIp })&nbsp;【${item.mountComputes}】</option>
							</c:forEach>
						</select>			
					</div>		
 
				</div>
			</div>
			
			<table class="table table-bordered table-condensed"  >
				<thead><tr><th>Protocol</th><th>SourcePort</th><th>TargetPort</th><th></th></tr></thead>
				<tbody>
					<c:choose>
					
						<c:when test="${empty eip.eipPortItems }">
							<tr class="clone">
								<td>
									<select id="protocol" name="protocols" class="input-small required">
										<c:forEach var="map" items="${protocolMap}">
											<option value="${map.key }">${map.value }</option>
										</c:forEach>
									</select>
								</td>
								<td><input type="text" id="sourcePort" name="sourcePorts" class="input-small required" maxlength="45" placeholder="...SourcePort"></td>
								<td><input type="text" id="targetPort" name="targetPorts" class="input-small required" maxlength="45" placeholder="...TargetPort"></td>
								<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
							</tr>
						</c:when>
						
						<c:otherwise>
							<c:forEach var="item" items="${eip.eipPortItems}">
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
						</c:otherwise>
					</c:choose>
					
				</tbody>
			</table>
				 
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" onclick="fillLinkType()" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
