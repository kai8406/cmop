<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>实例管理</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
		
			$("input[name=osBit2]:first").attr('disabled','');	//Windows2008R2 没有32bit,只有64bit		
			
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
			
			 /*点击选择规格时,将选中的操作系统,位数等保存在临时隐藏域中..*/
			$(".serverTypeBtn").click(function() {
				
				var $parent = $(this).parent().parent();
				
				var osId = $parent.find("#osId").val();//操作系统ID
				
				var osNAME = $parent.find("span").text(); //操作系统名
	
				var osBitId = $parent.find("input[name='osBit" + osId + "']:checked").val();//选中的位数Id
	
				var osBitText =  $.trim($parent.find("input[name='osBit" + osId + "']:checked").closest("label").text()); //选中的位数文本.
				
				
				//装入临时隐藏域
				$("#osIdTmp").val(osId);
				$("#osNameTmp").val(osNAME);
				$("#bitValueTemp").val(osBitId);
				$("#bitTextTmp").val(osBitText);
				
			});
			
			
			 /*点击弹出窗口保存时.*/
			 
			$("#modalSave").click(function() {
				
				$("input[id^='inputCount']").each(function() { 
					
					var $this = $(this);
					
					var nCount = $this.val();//数量输入框值
					
					
					//数量输入框不为空时
					
					if (nCount != "") {
						
						//输入框的ID是有字符串"inputCount"+规格ID组成的. 所以获得字符串"inputCount"的长度,用于截取规格的ID
						
						var nLen = "inputCount".length;
						
						var serverTypeId = this.id.substring(nLen);
						
						var serverTypeText = $.trim($this.parent().parent().find("td:first").text());//规格名
						
						
						//从隐藏域中取出之前选择的操作系统和位数.
						
						var osId = $("#osIdTmp").val();
						var osNAME = $("#osNameTmp").val();
						var osBitId = $("#bitValueTemp").val();
						var osBitText = $("#bitTextTmp").val();
						
						
						//页面已生成的实例个数.
						
						var instanceCount = $("#resourcesDIV div.resources").size();
						
						
						for (var i = 0; i < nCount; i++) {
							
							//获得页面已有的alert数量,再加上i.用于区别不同的remark,以便检验.
							
							var loopId = instanceCount+ i; 
							
							var html = '<div class="resources alert alert-block alert-info fade in">';
							html += '<button type="button" class="close" data-dismiss="alert">×</button>';
							html += '<div class="row">';
							html += '<div class="span5">'+osNAME+' &nbsp; '+osBitText+' &nbsp; '+serverTypeText+'</div>';
							html += '<div class="span2 control-group" style="margin-bottom: 0px; margin-left: 0px;"><input type="text" id="remarks'+loopId+'" name="remarks" class="required input-small" maxlength="45" placeholder="...用途信息"></div>';
							html += '<div class="span2"><select class="required input-medium" name="esgIds"><option value="1">ESG List</option></select></div>';
							
							html += '<input type="hidden" name="osTypes" value="'+osId+'">';
							html += '<input type="hidden" name="osBits" value="'+osBitId+'">';
							html += '<input type="hidden" name="serverTypes" value="'+serverTypeId+'">';
							html += '</div>';
							html += '</div>';
							
							$("#resourcesDIV").append(html);
						}
						
					}
					
					$this.val('');//清空数量框
					
				});
				
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

	<form id="inputForm" action="." method="post" class="input-form form-horizontal" style="max-width: 960px;">
		
		<input type="hidden" id="computeType" name="computeType" value="${computeType}">
		
		<!-- 临时隐藏域 -->
		<input type="hidden" id="osIdTmp">
		<input type="hidden" id="osNameTmp">
		<input type="hidden" id="bitValueTemp">
		<input type="hidden" id="bitTextTmp">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${computeType == 1 }">创建实例PCS</c:when>
					<c:otherwise>创建实例ECS</c:otherwise>
				</c:choose>
			</small></legend>
			
			<!-- Step.1 -->
			<div class="step">
			
				<div class="control-group">
					<label class="control-label" for="applyId">所属服务申请</label>
					<div class="controls">
						<select id="applyId" name="applyId" class="required">
							<c:forEach var="item" items="${baseStationApplys}">
								<option value="${item.id }">${item.title }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<hr>
				
				<c:forEach var="map" items="${osTypeMap}">
					
					 <div class="row-fluid" style="margin-top: 10px;margin-bottom: 10px">
						
						<!-- 位数Id -->
						<input type="hidden" id="osId" value="${map.key}">
						
						<!-- Logo -->
						<div class="span3">
					 		<c:choose>
								<c:when test="${map.key == 1 || map.key ==2 || map.key ==5 }">
									<img alt="windowsOS" src="${ctx}/static/common/img/logo/windows-logo.png" />
								</c:when>
								<c:otherwise>
									<img alt="windowsOS" src="${ctx}/static/common/img/logo/centos-logo.png" />
								</c:otherwise>
							</c:choose>
						</div>
						 
						<!-- 操作系统名 -->
						<div class="span4"><h4><span>${map.value}</span></h4></div>
						
						<!-- 操作系统位数 -->
						<div class="span2">
							<c:forEach var="osBitMap" items="${osBitMap}">
								<label class="radio"> 
									<input type="radio" value="${osBitMap.key}" name="osBit${map.key }" <c:if test="${osBitMap.key == 2 }">checked="checked"</c:if> 
										><label id="osBitLab"><c:out value="${osBitMap.value}"/></label>
								</label>
							</c:forEach>
						</div>
						
						<!-- 选择规格 -->
						<div class="span2"><a class="btn serverTypeBtn" data-toggle="modal" href="#serverTypeModal">选择规格</a></div>
						 
					</div>
					
				</c:forEach>
				 
				 <hr>
				
				<!-- 生成的资源 -->
				<div id="resourcesDIV"></div>
				
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
	
	<!-- 实例规格选择的Modal -->
	<form id="modalForm" action="#" >
		<div id="serverTypeModal" class="modal hide fade" tabindex="-1">
	
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
