<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#esg").addClass("active");
			
			$("#description").focus();
			
			/*禁用回车提交form表单.*/
			$("#inputForm").keypress(function(e) {
				if (e.which == 13) {return false;}
			});
			
			 $('#inputForm').validate();
			 
			/*点击页面"生成规则"按钮时,根据前面的协议,端口范围,访问源生成alert的资源.*/
			$("#createBtn").click(function() {
				
				$("input").addClass("required");
				
				if (!$("#inputForm").valid()) {
					return false;
				}
				
				var protocol = $("#protocol").val();
				var portRange = $("#portRange").val();
				var visitSource = $("#visitSource").val();
				var visitTarget = $("#visitTarget").val();
				
				//如果重复,则删除重复的项.
				$("div.resources").each(function() {
					var $this = $(this);
					$this.find("input[name='protocols']").val() == protocol && $this.find("input[name='portRanges']").val() == portRange && $this.find("input[name='visitSources']").val() == visitSource && $this.find("input[name='visitTargets']").val() == visitTarget && $this.remove();
				});
				
				var html = '<div class="resources alert alert-block alert-info fade in">';
				html += '<button data-dismiss="alert" class="close" type="button">×</button>';
				html += '<input type="hidden" value="' + protocol + '" name="protocols">';
				html += '<input type="hidden" value="' + portRange + '" name="portRanges">';
				html += '<input type="hidden" value="' + visitSource + '" name="visitSources">';
				html += '<input type="hidden" value="' + visitTarget + '" name="visitTargets">';
				html += '<dd><em>协议</em>&nbsp;&nbsp;<strong>' + protocol + '</strong></dd>';
				html += '<dd><em>端口范围</em>&nbsp;&nbsp;<strong>' + portRange + '</strong></dd>';
				html += '<dd><em>访问来源IP</em>&nbsp;&nbsp;<strong>' + visitSource + '</strong></dd>';
				html += '<dd><em>访问目的IP</em>&nbsp;&nbsp;<strong>' + visitTarget + '</strong></dd>';
				html += '</div>';
				
				$("#resourcesDIV dl").append(html);
			});
			 
		});
		 
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${esg.id}">
		
		<fieldset>
			<legend><small>
				<c:choose><c:when test="${not empty esg }">修改</c:when><c:otherwise>创建</c:otherwise></c:choose>安全组ESG
			</small></legend>
			
			<c:if test="${not empty esg}">
				<div class="control-group">
					<label class="control-label" for="identifier">标题</label>
					<div class="controls">
						<p class="help-inline plain-text">${esg.identifier}</p>
					</div>
				</div>
			</c:if>
			
			<div class="control-group">
				<label class="control-label" for="description">安全组的描述</label>
				<div class="controls">
					<input type="text" id="description" name="description" class="required" maxlength="45" value="${esg.description}" placeholder="...安全组的描述">
				</div>
			</div>
			
		 	<div class="control-group">
				<label class="control-label" for="protocol">协议</label>
				<div class="controls">
					<select id="protocol" class="required">
						<c:forEach var="map" items="${esgProtocolMap}">
							<option value="${map.key}">${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="portRange">端口范围</label>
				<div class="controls">
					<input type="text" id="portRange" class="required portValidate" placeholder="80 or 8080/65535">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="visitSource">访问来源IP</label>
				<div class="controls">
					<input type="text" id="visitSource" class="required ipValidate" placeholder="192.168.0.1 or 192.168.0.1/10">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="visitTarget">访问目的IP</label>
				<div class="controls">
					<input type="text" id="visitTarget"  class="required ipValidate" placeholder="192.168.0.1 or 192.168.0.1/10">
				</div>
			</div>
			
			<div class="control-group">
				<div class="controls">
					<input id="createBtn" type="button" class="btn" value="生成规则">
				</div>
			</div>
			
			<!-- 生成的资源 -->
			<div id="resourcesDIV"><dl class="dl-horizontal">
				<c:forEach var="item" items="${esg.esgRuleItems }">
					<div class="resources alert alert-block alert-info fade in">
						<button data-dismiss="alert" class="close" type="button">×</button>
						<input type="hidden" name="protocols" value="${item.protocol }">
						<input type="hidden" name="portRanges" value="${item.portRange }">
						<input type="hidden" name="visitSources" value="${item.visitSource }">
						<input type="hidden" name="visitTargets" value="${item.visitTarget }">
						<dd><em>协议</em>&nbsp;&nbsp;<strong>${item.protocol }</strong></dd>
						<dd><em>端口范围</em>&nbsp;&nbsp;<strong>${item.portRange }</strong></dd>
						<dd><em>访问来源IP</em>&nbsp;&nbsp;<strong>${item.visitSource }</strong></dd>
						<dd><em>访问目的IP</em>&nbsp;&nbsp;<strong>${item.visitTarget }</strong></dd>
					</div>
				</c:forEach>
			</dl></div>
			
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" onclick="clearInputeOfRequired()" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
