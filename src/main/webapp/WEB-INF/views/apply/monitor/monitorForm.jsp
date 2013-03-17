<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#serviceTag").focus();
			
			/*禁用回车提交form表单.*/
			$("#inputForm").keypress(function(e) {
				if (e.which == 13) {return false;}
			});
			
			$("#inputForm").validate();
			
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
		
		
		/*点击选择ELB弹出窗口保存时,连同ELB的信息生成HTML代码插入页面.*/
		$(document).on("click", "#elbModalSave", function() {
			//Step.1
			var selectedArray = [],
				html = "";
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//Step.2 遍历页面,将存在于页面的elbIds放入临时数组selectedArray中
			$("div.resources").each(function() {
				var elbIds = $(this).find("input[name='elbIds']").val();
				selectedArray.push(elbIds);
			});
			
			//遍历选择的ELB
			$CheckedIds.each(function() {
				var $this = $(this);
				var elbIds = $this.val();
				var $td = $this.closest("tr").find("td");
				var identifier = $td.eq(1).text();
				var virtualIp = $td.eq(2).text();
				//Step.3 对选择的实例ID和临时数组selectedArray进行比较.如果存在,跳过.
				if ($.inArray(elbIds, selectedArray) == -1) {
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button type="button" class="close" data-dismiss="alert">×</button>';
					html += '<input type="hidden" value="' + elbIds + '" name="elbIds">';
					html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + identifier + '</strong></dd>';
					html += '<dd><em>负载均衡虚拟IP</em>&nbsp;&nbsp;<strong>' + virtualIp + '</strong></dd>';
					html += '</div> ';
				}
			});
			
			$("#resourcesDIV dl").append(html);
			
			//初始化
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			$ModalDiv.find(".checker > span").removeClass("checked"); //uniform checkbox的处理
		});
		
		
		/*点击选择实例弹出窗口保存时,连同实例的信息生成HTML代码插入页面.*/
		$(document).on("click", "#computeModalSave", function() {
			//Step.1
			var selectedArray = [],
				html = "";
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//Step.2 遍历页面,将存在于页面的elbIds放入临时数组selectedArray中
			$("div.resources").each(function() {
				var computeIds = $(this).find("input[name='computeIds']").val();
				selectedArray.push(computeIds);
			});
			
			var splitStr = "-"; //分割符号
			var blank = "&nbsp;"; //空格
			
			//遍历选择的实例
			$CheckedIds.each(function() {
				var $this = $(this);
				var computeId = $this.val();
				var $td = $this.closest("tr").find("td");
				var identifier = $td.eq(1).text();
				var remark = $td.eq(2).text();
				var innerIp = $td.eq(3).text();
				
				//Step.3 对选择的实例ID和临时数组selectedArray进行比较.如果存在,跳过.
				if ($.inArray(computeId, selectedArray) == -1) {
					
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button type="button" class="close" data-dismiss="alert">×</button>';
					html += '<input type="hidden" value="' + computeId + '" name="computeIds">';
					html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + identifier + '</strong></dd>';
					html += '<dd><em>用途信息</em>&nbsp;&nbsp;<strong>' + remark + '</strong></dd>';
					html += '<dd><em>IP地址</em>&nbsp;&nbsp;<strong>' + innerIp + '</strong></dd>';
					
					var port = "",
						portText = "",
						process = "",
						processText = "",
						mountPoint = "",
						mountPointText = "";
					
					//监控端口
					$(".monitorPort").each(function() {
						var $this = $(this);
						port += $this.val() + splitStr;
						portText += $this.val() + blank;
					});
					
					//监控进程
					$(".monitorMaxProcess").each(function() {
						var $this = $(this);
						process += $this.val() + splitStr;
						processText += $this.val() + blank;
					});
					
					//挂载路径
					$(".monitorMountPoint").each(function() {
						var $this = $(this);
						mountPoint += $this.val() + splitStr;
						mountPointText += $this.val() + blank;
					});
					
					html += '<input type="hidden" value="' + port + '" name="ports">';
					html += '<input type="hidden" value="' + process + '" name="processes">';
					html += '<input type="hidden" value="' + mountPoint + '" name="mountPoints">';
					
					if (portText != blank) {
						html += '<dd><em>监控端口</em>&nbsp;&nbsp;<strong>' + portText + '</strong>';
					}
					if (processText != blank) {
						html += '<dd><em>监控进程</em>&nbsp;&nbsp;<strong>' + processText + '</strong>';
					}
					if (mountPointText != blank) {
						html += '<dd><em>挂载路径</em>&nbsp;&nbsp;<strong>' + mountPointText + '</strong>';
					}
					
					//CPU
					var $cpu = $("#cpu");
					html += '<dd><em>' + $cpu.find(".control-label").text() + '</em>';
					html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>' + $cpu.find(".warn-threshold>option:selected").text() + '</strong>';
					html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>' + $cpu.find(".critical-threshold>option:selected").text() + '</strong></dd>';
					html += '<input type="hidden" value="' + $cpu.find(".warn-threshold").val() + '" name="cpuWarns">';
					html += '<input type="hidden" value="' + $cpu.find(".critical-threshold").val() + '" name="cpuCriticals">';
					
					//Memory 
					var $memory = $("#memory");
					html += '<dd><em>' + $memory.find(".control-label").text() + '</em>';
					html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>' + $memory.find(".warn-threshold>option:selected").text() + '</strong>';
					html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>' + $memory.find(".critical-threshold>option:selected").text() + '</strong></dd>';
					html += '<input type="hidden" value="' + $memory.find(".warn-threshold").val() + '" name="memoryWarns">';
					html += '<input type="hidden" value="' + $memory.find(".critical-threshold").val() + '" name="memoryCriticals">';
					
					//网络丢包率
					var $pingLoss = $("#pingLoss");
					html += '<dd><em>' + $pingLoss.find(".control-label").text() + '</em>';
					html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>' + $pingLoss.find(".warn-threshold>option:selected").text() + '</strong>';
					html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>' + $pingLoss.find(".critical-threshold>option:selected").text() + '</strong></dd>';
					html += '<input type="hidden" value="' + $pingLoss.find(".warn-threshold").val() + '" name="pingLossWarns">';
					html += '<input type="hidden" value="' + $pingLoss.find(".critical-threshold").val() + '" name="pingLossCriticals">';
					
					//硬盘可用率
					var $disk = $("#disk");
					html += '<dd><em>' + $disk.find(".control-label").text() + '</em>';
					html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>' + $disk.find(".warn-threshold>option:selected").text() + '</strong>';
					html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>' + $disk.find(".critical-threshold>option:selected").text() + '</strong></dd>';
					html += '<input type="hidden" value="' + $disk.find(".warn-threshold").val() + '" name="diskWarns">';
					html += '<input type="hidden" value="' + $disk.find(".critical-threshold").val() + '" name="diskCriticals">';
					
					//网络延时率
					var $pingDelay = $("#pingDelay");
					html += '<dd><em>' + $pingDelay.find(".control-label").text() + '</em>';
					html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>' + $pingDelay.find(".warn-threshold>option:selected").text() + '</strong>';
					html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>' + $pingDelay.find(".critical-threshold>option:selected").text() + '</strong></dd>';
					html += '<input type="hidden" value="' + $pingDelay.find(".warn-threshold").val() + '" name="pingDelayWarns">';
					html += '<input type="hidden" value="' + $pingDelay.find(".critical-threshold").val() + '" name="pingDelayCriticals">';
					
					//最大进程数
					var $maxProcess = $("#maxProcess");
					html += '<dd><em>' + $maxProcess.find(".control-label").text() + '</em>';
					html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>' + $maxProcess.find(".warn-threshold>option:selected").text() + '</strong>';
					html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>' + $maxProcess.find(".critical-threshold>option:selected").text() + '</strong></dd>';
					html += '<input type="hidden" value="' + $maxProcess.find(".warn-threshold").val() + '" name="maxProcessWarns">';
					html += '<input type="hidden" value="' + $maxProcess.find(".critical-threshold").val() + '" name="maxProcessCriticals">';
					
					//网卡流量
					var $networkFlow = $("#networkFlow");
					html += '<dd><em>' + $networkFlow.find(".control-label").text() + '</em>';
					html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>' + $networkFlow.find(".warn-threshold>option:selected").text() + '</strong>';
					html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>' + $networkFlow.find(".critical-threshold>option:selected").text() + '</strong></dd>';
					html += '<input type="hidden" value="' + $networkFlow.find(".warn-threshold").val() + '" name="networkFlowWarns">';
					html += '<input type="hidden" value="' + $networkFlow.find(".critical-threshold").val() + '" name="networkFlowCriticals">';
					html += '</div> ';
				}
			});
			
			$("#resourcesDIV dl").append(html);
			
			//初始化
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			$(".checker > span").removeClass("checked"); //针对页面所有的uniform checkbox的处理
		});
		
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建监控</small></legend>
			
			<!-- Step.1 -->
			<div class="step">
			
				<div class="control-group">
					<label class="control-label" for="serviceTag">服务标签</label>
					<div class="controls">
						<input type="text"  id="serviceTag" name="serviceTag"  class="required" maxlength="45" placeholder="...服务标签">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="priority">优先级</label>
					<div class="controls">
						<select id="priority" name="priority">
							<c:forEach var="map" items="${priorityMap }">
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
						<input type="text" id="serviceStart" name="serviceStart" readonly class="datepicker required"  placeholder="...服务开始时间">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="serviceEnd">服务结束时间</label>
					<div class="controls">
						<input type="text" id=serviceEnd name="serviceEnd" readonly class="datepicker required"  placeholder="...服务结束时间">
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
					<input class="btn" type="button" value="返回" onClick="history.back()">
					<input class="btn btn-primary nextStep" type="button" value="下一步">
				</div>
			
			</div><!-- Step.1 End -->
			
			<!-- Step.2 -->
			<div class="step">
				
				<div class="control-group">
					<label class="control-label" for="monitorMail">监控邮件列表</label>
					<div class="controls">
						<table class="table table-bordered table-condensed"  >
							<tbody>
								<tr class="clone">
									<td><input type="text"  id="monitorMails" name="monitorMails" class="required" maxlength="45" placeholder="...Email"></td>
									<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
								</tr>
							</tbody>
						</table>	
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="monitorPhone">监控手机列表</label>
					<div class="controls">
						 <table class="table table-bordered table-condensed"  >
							<tbody>
								<tr class="clone">
									<td><input type="text"  id="monitorPhones" name="monitorPhones" class="required" maxlength="45" placeholder="...Phone"></td>
									<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			
				<div class="form-actions">
					<input class="btn backStep" type="button" value="上一步">
					<input class="btn btn-primary nextStep" type="button" value="下一步">
				</div>
				
			</div><!-- Step.2 End -->
			
			<!-- Step.3 -->
			<div class="step">
				
			    <div>
				    <ul class="nav nav-tabs">
					    <li class="active"><a href="#elbTab" data-toggle="tab">ELB监控</a></li>
					    <li><a href="#computeTab" data-toggle="tab">实例监控</a></li>
				    </ul>
				    
				    <div class="tab-content">
				    
					    <!-- ELB -->
					    <div class="tab-pane active" id="elbTab">
							<div class="control-group">
								<div class="controls">
									 <a id="addElbBtn" class="btn" data-toggle="modal" href="#elbModal" >ELB相关资源</a>
								</div>
							</div>
					    </div><!-- ELB End -->
					    
				    	<!-- Compute -->
				    	<div class="tab-pane" id="computeTab">
				    	
				    		<div class="control-group">
								<label class="control-label" for="monitorPort">监控端口</label>
								<div class="controls">
									<table class="table table-bordered table-condensed"  >
										<tbody>
											<tr class="clone">
												<td><input type="text"  class="monitorPort required" maxlength="45" placeholder="...监控端口"></td>
												<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
											</tr>
										</tbody>
									</table>	
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="monitorMaxProcess">监控进程</label>
								<div class="controls">
									<table class="table table-bordered table-condensed"  >
										<tbody>
											<tr class="clone">
												<td><input type="text"  class="monitorMaxProcess required" maxlength="45" placeholder="...监控进程"></td>
												<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
											</tr>
										</tbody>
									</table>	
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="monitorMountPoint">挂载路径</label>
								<div class="controls">
									<table class="table table-bordered table-condensed"  >
										<tbody>
											<tr class="clone">
												<td><input type="text"  class="monitorMountPoint required" maxlength="45" placeholder="...挂载路径"></td>
												<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
											</tr>
										</tbody>
									</table>	
								</div>
							</div>
							
							<!-- cpu -->
							<div class="control-group threshold" id="cpu">
								<label class="control-label" for="cpu">CPU占用率</label>
								<div class="controls">
									<div class="span2">
							    		报警阀值&nbsp;
							    		<select class="input-small warn-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
							    	<div class="span2">
							    		警告阀值&nbsp;
							    		<select class="input-small critical-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<!-- Memory -->
							<div class="control-group threshold" id="memory">
								<label class="control-label" for="memory">内存占用率</label>
								<div class="controls">
									<div class="span2">
							    		报警阀值&nbsp;
							    		<select class="input-small warn-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
							    	<div class="span2">
							    		警告阀值&nbsp;
							    		<select class="input-small critical-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<!-- 网络丢包率 -->
							<div class="control-group threshold" id="pingLoss">
								<label class="control-label" for="pingLoss">网络丢包率</label>
								<div class="controls">
									<div class="span2">
							    		报警阀值&nbsp;
							    		<select class="input-small warn-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
							    	<div class="span2">
							    		警告阀值&nbsp;
							    		<select class="input-small critical-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<!-- 硬盘可用率 -->
							<div class="control-group threshold" id="disk">
								<label class="control-label" for="disk">硬盘可用率</label>
								<div class="controls">
									<div class="span2">
							    		报警阀值&nbsp;
							    		<select class="input-small warn-threshold">
											<c:forEach var="map" items="${thresholdLtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
							    	<div class="span2">
							    		警告阀值&nbsp;
							    		<select class="input-small critical-threshold">
											<c:forEach var="map" items="${thresholdLtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
								</div>
							</div>
							
							<!--  网络延时率 -->
							<div class="control-group threshold" id="pingDelay">
								<label class="control-label" for="pingDelay">网络延时率</label>
								<div class="controls">
									<div class="span2">
							    		报警阀值&nbsp;
							    		<select class="input-small warn-threshold">
											<c:forEach var="map" items="${thresholdNetGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
							    	<div class="span2">
							    		警告阀值&nbsp;
							    		<select class="input-small critical-threshold">
											<c:forEach var="map" items="${thresholdNetGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
								</div>
							</div>
							
				    		<!-- 最大进程数 -->
							<div class="control-group threshold" id="maxProcess">
								<label class="control-label" for="maxProcess">最大进程数</label>
								<div class="controls">
									<div class="span2">
							    		报警阀值&nbsp;
							    		<select class="input-small warn-threshold">
											<c:forEach var="map" items="${maxProcessMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
							    	<div class="span2">
							    		警告阀值&nbsp;
							    		<select class="input-small critical-threshold">
											<c:forEach var="map" items="${maxProcessMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
								</div>
							</div>
							
				    		<!-- 网卡流量 -->
							<div class="control-group threshold" id="networkFlow">
								<label class="control-label" for="networkFlow">网卡流量</label>
								<div class="controls">
									<div class="span2">
							    		报警阀值&nbsp;
							    		<select class="input-small warn-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
							    	<div class="span2">
							    		警告阀值&nbsp;
							    		<select class="input-small critical-threshold">
											<c:forEach var="map" items="${thresholdGtMap }"><option value="${map.key }">${map.value }</option></c:forEach>
										</select>
									</div>
								</div>
							</div>
							
					    	<div class="control-group" style="padding-top: 10px;">
								<div class="controls">
									 <a id="addComputeBtn" class="btn" data-toggle="modal" href="#computeModal" >实例相关资源</a>
								</div>
							</div>
					    </div><!-- Compute End -->
					    
				    </div>
			    </div>
			    
			    <hr>
			
				<div id="resourcesDIV"><dl class="dl-horizontal"></dl></div>
				
				<div class="form-actions">
					<input class="btn backStep" type="button" value="上一步">
					<input class="btn btn-primary" type="submit" value="提交">
				</div>
				
			</div><!-- Step.3 End -->
			
		</fieldset>
		
	</form>
	
	<!-- 选择ELB的Modal -->
	<form id="elbModalForm" action="#" >
		<div id="elbModal" class="modal hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>负载均衡器ELB</h4></div>
				
			<div class="modal-body">
				<table class="table table-striped table-bordered table-condensed">
					<thead><tr><th><input type="checkbox"></th><th>ELB标识符</th><th>负载均衡虚拟IP</th></tr></thead>
					<tbody id="resources-tbody">
						<c:forEach var="elb" items="${monitorElbs}">
							<tr>
								<td><input type="checkbox" value="${elb.id }"></td>
								<td>${elb.identifier}</td>
								<td>${elb.virtualIp}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
				<a id="elbModalSave" href="#" class="btn btn-primary" data-dismiss="modal" >确定</a>
			</div>
		</div>
	</form><!-- 实例规格选择的Modal End -->
	
	<!-- 实例选择的Modal -->
	<form id="computeModalForm" action="#" >
		<div id="computeModal" class="modal container hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>实例</h4></div>
				
			<div class="modal-body">
				<table class="table table-striped table-bordered table-condensed">
					<thead><tr><th><input type="checkbox"></th><th>实例标识符</th><th>用途信息</th><th>IP地址</th></tr></thead>
					<tbody id="resources-tbody">
						<c:forEach var="compute" items="${allComputes}">
							<tr>
								<td><input type="checkbox" value="${compute.id }"></td>
								<td>${compute.identifier}</td>
								<td>${compute.remark}</td>
								<td>${compute.innerIp}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
				
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
				<a id="computeModalSave" href="#" class="btn btn-primary" data-dismiss="modal" >确定</a>
			</div>
		</div>
	</form><!-- 实例规格选择的Modal End -->
	
</body>
</html>
