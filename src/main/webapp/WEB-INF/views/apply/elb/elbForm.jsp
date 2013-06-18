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
			
			
			//注意该变量是全局变量!!!
			var computeHTML;
			/* ajax获得computeList*/ 
			$.ajax({
				type: "GET",
				url: "${ctx}/ajax/getComputeList",
				dataType: "json",
				success: function(data) {
					for (var i = 0; i < data.length; i++) {
						computeHTML += '<option value="' + data[i].id + '">' + data[i].identifier+'('+data[i].remark+ ' - '+ data[i].innerIp  +')' + '</option>';
					}
				}
			});
			
			$("#addComputeBtn").click(function() {
				
				if (!$("#inputForm").valid()) {
					return false;
				}
				
				var computeIds = "",html = "",portTempArray = [],
				protocolStr = "",
				sourcePortStr = "",
				targetPortStr = "";
				
			    var keepSession = $("input[name='keepSessionRadio']:checked").val();
			    var keepSessionText = $("input[name='keepSessionRadio']:checked").next("span.radioText").text();
						
				html += '<div class="resources alert alert-block alert-info fade in">';
				html += '<button type="button" class="close" data-dismiss="alert">×</button>';
				html += '<dd><em>是否保持会话</em>&nbsp;&nbsp;<strong>' + keepSessionText + '</strong></dd>';
				html += '<dd><em>端口映射 &nbsp;&nbsp;（协议、源端口、目标端口）</em></dd>';
				
				//端口信息相关的HTML
				$("tr.clone").each(function(){
					var $tr = $(this);
					var protocol = $tr.find("#protocol").val();
					var protocolText = $tr.find("#protocol>option:selected").text();
					var sourcePort = $tr.find("#sourcePort").val();
					var targetPort = $tr.find("#targetPort").val();
					var portTemp = protocol + "@" + sourcePort + "@" + targetPort;
					
					//检验LB的协议,端口,实例端口是否重复.(如果重复,生成的时候自动排除重复项.)
					if (portTempArray.length === 0 || $.inArray(portTemp, portTempArray) === -1) {
						portTempArray.push(portTemp);
						protocolStr += protocol + "@";
						sourcePortStr += sourcePort + "@";
						targetPortStr += targetPort + "@";
						html += '<dd><strong>' + protocolText + '&nbsp;,&nbsp;' + sourcePort + '&nbsp;,&nbsp;' + targetPort + '</strong></dd>';
					}
				});
				
				html += '<dd><em>关联实例</em>&nbsp;&nbsp;<select multiple class="multipleCompute">' + computeHTML + '</select></dd>';
				
				html += '<input type="hidden" name="computeIds">';
				html += '<input type="hidden" value="' + keepSession + '" name="keepSessions">';
				html += '<input type="hidden" value="' + protocolStr + '" name="protocols">';
				html += '<input type="hidden" value="' + sourcePortStr + '" name="sourcePorts">';
				html += '<input type="hidden" value="' + targetPortStr + '" name="targetPorts">';
				html += '</div> ';
			
				//插入HTML文本
				$("#resourcesDIV dl").append(html);
					
				 $("select.multipleCompute").select2();
				    
			  //为每个select.multipleESG元素绑定一个事件:每次变更select中的值,最近的隐藏域值也改变.
				$("select.multipleCompute").on("change",function(){
					$(this).parent().parent().find("input[name='computeIds']").val($(this).val());
				});
				
				//初始化
				$("tr.clone:gt(0)").remove().end().find("input[type=text]").val('');
				portTempArray =[];
				
			});
			
		});
		
	 
		 
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建负载均衡器ELB</small></legend>
			
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
						<td><input type="text" id="sourcePort" class="input-small required" maxlength="45" placeholder="...SourcePort"></td>
						<td><input type="text" id="targetPort" class="input-small required" maxlength="45" placeholder="...TargetPort"></td>
						<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
					</tr>
				</tbody>
			</table>	
			
			<div class="control-group">
				<div class="controls">
					 <a id="addComputeBtn" class="btn btn-success">生成ELB</a>
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
