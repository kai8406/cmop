<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
			$("#name").focus();
			
			$("#inputForm").validate({
				rules:{
					name:{
						remote: "${ctx}/ajax/checkServiceTagName?oldName=${serviceTag.name}"
					}
				},
				messages:{
					name:{remote:"服务标签名称已存在"}
				}
			});
			
			// 初始化服务开始和结束时间,结束时间默认为开始时间3个月后
			$("#serviceStart").val(getDatePlusMonthNum(0));
			$("#serviceEnd").val(getDatePlusMonthNum(3));
			
			$("#serviceStart").datepicker({
				changeMonth: true,
				onClose: function(selectedDate) {
					$("#serviceEnd").datepicker("option", "minDate", selectedDate);
				}
			});
			$("#serviceEnd").datepicker({
				changeMonth: true,
				onClose: function(selectedDate) {
					$("#serviceStart").datepicker("option", "maxDate", selectedDate);
				}
			});
			
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${serviceTag.id}">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${not empty serviceTag }">修改服务标签</c:when>
					<c:otherwise>创建服务申请单</c:otherwise>
				</c:choose>
			</small></legend>
			
			<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
			
			<div class="control-group">
				<label class="control-label" for="name">服务标签名称</label>
				<div class="controls">
					<input type="text" id="name" name="name" value="${serviceTag.name }" class="required" maxlength="45" placeholder="...服务标签名称">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="priority">优先级</label>
				<div class="controls">
					<select id="priority" name="priority">
						<c:forEach var="map" items="${priorityMap }">
							<option value="${map.key }" 
								<c:if test="${map.key == serviceTag.priority }">
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
					<input type="text" id="serviceStart" name="serviceStart" value="${serviceTag.serviceStart }" readonly="readonly" class="datepicker required"  placeholder="...服务开始时间">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="serviceEnd">服务结束时间</label>
				<div class="controls">
					<input type="text" id=serviceEnd name="serviceEnd" value="${serviceTag.serviceEnd }" readonly="readonly" class="datepicker required"  placeholder="...服务结束时间">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="description">用途描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...用途描述"
						maxlength="500" class="required ">${serviceTag.description }</textarea>
				</div>
			</div>
			
			<c:if test="${not empty serviceTag}">
				<div class="control-group">
					<label class="control-label" for="createTime">创建日期</label>
					<div class="controls">
						<p class="help-inline plain-text"><fmt:formatDate value="${serviceTag.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></p>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="status">状态</label>
					<div class="controls">
						<p class="help-inline plain-text">
						 <c:forEach var="map" items="${resourcesStatusMap }">
						 	<c:if test="${map.key == serviceTag.status }">${map.value }</c:if>
						</c:forEach>
						</p>
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
