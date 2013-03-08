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
						html += '<td>'+(data[i].ipAddress == null ? "" : data[i].ipAddress ) +'</td>';
						html += '<td>'+data[i].link+'</td>';
						html += '</tr> ';
					}
					$("#resources-tbody").append(html);
 				}		
			});
			 
			
		});
		
		/*
			选择域名类型时,根据不同的类型切换不同的目标IP/CNAME域名
			GSLB,A : 选择EIP
			CNAME : 输入文本框
		*/
		$(document).on("change", "#domainType", function(){
			var $parent = $(this).parent().parent();
			$parent.find("#cnameDomainDiv").empty();
			
			if ($parent.find("#domainType").val()== 3 ) {
				//CNAME
				$parent.find("#cnameDomainDiv").append('<input type="text" name="eipIds" id="eipIds" maxlength="45">');
				
			}else{
				//GSLB,A
				$parent.find("#cnameDomainDiv").append('<input type="hidden" name="eipIds" id="eipIds" maxlength="45"><a id="addEipBtn" class="btn selectEip" data-toggle="modal" href="#eipModal" >EIP资源</a><p class="text-info eipIdentifierText"></p>');
			}
				
		});
		
		 /*目前没有办法区分modal到底是谁点击的.只有设置点击a标签一个临时的name,标示其被点击.*/
		
		$(document).on("click", "#addEipBtn", function(){
			
			//将class为selectEip的a标签name初始化为""
			$("a.selectEip").attr("name", ""); 
			
			  //为了区分哪一行,设置当前点击a标签的name为"selectedRow".
			$(this).attr("name", "selectedRow");
			
		});
		
		
		 /*点击弹出窗口保存时,连同Eip的信息生成HTML代码插入页面.*/
	  	 
		$(document).on("click", "#ModalSave", function() {
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			var html = '';
			var eipIds = "";
			var eipIdentifier = "";
			
			//遍历挂载EIP的Id和identifier.
			
			$CheckedIds.each(function(){
				var $this = $(this);
				eipIds +=  $this.val() +"-";
				eipIdentifier += $this.closest("tr").find("td").eq(1).text()+"&nbsp;";
			});
			
			//初始化
			
			$("input[type=checkbox]").removeAttr('checked');
			$ModalDiv.find(".checker > span").removeClass("checked");//uniform checkbox的处理
			
			//插入HTML文本	
			$("a.selectEip[name='selectedRow']").parent().parent().find("p.eipIdentifierText").empty().append(eipIdentifier).end().find("#eipIds").val(eipIds);
			
		}); 
		 
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建DNS域名映射</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="applyId">所属服务申请</label>
				<div class="controls">
					<select id="applyId" name="applyId" class="required">
						<c:forEach var="item" items="${baseStationApplys}"><option value="${item.id }">${item.title} ( ${item.description} )</option></c:forEach>
					</select>
				</div>
			</div>
			
			<table class="table table-bordered table-condensed"  >
				<thead><tr><th>域名</th><th>域名类型</th><th>目标IP/CNAME域名</th><th></th></tr></thead>
				<tbody>
					<tr class="clone">
						<td><input type="text" id="domainName" name="domainNames" class="input-small " maxlength="45" placeholder="...域名"></td>
						<td>
							<select id="domainType" name="domainTypes" class="input-small required">
								<c:forEach var="map" items="${domainTypeMap}"><option value="${map.key }">${map.value }</option></c:forEach>
							</select>
						</td>
						<td>
							<div id="cnameDomainDiv">
								<input type="hidden" name="eipIds" id="eipIds" maxlength="45">
								<a id="addEipBtn" class="btn selectEip" data-toggle="modal" href="#eipModal" >EIP资源</a>
								<p class="text-info eipIdentifierText"></p>
							</div>
						
						</td>
						<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
					</tr>
				</tbody>
			</table>	
				
			<hr>
			
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
