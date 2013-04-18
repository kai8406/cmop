<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>


	<title>故障申报</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#failure").addClass("active");
			
			$("#loginName").focus();
			
			$("#inputForm").validate();
			
			$("#upload").click(function() {
				$(this).fileupload({
					dataType: 'json',
					done: function(e, data) {
						$.each(data.result, function(index, file) {
							//判断页面是否存在相同的文件名.如果存在,则不显示.
							var isExist = false;
							$(".fileuploadDiv").each(function() {
								if (file.name == $(this).find("#fileInput").val()) {
									isExist = true;
								}
							});
							if (!isExist) {
								$('#filename').append(formatFailureFileDisplay(file));
							}
						});
					}
				});
			});
			
			 
		});
		
		/**
		 * 插入上传文件的名称,大小,删除链接.
		 * 
		 * @param file
		 * @returns {String}
		 */
		function formatFailureFileDisplay(file) {
			var size = (file.size / 1000).toFixed(2) ;
			var html = "<div class='fileuploadDiv'>";
			html += '<input type="hidden" value="' + file.name + '" id="fileInput" name="fileName">';
			html += '<input type="hidden" value="' + file.size + '" name="fileSize">';
			html += '<span class="text-warning">' + file.name + ' (' + size + 'K)&nbsp;&nbsp;</span>';
			html += '<input type="text" name="fileDesc" value=" " class="input-small required" maxlength="45">'; //值为空格,去掉必填验证.
			html += "<a href='${ctx}/failure/upload/" + file.deleteUrl + "' onclick='deleteUpdateLoad(this);return false'>删除</a><br/>";
			html += "</div>";
			return html;
		}
		
		/**
		1.每次点击确认时,遍历页面,将存在页面的resourcesId放入一个临时数组.
		2.判断选中的资源是否在页面上.如果存在跳过,不存在生成该资源对应的resource代码插入页面
		3.初始化临时数组和checkbox
		**/
		$(document).on("click", "#ModalSave", function() {
			var selectedArray = [];
			var $ModalDiv = $(this).parent().parent();
			var $CheckedIds = $ModalDiv.find("tbody input:checked");
			
			//Step.1
			
			$("div.resources").each(function() {
				selectedArray.push($(this).find("#resourcesId").val());
			});
			
			//Step.2
			
			$CheckedIds.each(function() {
				var resourcesId = $(this).val();
				if ($.inArray(resourcesId, selectedArray) == -1) {
					var serviceType = $(this).parent().parent().find('#resources-serviceType').text();
					//根据resourcesId生成HTML字符串,并拼装
					fGetUnitByResourcesId(resourcesId, serviceType);
				}
			});
			
			//Step.3
			
			selectedArray = [];
			$("input[type=checkbox]").removeAttr('checked');
			
		});

		/**
		 * 查询resourceList,遍历Json数据插入到页面
		 */
		function searchResources() {
			$("#resources-tbody").empty();
			
			//Post方法提交查询资源Resources.
			
			$("#resourcesForm").ajaxSubmit({
				url: "${ctx}/ajax/getResourcesList",
				type: "POST",
				dataType: "json",
				success: function(responseText, statusText, xhr, $form) {
					var html = '';
					for (var i = 0; i < responseText.length; i++) {
						html += '<tr>';
						html += '<td><input type="checkbox" value="' + responseText[i].id + '"></td>';
						html += '<td>' + responseText[i].serviceIdentifier + '</td>';
						html += '<td>' + responseText[i].serviceTag.name + '</td>';
						html += '<td>' + (responseText[i].ipAddress == null ? "" : responseText[i].ipAddress) + '</td>';
						html += "<td id='resources-serviceType' class='hide'>" + responseText[i].serviceType + "</td>";
						html += '</tr>';
					}
					$("#resources-tbody").append(html);
				}
			});
			return false;
		};
		

		/**
		 * 通过resourcesId Ajax获得资源Json数据后,根据serviceType区分不同的服务类型插入不同的数据至HTML页面
		 * @param resourcesId 资源ID
		 * @param serviceType 资源服务类型 eg:PCS,ECS
		 */
		function fGetUnitByResourcesId(resourcesId, serviceType) {
			 
			if (serviceType == 1 || serviceType == 2) {
				
				//PCS & ECS
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getCompute?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>用途信息</em>&nbsp;&nbsp;<strong>' + data.remark + '</strong></dd>';
						html += '<dd><em>基本信息</em>&nbsp;&nbsp;<strong>' + data.osType + '&nbsp;' + data.osBit + '</strong></dd>';
						html += '<dd><em>规格</em>&nbsp;&nbsp;<strong>' + data.serverType + '</strong></dd>';
						html += '<dd><em>关联ESG</em>&nbsp;&nbsp;<strong>' + data.networkEsgItem + '</strong></dd>';
						html += '<dd><em>内网IP</em>&nbsp;&nbsp;<strong>' + data.innerIp  + '</strong></dd>';
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
			}else if(serviceType == 3){
				
				//ES3
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getStorage?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>存储类型</em>&nbsp;&nbsp;<strong>' + data.storageType + '</strong></dd>';
						html += '<dd><em>容量空间</em>&nbsp;&nbsp;<strong>' + data.space+ 'GB</strong></dd>';
						html += '<dd><em>挂载实例</em>&nbsp;&nbsp;<strong>' + data.mountComputes + '</strong></dd>';
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
			
				
			}else if(serviceType == 4){
				
				//ELB
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getElb?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>是否保持会话</em>&nbsp;&nbsp;<strong>' + data.keepSession + '</strong></dd>';
						html += '<dd><em>关联实例</em>&nbsp;&nbsp;<strong>' + data.mountComputes + '</strong></dd>';
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
			}else if(serviceType == 5){
			
				//EIP
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getEip?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>ISP运营商</em>&nbsp;&nbsp;<strong>' + data.ispType + '</strong></dd>';
						if(data.linkType == 1 ){
							html += '<dd><em>关联实例</em>&nbsp;&nbsp;<strong>' + data.link + '</strong></dd>';
						}else{
							html += '<dd><em>关联ELB</em>&nbsp;&nbsp;<strong>' + data.link + '</strong></dd>';
						}
						
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
			}else if(serviceType == 6){
			
				//DNS
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getDns?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>域名</em>&nbsp;&nbsp;<strong>' + data.domainName + '</strong></dd>';
						html += '<dd><em>域名类型</em>&nbsp;&nbsp;<strong>' + data.domainType + '</strong></dd>';
						if(data.cnameDomain != null ){
							html += '<dd><em>CNAME域名</em>&nbsp;&nbsp;<strong>' + data.cnameDomain + '</strong></dd>';
						}else{
							html += '<dd><em>目标IP</em>&nbsp;&nbsp;<strong>' + data.targetEip + '</strong></dd>';
						}
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
			}else if(serviceType == 8){
			
				//MDN
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getMdn?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>重点覆盖地域</em>&nbsp;&nbsp;<strong>' + data.coverArea + '</strong></dd>';
						html += '<dd><em>重点覆盖ISP</em>&nbsp;&nbsp;<strong>' + data.coverIsp + '</strong></dd>';
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
			}else if(serviceType == 9){
				
				//monitorCompute
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getmonitorCompute?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>监控实例</em>&nbsp;&nbsp;<strong>' + (data.ipAddress == null ? "" : data.ipAddress) + '</strong></dd>';
						html += '<dd><em>监控端口</em>&nbsp;&nbsp;<strong>' + data.port + '</strong></dd>';
						html += '<dd><em>监控进程</em>&nbsp;&nbsp;<strong>' + data.process + '</strong></dd>';
						html += '<dd><em>挂载路径</em>&nbsp;&nbsp;<strong>' + data.mountPoint + '</strong></dd>';
						html += '<dd><em>CPU占用率</em>&nbsp;&nbsp;报警阀值&nbsp;<strong>' + data.cpuWarn + '</strong>&nbsp;&nbsp;警告阀值&nbsp;<strong>' + data.cpuCritical + '</strong></dd>';
						html += '<dd><em>内存占用率</em>&nbsp;&nbsp;报警阀值&nbsp;<strong>' + data.memoryWarn + '</strong>&nbsp;&nbsp;警告阀值&nbsp;<strong>' + data.memoryCritical + '</strong></dd>';
						html += '<dd><em>网络丢包率</em>&nbsp;&nbsp;报警阀值&nbsp;<strong>' + data.diskWarn + '</strong>&nbsp;&nbsp;警告阀值&nbsp;<strong>' + data.diskCritical + '</strong></dd>';
						html += '<dd><em>硬盘可用率</em>&nbsp;&nbsp;报警阀值&nbsp;<strong>' + data.pingLossWarn + '</strong>&nbsp;&nbsp;警告阀值&nbsp;<strong>' + data.pingLossCritical + '</strong></dd>';
						html += '<dd><em>网络延时率</em>&nbsp;&nbsp;报警阀值&nbsp;<strong>' + data.pingDelayWarn + '</strong>&nbsp;&nbsp;警告阀值&nbsp;<strong>' + data.pingDelayCritical + '</strong></dd>';
						html += '<dd><em>最大进程数</em>&nbsp;&nbsp;报警阀值&nbsp;<strong>' + data.maxProcessWarn + '</strong>&nbsp;&nbsp;警告阀值&nbsp;<strong>' + data.maxProcessCritical + '</strong></dd>';
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
				
			}else if(serviceType == 10){
			
				//monitorElb
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getmonitorElb?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>监控ELB</em>&nbsp;&nbsp;<strong>' + data.networkElbItem + '</strong></dd>';
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
			}else if(serviceType == 11){
			
				//cpItem
				
				$.ajax({
					type: "GET",
					url: "${ctx}/ajax/getCP?id=" + resourcesId,
					dataType: "json",
					success: function(data) {
						var html = '';
						html += '<div class="resources alert alert-block alert-info fade in">';
						html += '<button data-dismiss="alert" class="close" type="button">×</button>';
						html += '<input type="hidden" id="resourcesId" name="resourcesId" value="' + resourcesId + '">';
						html += '<dd><em>标识符</em>&nbsp;&nbsp;<strong>' + data.identifier + '</strong></dd>';
						html += '<dd><em>收录流URL</em>&nbsp;&nbsp;<strong>' + data.recordStreamUrl + '</strong></dd>';
						html += '<dd><em>收录码率</em>&nbsp;&nbsp;<strong>' + data.recordBitrate + '</strong></dd>';
						html += '<dd><em>输出编码</em><br>&nbsp;&nbsp;<strong>' + data.exportEncode + '</strong></dd>';
						html += '<dd><em>收录类型</em>&nbsp;&nbsp;<strong>' + data.recordType + '</strong></dd>';
						html += '<dd><em>收录时段</em>&nbsp;&nbsp;<strong>' + data.recordTime + '</strong></dd>';
						html += '<dd><em>发布接口地址</em>&nbsp;&nbsp;<strong>' + data.publishUrl + '</strong></dd>';
						html += '<dd><em>是否推送内容交易平台</em>&nbsp;&nbsp;<strong>' + data.isPushCtp + '</strong></dd>';
						html += '<dd><strong>视频配置</strong></dd>';
						html += '<dd><em>FTP上传IP</em>&nbsp;&nbsp;<strong>' + data.videoFtpIp + '</strong></dd>';
						html += '<dd><em>端口</em>&nbsp;&nbsp;<strong>' + data.videoFtpPort + '</strong></dd>';
						html += '<dd><em>FTP用户名</em>&nbsp;&nbsp;<strong>' + data.videoFtpUsername + '</strong></dd>';
						html += '<dd><em>FTP密码</em>&nbsp;&nbsp;<strong>' + data.videoFtpPassword + '</strong></dd>';
						html += '<dd><em>FTP根路径</em>&nbsp;&nbsp;<strong>' + data.videoFtpRootpath + '</strong></dd>';
						html += '<dd><em>FTP上传路径</em>&nbsp;&nbsp;<strong>' + data.videoFtpUploadpath + '</strong></dd>';
						html += '<dd><em>输出组类型</em>&nbsp;&nbsp;<strong>' + data.videoOutputGroup + '</strong></dd>';
						html += '<dd><em>输出方式配置</em>&nbsp;&nbsp;<strong>' + data.videoOutputWay + '</strong></dd>';
						html += '<dd><strong>图片配置</strong></dd>';
						html += '<dd><em>FTP上传IP</em>&nbsp;&nbsp;<strong>' + data.pictrueFtpIp + '</strong></dd>';
						html += '<dd><em>端口</em>&nbsp;&nbsp;<strong>' + data.pictrueFtpPort + '</strong></dd>';
						html += '<dd><em>FTP用户名</em>&nbsp;&nbsp;<strong>' + data.pictrueFtpUsername + '</strong></dd>';
						html += '<dd><em>FTP密码</em>&nbsp;&nbsp;<strong>' + data.pictrueFtpPassword + '</strong></dd>';
						html += '<dd><em>FTP根路径</em>&nbsp;&nbsp;<strong>' + data.pictrueFtpRootpath + '</strong></dd>';
						html += '<dd><em>FTP上传路径</em>&nbsp;&nbsp;<strong>' + data.pictrueFtpUploadpath + '</strong></dd>';
						html += '<dd><em>输出组类型</em>&nbsp;&nbsp;<strong>' + data.pictrueOutputGroup + '</strong></dd>';
						html += '<dd><em>输出媒体类型</em>&nbsp;&nbsp;<strong>' + data.pictrueOutputMedia + '</strong></dd>';
						html += '</div>';
						$("#resourcesDIV dl").append(html);
					}
				});
				
			} 
			
		};
		 
		
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
		
		<fieldset>
		
			<legend><small>故障申报</small></legend>
			
			
			<div class="control-group">
				<label class="control-label" for="faultType">故障类型</label>
				<div class="controls">
					<select id="faultType" name="faultType" class="required">
						<c:forEach var="map" items="${applyServiceTypeMap}">
							<option value="${map.key}">${map.value}</option>							
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="level">优先级</label>
				<div class="controls">
					<select id="level" name="level">
						<c:forEach var="map" items="${priorityMap }">
							<option value="${map.key}">${map.value}</option>		
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="level">受理人</label>
				<div class="controls">
					<select id="assignee" name="assignee">
						<c:forEach var="map" items="${assigneeMap }">
							<option value="${map.key}">${map.value}</option>		
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="description">故障现象及描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...故障现象及描述"
						maxlength="500" class="required"></textarea>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="filename">附件上传</label>
				<div class="controls">
					<input id="upload" type="file" name="file" data-url="${ctx}/failure/upload/file" multiple="multiple">
					<span>(最大尺寸:5 MB)</span>
					<br><br>
					<span id='filename'></span> 
				</div>
			</div>
			
			<hr>
			
			<div class="control-group">
				<div class="controls">
					 <a id="addResourcesBtn" class="btn" href="#resourcesModal" data-toggle="modal">故障相关资源</a>
				</div>
			</div>
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal"></dl></div>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
	<!-- 资源列表Modal -->
	<div id="resourcesModal" class="modal container hide fade" tabindex="-1">
		<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>资源列表</h3></div>
		<div class="modal-body">
		
		<form id="resourcesForm" class="form-inline well well-small">
			
			<!-- 搜索 -->
			<div class="row">
			
				<div class="span3">
					<label class="search-text">服务标签</label> 
					<input type="text" id="serviceTagName" name="serviceTagName" class="span2" maxlength="45">
				</div>
				
				<div class="span3">
					<label class="search-text">IP地址</label> 
					<input type="text" id="ipAddress" name="ipAddress" class="span2" maxlength="45">
				</div>
				
				<div class="span3">
					<label class="control-label search-text">服务类型</label> 
					<select id="serviceType" name="serviceType" class="span2">
						<option selected="selected" value="">Choose...</option>
						<c:forEach var="map" items="${resourcesServiceTypeMap }">
							<option value="${map.key }">${map.value }</option>
						</c:forEach>
					</select>
				</div>
				
				<div class="span2 pull-right">
					<button class="btn tip-bottom" title="搜索" type="button" onclick="searchResources()" ><i class="icon-search"></i></button>
					<button class="btn tip-bottom" title="刷新" type="reset"><i class="icon-refresh"></i></button>
					<button class="btn tip-bottom options"  title="更多搜索条件" type="button"><i class="icon-resize-small"></i></button>
				</div>
	
			</div>
			
			<!-- 多个搜索条件的话,启用 div.options -->
			<div class="row options">
			
				<div class="span3">
					<label class="search-text">标识符</label> 
					<input type="text" id="serviceIdentifier" name="serviceIdentifier" class="span2" maxlength="45">
				</div>
				
			</div>
	
		</form>
		
		<div class="singlePage">
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th><input type="checkbox"></th>
					<th>标识符</th>
					<th>服务标签</th>
					<th>IP地址</th>
				</tr>
			</thead>
			<tbody id="resources-tbody"></tbody>
		</table>
		</div>
		
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal">关闭</button>
			<a id="ModalSave" href="#" class="btn btn-primary" data-dismiss="modal" >确定</a>
		</div>
		
	</div>
							
</body>
</html>
