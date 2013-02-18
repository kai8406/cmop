<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>

	<title>基础数据-IP管理</title>

	<script>
	$(document).ready(function() {
		
		$("ul#navbar li#basicdata").addClass("active");
	
		$('#inputForm').validate({
			rules : {
		    	ipAddress  : { ipValidate : true }
		    },
		    errorClass: "help-inline",
			errorElement: "span",
			highlight:function(element, errorClass, validClass) {
				$(element).closest('.control-group').addClass('error');
			},
			unhighlight: function(element, errorClass, validClass) {
				$(element).closest('.control-group').removeClass('error');
			}
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
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
		
		<input type="hidden" name="id" value="${ipPool.id}">
		<input type="hidden" name="oldIpAddress" value="${ipPool.ipAddress}">
		
		<fieldset>

			<legend><small>
				<c:choose>
					<c:when test="${not empty ipPool.id }">修改</c:when>
					<c:otherwise>创建</c:otherwise>
				</c:choose>IP
			</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="locationId">IDC</label>
				<div class="controls">
					<select id="locationId" name="locationId" class="required" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if>  onchange="changeLocation()">
						<option value="" selected="selected">Choose...</option>
						<c:forEach var="item" items="${locationList}">
							<option <c:if test="${item.id == ipPool.vlan.location.id }">selected="selected"</c:if> value="${item.id }"
							>${item.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>		
									
			<div class="control-group">
				<label class="control-label" for="vlanId">Vlan</label>
				<div class="controls">
					<select id="vlanId" name="vlanId" class="required" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if> >
						<option value="" selected="selected">Choose...</option>
						<c:if test="${not empty ipPool.id}">
							<c:forEach var="item" items="${vlanList}">
								<option <c:if test="${item.id == ipPool.vlan.id }">selected="selected"</c:if> value="${item.id }"
								>${item.name }(${item.description})</option>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>
						
			<div class="control-group">
				<label class="control-label" for="poolType">IP池类型</label>
				<div class="controls">
					<select id="poolType" name="poolType" class="required" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if> >
						<c:forEach var="map" items="${poolTypeMap}">
							<option	<c:if test="${map.key == ipPool.poolType}">selected="selected"</c:if> value="${map.key}"
							>${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ipAddress">IP地址</label>
				<div class="controls">
					<input type="text" id="ipAddress" name="ipAddress"  class="required" maxlength="45" value="${ipPool.ipAddress}" placeholder="...192.168.0.1 or 192.168.0.1/254" <c:if test="${not empty ipPool.id}">disabled="disabled"</c:if>>
				</div>
			</div>
			
			<c:if test="${not empty ipPool.id}">		
				<div class="control-group">
					<label class="control-label" for="status" >状态</label>
					<div class="controls">
						<select id="status" name="status" class="required">
							<c:forEach var="map" items="${ipStausMap }">
								<option <c:if test="${map.key == ipPool.status }">	selected="selected"</c:if> value="${map.key }"
									>${map.value }</option>	
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
