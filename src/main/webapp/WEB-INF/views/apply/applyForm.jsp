<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>申请管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#serviceTag").focus();
			
			// 初始化服务开始和结束时间,结束时间默认为开始时间3个月后
			
			$("#serviceStart").val(getDatePlusMonthNum(0));
			$("#serviceEnd").val(getDatePlusMonthNum(3));
			 
			
			$("#inputForm").validate({
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
	</script>
</head>

<body>

<link href="${ctx}/static/common/css/inputForm.css" rel="stylesheet">

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${apply.id}">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${not empty apply }">修改服务申请单</c:when>
					<c:otherwise>创建服务申请单</c:otherwise>
				</c:choose>
			</small></legend>
			
			
			<div class="control-group">
				<label class="control-label" for="serviceTag">服务标签</label>
				<div class="controls">
					<input type="text" id="serviceTag" name="serviceTag" class="required" placeholder="...服务标签">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="priority">优先级</label>
				<div class="controls">
					<select id="priority" name="priority">
						<option value="1">普通</option>
						<option value="2">高</option>
						<option value="3">紧急</option>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="serviceStart">服务开始时间</label>
				<div class="controls">
					<input type="time" id="serviceStart" name="serviceStart" class="datepicker required"  placeholder="...服务开始时间">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="serviceEnd">服务结束时间</label>
				<div class="controls">
					<input type="time" id=serviceEnd name="serviceEnd" class="datepicker required"  placeholder="...服务结束时间">
				</div>
			</div>
		
			<div class="control-group">
					<label class="control-label" for="description">用途描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...用途描述"
						maxlength="500" class="required "></textarea>
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
