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
				
				html += ' <dt>ELB相关资源</dt>';
				
				$("div.resources").each(function() {
					var $this = $(this);
					html += '<dd>' + $this.find("dd:first").find("strong").text()+'('+$this.find("dd:eq(1)").find("strong").text()+')' + '</dd>';
				});

				html += '</dl>';

				$("#resourcesList").append(html);
			});
			
		});
		
		
		
		
		 /*点击弹出窗口保存时,连同ELB的信息生成HTML代码插入页面.*/
	  	 
		$(document).on("click", "#ModalSave", function() {
			
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
		
		
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建ELB监控</small></legend>
			
			<!-- Step.1 -->
			<div class="step">
			
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
						<input type="text" id="serviceStart" name="serviceStart" value="${apply.serviceStart }" readonly class="datepicker required"  placeholder="...服务开始时间">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="serviceEnd">服务结束时间</label>
					<div class="controls">
						<input type="text" id=serviceEnd name="serviceEnd" value="${apply.serviceEnd }" readonly class="datepicker required"  placeholder="...服务结束时间">
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
									<td><input type="text" id="monitorMails" name="monitorMails" class="required" maxlength="45" placeholder="...Email"></td>
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
									<td><input type="text" id="monitorPhones" name="monitorPhones" class="required" maxlength="45" placeholder="...Phone"></td>
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
				
				<div class="control-group">
					<div class="controls">
						 <a id="addElbBtn" class="btn" data-toggle="modal" href="#elbModal" >ELB相关资源</a>
					</div>
				</div>
			
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
	
	<!-- 实例选择的Modal -->
	<form id="modalForm" action="#" >
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
				<a id="ModalSave" href="#" class="btn btn-primary" data-dismiss="modal" >确定</a>
			</div>
		</div>
	</form><!-- 实例规格选择的Modal End -->
	
</body>
</html>
