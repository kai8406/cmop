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
		
		
		function searchResources(){
			
			//Post方法提交查询资源Resources.
			$("#resourcesModal").ajaxSubmit({
				url : "${ctx}/ajax/getResourcesList",
				type: "POST" ,
				dataType: "json",
				success : function(responseText, statusText, xhr, $form)  { 
					alert(statusText);
				}  
			});
			
			return false; 
			
		};
		
		
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
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"></div>
			 
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
	<!-- 资源列表Modal -->
	<div id="resourcesModal" class="modal container hide fade" tabindex="-1">
		<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>资源列表</h3></div>
		<div class="modal-body">
		
		<form class="form-inline well well-small">

			<div class="row">
			
				<div class="span3">
					<label class="search-text">服务标签</label> 
					<input type="text" id="serviceTagName" name="serviceTagName" class="span2" maxlength="45">
				</div>
				
				<div class="span3">
					<label class="search-text">IP地址</label> 
					<input type="text" id="ipAddress" name="ipAddress" class="span2" maxlength="45">
				</div>
				
				<div class="span3">
					<label class="control-label search-text">服务类型</label> 
					<select id="serviceType" name="serviceType" class="span2">
						<option selected="selected" value="">Choose...</option>
						<c:forEach var="map" items="${resourcesServiceTypeMap }">
							<option value="${map.key }">${map.value }</option>
						</c:forEach>
					</select>
				</div>
				
				<div class="span2 pull-right">
					<button class="btn tip-bottom" title="搜索" type="button" onclick="searchResources()" ><i class="icon-search"></i></button>
					<button class="btn tip-bottom" title="刷新" type="reset"><i class="icon-refresh"></i></button>
					<button class="btn tip-bottom options"  title="更多搜索条件" type="button"><i class="icon-resize-small"></i></button>
				</div>
	
			</div>
			
			<!-- 多个搜索条件的话,启用 div.options -->
			<div class="row options">
			
				<div class="span3">
					<label class="search-text">标识符</label> 
					<input type="text" id="serviceIdentifier" name="serviceIdentifier" class="span2" maxlength="45">
				</div>
				
			</div>
	
		</form>
		
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal">关闭</button>
			<a href="delete/${item.id}" class="btn btn-primary">确定</a>
		</div>
	</div>
						
						
</body>
</html>
