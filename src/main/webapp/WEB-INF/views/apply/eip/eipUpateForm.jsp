<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>
	
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
			
			/*关联实例和关联ELB select控件的切换*/
			
			$("input[name='linkRadio']").click(function(){
				if($(this).val() == "isCompute"){
					$("#computeSelectDiv").addClass("show").removeClass("hidden");
					$("#elbSelectDiv").addClass("hidden").removeClass("show");
				}else{
					$("#elbSelectDiv").addClass("show").removeClass("hidden");
					$("#computeSelectDiv").addClass("hidden").removeClass("show");
				}
			});
			
		});
		
		
		/**
		 * 提交form的时候.将关联类型和关联ID写入隐藏域中.
		 */
		function fillLinkType(){
			
			var $elbSelect = $("#elbSelectDiv.show #elbSelect");
			var $computeSelect = $("#computeSelectDiv.show #computeSelect");
			var linkType;
			var linkId;
			
			if($elbSelect.val() != undefined){
				
				//关联ELB
				linkType = "0";
				linkId = $elbSelect.val();
				
			}else{
				
				//关联实例
				linkType = "1";
				linkId = $computeSelect.val();
				
			}
			$("#linkType").val(linkType);
			$("#linkId").val(linkId);
			
		};
	  	 
	</script>
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="." method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="applyId" value="${eip.apply.id }">
		<input type="hidden" id="linkId" name="linkId" >
		<input type="hidden" id="linkType" name="linkType">
		
		<fieldset>
			<legend><small>修改EIP</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${eip.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="ispType">ISP运营商</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${ispTypeMap}"><c:if test="${map.key == eip.ispType }">${map.value }</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					<label class="radio inline">
 						<input type="radio" name="linkRadio" value="isCompute" checked="checked">关联实例
					</label>
					<label class="radio inline">
	 					<input type="radio" name="linkRadio" value="isElb">关联Elb
					</label>
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
				
					<div id="computeSelectDiv" class="show">
						<select id="computeSelect" class="required">
							<c:forEach var="item" items="${allComputes }">
								<option value="${item.id }">${item.identifier}(${item.innerIp })</option>
							</c:forEach>
						</select>					
					</div>
					
					<div id="elbSelectDiv" class="hidden">
						<select id="elbSelect" class="required">
							<c:forEach var="item" items="${allElbs }">
								<option value="${item.id }">${item.identifier}(${item.virtualIp })</option>
							</c:forEach>
						</select>			
					</div>		
 
				</div>
			</div>
			
			<table class="table table-bordered table-condensed"  >
				<thead><tr><th>Protocol</th><th>SourcePort</th><th>TargetPort</th><th></th></tr></thead>
				<tbody>
					<c:forEach var="item" items="${eip.eipPortItems}">
						<tr class="clone">
							<td>
								<select id="protocol" name="protocols" class="input-small required">
									<c:forEach var="map" items="${protocolMap}">
										<option value="${map.key }" <c:if test="${item.protocol == map.value }">selected="selected"</c:if>	
										>${map.value }</option>
									</c:forEach>
								</select>
							</td>
							<td><input type="text" id="sourcePort" name="sourcePorts" value="${item.sourcePort }" class="input-small " maxlength="45" placeholder="...SourcePort"></td>
							<td><input type="text" id="targetPort" name="targetPorts" value="${item.targetPort }" class="input-small " maxlength="45" placeholder="...TargetPort"></td>
							<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
			<div class="form-actions">
				<a href="${ctx}/apply/update/${elb.apply.id}/" class="btn">返回</a>
				<input class="btn btn-primary" type="submit" onclick="fillLinkType()" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
