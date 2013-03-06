<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>EIP管理</title>

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
		
		
		 /*点击弹出窗口保存时,连同ES3的信息生成HTML代码插入页面.*/
	  	 
		$(document).on("click", "#createEIPBtn", function() {
			
			 //选中的关联实例或关联ELB对象
			
			var $elbSelect = $("#elbSelectDiv.show #elbSelect");
			var $computeSelect = $("#computeSelectDiv.show #computeSelect");
			
			
			
			
			
			/*
				1.创建一个临时数组selectedArray 以及一个是否重复的标识符isUnique
				2.遍历页面,将存在于页面的computeId放入临时数组selectedArray中.
				3.对选择的实例ID和临时数组selectedArray进行比较.如果存在,设置isUnique为false.
				4.只有isUnique为true,表示所选的实例ID没有被使用则可以创建HTML字符串在页面显示.
			*/
			
			//Step.1
			
			var selectedArray = [];
			var isUnique = true;
			
			
			//Step.2 遍历页面,将存在于页面的computeId放入临时数组selectedArray中
			
			/*
			$("div.resources").each(function() {
	 
				var computeIds = $(this).find("input[name='computeIds']").val(); // 1-2-3-
				var computeId = computeIds.substring(0,computeIds.length-1); // 1-2-3
				var computeIdArray = computeId.split("-"); //{1,2,3}
				for ( var i = 0; i < computeIdArray.length; i++) {
					 
					selectedArray.push(computeIdArray[i]);
					 
				}
			});
				 */
			var html = '';
			var $ispTyp = $("input[name='ispTypeCheckbox']:checked");
			$ispTyp.each(function(){
				
				//ISP的ID
				var $this = $(this);
				
				//ISP的文本
				var ispTypText =  $this.parent().parent().parent().find("span.checkboxText").text();
				
				//拼装HTML文本
				html +='<div class="resources alert alert-block alert-info fade in">';
				html +='<button type="button" class="close" data-dismiss="alert">×</button>';
			    html +='<input type="hidden" value="'+$this.val()+'" name="ispTypes">';
				html +='<dd><em>ISP运营商</em>&nbsp;&nbsp;<strong>'+ispTypText+'</strong></dd>';
				
				//判断选中的是哪种关联(linkTypes 关联类型 0:ELB ; 1: 实例)
				if($elbSelect.val() != undefined){
					//关联ELB
					var elbSelectText =   $elbSelect.find("option:selected").text();
					html +='<input type="hidden" value="0" name="linkTypes">';
					html +='<input type="hidden" value="'+$elbSelect.val()+'" name="linkIds">';
					html +='<dd><em>关联ELB</em>&nbsp;&nbsp;<strong>'+elbSelectText+'</strong></dd>';
				}else{
					//关联实例
					var computeSelectText = $computeSelect.find("option:selected").text();
					html +='<input type="hidden" value="1" name="linkTypes">';
					html +='<input type="hidden" value="'+$computeSelect.val()+'" name="linkIds">';
					html +='<dd><em>关联实例</em>&nbsp;&nbsp;<strong>'+computeSelectText+'</strong></dd>';
				}
				
				
				html +='<dd><em>端口映射 &nbsp;&nbsp;（协议、负载端口、实例端口）</em></dd>';  
				
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
				
				
			});
			
			
			if(isUnique){
			
				//插入HTML文本
				
				$("#resourcesDIV dl").append(html);
			
			}else{
				 alert("一个实例/ELB不能关联多个相同的ISP");
			}
			
			//初始化
			
			$("tr.clone:gt(0)").remove().end().find("input[type=text]").val('');
			portTempArray =[];
			selectedArray = [];
			isUnique = true;
			
			
		}); 
		 
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建EIP</small></legend>
			
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
				<label class="control-label" for="ispTypeCheckbox">ISP运营商</label>
				<div class="controls">
					<c:forEach var="map" items="${ispTypeMap}">
						<label class="checkbox">
							<input type="checkbox" name="ispTypeCheckbox" value="${map.key}"><span class="checkboxText">${map.value}</span>
						</label>
					</c:forEach>
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
					 <a id="createEIPBtn" class="btn btn-success">生成EIP</a>
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
	
</body>
</html>
