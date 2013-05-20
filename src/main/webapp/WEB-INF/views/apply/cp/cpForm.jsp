<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active"); 
			
			/*禁用回车提交form表单.*/
			$("#inputForm").keypress(function(e) {
				if (e.which == 13) {
					return false;
				}
			});
			
			$("#inputForm").validate();
			
			// 初始化服务开始和结束时间,结束时间默认为开始时间3个月后
			$("#serviceStart").val(getDatePlusMonthNum(0));
			$("#serviceEnd").val(getDatePlusMonthNum(3));
			
			$("#serviceStart").datepicker({
				changeMonth: true,
				minDate: 'D',
				onClose: function(selectedDate) {
					$("#serviceEnd").datepicker("option", "minDate", selectedDate);
				}
			});
			
			$("#serviceEnd").datepicker({
				changeMonth: true,
				onClose: function(selectedDate) {
					$("#serviceStart").datepicker("option", "maxDate", selectedDate);
				}
			});
			
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
								$('#filename').append(formatCPFileDisplay(file));
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
		function formatCPFileDisplay(file) {
			var size = (file.size / 1000).toFixed(2) ;
			var html = "<div class='fileuploadDiv'>";
			html += '<input type="hidden" value="' + file.name + '" id="fileInput" name="fileName">';
			html += '<input type="hidden" value="' + file.size + '" id="fileSize" name="fileSize">';
			html += '<span class="text-warning">' + file.name + ' (' + size + 'K)&nbsp;&nbsp;</span>';
			html += "<a href='${ctx}/apply/cp/upload/" + file.deleteUrl + "' onclick='deleteUpdateLoad(this);return false'>删除</a><br/>";
			html += "</div>";
			return html;
		}
		 
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建CP云生产</small></legend>
			
			<!-- Step.1 -->
			<div class="step">
			
				<div class="control-group">
					<label class="control-label" for="serviceTag">服务标签</label>
					<div class="controls">
						<input type="text"  id="serviceTag" name="serviceTag"  class="required" maxlength="45" placeholder="...服务标签">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="priority">优先级</label>
					<div class="controls">
						<select id="priority" name="priority">
							<c:forEach var="map" items="${priorityMap }">
								<option value="${map.key }" 
									<c:if test="${map.key == apply.priority }">
										selected="selected"
									</c:if>
								>${map.value }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="serviceStart">服务开始时间</label>
					<div class="controls">
						<input type="text" id="serviceStart" name="serviceStart" readonly class="datepicker required"  placeholder="...服务开始时间">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="serviceEnd">服务结束时间</label>
					<div class="controls">
						<input type="text" id=serviceEnd name="serviceEnd" readonly class="datepicker required"  placeholder="...服务结束时间">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="description">用途描述</label>
					<div class="controls">
						<textarea rows="3" id="description" name="description" placeholder="...用途描述"
							maxlength="500" class="required"></textarea>
					</div>
				</div>
				
				<div class="form-actions">
					<input class="btn" type="button" value="返回" onClick="history.back()">
					<input class="btn btn-primary nextStep" type="button" value="下一步">
				</div>
			
			</div><!-- Step.1 End -->
			
			<!-- Step.2 -->
			<div class="step">
			
			    <div class="control-group">
					<label class="control-label" for="recordStreamUrl">收录流URL</label>
					<div class="controls">
						<input type="text" id="recordStreamUrl" name="recordStreamUrl" class="required" maxlength="100" placeholder="...收录流URL">
					</div>
				</div>
				
			    <div class="control-group">
					<label class="control-label" for="recordBitrate">收录码率</label>
					<div class="controls">
						<select id="recordBitrate" name="recordBitrate">
							<c:forEach var="map" items="${recordBitrateMap }">
								<option value="${map.key }" 
									<c:if test="${map.key == cp.recordBitrate }">
										selected="selected"
									</c:if>
								>${map.value }</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="exportEncode">输出编码</label>
					<div class="controls">
						<c:forEach var="map" items="${exportEncodeMap}">
							<label class="checkbox">
								<input type="checkbox" name="exportEncode" value="${map.key}" class="required"><span class="checkboxText ">${map.value}</span>
							</label>
						</c:forEach>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="recordType">收录类型</label>
					<div class="controls">
						<c:forEach var="map" items="${recordTypeMap}">
							<label class="radio inline">
								<input type="radio" name="recordType" value="${map.key}" checked="checked" class="required"><span class="radioText ">${map.value}</span>
							</label>
						</c:forEach>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="recordTime">收录时段</label>
					<div class="controls">
						<input type="text" id="recordTime" name="recordTime" class="required" maxlength="45" placeholder="...收录时段">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="publishUrl">发布接口地址</label>
					<div class="controls">
						<input type="text" id="publishUrl" name="publishUrl" maxlength="100" placeholder="...发布接口地址">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="isPushCtp">是否推送内容交易平台</label>
					<div class="controls">
						<c:forEach var="map" items="${isPushCtpMap}">
							<label class="radio inline">
								<input type="radio" name="isPushCtp" value="${map.key}" checked="checked"><span class="radioText">${map.value}</span>
							</label>
						</c:forEach>
					</div>
				</div>
				
				<div class="page-header"><em>视频配置</em></div>
			
				<div class="control-group">
					<label class="control-label" for="videoFtpIp">FTP上传IP</label>
					<div class="controls">
						<input type="text" id="videoFtpIp" name="videoFtpIp" class="required" maxlength="45" placeholder="...FTP上传IP">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="videoFtpPort">端口</label>
					<div class="controls">
						<input type="text" id="videoFtpPort" name="videoFtpPort" class="required" maxlength="10" placeholder="...端口">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="videoFtpUsername">FTP用户名</label>
					<div class="controls">
						<input type="text" id="videoFtpUsername" name="videoFtpUsername" class="required" maxlength="255" placeholder="...FTP上传IP">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="videoFtpPassword">FTP密码</label>
					<div class="controls">
						<input type="text" id="videoFtpPassword" name="videoFtpPassword" class="required" maxlength="45" placeholder="...FTP密码">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="videoFtpRootpath">FTP根路径</label>
					<div class="controls">
						<input type="text" id="videoFtpRootpath" name="videoFtpRootpath" class="required" maxlength="45" placeholder="...FTP根路径">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="videoFtpUploadpath">FTP上传路径</label>
					<div class="controls">
						<input type="text" id="videoFtpUploadpath" name="videoFtpUploadpath" class="required" maxlength="45" placeholder="...FTP上传路径">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="videoOutputGroup">输出组类型</label>
					<div class="controls">
						<input type="text" id="videoOutputGroup" name="videoOutputGroup" class="required" maxlength="100" placeholder="...输出组类型">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="videoOutputWay">输出方式配置</label>
					<div class="controls">
						<c:forEach var="map" items="${videoOutputWayMap }">
							<label class="radio inline">
								<input type="radio" name="videoOutputWay" value="${map.key }" checked="checked"><span class="radioText ">${map.value }</span>
							</label>						
						</c:forEach>
					</div>
				</div>
			
				<div class="page-header"><em>图片配置</em></div>
			
				<div class="control-group">
					<label class="control-label" for="pictrueFtpIp">FTP上传IP</label>
					<div class="controls">
						<input type="text" id="pictrueFtpIp" name="pictrueFtpIp" class="required" maxlength="45" placeholder="...FTP上传IP">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="pictrueFtpPort">端口</label>
					<div class="controls">
						<input type="text" id="pictrueFtpPort" name="pictrueFtpPort" class="required" maxlength="10" placeholder="...端口">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="pictrueFtpUsername">FTP用户名</label>
					<div class="controls">
						<input type="text" id="pictrueFtpUsername" name="pictrueFtpUsername" class="required" maxlength="255" placeholder="...FTP上传IP">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="pictrueFtpPassword">FTP密码</label>
					<div class="controls">
						<input type="text" id="pictrueFtpPassword" name="pictrueFtpPassword" class="required" maxlength="45" placeholder="...FTP密码">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="pictrueFtpRootpath">FTP根路径</label>
					<div class="controls">
						<input type="text" id="pictrueFtpRootpath" name="pictrueFtpRootpath" class="required" maxlength="45" placeholder="...FTP根路径">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="pictrueFtpUploadpath">FTP上传路径</label>
					<div class="controls">
						<input type="text" id="pictrueFtpUploadpath" name="pictrueFtpUploadpath" class="required" maxlength="45" placeholder="...FTP上传路径">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="pictrueOutputGroup">输出组类型</label>
					<div class="controls">
						<input type="text" id="pictrueOutputGroup" name="pictrueOutputGroup" class="required" maxlength="100" placeholder="...输出组类型">
					</div>
				</div>
				 
				
				<div class="control-group">
					<label class="control-label" for="pictrueOutputMedia">输出媒体类型</label>
					<div class="controls">
						<input type="text" id="pictrueOutputMedia" name="pictrueOutputMedia" class="required" maxlength="100" placeholder="...输出媒体类型">
					</div>
				</div>
				
			    <hr>
			    
				<div class="control-group">
					<label class="control-label" for="filename">拆条节目单上传</label>
					<div class="controls">
						<input id="upload" type="file" name="file" data-url="${ctx}/failure/upload/file" multiple="multiple">
						<span>(最大尺寸:5 MB)</span>
						<br><br>
						<span id='filename'></span> 
					</div>
				</div>
			
				
				<div class="form-actions">
					<input class="btn backStep" type="button" value="上一步">
					<input class="btn btn-primary" type="submit" value="提交">
				</div>
				
			</div><!-- Step.2 End -->
			
		</fieldset>
		
	</form>
	
</body>
</html>
