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
				
				var space = $("#space").val();
				var storageTypeText = $("#storageType>option:selected").text();
				var storageType = $("#storageType").val();
				
				var html = '<div class="resources alert alert-block alert-info fade in">';
				html += '<button type="button" class="close" data-dismiss="alert">×</button>';
				html += '<input type="hidden" value="' + space + '" name="spaces">';
				html += '<input type="hidden" value="' + storageType + '" name="storageTypes">';
				html += '<input type="hidden" name="computeIds">';
				html += '<dd><em>存储类型</em>&nbsp;&nbsp;<strong>' + storageTypeText + '</strong></dd>';
				html += '<dd><em>容量空间(GB)</em>&nbsp;&nbsp;<strong>' + space + '</strong></dd>';
				html += '<dd><em>挂载实例</em>&nbsp;&nbsp;<select multiple class="multipleCompute">' + computeHTML + '</select></dd>';
				html += '</div> ';
				
				  //插入HTML文本
			    $("#resourcesDIV dl").append(html);
				  
			    $("select.multipleCompute").select2();
			    
			  //为每个select.multipleESG元素绑定一个事件:每次变更select中的值,最近的隐藏域值也改变.
				$("select.multipleCompute").on("change",function(){
					$(this).parent().parent().find("input[name='computeIds']").val($(this).val());
				});
			    
			});
			
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
				<label class="control-label" for="storageType">存储类型</label>
				<div class="controls">
					<select id="storageType" class="required">
						<c:forEach var="map" items="${storageTypeMap}">
							<option value="${map.key}">${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="space">容量空间(GB)</label>
				<div class="controls">
					<input type="text" id="space" class="required digits" maxlength="6" placeholder="...容量空间(GB)">
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					 <a id="addComputeBtn" class="btn btn-success">生成ES3</a>
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
