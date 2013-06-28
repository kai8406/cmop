<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#inputForm").validate();
			
		});
		 
	</script>
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="." method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="applyId" value="${mdnVod.mdnItem.apply.id }">
		
		<fieldset>
			<legend><small>修改MDN点播加速</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${mdnVod.mdnItem.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="vodDomain">服务域名</label>
				<div class="controls">
					<input type="text" id="vodDomain" name="vodDomain" value="${mdnVod.vodDomain }" class="required" maxlength="45" placeholder="...服务域名">
				</div>
			</div>
					
			<div class="control-group">
				<label class="control-label" for="vodProtocol">播放协议选择</label>
				<div class="controls">
					<c:forEach var="map" items="${palyProtocolMap}">
				 		<label class="checkbox inline">
				 			<input type="checkbox" id="vodProtocol" name="vodProtocol" value="${map.key}" 
								<c:forEach var="protocol" items="${fn:split(mdnVod.vodProtocol,',')}">
									<c:if test="${map.key == protocol }"> checked="checked" </c:if>
						    	</c:forEach>
							class="required">
							<span class="checkboxText">${map.value }</span>
						</label>	
					</c:forEach>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="sourceOutBandwidth">源站出口带宽</label>
				<div class="controls">
					<input type="text" id="sourceOutBandwidth" name="sourceOutBandwidth" value="${mdnVod.sourceOutBandwidth }" class="required" maxlength="45" placeholder="...源站出口带宽">
				</div>
			</div>
					
			<div class="control-group">
				<label class="control-label" for="sourceStreamerUrl">Streamer地址</label>
				<div class="controls">
					<input type="text" id="sourceStreamerUrl" name="sourceStreamerUrl" value="${mdnVod.sourceStreamerUrl }" class="required" maxlength="45" placeholder="...源站Streamer公网地址">
				</div>
			</div>
		
			<div class="form-actions">
				<a href="${ctx}/apply/update/${mdnVod.mdnItem.apply.id}/" class="btn">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
