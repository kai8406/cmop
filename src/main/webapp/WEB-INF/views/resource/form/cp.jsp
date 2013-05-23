<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
			$("#inputForm").validate();
			
		});
		
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="${ctx}/resources/update/cp/" method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="id" value="${resources.id }">
		
		<fieldset>
			<legend><small>变更CP云生产</small></legend>
			
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
					<p class="help-inline plain-text">${cp.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordStreamUrl">收录流URL</label>
				<div class="controls">
					<input type="text" id="recordStreamUrl" name="recordStreamUrl" value="${cp.recordStreamUrl }" class="required" maxlength="100" placeholder="...收录流URL">
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
							<input type="checkbox" name="exportEncode" value="${map.key}" 
								<c:forEach var="exportEncode" items="${fn:split(cp.exportEncode,',')}">
									<c:if test="${map.key == exportEncode }"> checked="checked" </c:if>
						    	</c:forEach>
							class="required">
							<span class="checkboxText">${map.value}</span>
						</label>
					</c:forEach>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordType">收录类型</label>
				<div class="controls">
					<c:forEach var="map" items="${recordTypeMap}">
						<label class="radio inline">
							<input type="radio" name="recordType" value="${map.key}" 
								<c:if test="${map.key == cp.recordType }">checked="checked"</c:if>
							class="required"><span class="radioText ">${map.value}</span>
						</label>
					</c:forEach>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordTime">收录时段</label>
				<div class="controls">
					<input type="text" id="recordTime" name="recordTime" value="${cp.recordTime }" class="required" maxlength="45" placeholder="...收录时段">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordDuration">收录时长(小时)</label>
				<div class="controls">
					<input type="text" id="recordDuration" name="recordDuration" value="${cp.recordDuration }" class="required digits" maxlength="45" placeholder="...收录时长">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="publishUrl">发布接口地址</label>
				<div class="controls">
					<input type="text" id="publishUrl" name="publishUrl" value="${cp.publishUrl }" maxlength="100" placeholder="...发布接口地址">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="isPushCtp">是否推送内容交易平台</label>
				<div class="controls">
					<c:forEach var="map" items="${isPushCtpMap}">
						<label class="radio inline"> 
							<input type="radio" name="isPushCtp" value="${map.key}" <c:if test="${cp.isPushCtp == map.key }"> checked="checked"</c:if>>${map.value}
						</label>
					</c:forEach>
				</div>
			</div>
			
			<div class="page-header"><em>视频配置</em></div>
		
			<div class="control-group">
				<label class="control-label" for="videoFtpIp">FTP上传IP</label>
				<div class="controls">
					<input type="text" id="videoFtpIp" name="videoFtpIp" value="${cp.videoFtpIp }" class="required" maxlength="45" placeholder="...FTP上传IP">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpPort">端口</label>
				<div class="controls">
					<input type="text" id="videoFtpPort" name="videoFtpPort" value="${cp.videoFtpPort }" class="required" maxlength="10" placeholder="...端口">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpUsername">FTP用户名</label>
				<div class="controls">
					<input type="text" id="videoFtpUsername" name="videoFtpUsername" value="${cp.videoFtpUsername }" class="required" maxlength="255" placeholder="...FTP上传IP">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="videoFtpPassword">FTP密码</label>
				<div class="controls">
					<input type="text" id="videoFtpPassword" name="videoFtpPassword" value="${cp.videoFtpPassword }" class="required" maxlength="45" placeholder="...FTP密码">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpRootpath">FTP根路径</label>
				<div class="controls">
					<input type="text" id="videoFtpRootpath" name="videoFtpRootpath" value="${cp.videoFtpRootpath }" class="required" maxlength="45" placeholder="...FTP根路径">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpUploadpath">FTP上传路径</label>
				<div class="controls">
					<input type="text" id="videoFtpUploadpath" name="videoFtpUploadpath" value="${cp.videoFtpUploadpath }" class="required" maxlength="45" placeholder="...FTP上传路径">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="videoOutputGroup">输出组类型</label>
				<div class="controls">
					<input type="text" id="videoOutputGroup" name="videoOutputGroup" value="${cp.videoOutputGroup }" class="required" maxlength="100" placeholder="...输出组类型">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoOutputWay">输出方式配置</label>
				<div class="controls">
					<c:forEach var="map" items="${videoOutputWayMap }">
						<label class="radio inline">
							<input type="radio" name="videoOutputWay" value="${map.key }"
								 <c:if test="${map.key == cp.videoOutputWay }">checked="checked"</c:if>
							 ><span class="radioText ">${map.value }</span>
						</label>						
					</c:forEach>
				</div>
			</div>
		
			<div class="page-header"><em>图片配置</em></div>
		
			<div class="control-group">
				<label class="control-label" for="pictrueFtpIp">FTP上传IP</label>
				<div class="controls">
					<input type="text" id="pictrueFtpIp" name="pictrueFtpIp" value="${cp.pictrueFtpIp }" class="required" maxlength="45" placeholder="...FTP上传IP">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpPort">端口</label>
				<div class="controls">
					<input type="text" id="pictrueFtpPort" name="pictrueFtpPort" value="${cp.pictrueFtpPort }" class="required" maxlength="10" placeholder="...端口">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpUsername">FTP用户名</label>
				<div class="controls">
					<input type="text" id="pictrueFtpUsername" name="pictrueFtpUsername" value="${cp.pictrueFtpUsername }" class="required" maxlength="255" placeholder="...FTP上传IP">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="pictrueFtpPassword">FTP密码</label>
				<div class="controls">
					<input type="text" id="pictrueFtpPassword" name="pictrueFtpPassword" value="${cp.pictrueFtpPassword }" class="required" maxlength="45" placeholder="...FTP密码">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpRootpath">FTP根路径</label>
				<div class="controls">
					<input type="text" id="pictrueFtpRootpath" name="pictrueFtpRootpath" value="${cp.pictrueFtpRootpath }" class="required" maxlength="45" placeholder="...FTP根路径">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpUploadpath">FTP上传路径</label>
				<div class="controls">
					<input type="text" id="pictrueFtpUploadpath" name="pictrueFtpUploadpath" value="${cp.pictrueFtpUploadpath }" class="required" maxlength="45" placeholder="...FTP上传路径">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="pictrueOutputGroup">输出组类型</label>
				<div class="controls">
					<input type="text" id="pictrueOutputGroup" name="pictrueOutputGroup" value="${cp.pictrueOutputGroup }" class="required" maxlength="100" placeholder="...输出组类型">
				</div>
			</div>
			 
			
			<div class="control-group">
				<label class="control-label" for="pictrueOutputMedia">输出媒体类型</label>
				<div class="controls">
					<input type="text" id="pictrueOutputMedia" name="pictrueOutputMedia" value="${cp.pictrueOutputMedia }" class="required" maxlength="100" placeholder="...输出媒体类型">
				</div>
			</div>
			
		    <hr>
		    
		    <div class="control-group">
				<label class="control-label" for="program">拆条节目单上传</label>
				<div class="controls">
					<input type="file" id="program" name="program">
				</div>
			</div>
			 
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
