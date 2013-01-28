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
			groups:{
				time:"serviceStart serviceEnd"
			},
			errorPlacement: function(error, element) {
				  var reset = checkTimeReset();
				  var $message = $("#message");
				  reset.length == 0 ?  $message.removeClass("in"):$message.addClass("in").find("span").text(reset);
				  //TODO JQuery.validate.js 中错误信息如何给个标示防止表单提交?现在虽然有提示信息.但是依然会提交.
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
			
			<div id="message" class="alert alert-error fade"><span></span></div>
			
			<c:if test="${not empty apply}">
			
				<div class="control-group">
					<label class="control-label" for="title">标题</label>
					<div class="controls">
						<p class="help-inline plain-text">${apply.title}</p>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="createTime">申请日期</label>
					<div class="controls">
						<p class="help-inline plain-text"><fmt:formatDate value="${apply.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></p>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="createTime">状态</label>
					<div class="controls">
						<p class="help-inline plain-text">
						 <c:forEach var="map" items="${applyStatusMap }">
						 	<c:if test="${map.key == apply.status }">${map.value }</c:if>
						</c:forEach>
						</p>
					</div>
				</div>
				
			</c:if>
			
			<div class="control-group">
				<label class="control-label" for="serviceTag">服务标签</label>
				<div class="controls">
					<input type="text" id="serviceTag" name="serviceTag" value="${apply.serviceTag }" class="required" maxlength="45" placeholder="...服务标签">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="priority">优先级</label>
				<div class="controls">
					<select id="priority" name="priority">
						<c:forEach var="map" items="${applyPriorityMap }">
							<option value="${map.key }" 
								<c:if test="${map.key == apply.priority }">
									selected="selected"
								</c:if>
							>${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="serviceStart">服务开始时间</label>
				<div class="controls">
					<input type="time" id="serviceStart" name="serviceStart" value="${apply.serviceStart }" readonly="readonly" class="datepicker required"  placeholder="...服务开始时间">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="serviceEnd">服务结束时间</label>
				<div class="controls">
					<input type="time" id=serviceEnd name="serviceEnd" value="${apply.serviceEnd }" readonly="readonly" class="datepicker required"  placeholder="...服务结束时间">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="description">用途描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...用途描述"
						maxlength="500" class="required ">${apply.description }</textarea>
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
