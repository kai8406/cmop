<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>权限组管理</title>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#ipAddress").focus();
			
			//active tab
			$("#basicData-tab").addClass("active");
			
			//为inputForm注册validate函数
			 $('#inputForm').validate({
			        rules : {
			        	ipAddress  : { ipValidate : true }
			        }
			    });
		});
	</script>
</head>

<body>

	<c:if test="${not empty errorMessage}">
		<div id="errorMessage" class="alert alert-danger"><button data-dismiss="alert" class="close">×</button>${errorMessage}</div>
	</c:if>
	
	<form:form id="inputForm" modelAttribute="ipPool" action="." method="post" cssClass="form-horizontal">
	
		<input type="hidden" name="id" value="${ipPool.id}"/>
		<input type="hidden" name="oldIpAddress" value="${ipPool.ipAddress}"/>
		
		<div class="tab-content">
			<div class="span8 offset2">
				<fieldset>
				
					<legend><small>IP地址管理</small></legend>
					
					<div class="control-group">
						<label for="name" class="control-label">IP地址</label>
						<div class="controls">
							<input type="text" id="ipAddress" name="ipAddress" size="50" class="required" value="${ipPool.ipAddress}"/>
							<span class="help-inline">请输入IP地址
							<c:if test="${empty ipPool }">
							（例如：192.168.0.1 or 192.168.0.1,192.168.0.2 or 192.168.0.20/100）
							</c:if>
							</span>
						</div>
					</div>
					
					<div class="control-group">
						<label for="name" class="control-label">IP池类型</label>
						<div class="controls">
							<select name="poolType">
								
								<c:forEach var="map" items="${poolTypeMap }">
								<option	<c:if test="${map.key == ipPool.poolType }">selected="selected"</c:if>
								 	value="<c:out value='${map.key }'/>"><c:out value='${map.value }'/></option>
								</c:forEach>
							
							</select>
							<span class="help-inline">请选择IP池类型</span>
						</div>
					</div>
					
					<div class="form-actions">
						<input class="btn btn-primary" type="submit" value="保存"/>&nbsp;	
						<input id="cancel" class="btn" type="button" value="返回" onclick="history.back()"/>
					</div>
				</fieldset>
			</div>
		</div>
	</form:form>
</body>
</html>
