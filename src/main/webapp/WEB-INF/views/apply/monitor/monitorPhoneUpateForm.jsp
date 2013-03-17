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
		
		<input type="hidden" name="applyId" value="${apply.id }">
		
		<fieldset>
			<legend><small>修改监控手机列表</small></legend>
			
			<div class="control-group">
				<label class="control-label" for="title">所属服务申请</label>
				<div class="controls">
					<p class="help-inline plain-text">${apply.title}</p>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="monitorPhone">监控手机列表</label>
				<div class="controls">
					 <table class="table table-bordered table-condensed"  >
						<tbody>
							<c:forEach var="phone" items="${apply.monitorPhones }">
								<tr class="clone">
									<td><input type="text" value="${phone.telephone }" id="monitorPhones" name="monitorPhones" class="required" maxlength="45" placeholder="...Phone"></td>
									<td><a class="btn clone">添加</a>&nbsp;<a class="btn clone disabled" >删除</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		
			<div class="form-actions">
				<a href="${ctx}/apply/update/${apply.id}/" class="btn">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
			
		</fieldset>
		
	</form>
	
</body>
</html>
