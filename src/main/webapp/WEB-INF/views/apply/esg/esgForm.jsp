<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

<title>安全组ESG管理</title>

<script>
	$(document).ready(function() {
		
		$("ul#navbar li#apply").addClass("active");
		
		 
		
	});
</script>
</head>

<body>
<link href="${ctx}/static/common/css/inputForm.css" rel="stylesheet">

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${apply.id}">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${not empty esg }">修改安全组ESG </c:when>
					<c:otherwise>创建安全组ESG</c:otherwise>
				</c:choose>
			</small></legend>
			
			<div id="message" class="alert alert-error fade"><span></span></div>
			
			<c:if test="${not empty esg}">
			
				<div class="control-group">
					<label class="control-label" for="identifier">标题</label>
					<div class="controls">
						<p class="help-inline plain-text">${esg.identifier}</p>
					</div>
				</div>
				
			</c:if>
			
			<div class="control-group">
				<label class="control-label" for="description">安全组的描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...安全组的描述"
						maxlength="45" class="required ">${esg.description }</textarea>
				</div>
			</div>
			
		 
		
		 
			 
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
