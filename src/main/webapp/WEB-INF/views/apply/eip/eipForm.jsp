<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#inputForm").validate();
			
			/*禁用回车提交form表单.*/
			$("#inputForm").keypress(function(e) {
				if (e.which == 13) {return false;}
			});
			
			/*关联实例和关联ELB select控件的切换*/
			$("input[name='linkRadio']").click(function() {
				if ($(this).val() == "isCompute") {
					$("#computeSelectDiv").addClass("show").removeClass("hidden");
					$("#elbSelectDiv").addClass("hidden").removeClass("show");
				} else {
					$("#elbSelectDiv").addClass("show").removeClass("hidden");
					$("#computeSelectDiv").addClass("hidden").removeClass("show");
				}
			});
			
			$('#createEIPBtn').click(function(){
				if (!$("#inputForm").valid()) {
					return false;
				}
			});
			
			$('#createEIPBtn').popover({
				trigger: "hover",
			 	placement: "right",
			 	title: "Note",
			 	content: "同一ISP运营商不能关联多个相同实例或ELB."
			});
			
		});
		
		
		 /*点击"生成EIP"按钮,连同EIP的信息生成HTML代码插入页面.*/
	  	 
		$(document).on("click", "#createEIPBtn", function() {
			
			/*
				1.创建一个临时数组selectedArray,获得关联ELB和关联实例的下拉框对象.
				2.遍历页面, 将存在于页面的ISP和关联类型,关联ID组装后(ispType-linkType-linkId),放入数组selectedArray中.
				3.遍历选中的ISP运营商的checkbox,选中几个checkbox就生成几条ELB的HTML资源信息.
				4.判断页面选中的是哪种关联类型( 0:ELB ; 1: 实例),并按不同的关联类型生成不同的HTML代码.
				5.对选择的值拼接成temp(ispType-linkType-linkId)和数组selectedArray进行比较.
					如果存在,设置isUnique为false,
					不存在,将temp值放入数组selectedArray中.
				6.初始化isUnique
			*/
			
			//step.1
			var selectedArray = [],
				html = "";
			
			 //选中的关联实例或关联ELB对象
			 var $elbSelect = $("#elbSelectDiv.show #elbSelect");
			 var $computeSelect = $("#computeSelectDiv.show #computeSelect");
			 var $ispTyp = $("input[name='ispTypeCheckbox']:checked");
			
			//Step.2 遍历页面, 将存在于页面的ISP和关联类型,关联ID组装后放入输入中
			 $("div.resources").each(function() {
			 	var $this = $(this);
			 	var ispTypes = $this.find("#ispTypes").val();
			 	var linkTypes = $this.find("#linkTypes").val();
			 	var linkIds = $this.find("#linkIds").val();
			 	var temp = ispTypes + "-" + linkTypes + "-" + linkIds;
			 	selectedArray.push(temp);
			 });
		
			//step.3 遍历选中的ISP
			$ispTyp.each(function() {
				
				var $this = $(this);
				var ispType = $this.val();
				var isUnique = true,
					linkType = "",
					linkId = "",
					str = "";
				var ispTypText = $this.next("span.checkboxText").text();	//ISP的文本
				
				//step.4 判断页面选中的是哪种关联类型( 0:ELB ; 1: 实例),并按不同的关联类型生成不同的HTML代码.
				if ($elbSelect.val() != undefined) {
					//关联ELB
					linkType = "0";
					linkId = $elbSelect.val();
					var elbSelectText = $elbSelect.find("option:selected").text();
					str += '<dd><em>关联ELB</em>&nbsp;&nbsp;<strong>' + elbSelectText + '</strong></dd>';
				} else {
					//关联实例
					linkType = "1";
					linkId = $computeSelect.val();
					var computeSelectText = $computeSelect.find("option:selected").text();
					str += '<dd><em>关联实例</em>&nbsp;&nbsp;<strong>' + computeSelectText + '</strong></dd>';
				}
				
				//Step.5
				var temp = ispType + "-" + linkType + "-" + linkId;
				if ($.inArray(temp, selectedArray) > -1) {
					isUnique = false;
				} else {
					selectedArray.push(temp);
				}
				
				if (isUnique) {
					
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button type="button" class="close" data-dismiss="alert">×</button>';
					html += '<input type="hidden" value="' + ispType + '" id="ispTypes" name="ispTypes">';
					html += '<dd><em>ISP运营商</em>&nbsp;&nbsp;<strong>' + ispTypText + '</strong></dd>';
					html += str;
					html += '<input type="hidden" value="' + linkId + '" id="linkIds" name="linkIds">';
					html += '<input type="hidden" value="' + linkType + '" id="linkTypes" name="linkTypes">';
					html += '<dd><em>端口映射 &nbsp;&nbsp;（协议、源端口、目标端口）</em></dd>';
					
					var portTempArray = [],
						protocolStr = "",
						sourcePortStr = "",
						targetPortStr = "";
					
					//端口信息相关的HTML
					$("tr.clone").each(function() {
						var $tr = $(this);
						var protocol = $tr.find("#protocol").val();
						var protocolText = $tr.find("#protocol>option:selected").text();
						var sourcePort = $tr.find("#sourcePort").val();
						var targetPort = $tr.find("#targetPort").val();
						var portTemp = protocol + "-" + sourcePort + "-" + targetPort;
						//检验LB的协议,端口,实例端口是否重复.(如果重复,生成的时候自动排除重复项.)
						if (portTempArray.length === 0 || $.inArray(portTemp, portTempArray) === -1) {
							portTempArray.push(portTemp);
							protocolStr += protocol + "-";
							sourcePortStr += sourcePort + "-";
							targetPortStr += targetPort + "-";
							html += '<dd><strong>' + protocolText + '&nbsp;,&nbsp;' + sourcePort + '&nbsp;,&nbsp;' + targetPort + '</strong></dd>';
						}
					});
					
					html += '<input type="hidden" value="' + protocolStr + '" name="protocols">';
					html += '<input type="hidden" value="' + sourcePortStr + '" name="sourcePorts">';
					html += '<input type="hidden" value="' + targetPortStr + '" name="targetPorts">';
					html += '</div> ';
				}
				//step.6 初始化isUnique
				isUnique = true;
				portTempArray = [];
			});
			
			//插入HTML文本至页面
			$("#resourcesDIV dl").append(html);
			
			//初始化
			$("tr.clone:gt(0)").remove().end().find("input[type=text]").val('');
			$("input[type=checkbox]").removeAttr('checked');
			selectedArray = [];
			
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
								<option value="${item.id }">${item.identifier}(${item.remark } - ${item.innerIp })</option>
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
						<td><input type="text" id="sourcePort" class="input-small required" maxlength="45" placeholder="...SourcePort"></td>
						<td><input type="text" id="targetPort" class="input-small required" maxlength="45" placeholder="...TargetPort"></td>
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
				<input class="btn btn-primary" type="submit" onclick="clearCloneClassOfRequired()" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
