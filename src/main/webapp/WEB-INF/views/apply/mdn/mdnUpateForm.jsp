<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#inputForm").validate();
			
		});
		 
	</script>
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="." method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="applyId" value="${mdn.apply.id }">
		
		<fieldset>
			<legend><small>修改MDN</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${mdn.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="coverArea">重点覆盖地域</label>
				<div class="controls">
					<input type="text" id="coverArea" name="coverArea" value="${mdn.coverArea }" class="required" maxlength="45" placeholder="...重点覆盖地域">
				</div>
			</div>
						    
			<div class="control-group">
				<label class="control-label" for="coverIsp">重点覆盖ISP</label>
				<div class="controls">
					<c:forEach var="map" items="${ispTypeMap}">
						<label class="checkbox">
							<input type="checkbox" name="coverIsp" value="${map.key}" 
								<c:forEach var="coverIsp" items="${fn:split(mdn.coverIsp,',')}">
									<c:if test="${map.key == coverIsp }"> checked="checked" </c:if>
						    	</c:forEach>
							class="required">
							<span class="checkboxText ">${map.value}</span>
						</label>
					</c:forEach>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="bandwidth">加速服务带宽(M)</label>
				<div class="controls">
					<input type="text" id="bandwidth" name="bandwidth" class="required digits" value="${mdn.bandwidth }" maxlength="45" placeholder="...加速服务带宽">
				</div>
			</div>
			
			<div class="form-actions">
				<a href="${ctx}/apply/update/${mdn.apply.id}/" class="btn">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
