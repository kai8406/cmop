<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>ES3管理</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			
			$("#inputForm").validate({
				errorClass: "help-inline",
				errorElement: "span",
				highlight: function(element, errorClass, validClass) {
					$(element).closest('.control-group').addClass('error');
				},
				unhighlight: function(element, errorClass, validClass) {
					$(element).closest('.control-group').removeClass('error');
				}
	
			});
			
			
			
			 /*点击弹出窗口保存时.*/
			 
			$("#modalSave").click(function() {
				
				
			});
			 
			 
			/*根据alert中的资源信息,组成汇总信息.*/
			$(".nextStep").click(function() {
	
				var html = '<dl class="dl-horizontal">';
				
				html += ' <dt>所属服务申请</dt>';
				
				html += '<dd>' + $("#applyId>option:selected").text() + '</dd>';
	
				$("#resourcesDIV div.resources").each(function() {
					
					var $this = $(this);
	
					var basicInfo = $this.find("div.row").find("div:first").text();
					var remark = $this.find("input").val();
					var esg = $this.find("select>option:selected").text();
					
				    html += '<hr>';
					html += '<dt>基本信息</dt>';
					html += '<dd>' + basicInfo + '</dd>';
					
					html += '<dt>用途</dt>';
					html += '<dd>' + remark + '</dd>';
					html += '<dt>关联ESG</dt>';
					html += '<dd>' + esg + '</dd>';  
	
				});
	
				html += '</dl>';
	
				$("#resourcesList").append(html);
				
			});
			
		});
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>修改ES3</small></legend>
			
			<!-- Step.1 -->
			<div class="step">
			
				<div class="control-group">
					<label class="control-label" for="applyId">所属服务申请</label>
					<div class="controls">
						<select id="applyId" name="applyId" class="required">
							<c:forEach var="item" items="${baseStationApplys}">
								<option value="${item.id }">${item.title} ( ${item.description} )</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<hr>
				
				<div class="control-group">
					<label class="control-label" for="applyId">存储类型</label>
					<div class="controls">
						<select id="storageType" class="required">
							<c:forEach var="map" items="${storageTypeMap}">
								<option value="${map.key}">${map.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="space">容量空间</label>
					<div class="controls">
						<input type="text" id="space" class="digits" maxlength="6" placeholder="...容量空间">
					</div>
				</div>
				
				<div class="control-group">
					<div class="controls">
						 <a id="addComputeBtn" class="btn" href="#computeModal" data-toggle="modal">实例相关资源</a>
					</div>
				</div>
					
				 
				 <hr>
				
				<!-- 生成的资源 -->
				<div id="resourcesDIV">
					<div class="resources alert alert-block alert-info fade in">
						<button data-dismiss="alert" class="close" type="button">×</button>
						<div class="row">
							<div class="span5">Windows7 &nbsp; 64bit &nbsp; DELL_C6100</div>
							<div style="margin-bottom: 0px; margin-left: 0px;"
								class="span2 control-group">
								<input type="text" placeholder="...用途信息" maxlength="45"
									class="required input-small" name="remarks" id="remarks1">
							</div>
							<div class="span2">
								<select name="esgIds" class="required input-medium"><option
										value="1">ESG List</option></select>
							</div>
							<input type="hidden" value="5" name="osTypes"><input
								type="hidden" value="2" name="osBits"><input
								type="hidden" value="7" name="serverTypes">
						</div>
					</div>
				</div>
				
				
				
				<div class="form-actions">
					<input class="btn" type="button" value="返回" onclick="history.back()">
					<input class="btn btn-primary nextStep" type="button" value="下一步">
				</div>
			
			</div><!-- Step.1 End -->
			
			<!-- Step.2 -->
			<div class="step">
				 
				 <!-- 汇总信息 -->
				 <div id="resourcesList"></div>
				 
				<div class="form-actions">
					<input class="btn backStep" type="button" value="返回">
					<input class="btn btn-primary" type="submit" value="提交">
				</div>
			
			</div><!-- Step.2 End -->
			
		</fieldset>
		
	</form>
	
	<!-- 实例选择的Modal -->
	<form id="modalForm" action="#" >
		<div id="computeModal" class="modal hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>选择规格和数量</h4></div>
				
			<div class="modal-body">
				<table class="table">
					<thead><tr><th>实例规格</th><th>数量</th></tr></thead>
					<tbody>
					
						<c:choose>
							<c:when test="${computeType == 1 }">
								 <c:forEach var="map" items="${pcsServerTypeMap }">
									<tr>
										<td>${map.value }</td>
										<td><input type="text" id="inputCount${map.key}" class="input-mini digits" min="1" maxlength="2" placeholder="..1-99" ></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								 <c:forEach var="map" items="${ecsServerTypeMap }">
									<tr>
										<td>${map.value }</td>
										<td><input type="text" id="inputCount${map.key}" class="input-mini digits" min="1" maxlength="2" placeholder="..1-99"></td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
				
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
				<button id="modalSave" class="btn btn-primary" data-dismiss="modal">确定</button>
			</div>
		</div>
	</form><!-- 实例规格选择的Modal End -->
	
</body>
</html>
