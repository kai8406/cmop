<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>ELB监控管理</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#serviceTag").focus();
			
			// 初始化服务开始和结束时间,结束时间默认为开始时间3个月后
			
			$("#serviceStart").val(getDatePlusMonthNum(0));
			$("#serviceEnd").val(getDatePlusMonthNum(3));
			
			$( "#serviceStart" ).datepicker({
				changeMonth: true,
				onClose: function( selectedDate ) {
				$( "#serviceEnd" ).datepicker( "option", "minDate", selectedDate );
				}
			});
			
			$( "#serviceEnd" ).datepicker({
				changeMonth: true,
				onClose: function( selectedDate ) {
				$( "#serviceStart" ).datepicker( "option", "maxDate", selectedDate );
				}
			});
			
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
			
			
			/*根据alert中的资源信息,组成汇总信息.*/
			$(".nextStep").click(function() {
				
				$("#resourcesList").empty();
				
				var html = '<dl class="dl-horizontal">';
				
				html += ' <dt>申请单信息</dt>';
				html += '<dd><em>服务标签<em>&nbsp;' + $("#serviceTag").val() + '</dd>';
				html += '<dd><em>优先级</em>&nbsp;' + $("#priority>option:selected").text() + '</dd>';
				html += '<dd><em>服务开始时间</em>&nbsp;' + $("#serviceStart").val() + '</dd>';
				html += '<dd><em>服务结束时间</em>&nbsp;' + $("#serviceEnd").val() + '</dd>';
				html += '<dd><em>用途描述</em>&nbsp;' + $("#description").val() + '</dd>';
				
				html += '<br>';
				
				html += ' <dt>监控邮件列表</dt>';
				$("input[name='monitorMails']").each(function() {
					html += '<dd>' + $(this).val() + '</dd>';
				});
				
				html += '<br>';
				
				html += ' <dt>手机邮件列表</dt>';
				$("input[name='monitorPhones']").each(function() {
					html += '<dd>' + $(this).val() + '</dd>';
				});
				
				html += '<br>';
				
				html += ' <dt>监控实例 & ELB相关资源</dt>';
				
				//遍历包含elbId隐藏域的资源
				$("div.resources").has("input[name='elbIds']").each(function() {
					var $this = $(this);
					html += '<dd>' + $this.find("dd:first").find("strong").text()+'('+$this.find("dd:eq(1)").find("strong").text()+')' + '</dd>';
				});
				
				//遍历包含computeId隐藏域的资源
				$("div.resources").has("input[name='computeIds']").each(function() {
					var $this = $(this);
					html += '<dd>' + $this.find("dd:first").find("strong").text()+'('+$this.find("dd:eq(1)").find("strong").text()+')' + '</dd>';
				});

				html += '</dl>';

				$("#resourcesList").append(html);
			});
			
		});
		
		
		/*点击选择ELB弹出窗口保存时,连同ELB的信息生成HTML代码插入页面.*/
	  	 
		$(document).on("click", "#elbModalSave", function() {
			
			//Step.1
			
			var selectedArray = [];
			var html = "";
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//Step.2 遍历页面,将存在于页面的elbIds放入临时数组selectedArray中
			
			$("div.resources").each(function() {
				var elbIds = $(this).find("input[name='elbIds']").val(); 
				selectedArray.push(elbIds);
			});
			
			//遍历选择的ELB
			
			$CheckedIds.each(function(){
				
				var $this = $(this);
				var elbIds =  $this.val();
		    	var identifier = $this.closest("tr").find("td").eq(1).text();
		    	var virtualIp = $this.closest("tr").find("td").eq(2).text();
		    	
		    	//Step.3 对选择的实例ID和临时数组selectedArray进行比较.如果存在,设置isUnique为false.
		    	
		      	if($.inArray(elbIds, selectedArray) == -1){
		      		html +='<div class="resources alert alert-block alert-info fade in">';
					html +='<button type="button" class="close" data-dismiss="alert">×</button>';
					html +='<input type="hidden" value="'+elbIds+'" name="elbIds">';
					html +='<dd><em>标识符</em>&nbsp;&nbsp;<strong>'+identifier+'</strong></dd>';
					html +='<dd><em>负载均衡虚拟IP</em>&nbsp;&nbsp;<strong>'+virtualIp+'</strong></dd>';
					html +='</div> ';
				}   
				
			});
			
			$("#resourcesDIV dl").append(html);
			
			//初始化
			
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			$ModalDiv.find(".checker > span").removeClass("checked");//uniform checkbox的处理
			
		}); 
		
		/*点击选择实例弹出窗口保存时,连同实例的信息生成HTML代码插入页面.*/
		$(document).on("click", "#computeModalSave", function() {
			
			//Step.1
			
			var selectedArray = [];
			var html = "";
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//Step.2 遍历页面,将存在于页面的elbIds放入临时数组selectedArray中
			
			$("div.resources").each(function() {
				var computeIds = $(this).find("input[name='computeIds']").val(); 
				selectedArray.push(computeIds);
			});
			
			
		
			var splitStr = "-";	//分割符号
			var invalidId = "0";//未选择的参数的Id
			var blank = "&nbsp;";//空格
			
			//遍历选择的实例
			
			$CheckedIds.each(function(){
				
				var $this = $(this);
				var computeId =  $this.val();
		    	var identifier = $this.closest("tr").find("td").eq(1).text();
		    	var remark = $this.closest("tr").find("td").eq(2).text();
		    	var innerIp = $this.closest("tr").find("td").eq(3).text();
		    	
				//Step.3 对选择的实例ID和临时数组selectedArray进行比较.如果存在,设置isUnique为false.
		    	
		      	if($.inArray(computeId, selectedArray) == -1){
		      		
		      		html +='<div class="resources alert alert-block alert-info fade in">';
					html +='<button type="button" class="close" data-dismiss="alert">×</button>';
					html +='<input type="hidden" value="'+computeId+'" name="computeIds">';
					html +='<dd><em>标识符</em>&nbsp;&nbsp;<strong>'+identifier+'</strong></dd>';
					html +='<dd><em>用途信息</em>&nbsp;&nbsp;<strong>'+remark+'</strong></dd>';
					html +='<dd><em>IP地址</em>&nbsp;&nbsp;<strong>'+innerIp+'</strong></dd>';
		    	
			    	//监控端口
			    	
			    	var port = "";
			    	var portText = "";
			    	var maxProcess = "";
			    	var maxProcessText = "";
			    	var mountPath = "";
			    	var mountPathText = "";
			    	
		    		$(".monitorPort").each(function(){
		    			var $this = $(this);
		    			port += $this.val() + splitStr;
		    			portText += $this.val() + blank;
			    	});
		    		
			    	//监控进程
			    	
			    	$(".monitorMaxProcess").each(function(){
			    		var $this = $(this);
			    		maxProcess += $this.val() + splitStr;
			    		maxProcessText += $this.val() + blank;
			    	});
	
			    	//挂载路径
			    	
			    	$(".monitorMountPath").each(function(){
			    		var $this = $(this);
			    		mountPath += $this.val() + splitStr;
			    		mountPathText += $this.val() + blank;
			    	});
			    	
			    	html +='<input type="hidden" value="'+port+'" name="ports">';
			    	html +='<input type="hidden" value="'+maxProcess+'" name="maxProcesses">';
			    	html +='<input type="hidden" value="'+mountPath+'" name="mountPaths">';
			    	
			    	if(portText != blank){
				    	html += '<dd><em>监控端口</em>&nbsp;&nbsp;<strong>'+portText+'</strong>';
			    	}
			    	
			    	if(maxProcessText != blank){
				    	html += '<dd><em>监控进程</em>&nbsp;&nbsp;<strong>'+maxProcessText+'</strong>';
			    	}
			    	
			    	if(mountPathText != blank){
			    		html += '<dd><em>挂载路径</em>&nbsp;&nbsp;<strong>'+mountPathText+'</strong>';
			    	}
			    	
			    	//CPU
			    	var $cpuChecked = $("#isCpuChecked");
			    	if($cpuChecked.is(":checked")){
			    		var $cpu = $cpuChecked.closest(".threshold");
			    		html += '<dd><em>'+$cpu.find(".checkboxText").text()+'</em>';
			    		html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>'+$cpu.find(".warn-threshold>option:selected").text()+'</strong>';
			    		html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>'+$cpu.find(".critical-threshold>option:selected").text()+'</strong></dd>';
			    		html +='<input type="hidden" value="'+$cpu.find(".warn-threshold").val()+'" name="cpuWarns">';
			    		html +='<input type="hidden" value="'+$cpu.find(".critical-threshold").val()+'" name="cpuCriticals">';
			    	}else{
			    		html +='<input type="hidden" value="'+invalidId+'" name="cpuWarns">';
			    		html +='<input type="hidden" value="'+invalidId+'" name="cpuCriticals">';
			    	}
			    	
			    	//Memory 
			    	var $memoryChecked = $("#isMemoryChecked");
			    	if($memoryChecked.is(":checked")){
			    		var $memory = $memoryChecked.closest(".threshold");
			    		html += '<dd><em>'+$memory.find(".checkboxText").text()+'</em>';
			    		html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>'+$memory.find(".warn-threshold>option:selected").text()+'</strong>';
			    		html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>'+$memory.find(".critical-threshold>option:selected").text()+'</strong></dd>';
			    		html +='<input type="hidden" value="'+$memory.find(".warn-threshold").val()+'" name="memoryWarns">';
			    		html +='<input type="hidden" value="'+$memory.find(".critical-threshold").val()+'" name="memoryCriticals">';
			    	}else{
			    		html +='<input type="hidden" value="'+invalidId+'" name="memoryWarns">';
			    		html +='<input type="hidden" value="'+invalidId+'" name="memoryCriticals">';
			    	}
			    	
			    	//网络丢包率
			    	var $pingLossChecked = $("#isPingLossChecked");
			    	if($pingLossChecked.is(":checked")){
			    		var $pingLoss = $pingLossChecked.closest(".threshold");
			    		html += '<dd><em>'+$pingLoss.find(".checkboxText").text()+'</em>';
			    		html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>'+$pingLoss.find(".warn-threshold>option:selected").text()+'</strong>';
			    		html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>'+$pingLoss.find(".critical-threshold>option:selected").text()+'</strong></dd>';
			    		html +='<input type="hidden" value="'+$pingLoss.find(".warn-threshold").val()+'" name="pingLossWarns">';
			    		html +='<input type="hidden" value="'+$pingLoss.find(".critical-threshold").val()+'" name="pingLossCriticals">';
			    	}else{
			    		html +='<input type="hidden" value="'+invalidId+'" name="pingLossWarns">';
			    		html +='<input type="hidden" value="'+invalidId+'" name="pingLossCriticals">';
			    	}
			    	
			    	//硬盘可用率
			    	var $diskChecked = $("#isDiskChecked");
			    	if($diskChecked.is(":checked")){
			    		var $disk = $diskChecked.closest(".threshold");
			    		html += '<dd><em>'+$disk.find(".checkboxText").text()+'</em>';
			    		html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>'+$disk.find(".warn-threshold>option:selected").text()+'</strong>';
			    		html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>'+$disk.find(".critical-threshold>option:selected").text()+'</strong></dd>';
			    		html +='<input type="hidden" value="'+$disk.find(".warn-threshold").val()+'" name="diskWarns">';
			    		html +='<input type="hidden" value="'+$disk.find(".critical-threshold").val()+'" name="diskCriticals">';
			    	}else{
			    		html +='<input type="hidden" value="'+invalidId+'" name="diskWarns">';
			    		html +='<input type="hidden" value="'+invalidId+'" name="diskCriticals">';
			    	}
			    	
			    	//网络延时率
			    	var $pingDelayChecked = $("#isPingDelayChecked");
			    	if($pingDelayChecked.is(":checked")){
			    		var $pingDelay = $pingDelayChecked.closest(".threshold");
			    		html += '<dd><em>'+$pingDelay.find(".checkboxText").text()+'</em>';
			    		html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>'+$pingDelay.find(".warn-threshold>option:selected").text()+'</strong>';
			    		html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>'+$pingDelay.find(".critical-threshold>option:selected").text()+'</strong></dd>';
			    		html +='<input type="hidden" value="'+$pingDelay.find(".warn-threshold").val()+'" name="pingDelayWarns">';
			    		html +='<input type="hidden" value="'+$pingDelay.find(".critical-threshold").val()+'" name="pingDelayCriticals">';
			    	}else{
			    		html +='<input type="hidden" value="'+invalidId+'" name="pingDelayWarns">';
			    		html +='<input type="hidden" value="'+invalidId+'" name="pingDelayCriticals">';
			    	}
			    	
			    	
			    	//最大进程数
			    	var $maxProcessChecked = $("#isMaxProcess");
			    	if($maxProcessChecked.is(":checked")){
			    		var $maxProcess = $maxProcessChecked.closest(".threshold");
			    		html += '<dd><em>'+$maxProcess.find(".checkboxText").text()+'</em>';
			    		html += '&nbsp;&nbsp;报警阀值&nbsp;<strong>'+$maxProcess.find(".warn-threshold>option:selected").text()+'</strong>';
			    		html += '&nbsp;&nbsp;警告阀值&nbsp;<strong>'+$maxProcess.find(".critical-threshold>option:selected").text()+'</strong></dd>';
			    		html +='<input type="hidden" value="'+$maxProcess.find(".warn-threshold").val()+'" name="maxProcessWarns">';
			    		html +='<input type="hidden" value="'+$maxProcess.find(".critical-threshold").val()+'" name="maxProcessCriticals">';
			    	}else{
			    		html +='<input type="hidden" value="'+invalidId+'" name="maxProcessWarns">';
			    		html +='<input type="hidden" value="'+invalidId+'" name="maxProcessCriticals">';
			    	}
			    	
		      		html +='</div> ';
		    	
		      	} 
				
		    	
			});
			
			$("#resourcesDIV dl").append(html);
			
			//初始化
			
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			$(".checker > span").removeClass("checked");//针对页面所有的uniform checkbox的处理
			
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
						<input type="text"  id="serviceTag" name="serviceTag" value="test" class="required" maxlength="45" placeholder="...服务标签">
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
							maxlength="500" class="required ">test</textarea>
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
									<td><input type="text" value="test" id="monitorMails" name="monitorMails" class="required" maxlength="45" placeholder="...Email"></td>
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
									<td><input type="text" value="test" id="monitorPhones" name="monitorPhones" class="required" maxlength="45" placeholder="...Phone"></td>
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
												<td><input type="text" value="test" class="monitorPort" maxlength="45" placeholder="...监控端口"></td>
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
												<td><input type="text" value="test" class="monitorMaxProcess" maxlength="45" placeholder="...监控进程"></td>
												<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
											</tr>
										</tbody>
									</table>	
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="monitorMountPath">挂载路径</label>
								<div class="controls">
									<table class="table table-bordered table-condensed"  >
										<tbody>
											<tr class="clone">
												<td><input type="text" value="test" class="monitorMountPath" maxlength="45" placeholder="...挂载路径"></td>
												<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
											</tr>
										</tbody>
									</table>	
								</div>
							</div>
							
				    	
				    		<!-- CPU -->
					    	<div class="row threshold">
						    	<div class="span2">
						    		<label class="checkbox inline"><input type="checkbox" id="isCpuChecked"></label><span class="checkboxText">CPU占用率</span>
					    		</div>
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
							
				    		<!-- Memory -->
					    	<div class="row threshold">
						    	<div class="span2">
						    		<label class="checkbox inline"><input type="checkbox" id="isMemoryChecked"></label><span class="checkboxText">内存占用率</span>
					    		</div>
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
							
				    		<!-- 网络丢包率 -->
					    	<div class="row threshold">
						    	<div class="span2">
						    		<label class="checkbox inline"><input type="checkbox" id="isPingLossChecked"></label><span class="checkboxText">网络丢包率</span>
					    		</div>
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
							
				    		<!-- 硬盘可用率 -->
					    	<div class="row threshold">
						    	<div class="span2">
						    		<label class="checkbox inline"><input type="checkbox" id="isDiskChecked"></label><span class="checkboxText">硬盘可用率</span>
					    		</div>
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
							
				    		<!-- 网络延时率 -->
					    	<div class="row threshold">
						    	<div class="span2">
						    		<label class="checkbox inline"><input type="checkbox" id="isPingDelayChecked"></label><span class="checkboxText">网络延时率</span>
					    		</div>
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
							
				    		<!-- 最大进程数 -->
					    	<div class="row threshold">
						    	<div class="span2">
						    		<label class="checkbox inline"><input type="checkbox" id="isMaxProcess"></label><span class="checkboxText">最大进程数</span>
					    		</div>
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
					<input class="btn btn-primary nextStep" type="button" value="下一步">
				</div>
				
			</div><!-- Step.3 End -->
			
			<!-- Step.4 -->
			<div class="step">
				 <!-- 汇总信息 -->
				<div id="resourcesList"></div>
				<div class="form-actions">
					<input class="btn backStep" type="button" value="上一步">
					<input class="btn btn-primary" type="submit" value="提交">
				</div>
			</div><!-- Step.4 End -->
			 
			
		</fieldset>
		
	</form>
	
	<!-- 选择ELB的Modal -->
	<form id="elbModalForm" action="#" >
		<div id="elbModal" class="modal hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>负载均衡器ELB</h4></div>
				
			<div class="modal-body">
				<table class="table table-striped table-bordered table-condensed">
					<thead><tr><th><input type="checkbox"></th><th>标识符</th><th>负载均衡虚拟IP</th></tr></thead>
					<tbody id="resources-tbody">
						<c:forEach var="elb" items="${allElbs}">
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
					<thead><tr>
						<th><input type="checkbox"></th>
						<th>标识符</th>
						<th>用途信息</th>
						<th>IP地址</th>
					</tr></thead>
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
