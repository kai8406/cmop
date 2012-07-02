<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>存储资源申请</title>
	
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
						<li class="active"><a href="#profile1" data-toggle="tab">1.存储资源申请</a></li>
						<li><a href="#profile2" data-toggle="tab">2.存储资源</a></li>
						<li><a href="#profile3" data-toggle="tab">3.提交</a></li>
					</ul>
				</div>
			</div>
			
			<div id="main" class="span10">
			
				<form:form id="inputForm" modelAttribute="apply" action="${ctx}/apply/support/storage/save/${apply.id}" method="post">

					<div class="tab-content">
					
						<!-- 第1步 -->
						<div class="tab-pane fade in active span6 offset1" id="profile1">
							<fieldset>
								<legend>存储资源申请</legend>
								
								<div class="control-group">
									<label class="control-label">存储资源申请主题</label>
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
						<div class="tab-pane fade span7 offset1" id="profile2">

							<fieldset>
								<legend>存储资源</legend>

								<div class="row">

									<div class="span3" id="dataStorage">
										<fieldset>
											<legend>
												<input type="checkbox" id="dataStorageType" name="dataStorageType" value="1" />
												<small>数据存储</small>
											</legend>

											<div class="control-group">
												<label class="control-label">容量空间</label>
												<div class="controls">
												
													<label class="radio"> 
														<input type="radio"	value="20" name="dataSorageSpace" /> 20GB
													</label>
													<label class="radio"> 
														<input type="radio" value="30" name="dataSorageSpace" /> 30GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="50" name="dataSorageSpace" /> 50GB
													</label> 
													<label class="radio">
														<input type="radio" value="100" name="dataSorageSpace" /> 100GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="200" name="dataSorageSpace" /> 200GB
													</label> 
													<label class="radio">
														<input type="radio" value="" name="" />其它 
														<input type="text" value="" class="input-medium" />
													</label>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">Throughput（吞吐量）</label>
													<div class="controls">
														<label class="radio"> 
															<input type="radio" value="1" name="dataStorageThroughput" /> 50 Mbps以内
														</label> 
														<label class="radio"> 
															<input type="radio" value="2" name="dataStorageThroughput" /> 50 Mbps以上
														</label>
													</div>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">IOPS（每秒进行读写（I/O）操作的次数）</label>
													<div class="controls">
														<input type="text" id="dataStorageIops" name="dataStorageIops" value="" class="input-medium" />
													</div>
												</div>

											</div>
										</fieldset>
									</div>
									<div class="span3 offset1" id="businessStorage">
										<fieldset>
											<legend>
												<input type="checkbox" id="businessStorageType" name="businessStorageType" value="2" />
												<small>业务存储</small>
											</legend>

											<div class="control-group">
												<label class="control-label">容量空间</label>
												<div class="controls">
													<label class="radio"> 
														<input type="radio"	value="20" name="businessStorageSpace" /> 20GB
													</label>
													<label class="radio"> 
														<input type="radio" value="30" name="businessStorageSpace" /> 30GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="50" name="businessStorageSpace" /> 50GB
													</label> 
													<label class="radio">
														<input type="radio" value="100" name="businessStorageSpace" /> 100GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="200" name="businessStorageSpace" /> 200GB
													</label> 
													<label class="radio">
														<input type="radio" value="" name="businessStorageSpace" />其它 
														<input type="text" value="" class="input-medium" />
													</label>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">Throughput（吞吐量）</label>
													<div class="controls">
														<label class="radio"> 
															<input type="radio" value="1" name="businessStorageThroughput" /> 50 Mbps以内
														</label> 
														<label class="radio"> 
															<input type="radio" value="2" name="businessStorageThroughput" /> 50 Mbps以上
														</label>
													</div>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">IOPS（每秒进行读写（I/O）操作的次数）</label>
													<div class="controls">
														<input type="text" id="businessStorageIops" name="businessStorageIops" value="" class="input-medium" />
													</div>
												</div>

											</div>
										</fieldset>

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
								<legend>存储资源申请详情</legend>
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
										<tr id="tr_dataStorage" style="display: none;">
											<td>存储资源</td>
											<td id="td_dataStorage"></td>
										</tr>
										<tr id="tr_businessStorage" style="display: none;">
											<td>存储资源</td>
											<td id="td_businessStorage"></td>
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
