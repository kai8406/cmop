<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>用户管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#failure").addClass("active");
			
			$("#loginName").focus();
			
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
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
		
		<fieldset>
		
			<legend><small>故障申报</small></legend>
			
			
			<div class="control-group">
				<label class="control-label" for="faultType">故障类型</label>
				<div class="controls">
					<select id="faultType" name="faultType" class="required">
						<c:forEach var="map" items="${applyServiceTypeMap}">
							<option value="${map.key}">${map.value}</option>							
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="level">优先级</label>
				<div class="controls">
					<select id="level" name="level">
						<c:forEach var="map" items="${priorityMap }">
							<option value="${map.key}">${map.value}</option>		
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="level">受理人</label>
				<div class="controls">
					<select id="assignee" name="assignee">
						<c:forEach var="map" items="${assigneeMap }">
							<option value="${map.key}">${map.value}</option>		
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="description">故障现象及描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...故障现象及描述"
						maxlength="500" class="required"></textarea>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="filename">附件上传</label>
				<div class="controls">
					<span id='filename'></span> 
					<input id="upload" type="file" name="file" data-url="${ctx}/failure/upload/file" multiple="multiple">
					<span>(最大尺寸:5 MB)</span>
					<br>
				</div>
			</div>
			
			<hr>
			
			<div class="control-group">
				<div class="controls">
					 <a id="addResourcesBtn" class="btn" href="#resourcesModal" data-toggle="modal">故障相关资源</a>
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
