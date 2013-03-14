<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
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
	
	<form id="inputForm" action="." method="post" class="input-form form-horizontal" >
		
		<input type="hidden" name="applyId" value="${monitorElb.apply.id }">
		
		<fieldset>
			<legend><small>修改ELB监控</small></legend>
			
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
				<label class="control-label" for="monitorMail">监控邮件列表</label>
				<div class="controls">
					<c:forEach var="email" items="${monitorElb.apply.monitorMails  }">
						<p class="help-inline plain-text">${email.email }</p><br>
					</c:forEach>
				</div>
			</div>
				
			<div class="control-group">
				<label class="control-label" for="monitorPhone">监控手机列表</label>
				<div class="controls">
					<c:forEach var="phone" items="${monitorElb.apply.monitorPhones  }">
						<p class="help-inline plain-text">${phone.telephone }</p><br>
					</c:forEach>
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="monitorElbType">监控ELB</label>
				<div class="controls">
					<select id="elbId" name="elbId" class="required">
						<option value="${monitorElb.networkElbItem.id }" selected="selected">${monitorElb.networkElbItem.identifier}(${monitorElb.networkElbItem.virtualIp })</option>
						<c:forEach var="item" items="${monitorElbs }">
							<option value="${item.id }" >${item.identifier}(${item.virtualIp })</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="form-actions">
				<a href="${ctx}/apply/update/${monitorElb.apply.id}/" class="btn">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
