<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>

<head>
<style type="text/css">
.alert {
	padding: 8px 35px 8px 14px;
	margin-bottom: 0px;
	color: black;
	text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
	background-color: #fff;
	border: 1px solid #fff;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
}
</style>
	<title>ECS服务申请</title>
	
	 <script>
	$(document).ready(function() {
		
		//聚焦指定的Tab
		$("#support-tab").addClass("active");
		
		switchTab();
		
		//起始时间
		$("#serviceStart").datepicker();
		$("#serviceEnd").datepicker();
		

		$("#serviceStart").val(getDateByMonthNum(0)); // 当前的日期
		$("#serviceEnd").val(getDateByMonthNum(1)); // 一个月后的日期
		
		inputServiceDate();
		
		//点击弹出窗口保存时.
		$("#modalSave").click(function() {
			selectServer($("#inputResources"),$(this));
		});
		$("#modalSave2").click(function() {
			selectServer($("#inputResources2"),$(this));
		});
		$("#modalSave3").click(function() {
			selectServer($("#inputResources3"),$(this));
		});
		$("#modalSave4").click(function() {
			selectServer($("#inputResources4"),$(this));
		});

	});
</script>
</head>

<body>
	<div class="row">

			<!-- Guide -->
			<div id="Guide" class="span2">
				<div class="tabbable tabs-left">
					<ul class="nav nav-tabs" id="myTab">
						<li class="active"><a href="#profile1" data-toggle="tab">1.ECS资源申请</a></li>
						<li><a href="#profile2" data-toggle="tab">2.ECS资源</a></li>
						<li><a href="#profile3" data-toggle="tab">3.提交</a></li>
					</ul>
				</div>
			</div>
			
			<div id="main" class="span10">
			
				<form:form id="inputForm" modelAttribute="apply" action="." method="post">
				
				<input type="hidden" name="id" value="${apply.id}"/>
				<input type="hidden" name="title" value="${apply.title}"/>
				
				<input type="hidden" id="osTypes" name="osTypes" value=""/>
				<input type="hidden" id="bits" name="bits" value=""/>
				<input type="hidden" id="serverTypeIds" name="serverTypeIds" value=""/>
				<input type="hidden" id="serverCount" name="serverCount" value=""/>
				
					<div class="tab-content">
					
						<!-- 第1步 -->
						<div class="tab-pane fade in active span6" id="profile1">
							<fieldset>
								<legend>ECS资源申请</legend>
								
								<div class="control-group">
									<label class="control-label">资源类型</label>
									<div class="controls">
										<label class="radio"> 
											<input type="radio"  value="1" name="resourceType"   
											<c:if test="${apply.resourceType == 1 }">
												checked="checked" 
											</c:if>
											/>生产资源
										</label> 
										<label class="radio"> 
											<input type="radio" value=" 2"	name="resourceType" 
											<c:if test="${apply.resourceType == 2 }">
												checked="checked" 
											</c:if>
											/> 测试/演示资源
										</label> 
										<label class="radio"> 
											<input type="radio" value="3" name="resourceType"
											<c:if test="${apply.resourceType == 3 }">
												checked="checked" 
											</c:if>
											 /> 公测资源
										</label>
									</div>
								</div>
								
								
								<div class="control-group">
									<label class="control-label">申请起始时间</label>
									<div class="controls">
										<input type="text" id="serviceStart" name="serviceStart" value="${apply.serviceStart }" class="input-medium" />&mdash;
										<input type="text" id="serviceEnd" name="serviceEnd" value="${apply.serviceEnd }" class="input-medium" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">申请用途</label>
									<div class="controls">
										<textarea rows="2" id="description" name="description" class="input-xlarge">${apply.description}</textarea>
									</div>
								</div>
								
							</fieldset>
							
							<div class="form-actions">
								<a id="nextStep" class="btn btn-primary">下一步</a>
							</div>
						</div>

						<!-- 第2步 -->
						<div class="tab-pane fade span8" id="profile2">
						<div class="page-header">操作系统</div>
						<div class="row">
								<div class="span1">
									<img alt="windows OS" src="${ctx}/static/custom/images/windows-logo.png" />
								</div>
								<div class="span5">
									<div id="osId" class="hidden">1</div>
									<h3 id="osName">Windwos2003R2</h3>
									<p>Nullam quis risus eget urna mollis ornare vel eu leo.
										ultricies vehicula.</p>
								</div>
								<div class="span1">
									<label class="radio"> <input type="radio" value="1"
										checked="checked" name="osBit1" /> 32 Bit
									</label> <label class="radio"> <input type="radio" value="2"
										name="osBit1" /> 64 Bit
									</label>
								</div>
								<div class="span1">
									<a id="inputResources" class="btn" data-toggle="modal"
										href="#ComputResources">Select</a>
								</div>
							</div>
							<div class="row">
								<div class="span1">
									<img alt="windows OS" src="${ctx}/static/custom/images/windows-logo.png" />
								</div>
								<div class="span5">
									<div id="osId" class="hidden">2</div>
									<h3 id="osName">Windwos2008R2</h3>
									<p>Nullam quis risus eget urna mollis ornare vel eu leo.
										ultricies vehicula.</p>
								</div>
								<div class="span1">
									<label class="radio"> <input type="radio" value="1"
										checked="checked" name="osBit2" /> 32 Bit
									</label> <label class="radio"> <input type="radio" value="2"
										name="osBit2" /> 64 Bit
									</label>
								</div>
								<div class="span1">
									<a id="inputResources2" class="btn" data-toggle="modal"
										href="#ComputResources2">Select</a>
								</div>
							</div>
							<div class="row">
								<div class="span1">
									<img alt="centOS" src="${ctx}/static/custom/images/centos-logo.png" />
								</div>
								<div class="span5">
									<div id="osId" class="hidden">3</div>
									<h3 id="osName">Centos6.3</h3>
									<p>Nullam quis risus eget urna mollis ornare vel eu leo.
										ultricies vehicula.</p>
								</div>
								<div class="span1">
									<label class="radio"> <input type="radio" value="1"
										checked="checked" name="osBit3" /> 32 Bit
									</label> <label class="radio"> <input type="radio" value="2"
										name="osBit3" /> 64 Bit
									</label>
								</div>
								<div class="span1">
									<a id="inputResources3" class="btn" data-toggle="modal"
										href="#ComputResources3">Select</a>
								</div>
							</div>
							<div class="row">
								<div class="span1">
									<img alt="centOS" src="${ctx}/static/custom/images/centos-logo.png" />
								</div>
								<div class="span5">
									<div id="osId" class="hidden">4</div>
									<h3 id="osName">Centos5.6</h3>
									<p>Nullam quis risus eget urna mollis ornare vel eu leo.
										ultricies vehicula.</p>
								</div>
								<div class="span1">
									<label class="radio"> <input type="radio" value="1"
										checked="checked" name="osBit4" /> 32 Bit
									</label> <label class="radio"> <input type="radio" value="2"
										name="osBit4" /> 64 Bit
									</label>
								</div>
								<div class="span1">
									<a id="inputResources4" class="btn" data-toggle="modal"
										href="#ComputResources4">Select</a>
								</div>
							</div>
							
							<!-- 显示选中的计算资源 -->
							<div id="selectedResources">
								<div class="page-header">选中计算资源</div>
								
								<c:if test="${not empty computeList}">
								<c:forEach var="item" items="${computeList }">
									<div id="singleResources" class="row alert">
									
										<div id="osName_SingleResources" class="span1">
											<c:if test="${item[1] == 1 }">Windwos2003R2
											</c:if>
											<c:if test="${item[1] == 2 }">Windwos2008R2
											</c:if>
											<c:if test="${item[1] == 3 }">Centos5.6
											</c:if>
											<c:if test="${item[1] == 4 }">Centos6.3
											</c:if>
										</div>
										
										<div id="osId_SingleResources" class="hidden">${item[1] }</div>
										
										
										<div id="osBitId__SingleResources" class="hidden">${item[2] }</div>
										
										<div id="osBitName__SingleResources" class="span1">
											<c:if test="${item[2] == 1 }">32 Bit
											</c:if>
											<c:if test="${item[2] == 2 }">64 Bit
											</c:if>
										</div>
										
										<div id="serverTypeId__SingleResources" class="hidden">${item[3] }</div>
										
											<div id="serverTypeName__SingleResources" class="span4">
											<c:if test="${item[3] == 1 }">Small &mdash;CPU[单核] Memory[1GB] Disk[20GB]
											</c:if>
											<c:if test="${item[3] == 2 }">Middle &mdash; CPU[双核] Memory[2GB] Disk[20GB]
											</c:if>
											<c:if test="${item[3] == 3 }">Large &mdash; CPU[四核] Memory[4GB] Disk[20GB]
											</c:if>
										
										</div>
										
										<div id="serverCount__SingleResources" class="span1">${item[4] }</div>
										
										<button data-dismiss="alert" id="removeResources" class="close">×</button>
									</div>
									
								</c:forEach>
								
								</c:if>
							</div>
							
							
							<div class="form-actions">
								<a id="backStep" class="btn">返回</a> 
								<a id="nextStep" class="btn btn-primary">下一步</a>
							</div>
						</div>


						<!-- 第3步 -->
						<div class="tab-pane fade span8" id="profile3">
							<fieldset>
								<legend>ECS资源申请详情</legend>
								<table class="table table-bordered table-striped"
									id="formDetail">
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
											<td id="td_usage"></td>
										</tr>
										<tr>
											<td>资源类型</td>
											<td id="td_resourceType"></td>
										</tr>

									</tbody>
								</table>
							</fieldset>
							
							<div class="form-actions">
								<a id="backStep" class="btn ">返回</a>
								<button class="btn btn-success">保存修改</button>
							</div>
							
						</div>

					</div>

				</form:form>
			</div>
		</div>
				<!-- 弹出选择计算资源规格DIV -->
							<div id="ComputResources" class="modal hide fade form-horizontal">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h3>选择规格和数量</h3>
								</div>
								<div class="modal-body">
									<table class="table">
										<colgroup>
											<col class="span4">
											<col class="span2">
										</colgroup>
										<thead>
											<tr>
												<th>规格</th>
												<th>数量</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><div class="hidden" id="smallTypeId">1</div>Small
													&mdash;CPU[单核] Memory[1GB] Disk[20GB]</td>
												<td><input type="text" id="smallServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="middleTypeId">2</div>Middle
													&mdash; CPU[双核] Memory[2GB] Disk[20GB]</td>
												<td><input type="text" id="middleServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="largeTypeId">3</div>Large
													&mdash; CPU[四核] Memory[4GB] Disk[20GB]</td>
												<td><input type="text" id="largeServerCount"
													class="input-mini" /></td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="modal-footer">
									<a href="#" class="btn" data-dismiss="modal">取消</a> <a href="#"
										id="modalSave" data-dismiss="modal" class="btn btn-primary">确定</a>
								</div>
							</div>
							
							<div id="ComputResources2" class="modal hide fade form-horizontal">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h3>选择规格和数量</h3>
								</div>
								<div class="modal-body">
									<table class="table">
										<colgroup>
											<col class="span4">
											<col class="span2">
										</colgroup>
										<thead>
											<tr>
												<th>规格</th>
												<th>数量</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><div class="hidden" id="smallTypeId">1</div>Small
													&mdash;CPU[单核] Memory[1GB] Disk[20GB]</td>
												<td><input type="text" id="smallServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="middleTypeId">2</div>Middle
													&mdash; CPU[双核] Memory[2GB] Disk[20GB]</td>
												<td><input type="text" id="middleServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="largeTypeId">3</div>Large
													&mdash; CPU[四核] Memory[4GB] Disk[20GB]</td>
												<td><input type="text" id="largeServerCount"
													class="input-mini" /></td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="modal-footer">
									<a href="#" class="btn" data-dismiss="modal">取消</a> <a href="#"
										id="modalSave2" data-dismiss="modal" class="btn btn-primary">确定</a>
								</div>
							</div>
							
							<div id="ComputResources3" class="modal hide fade form-horizontal">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h3>选择规格和数量</h3>
								</div>
								<div class="modal-body">
									<table class="table">
										<colgroup>
											<col class="span4">
											<col class="span2">
										</colgroup>
										<thead>
											<tr>
												<th>规格</th>
												<th>数量</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><div class="hidden" id="smallTypeId">1</div>Small
													&mdash;CPU[单核] Memory[1GB] Disk[20GB]</td>
												<td><input type="text" id="smallServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="middleTypeId">2</div>Middle
													&mdash; CPU[双核] Memory[2GB] Disk[20GB]</td>
												<td><input type="text" id="middleServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="largeTypeId">3</div>Large
													&mdash; CPU[四核] Memory[4GB] Disk[20GB]</td>
												<td><input type="text" id="largeServerCount"
													class="input-mini" /></td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="modal-footer">
									<a href="#" class="btn" data-dismiss="modal">取消</a> <a href="#"
										id="modalSave3" data-dismiss="modal" class="btn btn-primary">确定</a>
								</div>
							</div>
							
							<div id="ComputResources4" class="modal hide fade form-horizontal">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h3>选择规格和数量</h3>
								</div>
								<div class="modal-body">
									<table class="table">
										<colgroup>
											<col class="span4">
											<col class="span2">
										</colgroup>
										<thead>
											<tr>
												<th>规格</th>
												<th>数量</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td><div class="hidden" id="smallTypeId">1</div>Small
													&mdash;CPU[单核] Memory[1GB] Disk[20GB]</td>
												<td><input type="text" id="smallServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="middleTypeId">2</div>Middle
													&mdash; CPU[双核] Memory[2GB] Disk[20GB]</td>
												<td><input type="text" id="middleServerCount"
													class="input-mini" /></td>
											</tr>
											<tr>
												<td><div class="hidden" id="largeTypeId">3</div>Large
													&mdash; CPU[四核] Memory[4GB] Disk[20GB]</td>
												<td><input type="text" id="largeServerCount"
													class="input-mini" /></td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="modal-footer">
									<a href="#" class="btn" data-dismiss="modal">取消</a> <a href="#"
										id="modalSave4" data-dismiss="modal" class="btn btn-primary">确定</a>
								</div>
							</div>
		
		
		
		
</body>
</html>
