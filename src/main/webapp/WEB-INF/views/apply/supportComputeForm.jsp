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
						<li class="active"><a href="#profile1" data-toggle="tab">1.计算资源申请</a></li>
						<li><a href="#profile2" data-toggle="tab">2.计算资源</a></li>
						<li><a href="#profile3" data-toggle="tab">3.提交</a></li>
					</ul>
				</div>
			</div>
			
			<div id="main" class="span10">
			
				<form:form id="inputForm" modelAttribute="apply" action="${ctx}/apply/support/compute/save/${apply.id}" method="post">

					<input type="hidden" id="osType" name="osType" value="${osType }" />

					<div class="tab-content">
					
						<!-- 第1步 -->
						<div class="tab-pane fade in active span6 offset1" id="profile1">
							<fieldset>
								<legend>接入服务申请</legend>
								
								<div class="control-group">
									<label class="control-label">计算资源申请主题</label>
									<div class="controls">
										<input type="text" id="title" name="title" value="${title }"  class="input-large" />
									</div>
								</div>
								
								
								<div class="control-group">
									<label class="control-label">申请起始时间</label>
									<div class="controls">
										<input type="text" id="serviceStart" name="serviceStart" value="${serviceStart }" class="input-medium" />&mdash;
										<input type="text" id="serviceEnd" name="serviceEnd" value="${serviceEnd }" class="input-medium" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">申请用途</label>
									<div class="controls">
										<textarea rows="3" id="description" name="description" class="input-xlarge">${description}</textarea>
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
								<legend>计算资源</legend>

								<div class="control-group">
									<label for="focusedInput" class="control-label">服务器类型</label>
									<div class="controls">
										<label class="radio"> 
											<input type="radio"  value="1" name="serverType" />Small &mdash; CPU[单核] Memory[1GB] Disk[20GB]
										</label> 
										<label class="radio"> 
											<input type="radio" value=" 2"	name="serverType" />Middle &mdash; CPU[双核] Memory[2GB] Disk[20GB]
										</label> 
										<label class="radio"> 
											<input type="radio" value="3" name="serverType" />Large &mdash; CPU[四核] Memory[4GB] Disk[20GB]
										</label>
									</div>
								</div>
								
								<div class="page-header"></div>
								
								<div class="control-group">
									<label for="focusedInput" class="control-label">实例数量</label>
									<div class="controls">
										<input type="text"  id="serverCount" name="serverCount" value="${serverCount }" class="input-medium" />
									</div>
								</div>
							</fieldset>

							<fieldset>
								<legend>操作系统</legend>
								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="windows OS" src="./img/windows_logo.png" />
											</div>
											<div class="span4">
											
												<input type="hidden" id="osId" value="1" />
												
												<h3 id="osName">Windwos2003R2</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
									</div>
								</div>

								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="windows OS" src="./img/windows_logo.png" />
											</div>
											<div class="span4">
												<input type="hidden" id="osId" value="2" />
												<h3 id="osName">Windwos2008R2</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
									</div>
								</div>

								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="centOS" src="./img/centos-logo.png" />
											</div>
											<div class="span4">
												<input type="hidden" id="osId" value="3" />
												<h3 id="osName">Centos6.3</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
									</div>
								</div>

								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="centOS" src="./img/centos-logo.png" />
											</div>
											<div class="span4">
												<input type="hidden" id="osId" value="4" />
												<h3 id="osName" >Centos5.6</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
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
											<td>操作系统</td>
											<td id="td_osType"></td>
										</tr>
										<tr>
											<td>计算机资源</td>
											<td id="td_serverType"></td>
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
</body>
</html>
