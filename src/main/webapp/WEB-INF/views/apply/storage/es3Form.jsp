<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>

<head>
<title>ES3服务申请</title>

<script>
	$(document).ready(function() {

		//聚焦指定的Tab
		$("#support-tab").addClass("active");

		switchTab();

		//起始时间
		$("#serviceStart").datepicker();
		$("#serviceEnd").datepicker();

		$("#serviceStart").val(getDateByMonthNum(0)); // 一个月后的日期
		$("#serviceEnd").val(getDateByMonthNum(1)); // 一个月后的日期

		inputServiceDate();

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

			<form:form id="inputForm" modelAttribute="storageItem" action="."
				method="post">


				<div class="tab-content">

					<!-- 第1步 -->
					<div class="tab-pane fade in active span6" id="profile1">
						<fieldset>
							<legend>ES3资源申请</legend>

							<div class="control-group">
								<label class="control-label">资源类型</label>
								<div class="controls">
									<label class="radio"> <input type="radio" value="1"
										name="resourceType"
										<c:if test="${apply.resourceType == 1 }">
												checked="checked" 
											</c:if> />生产资源
									</label> <label class="radio"> <input type="radio" value=" 2"
										name="resourceType"
										<c:if test="${apply.resourceType == 2 }">
												checked="checked" 
											</c:if> />
										测试/演示资源
									</label> <label class="radio"> <input type="radio" value="3"
										name="resourceType"
										<c:if test="${apply.resourceType == 3 }">
												checked="checked" 
											</c:if> />
										公测资源
									</label>
								</div>
							</div>


							<div class="control-group">
								<label class="control-label">申请起始时间</label>
								<div class="controls">
									<input type="text" id="serviceStart" name="serviceStart"
										value="${apply.serviceStart }" class="input-medium" />&mdash;
									<input type="text" id="serviceEnd" name="serviceEnd"
										value="${apply.serviceEnd }" class="input-medium" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">申请用途</label>
								<div class="controls">
									<textarea rows="2" id="description" name="description"
										class="input-xlarge">${apply.description}</textarea>
								</div>
							</div>

						</fieldset>

						<div class="form-actions">
							<a id="nextStep" class="btn btn-primary">下一步</a>
						</div>
					</div>

					<!-- 第2步 -->
					<div class="tab-pane fade span8" id="profile2">
						<fieldset>
							<legend>存储资源</legend>
							<div class="control-group">
								<label class="control-label">容量空间</label>
								<div class="controls">
									<label class="radio"> <input type="radio" value="20"
										name="storageSpace" /> 20GB
									</label> <label class="radio"> <input type="radio" value="30"
										name="storageSpace" /> 30GB
									</label> <label class="radio"> <input type="radio" value="50"
										name="storageSpace" /> 50GB
									</label> <label class="radio"> <input type="radio" value="100"
										name="storageSpace" /> 100GB
									</label> <label class="radio"> <input type="radio" value="200"
										name="storageSpace" /> 200GB
									</label> <label class="radio"> <input type="radio" value=""
										id="otherSpace" name="storageSpace" />其它 <input type="text"
										id="otherSpaceValue" value="" class="input-medium" />
									</label>
								</div>
							</div>

							<a class="btn btn-warning" data-toggle="modal" href="#es3Modal">选择挂靠实例</a>

							<div id="es3Modal" class="modal hide fade form-horizontal ">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h3>实例列表</h3>
								</div>
								<div class="modal-body">

									<table
										class="table table-striped table-bordered table-condensed">
										<thead>
											<tr>
												<th>标识符</th>
												<th>操作系统</th>
												<th>ECS规格</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="item" items="${AllComputeItem }">
												<tr>
													<td><input type="checkbox" name="ids"
														value="${item.id }" /> ${item.identifier}</td>
													<td><c:if test="${item.osType == 1}">Windwos2003R2
							</c:if> <c:if test="${item.osType == 2}">Windwos2008R2
							</c:if> <c:if test="${item.osType == 3}">Centos5.6
							</c:if> <c:if test="${item.osType == 4}">Centos6.3
							</c:if> &nbsp; <c:if test="${item.osBit == 1}">
								32 Bit
							</c:if> <c:if test="${item.osBit ==2}">
								64 Bit
							</c:if></td>
													<td><c:if test="${item.serverType == 1}">Small 
							</c:if> <c:if test="${item.serverType == 2}">Middle
							</c:if> <c:if test="${item.serverType == 3}">Large 
							</c:if></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>

								</div>
								<div class="modal-footer">
									<a href="#" class="btn" data-dismiss="modal">取消</a> <a href="#"
										id="modalSave" data-dismiss="modal" class="btn btn-primary">确定</a>
								</div>
							</div>



						</fieldset>

						<div class="form-actions">
							<a id="backStep" class="btn">返回</a> <a id="nextStep"
								class="btn btn-primary">下一步</a>
						</div>
					</div>


					<!-- 第3步 -->
					<div class="tab-pane fade span8" id="profile3">
						<fieldset>
							<legend>ES3资源申请详情</legend>
							<table class="table table-bordered table-striped" id="formDetail">
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
									<tr>
										<td>容量空间</td>
										<td id="td_storage"></td>
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
