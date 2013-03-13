<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>ELB监控变更</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#resource").addClass("active");
			
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
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="${ctx}/resources/update/monitorElb/" method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="id" value="${resources.id }">
		
		<!-- 临时的elbId,后期看是否需要变更监控的elb -->
		<input type="hidden" name="elbId" value="${monitorElb.networkElbItem.id }">
		
		<fieldset>
			<legend><small>变更ELB监控</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${monitorElb.apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">标识符</label>
				<div class="controls">
					<p class="help-inline plain-text">${monitorElb.identifier}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="monitorMails">监控邮件列表</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="mail" items="${monitorElb.apply.monitorMails }">${mail.email }<br></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="monitorPhones">监控手机列表</label>
				<div class="controls">
					<p class="help-inline plain-text">
						<c:forEach var="phone" items="${monitorElb.apply.monitorPhones }">${phone.telephone }<br></c:forEach>
					</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="identifier">监控ELB</label>
				<div class="controls">
					<p class="help-inline plain-text">${monitorElb.networkElbItem.identifier}(${monitorElb.networkElbItem.virtualIp})</p>
				</div>
			</div>
			
			<hr>
			
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
				 
			<div class="form-actions">
				<input class="btn" type="button" value="返回" onclick="history.back()">
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
