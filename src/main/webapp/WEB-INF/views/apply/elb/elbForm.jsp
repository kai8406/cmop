<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>ELB管理</title>

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
			
			 /* 获得当前用户拥有的Compute拼装至实例相关资源的modal中.*/
			
			$.ajax({
				type: "GET",
				url: "${ctx}/ajax/getComputeByElbIsNullList",
				dataType: "json",
				success: function(data){
					
					var html = '';
					
					for ( var i = 0; i < data.length; i++) {
						
						html += '<tr>';
						html += '<td><input type="checkbox" value="'+data[i].id+'"></td>';
						html += '<td>'+data[i].identifier+'</td>';
						html += '<td>'+data[i].osType+'&nbsp;&nbsp;&nbsp;'+data[i].osBit+'&nbsp;&nbsp;&nbsp;'+data[i].serverType+'</td>';
						html += '<td>'+data[i].remark+'</td>';
						html += '<td>'+ (data[i].innerIp == null ? "" : data[i].innerIp ) +'</td>';
						html += '</tr> ';
						
					}
					
					$("#resources-tbody").append(html);
					
 				}		
			});
			
		});
		
		
		 /*点击弹出窗口保存时,连同ELB的信息生成HTML代码插入页面.*/
	  	 
		$(document).on("click", "#ModalSave", function() {
			
			/*
				1.创建一个临时数组selectedArray 以及一个是否重复的标识符isUnique
				2.遍历页面,将存在于页面的computeId放入数组selectedArray中.
				3.对选择的实例ID和数组selectedArray进行比较.如果存在,设置isUnique为false.
				4.只有isUnique为true,表示所选的实例ID没有被使用则可以创建HTML字符串在页面显示.
			*/
			
			//Step.1
			
			var selectedArray = [];
			var isUnique = true;
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//Step.2 遍历页面,将存在于页面的computeId放入临时数组selectedArray中
			
			$("div.resources").each(function() {
	 
				var computeIds = $(this).find("input[name='computeIds']").val(); // 1-2-3-
				var computeId = computeIds.substring(0,computeIds.length-1); // 1-2-3
				var computeIdArray = computeId.split("-"); //{1,2,3}
				for ( var i = 0; i < computeIdArray.length; i++) {
					 
					selectedArray.push(computeIdArray[i]);
					 
				}
			});
				
			var html = '';
			var computeIds = "";
			var computeIdentifier = "";
			var keepSession = $("input[name='keepSessionRadio']:checked").val();
			var keepSessionText = $("input[name='keepSessionRadio']:checked").parent().parent().parent().find("span.radioText").text();
			
			
			//遍历挂载Compute的Id和identifier.
			
			$CheckedIds.each(function(){
				
				var $this = $(this);
				computeIds +=  $this.val() +"-";
		    	computeIdentifier += $this.closest("tr").find("td").eq(1).text()+"&nbsp;";
		    	
		    	//Step.3 对选择的实例ID和临时数组selectedArray进行比较.如果存在,设置isUnique为false.
		    	
		    	if($.inArray($this.val(), selectedArray) > -1){
					isUnique = false;
				} 
				
			});
			
			//Step.4 只有isUnique为true,表示所选的实例ID没有被使用则可以创建HTML字符串在页面显示.
			
			if(isUnique){
			
				//拼装HTML文本
				
				html +='<div class="resources alert alert-block alert-info fade in">';
				html +='<button type="button" class="close" data-dismiss="alert">×</button>';
				html +='<input type="hidden" value="'+computeIds+'" name="computeIds">';
			    html +='<input type="hidden" value="'+keepSession+'" name="keepSessions">';
				html +='<dd><em>是否保持会话</em>&nbsp;&nbsp;<strong>'+keepSessionText+'</strong></dd>';
				html +='<dd><em>关联实例</em>&nbsp;&nbsp;<strong>'+computeIdentifier+'</strong></dd>';
				html +='<dd><em>端口映射 &nbsp;&nbsp;（协议、源端口、目标端口）</em></dd>';  
				
				var portTempArray =[];
				var protocolStr = "";
				var sourcePortStr = "";
				var targetPortStr = "";
				
				//端口信息相关的HTML
				
				$("tr.clone").each(function(){
					
					var $tr = $(this);
					
					var protocol = $tr.find("#protocol").val();
					var protocolText = $tr.find("#protocol>option:selected").text();
					var sourcePort = $tr.find("#sourcePort").val();
					var targetPort = $tr.find("#targetPort").val();
					
					var portTemp = protocol+"-"+sourcePort+"-"+targetPort;
					
					//检验LB的协议,端口,实例端口是否重复.(如果重复,生成的时候自动排除重复项.)
					
					if(portTempArray.length === 0 || $.inArray(portTemp, portTempArray) === -1){
						portTempArray.push(portTemp);
						protocolStr += protocol+"-";
						sourcePortStr +=  sourcePort+"-"; 
						targetPortStr += targetPort+"-";
						html +='<dd><strong>'+protocolText+'&nbsp;,&nbsp;'+sourcePort+'&nbsp;,&nbsp;'+targetPort+'</strong></dd>';
					}
					
				});
				
				html +='<input type="hidden" value="'+protocolStr+'" name="protocols">';
				html +='<input type="hidden" value="'+sourcePortStr+'" name="sourcePorts">';
				html +='<input type="hidden" value="'+targetPortStr+'" name="targetPorts">';
			
			
				html +='</div> ';
			
				//插入HTML文本
				
				$("#resourcesDIV dl").append(html);
			
			}else{
				 alert("同一计算资源不能关联多个ELB");
			}
			
			//初始化
			
			$("tr.clone:gt(0)").remove().end().find("input[type=text]").val('');
			portTempArray =[];
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			$ModalDiv.find(".checker > span").removeClass("checked");//uniform checkbox的处理
			isUnique = true;
			
			
		}); 
		 
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建ES3存储空间</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="applyId">所属服务申请</label>
				<div class="controls">
					<select id="applyId" name="applyId" class="required">
						<c:forEach var="item" items="${baseStationApplys}"><option value="${item.id }">${item.title} ( ${item.description} )</option></c:forEach>
					</select>
				</div>
			</div>
			
			<hr> 
			
			<div class="control-group">
				<label class="control-label" for="keepSession">是否保持会话</label>
				<div class="controls">
					<c:forEach var="map" items="${keepSessionMap}">
						<label class="radio inline">
							<input type="radio" name="keepSessionRadio" value="${map.key}" checked="checked"><span class="radioText">${map.value}</span>
						</label>
					</c:forEach>
				</div>
			</div>
			
			<table class="table table-bordered table-condensed"  >
				<thead><tr><th>Protocol</th><th>SourcePort</th><th>TargetPort</th><th></th></tr></thead>
				<tbody>
					<tr class="clone">
						<td>
							<select id="protocol" class="input-small required">
								<c:forEach var="map" items="${protocolMap}"><option value="${map.key }">${map.value }</option></c:forEach>
							</select>
						</td>
						<td><input type="text" id="sourcePort" class="input-small " maxlength="45" placeholder="...SourcePort"></td>
						<td><input type="text" id="targetPort" class="input-small " maxlength="45" placeholder="...TargetPort"></td>
						<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
					</tr>
				</tbody>
			</table>	
			
			<div class="control-group">
				<div class="controls">
					 <a id="addComputeBtn" class="btn" data-toggle="modal" href="#computeModal" >实例相关资源</a>
				</div>
			</div>
				
			<hr>
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal"></dl></div>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
	<!-- 实例选择的Modal -->
	<form id="modalForm" action="#" >
		<div id="computeModal" class="modal container hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>实例</h4></div>
				
			<div class="modal-body">
				<table class="table table-striped table-bordered table-condensed">
					<thead><tr>
						<th><input type="checkbox"></th>
						<th>实例标识符</th>
						<th>基本信息(操作系统,位数,规格)</th>
						<th>用途信息</th>
						<th>IP地址</th>
					</tr></thead>
					<tbody id="resources-tbody"></tbody>
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
