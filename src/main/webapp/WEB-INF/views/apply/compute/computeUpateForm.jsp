<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>实例管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#inputForm").validate({
				errorClass: "help-inline",
				errorElement: "span",
				highlight: function(element, errorClass, validClass) {
					$(element).closest('.control-group').addClass('error');
				},
				unhighlight: function(element, errorClass, validClass) {
					$(element).closest('.control-group').removeClass('error');
				}
			});
			
		});
	</script>
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="." method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="applyId" value="${compute.apply.id }">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${compute.computeType == 1 }">修改实例PCS</c:when>
					<c:otherwise>修改实例ECS</c:otherwise>
				</c:choose>
			</small></legend>
			
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
				<label class="control-label" for="esgId">关联ESG</label>
				<div class="controls">
					<select id="esgId" name="esgId" class="required">
						<c:forEach var="item" items="${esgList}">
							<option value="${item.id }" 
								<c:if test="${item.id == compute.networkEsgItem.id }">
									selected="selected"
								</c:if>
							>${item.identifier}(${item.description})</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="remark">用途信息</label>
				<div class="controls">
					<input type="text" id="remark" name="remark" value="${compute.remark }" class="required" maxlength="45" placeholder="...用途信息">
				</div>
			</div>
				 
			<div class="form-actions">
				<a href="${ctx}/apply/update/${apply.id}/" class="btn">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
