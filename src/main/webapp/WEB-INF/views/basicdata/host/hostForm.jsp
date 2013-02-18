<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>服务器管理</title>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#ipAddress").focus();
			
			//active tab
			$("#basicData-tab").addClass("active");
			
			$("#inputForm").validate({
				rules:{
					displayName:{
						remote: "${ctx}/ajax/checkDisplayName?oldDisplayName=${hostServer.displayName}"
					},
					displayName:{hostNameValidate:true},
					
					ipAddress:{
						remote: "${ctx}/ajax/checkIpAddress?oldIpAddress=${ipAddress}"
					}
					
				},
				messages:{
					displayName:{remote:"服务器名已存在"},
					ipAddress:{remote:"IP使用中或不存在,请在IP管理中创建新的IP."}
				}
			});
			
			
			 $("#submitBtn").click(function(){
				 if(!$("#inputForm").valid()){
					 return false;
				 }
			   	 loading=new ol.loading({id:"container"}).show();
			   });
		});
	</script>
</head>

<body>

	<c:if test="${not empty errorMessage}">
		<div id="errorMessage" class="alert alert-danger"><button data-dismiss="alert" class="close">×</button>${errorMessage}</div>
	</c:if>
	
	<form:form id="inputForm" modelAttribute="hostServer" action="." method="post" cssClass="form-horizontal">
	
		<input type="hidden" name="id" value="${hostServer.id}"/>
		
		<div class="tab-content">
			<div class="span8 offset2">
				<fieldset>
					<legend><small>服务器<c:if test="${not empty hostServer.id}">修改</c:if><c:if test="${ empty hostServer.id}">新增</c:if></small></legend>
								
					<div class="control-group">
						<label for="displayName" class="control-label">服务器名</label>
						<div class="controls">
							<input type="text" id="displayName" name="displayName" size="50" class="required" value="${hostServer.displayName}"/>
							<span class="help-inline">组成格式：Company Model Rack-Site.例如:HP DL2000 0416-1-1
							</span>
						</div>
					</div>
					
					<div class="control-group">
						<label for="name" class="control-label">IDC</label>
						<div class="controls">
							<select name="locationAlias" class="required">
								<c:forEach var="item" items="${locationList}">
									<option <c:if test="${item.alias == hostServer.locationAlias }">selected="selected"</c:if>
									value="${item.alias }">${item.name }</option>
								</c:forEach>
							</select>
							<span class="help-inline">请选择IDC</span>
						</div>
					</div>	
					
					<div class="control-group">
						<label for="name" class="control-label">服务器类型</label>
						<div class="controls">
							<select name="serverType" class="required">
								<c:forEach var="map" items="${hostServerTypeMap}">
								<option	<c:if test="${map.key == hostServer.serverType}">selected="selected"</c:if>
								 	value="<c:out value='${map.key}'/>"><c:out value='${map.value}'/></option>
								</c:forEach>
							</select>
							<span class="help-inline">请选择服务器类型</span>
						</div>
					</div>
					
					<div class="control-group">
						<label for="ipAddress" class="control-label">IP</label>
						<div class="controls">
							<input type="text" id="ipAddress" name="ipAddress" size="50" class="required ipAddressValidate" />
							<span class="help-inline">请输入可用IP</span>
						</div>
					</div>
					
					<div class="control-group">
						<label for="poolType" class="control-label">IP池类型</label>
						<div class="controls">
							<select name="poolType" class="required">
							 	<option value="4">生产IP池</option>
								<option value="5">公测IP池</option>
								<option value="6">测试IP池</option>
							<!--  
								<c:forEach var="map" items="${poolTypeMap}">
								<option	<c:if test="${map.key == ipPool.poolType}">selected="selected"</c:if>
								 	value="<c:out value='${map.key}'/>"><c:out value='${map.value}'/></option>
								</c:forEach>
								-->
							</select>
							<span class="help-inline">请选择IP池类型</span>
						</div>
					</div>
					
					<div class="form-actions">
						<input id="submitBtn" class="btn btn-primary" type="submit" value="保存"/>&nbsp;	
						<input id="cancel" class="btn" type="button" value="返回" onclick="history.back()"/>
					</div>
				</fieldset>
			</div>
		</div>
	</form:form>
</body>
</html>
