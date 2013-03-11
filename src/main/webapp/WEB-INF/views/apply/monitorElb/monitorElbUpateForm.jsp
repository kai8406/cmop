<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>ES3管理</title>
	
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
						<table class="table table-bordered table-condensed"  >
							<tbody>
								<tr class="clone">
									<td><input type="text" id="monitorMails" name="monitorMails" class="required" maxlength="45" placeholder="...Email"></td>
									<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
								</tr>
							</tbody>
						</table>	
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="monitorPhone">监控手机列表</label>
					<div class="controls">
						 <table class="table table-bordered table-condensed"  >
							<tbody>
								<tr class="clone">
									<td><input type="text" id="monitorPhones" name="monitorPhones" class="required" maxlength="45" placeholder="...Phone"></td>
									<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			
			<div class="control-group">
				<label class="control-label" for="monitorElbType">监控ELB</label>
				<div class="controls">
					<select id="elbId" name="elbId" class="required">
						<c:forEach var="item" items="${allElbs }">
							<option value="${item.id }" 
								<c:if test="${item.id == monitorElb.networkElbItem.id }">
									selected="selected"
								</c:if>
							>${item.identifier}(${item.virtualIp })</option>
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
