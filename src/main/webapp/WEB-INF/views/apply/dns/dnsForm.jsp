<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>DNS管理</title>

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
			
			 /* 获得当前用户拥有的EIP拼装至实例相关资源的modal中.*/
			
			$.ajax({
				type: "GET",
				url: "${ctx}/ajax/getEipList",
				dataType: "json",
				success: function(data){
					
					var html = '';
					
					for ( var i = 0; i < data.length; i++) {
						
						html += '<tr>';
						html += '<td><input type="checkbox" value="'+data[i].id+'"></td>';
						html += '<td>'+data[i].identifier+'</td>';
						html += '<td>'+data[i].ispType+'</td>';
						html += '<td>'+data[i].ipAddress+'</td>';
						html += '<td>'+data[i].link+'</td>';
						html += '</tr> ';
						
					}
					
					$("#resources-tbody").append(html);
					
 				}		
			});
			
		});
		
		
		 /*点击弹出窗口保存时,连同ES3的信息生成HTML代码插入页面.*/
	  	 
		$(document).on("click", "#ModalSave", function() {
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			var html = '';
			var computeIds = "";
			var computeIdentifier = "";
			var storageType = $("#storageType").val();
			var storageTypeText = $("#storageType>option:selected").text();
			var space = $("#space").val();
			
			//遍历挂载Compute的Id和identifier.
			
			$CheckedIds.each(function(){
				
				var $this = $(this);
				computeIds +=  $this.val() +"-";
		    	computeIdentifier += $this.closest("tr").find("td").eq(1).text()+"&nbsp;";
				
			});
			
			//拼装HTML文本
			
			html +='<div class="resources alert alert-block alert-info fade in">';
			html +='<button type="button" class="close" data-dismiss="alert">×</button>';
			html +='<input type="hidden" value="'+computeIds+'" name="computeIds">';
			html +='<input type="hidden" value="'+space+'" name="spaces">';
			html +='<input type="hidden" value="'+storageType+'" name="storageTypes">';
			html +='<dd><em>存储类型</em>&nbsp;&nbsp;<strong>'+storageTypeText+'</strong></dd>';
			html +='<dd><em>容量空间(GB)</em>&nbsp;&nbsp;<strong>'+space+'</strong></dd>';
			html +='<dd><em>挂载实例</em>&nbsp;&nbsp;<strong>'+computeIdentifier+'</strong></dd>';
			html +='</div> ';
			
			
			//初始化
			
			$CheckedIds.removeAttr('checked');
			$ModalDiv.find(".checker > span").removeClass("checked");
			 
			
			//插入HTML文本
			
			$("#resourcesDIV dl").append(html);
			
		}); 
		 
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建DNS</small></legend>
			
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
				<label class="control-label" for="domainName">域名</label>
				<div class="controls">
					<input type="text" id="domainName" class="required"  placeholder="...域名">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="domainType">域名类型</label>
				<div class="controls">
					<select id="domainType" class="required">
						<c:forEach var="map" items="${domainTypeMap}">
							<option value="${map.key}">${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			
			<div class="control-group">
				<div class="controls">
					 <a id="addEipBtn" class="btn" data-toggle="modal" href="#eipModal" >EIP相关资源</a>
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
	
	<!-- EIP选择的Modal -->
	<form id="modalForm" action="#" >
		<div id="eipModal" class="modal container hide fade" tabindex="-1">
	
			<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h4>EIP</h4></div>
				
			<div class="modal-body">
				<table class="table table-striped table-bordered table-condensed">
					<thead><tr>
						<th><input type="checkbox"></th>
						<th>标识符</th>
						<th>ISP</th>
						<th>IP</th>
						<th>关联实例/ELB</th>
					</tr></thead>
					<tbody id="resources-tbody"></tbody>
				</table>
			</div>
				
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
				<a id="ModalSave" href="#" class="btn btn-primary" data-dismiss="modal" >确定</a>
			</div>
		</div>
	</form><!-- EIP选择的Modal End -->
	
</body>
</html>
