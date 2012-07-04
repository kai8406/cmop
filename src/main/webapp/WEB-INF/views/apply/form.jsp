<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>接入服务申请</title>
	
	 <script>
	$(document).ready(function() {
		
		//聚焦指定的Tab
		$("#support-tab").addClass("active");
		
		switchTab();
		
		//起始时间
		$("#serviceStart").datepicker();
		$("#serviceEnd").datepicker();

	});
</script>
</head>

<body>
	<div class="row">

			<!-- Guide -->
			<div id="Guide" class="span2">
				<div class="tabbable tabs-left">
					<ul class="nav nav-tabs" id="myTab">
						<li class="active"><a href="#profile1" data-toggle="tab">1.接入服务申请</a></li>
						<li><a href="#profile2" data-toggle="tab">2.接入服务</a></li>
						<li><a href="#profile3" data-toggle="tab">3.提交</a></li>
					</ul>
				</div>
			</div>
			
			<div id="main" class="span10">
			
				<form:form id="inputForm" modelAttribute="inVpnItem" action="." method="post">
				 <form:hidden path="id" />
				<input type="text" name="applyId" value="${apply.id }" />
					<div class="tab-content">
					
						<!-- 第1步 -->
						<div class="tab-pane fade in active span6 offset1" id="profile1">
							<fieldset>
								<legend>接入服务申请</legend>
								
								<div class="control-group">
									<label class="control-label">接入服务申请主题</label>
									<div class="controls">
										<input type="text" id="title" name="title" value="${inVpnItem.apply.title }"  class="input-large" />
									</div>
								</div>
								
								
								<div class="control-group">
									<label class="control-label">申请起始时间</label>
									<div class="controls">
										<input type="text" id="serviceStart" name="serviceStart" value="${inVpnItem.apply.serviceStart }" class="input-medium" />&mdash;
										<input type="text" id="serviceEnd" name="serviceEnd" value="${inVpnItem.apply.serviceEnd }" class="input-medium" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">申请用途</label>
									<div class="controls">
										<textarea rows="3" id="description" name="description" class="input-xlarge">${inVpnItem.apply.description}</textarea>
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">资源类型</label>
									<div class="controls">
										<label class="radio"> 
											<input type="radio"  value="1" name="resourceType" />生产资源
										</label> 
										<label class="radio"> 
											<input type="radio" value=" 2"	name="resourceType" /> 测试/演示资源
										</label> 
										<label class="radio"> 
											<input type="radio" value="3" name="resourceType" /> 公测资源
										</label>
									</div>
								</div>
							</fieldset>
							
							<div class="form-actions">
								<a id="nextStep" class="btn btn-primary">下一步</a>
							</div>
						</div>

						<!-- 第2步 -->
						<div class="tab-pane fade span6 offset1" id="profile2">
							<fieldset>
								<legend>接入服务</legend>

								<div class="control-group">
									<label class="control-label">账号</label>
									<div class="controls">
										<input type="text" id="account" name="account" value="${inVpnItem.account }" class="input-large" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">使用人</label>
									<div class="controls">
										<input type="text" id="accountUser" name="accountUser" value="${inVpnItem.accountUser }" class="input-large" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">需要访问主机</label>
									<div class="controls">
										<input type="text" id="visitHost" name="visitHost" value="${inVpnItem.visitHost }" class="input-large" />
									</div>
								</div>

							</fieldset>
							
							<div class="form-actions">
								<a id="backStep" class="btn">返回</a> 
								<a id="nextStep" class="btn btn-primary">下一步</a>
							</div>
						</div>


						<!-- 第3步 -->
						<div class="tab-pane fade span8 offset1" id="profile3">
							<fieldset>
								<legend>接入服务申请详情</legend>
								<table class="table table-bordered table-striped">
									<colgroup>
										<col class="span2">
										<col class="span6">
									</colgroup>
									<thead>
										<tr>
											<th>Tag</th>
											<th>Description</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>申请主题</td>
											<td id="td_title"></td>
										</tr>
										<tr>
											<td>起始时间</td>
											<td id="td_time"></td>
										</tr>
										<tr>
											<td>申请用途</td>
											<td id="td_description"></td>
										</tr>
										<tr>
											<td>资源类型</td>
											<td id="td_resourceType"></td>
										</tr>
										<tr>
											<td>接入服务</td>
											<td id="td_inVpnItem"></td>
										</tr>
									</tbody>
								</table>
							</fieldset>
							
							<a href="${ctx }/apply/support/list" class="btn">list</a>
							
							<div class="form-actions">
								<a id="backStep" class="btn ">返回</a>
								<button class="btn btn-success">保存修改</button>
							</div>
							
						</div>

					</div>

				</form:form>
			</div>
		</div>
</body>
</html>
