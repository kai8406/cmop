<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>资源管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="${ctx}/summary/migrate" method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="resourceId" value="${resources.id }">
		
		<fieldset>
			<legend><small>资源详情</small></legend>
			
			 <div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="status">资源状态</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="map" items="${resourcesStatusMap}"><c:if test="${map.key == resources.status }">${map.value}</c:if></c:forEach>
					</p>
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
					<p class="help-inline plain-text">${cp.recordStreamUrl}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordBitrate">收录码率</label>
				<div class="controls">
					<p class="help-inline plain-text"><c:forEach var="map" items="${recordBitrateMap}"><c:if test="${map.key == cp.recordBitrate }">${map.value }</c:if></c:forEach></p>
				</div>
			</div>
			
			
			<div class="control-group">
				<label class="control-label" for="exportEncode">输出编码</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="exportEncode" items="${fn:split(cp.exportEncode,',')}">
							<c:forEach var="map" items="${exportEncodeMap }">
								<c:if test="${map.key == exportEncode }">${map.value }<br></c:if>
							</c:forEach>
				  		</c:forEach>
				    </p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordType">收录类型</label>
				<div class="controls">
					<p class="help-inline plain-text"><c:forEach var="map" items="${recordTypeMap}"><c:if test="${map.key == cp.recordType }">${map.value }</c:if></c:forEach></p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordTime">收录时段</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.recordTime}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="recordDuration">收录时长(小时)</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.recordDuration}</p>
				</div>
			</div>
			
			<c:if test="${not empty cp.publishUrl }">
				<div class="control-group">
					<label class="control-label" for="publishUrl">发布接口地址</label>
					<div class="controls">
						<p class="help-inline plain-text">${cp.publishUrl}</p>
					</div>
				</div>
			</c:if>
			
			<div class="control-group">
				<label class="control-label" for="isPushCtp">是否推送内容交易平台</label>
				<div class="controls">
					<p class="help-inline plain-text"><c:forEach var="map" items="${isPushCtpMap}"><c:if test="${map.key == cp.isPushCtp }">${map.value }</c:if></c:forEach></p>
				</div>
			</div>
			
			<div class="page-header"><em>视频配置</em></div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpIp">FTP上传IP</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.videoFtpIp}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpPort">端口</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.videoFtpPort}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpUsername">FTP用户名</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.videoFtpUsername}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpPassword">FTP密码</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.videoFtpPassword}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpRootpath">FTP根路径</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.videoFtpRootpath}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpUploadpath">FTP上传路径</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.videoFtpUploadpath}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoOutputGroup">输出组类型</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.videoOutputGroup}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="videoOutputWay">输出方式配置</label>
				<div class="controls">
					<p class="help-inline plain-text"><c:forEach var="map" items="${videoOutputWayMap}"><c:if test="${map.key == cp.videoOutputWay }">${map.value }</c:if></c:forEach></p>
				</div>
			</div>
			
			<div class="page-header"><em>图片配置</em></div>
			
			<div class="control-group">
				<label class="control-label" for="videoFtpIp">FTP上传IP</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueFtpIp}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpPort">端口</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueFtpPort}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpUsername">FTP用户名</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueFtpUsername}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpPassword">FTP密码</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueFtpPassword}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpRootpath">FTP根路径</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueFtpRootpath}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueFtpUploadpath">FTP上传路径</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueFtpUploadpath}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueOutputGroup">输出组类型</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueOutputGroup}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="pictrueOutputMedia">输出媒体类型</label>
				<div class="controls">
					<p class="help-inline plain-text">${cp.pictrueOutputMedia}</p>
				</div>
			</div>
			
			<hr>
			
			<div class="control-group">
				<label class="control-label" for="serviceTagId">服务标签</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="item" items="${allTags}"><c:if test="${item.id == resources.serviceTag.id }">${item.name}</c:if></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="changeDescription">变更描述</label>
				<div class="controls">
					<p class="help-inline plain-text">${change.description}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="changeDescription">变更时间</label>
				<div class="controls">
					<p class="help-inline plain-text"><fmt:formatDate value="${change.changeTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></p>
				</div>
			</div>
			
			<c:if test="${not empty migrate }">
				<div class="control-group">
					<label class="control-label" for="userId">资源所属人</label>
					<div class="controls">
						<select name="userId">
							<c:forEach var="item" items="${users }">
								<option value="${item.id }" 
									<c:if test="${item.id == resources.user.id }">selected="selected"</c:if>	
								>${item.name }</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</c:if>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<c:if test="${not empty migrate }"><input class="btn btn-primary" type="submit" value="提交"></c:if>
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
