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
			
			$("#submitBtn").click(function() {
				
				if(!$("#inputForm").valid()){
					return false;
				}
				
				var flag = true;
				
		//  $("tr.clone").each(function() {
		//	var $this = $(this);
		//	var domainType = $this.find("#domainType").val();
		//	var count = $this.find(".eipInfoText").length;
		//	
		//	/*
		//	提交时,验证DNS绑定的EIP数量.
		//		类型为GSLB时至少需要两个目标IP
		//		类型为A时只能选一个目标IP
		//	页面默认是有一个p.eipInfoText,用于标注插入点,所以统计都是+1.
		//	*/
		//	if (domainType == 1 && count < 3) {
		//		//GSLB
		//		alert("类型为GSLB时至少需要两个目标IP.");
		//		flag = false;
		//	} else if (domainType == 2 && count != 2) {
		//		//A
		//		alert("类型为A时只能选一个目标IP.");
		//		flag = false;
		//	} 
		//	
		//});  
				
				//只有flag为true时才提交.
				if(flag){
					$("#inputForm").submit();
					$(this).button('loading').addClass("disabled").closest("body").modalmanager('loading');
				}
			});
			
		});
		
		
		$(document).on("change", "#domainType", function(){
			
			/*
				选择域名类型时,根据不同的类型切换不同的目标IP/CNAME域名
				GSLB,A : 选择EIP
				CNAME : 输入文本框
			*/
		
			var $parent = $(this).parent().parent();
			$parent.find("#cnameDomainDiv").empty();
			if ($parent.find("#domainType").val() == 3) {
				//CNAME
				$parent.find("#cnameDomainDiv").append('<input type="text" name="eipIds" id="eipIds" class="required span2" maxlength="45">');
			} else {
				//GSLB,A
				$parent.find("#cnameDomainDiv").append('<input type="hidden" name="eipIds" id="eipIds" maxlength="45"><a id="addEipBtn" class="btn selectEip" data-toggle="modal" href="#eipModal" >EIP资源</a><p class="text-info eipInfoText"></p>');
			}
				
		});
		
		 /*目前没有办法区分modal到底是谁点击的.只有设置点击a标签一个临时的name,标示其被点击.*/
		$(document).on("click", "#addEipBtn", function() {
			//将class为selectEip的a标签name初始化为""
			$("a.selectEip").attr("name", "");
			
			//为了区分哪一行,设置当前点击a标签的name为"selectedRow".
			$(this).attr("name", "selectedRow");
		});
				
		
		 /*点击弹出窗口保存时,连同Eip的信息生成HTML代码插入页面.*/
		$(document).on("click", "#ModalSave", function() {
			
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			var eipIds = "",
				eipInfo = "";
			
			//遍历挂载EIP的Id和identifier.
			$CheckedIds.each(function() {
				var $this = $(this);
				var $td = $this.closest("tr").find("td");
				eipIds += $this.val() + "-";
				eipInfo += '<p class="text-info eipInfoText">'+ $td.eq(1).text() + "(" + $td.eq(3).text() + ")<br></p>";
				
			});
			
			//初始化
			$("input[type=checkbox]").removeAttr('checked');
			
			//插入HTML文本	
			$("a.selectEip[name='selectedRow']").parent().parent().find("p.eipInfoText").empty().append(eipInfo).end().find("#eipIds").val(eipIds);
			
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
						<td><input type="text" id="domainName" name="domainNames" class="input-small required" maxlength="45" placeholder="...域名"></td>
						<td>
							<select id="domainType" name="domainTypes" class="input-small required">
								<c:forEach var="map" items="${domainTypeMap}"><option value="${map.key }">${map.value }</option></c:forEach>
							</select>
						</td>
						<td>
							<div id="cnameDomainDiv">
								<input type="hidden" name="eipIds" id="eipIds" maxlength="45">
								<a id="addEipBtn" class="btn selectEip" data-toggle="modal" href="#eipModal" >EIP资源</a>
								<p class="text-info eipInfoText"></p>
							</div>
						
						</td>
						<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
					</tr>
				</tbody>
			</table>	
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input id="submitBtn" class="btn btn-primary" type="button" value="提交">
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
					<tbody id="resources-tbody">
						<c:forEach var="item" items="${allEips }">
							<tr>
								<td><input type="checkbox" value="${item.id }"></td>
								<td>${item.identifier}</td>
								<td><c:forEach var="map" items="${ispTypeMap}"><c:if test="${item.ispType == map.key}">${map.value}</c:if></c:forEach></td>
								<td>${item.ipAddress }</td>
								<td>
									<c:choose>
										<c:when test="${not empty item.computeItem }"><em>关联实例</em>&nbsp;&nbsp;${item.computeItem.identifier }(${item.computeItem.remark } - ${item.computeItem.innerIp })</c:when>
										<c:when test="${not empty item.networkElbItem }"><em>关联ELB</em>&nbsp;&nbsp;${item.networkElbItem.identifier }(${item.networkElbItem.virtualIp })&nbsp;【${item.networkElbItem.mountComputes}】</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
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
	</form><!-- EIP选择的Modal End -->
	
</body>
</html>
