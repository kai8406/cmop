<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
			$("#inputForm").validate();
			
			$("#submitBtn").click(function() {
				
				if(!$("#inputForm").valid()){
					return false;
				}
				
				var $length = $("div.resources").length;
				var domainType = $("#domainType").val();
				if (domainType == 1 && $length < 2) {
					//GSLB
					alert("类型为GSLB时至少需要两个目标IP.");
					return false;
				} else if (domainType == 2 && $length != 1) {
					//A
					alert("类型为A时只能选一个目标IP.");
					return false;
				} 
				
				$("#inputForm").submit();
				$(this).button('loading').addClass("disabled").closest("body").modalmanager('loading');
			});
			
		});
		
		/*
			选择域名类型时,根据不同的类型切换不同的目标IP/CNAME域名
			GSLB,A : 选择EIP
			CNAME : 输入文本框 cnameDomainDiv targetEIPDiv
		*/
		$(document).on("change", "#domainType", function(){
			
			var $this = $(this);
			if ($this.val() == 3) {
				//CNAME
				$("#cnameDomainDiv").addClass("show").removeClass("hidden");
				$("#targetEIPDiv").addClass("hidden").removeClass("show");
				//Hook,用于规避页面验证的情况.
				$("#resourcesDIV dl").empty().append('<div class="resources"></div>');
				
			} else {
				//GSLB,A
				$("#targetEIPDiv").addClass("show").removeClass("hidden");
				$("#cnameDomainDiv").addClass("hidden").removeClass("show");
				$("#resourcesDIV dl").empty();
			}
			$("#cnameDomain").val('');
		});
		
		/*点击弹出窗口保存时,生成Compute标识符信息HTML代码插入页面.*/
		$(document).on("click", "#ModalSave", function() {
			
			var selectedArray = [],
				html = "";
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//Step.1
			$("div.resources").each(function(){
				selectedArray.push($(this).find("#eipIds").val());
			});
			
			//遍历挂载EIP的Id和identifier.
			$CheckedIds.each(function() {
				var $this = $(this);
				var $td = $this.closest("tr").find("td");
				var eipInfo = $td.eq(1).text() + "(" + $td.eq(3).text() + ")";
				if ($.inArray($this.val(), selectedArray) == -1) {
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button type="button" class="close" data-dismiss="alert">×</button>';
					html += '<input type="hidden" value="' + $this.val() + '" id="eipIds" name="eipIds">';
					html += '<dd><em>目标IP</em>&nbsp;&nbsp;<strong>' + eipInfo + '</strong></dd>';
					html += '</div> ';
				}
			});
			
			//初始化
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			$ModalDiv.find(".checker > span").removeClass("checked");//uniform checkbox的处理
			
			//插入HTML文本
			$("#resourcesDIV dl").append(html);
		}); 
	
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="${ctx}/resources/update/dns/" method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="id" value="${resources.id }">
		
		<fieldset>
			<legend><small>变更DNS域名映射</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="serviceTagId">服务标签</label>
				<div class="controls">
					<select id="serviceTagId" name="serviceTagId" class="required">
						<c:forEach var="item" items="${tags}">
							<option value="${item.id }" 
								<c:if test="${item.id == resources.serviceTag.id }">
									selected="selected"
								</c:if>
							>${item.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="usedby">运维人</label>
				<div class="controls">
					<select id="usedby" name="usedby" class="required">
						<c:forEach var="map" items="${assigneeMap}">
							<option value="${map.key}" 
								<c:if test="${map.key == resources.usedby }">
									selected="selected"
								</c:if>
							>${map.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="changeDescription">变更描述</label>
				<div class="controls">
					<textarea rows="3" id="changeDescription" name="changeDescription" placeholder="...变更描述"
						maxlength="200" class="required">${change.description}</textarea>
				</div>
			</div>
			
			<hr>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${dns.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${dns.identifier}</p>
				</div>
			</div>
			
			<div class="control-group"> 
				<label class="control-label" for="domainName">域名</label>
				<div class="controls">
					<input type="text" id="domainName" name="domainName" value="${dns.domainName}" maxlength="45" class="required">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="domainType">域名类型</label>
				<div class="controls">
					<select id="domainType" name="domainType" class="required">
						<c:forEach var="map" items="${domainTypeMap }">
							<option value="${map.key }" 
								<c:if test="${map.key == dns.domainType }">
									selected="selected"
								</c:if>
							>${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div id="targetEIPDiv" 
				<c:choose>
				<c:when test="${ empty dns.cnameDomain  }">class="show"</c:when>
				<c:otherwise>class="hidden control-group"</c:otherwise>
				</c:choose>
			 >
				<label class="control-label" for="domainType">目标IP</label>
				<div class="controls">
					 <a id="addEipBtn" class="btn" data-toggle="modal" href="#eipModal" >EIP资源</a>
				</div>
			</div>
			
			<div id="cnameDomainDiv" 
				<c:choose>
				<c:when test="${not empty dns.cnameDomain }">class="show"</c:when>
				<c:otherwise>class="hidden control-group"</c:otherwise>
				</c:choose>
			>
				<label class="control-label" for="cnameDomain">CNAME域名</label>
				<div class="controls">
					 <input type="text" id="cnameDomain" name="cnameDomain" value="${dns.cnameDomain}" maxlength="45" >
				</div>
			</div>
			
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal">
			
				<c:if test="${not empty dns.cnameDomain }"><div class="resources"></div></c:if>
				
				<c:forEach var="eip" items="${dns.networkEipItemList }">
					<div class="resources alert alert-block alert-info fade in">
						<button data-dismiss="alert" class="close" type="button">×</button>
						<input type="hidden" name="eipIds" id="eipIds" value="${eip.id }">
						<dd>
							<em>目标IP</em>&nbsp;&nbsp;<strong>${eip.identifier }(<c:if test="${not empty eip.ipAddress }">${eip.ipAddress }</c:if>)&nbsp;</strong>
						</dd>
					</div>
				</c:forEach>
			</dl></div>
				 
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
						<c:forEach var="item" items="${eipResources }">
							<tr>
								<td><input type="checkbox" value="${item.id }"></td>
								<td>${item.identifier}</td>
								<td><c:forEach var="map" items="${ispTypeMap}"><c:if test="${item.ispType == map.key}">${map.value}</c:if></c:forEach></td>
								<td>${item.ipAddress }</td>
								<td>
									<c:choose>
										<c:when test="${not empty item.computeItem }">${item.computeItem.identifier }(${item.computeItem.remark } - ${item.computeItem.innerIp })</c:when>
										<c:otherwise>${item.networkElbItem.identifier }(${item.networkElbItem.virtualIp })</c:otherwise>
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
