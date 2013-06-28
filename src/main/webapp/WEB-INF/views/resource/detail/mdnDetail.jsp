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

	<form id="inputForm" action="#" method="post" class="form-horizontal input-form">
		
		<fieldset>
			<legend><small>资源详情</small></legend>
			
			 <div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${mdn.apply.title}</p>
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
					<p class="help-inline plain-text">${mdn.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="coverArea">重点覆盖地域</label>
				<div class="controls">
					<p class="help-inline plain-text">${mdn.coverArea}</p>		
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="coverIsp">重点覆盖ISP</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="coverIsp" items="${fn:split(mdn.coverIsp,',')}">
							<c:forEach var="map" items="${ispTypeMap }">
								<c:if test="${map.key == coverIsp }">${map.value }</c:if>
							</c:forEach>
					    </c:forEach>
				    </p>		
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="bandwidth">加速服务带宽(M)</label>
				<div class="controls">
					<p class="help-inline plain-text">${mdn.bandwidth}</p>		
				</div>
			</div>
			
			<dd><em>加速服务带宽(M)</em>&nbsp;&nbsp;${item.bandwidth}</dd>
			
			<dl class="dl-horizontal">
			
				<c:if test="${not empty mdn.mdnVodItems }">
					<br>
					<dt>MDN点播加速</dt>
					<c:forEach var="vod" items="${mdn.mdnVodItems}">
						<dd><em>服务子项ID</em>&nbsp;&nbsp;${vod.id}</dd>
						<dd><em>服务域名</em>&nbsp;&nbsp;${vod.vodDomain}</dd>
						<dd><em>播放协议选择</em>&nbsp;&nbsp;${vod.vodProtocol}</dd>
						<dd><em>源站出口带宽</em>&nbsp;&nbsp;${vod.sourceOutBandwidth}</dd>
						<dd><em>Streamer地址</em>&nbsp;&nbsp;${vod.sourceStreamerUrl}</dd>
						<br>
					</c:forEach>
				</c:if>
				
				<c:if test="${not empty mdn.mdnLiveItems }">
					<br>
					<dt>MDN直播</dt>
					<c:forEach var="live" items="${mdn.mdnLiveItems}">
						<dd><em>服务子项ID</em>&nbsp;&nbsp;${live.id}</dd>
						<dd><em>服务域名</em>&nbsp;&nbsp;${live.liveDomain}</dd>
						<dd><em>播放协议选择</em>&nbsp;&nbsp;${live.liveProtocol}</dd>
						<dd><em>源站出口带宽</em>&nbsp;&nbsp;${live.bandwidth}</dd>
						<dd><em>频道名称</em>&nbsp;&nbsp;${live.name}</dd>
						<dd><em>频道GUID</em>&nbsp;&nbsp;${live.guid}</dd>
						<dd><em>直播流输出模式</em>&nbsp;&nbsp;<c:forEach var="map" items="${outputModeMap }"><c:if test="${map.key == live.streamOutMode }">${map.value }</c:if></c:forEach></dd>
						<c:if test="${live.streamOutMode == 1 }">
							<dd><em>编码器模式</em>&nbsp;&nbsp;<c:forEach var="map" items="${encoderModeMap }"><c:if test="${map.key == live.encoderMode }">${map.value }</c:if></c:forEach></dd>
						</c:if>
						<c:choose>
							<c:when test="${live.streamOutMode == 1  }">
								<c:choose>
									<c:when test="${live.encoderMode == 1 }">
										<c:if test="${not empty live.httpUrl }">
											<dd><em>拉流地址</em>&nbsp;&nbsp;${live.httpUrl}</dd>
										</c:if>
										<c:if test="${not empty live.httpBitrate }">
											<dd><em>拉流混合码率</em>&nbsp;&nbsp;${live.httpBitrate}</dd>
										</c:if>
									</c:when>
									<c:when test="${live.encoderMode == 2 }">
										<c:if test="${not empty live.hlsUrl }">
											<dd><em>推流地址</em>&nbsp;&nbsp;${live.hlsUrl}</dd>
										</c:if>
										<c:if test="${not empty live.hlsBitrate }">
											<dd><em>推流混合码率</em>&nbsp;&nbsp;${live.hlsBitrate}</dd>
										</c:if>
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<dd><em>HTTP流地址</em>&nbsp;&nbsp;${live.httpUrl}</dd>
								<dd><em>HTTP流混合码率</em>&nbsp;&nbsp;${live.httpBitrate}</dd>
								
								<dd><em>HSL流地址</em>&nbsp;&nbsp;${live.hlsUrl}</dd>
								<dd><em>HSL流混合码率</em>&nbsp;&nbsp;${live.hlsBitrate}</dd>
								
							</c:otherwise>
						</c:choose>
						<br>
					</c:forEach>
				</c:if>
				
			</dl>
			
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
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
