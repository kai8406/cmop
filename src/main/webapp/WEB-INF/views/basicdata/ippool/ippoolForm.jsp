<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>IP管理</title>
<script>
$(document).ready(function() {
	$("ul#navbar li#basicdata").addClass("active");

	$("#ipAddress").focus();
	
	//为inputForm注册validate函数
	$('#inputForm').validate({
		rules : {
	    	ipAddress  : { ipValidate : true }
	    },
	    errorClass: "help-inline"
	});
});

function changeLocation(){
	if ($('#locationId').val()=="") {
		return;
	}
	$.ajax({
        type: "GET",
        url: "${ctx}/ajax/getVlanByLocation?location=" + $('#locationId').val(),
       	dataType: "json",
        success: function (data) {
        	$("#vlanId").empty();
        	var html = "";
        	for (var key in data) {  
        		html += ("<option value='"+key+"'>"+data[key]+"</option>");
       	    }
        	 $("#vlanId").append(html);
        }
    });
}		
</script>
</head>

<body>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
		<input type="hidden" name="id" value="${ipPool.id}"/>
		<input type="hidden" name="oldIpAddress" value="${ipPool.ipAddress}"/>
		
		<fieldset>
			<legend><small>IP新增</small></legend>
			
			<div class="control-group">
				<label for="locationId" class="control-label">IDC</label>
				<div class="controls">
					<select id="locationId" name="locationId" class="required" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if>  onchange="changeLocation()">
						<option value="">请选择IDC</option>
						<c:forEach var="item" items="${locationList}">
							<option <c:if test="${item.id == ipPool.vlan.location.id }">selected="selected"</c:if>
							value="${item.id }">${item.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>		
									
			<div class="control-group">
				<label for="vlanId" class="control-label">Vlan</label>
				<div class="controls">
					<select id="vlanId" name="vlanId" class="required" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if> >
						<option value="">请选择VLAN</option>
						<c:if test="${not empty ipPool.id}">
							<c:forEach var="item" items="${vlanList}">
								<option <c:if test="${item.id == ipPool.vlan.id }">selected="selected"</c:if>
								value="${item.id }">${item.name }(${item.description})</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>
						
			<div class="control-group">
				<label for="name" class="control-label">IP池类型</label>
				<div class="controls">
					<select name="poolType" class="required" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if> >
						<c:forEach var="map" items="${poolTypeMap}">
						<option	<c:if test="${map.key == ipPool.poolType}">selected="selected"</c:if>
						 	value="<c:out value='${map.key}'/>"><c:out value='${map.value}'/></option>
						</c:forEach>
					</select>
					<span class="help-inline">请选择IP池类型</span>
				</div>
			</div>
			
			<div class="control-group">
				<label for="name" class="control-label">IP地址</label>
				<div class="controls">
					<input type="text" id="ipAddress" name="ipAddress" size="50" class="required" value="${ipPool.ipAddress}" placeholder="请输入IP地址" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if> />
					<span class="help-inline">
						<c:if test="${empty ipPool}">
						例如：192.168.0.1 or 192.168.0.1,192.168.0.2 or 192.168.0.1/254
						</c:if>
					</span>
				</div>
			</div>
			
			<c:if test="${not empty ipPool.id}">		
					<div class="control-group">
						<label for="status" class="control-label">状态</label>
						<div class="controls">
							<select name="status" class="required">
								<c:forEach var="map" items="${ipStausMap }">
									<option 
										<c:if test="${map.key == ipPool.status }">	selected="selected"</c:if>
									value="${map.key }"><c:out value="${map.value }" /></option>	
								</c:forEach>
							</select>
						</div>
					</div>						
			</c:if>
					
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
		</fieldset>
	</form>		
	
</body>
</html>
