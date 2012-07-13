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
					<li class="active"><a href="#profile1" data-toggle="tab">1.ES3资源申请</a></li>
					<li><a href="#profile2" data-toggle="tab">2.ES3资源</a></li>
					<li><a href="#profile3" data-toggle="tab">3.提交</a></li>
				</ul>
			</div>
		</div>

		<div id="main" class="span10">

			<form:form id="inputForm" modelAttribute="apply" action="."
				class="form-horizontal" method="post">

				<input type="hidden" name="id" value="${apply.id}" />
				<input type="hidden" name="storageItemId" value="${storageItem.id}" />
				<input type="hidden" name="title" value="${apply.title}" />

				<div class="tab-content">

					<!-- 第1步 -->
					<div class="tab-pane fade in active span6" id="profile1">
						<fieldset>
							<legend>ES3资源申请</legend>

							<div class="control-group">
									<label class="control-label">资源类型</label>
									<div class="controls">
										<c:forEach var="map" items="${resourceTypeMap }">
											<label class="radio"> 
												<input type="radio" value="<c:out value='${map.key}' />" name="resourceType" 
													<c:if test="${apply.resourceType == null && map.key == 1}">checked="checked"</c:if>
													<c:if test="${apply.resourceType == map.key }">checked="checked"</c:if>
												 />  	
												<c:out value="${map.value}" />
											</label>
										</c:forEach>
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

						<hr />
						<div>
							<a id="nextStep" class="btn btn-info">下一步</a>
						</div>
					</div>

					<!-- 第2步 -->
					<div class="tab-pane fade span8" id="profile2">
						<fieldset>
							<legend>存储资源</legend>
							<div class="control-group">
								<label class="control-label">容量空间</label>
								<div class="controls">
								
								
										<c:forEach var="map" items="${storageSpaceMap }">
											<label class="radio"> 
												<input type="radio" value="<c:out value='${map.key}'/>"	name="storageSpace"
													<c:if test="${storageItem.storageSpace == map.key }"> checked="checked" </c:if>
												 />
												<c:out value="${map.value}" />
											</label>
										</c:forEach>
									
									
									  <label class="radio"> 
										  <input type="radio" value=""	id="otherSpace" name="storageSpace"
												<c:if test="${
													storageItem.storageSpace != 20 && 
													storageItem.storageSpace != 30 && 
													storageItem.storageSpace != 50 && 
													storageItem.storageSpace != 100 && 
													storageItem.storageSpace != 200  
												}">
													checked="checked" 
												</c:if>
											 />
										 
											其它 
										
											<input type="text" id="otherSpaceValue"	value="${storageItem.storageSpace}"
												<c:if test="${
													storageItem.storageSpace != 20 &&
													storageItem.storageSpace != 30 &&
													storageItem.storageSpace != 50 &&
													storageItem.storageSpace != 100 &&
													storageItem.storageSpace != 200  
												 }">
												  name="${storageItem.storageSpace}"
											  </c:if>
											class="input-medium" />

									</label>
								</div>
							</div>

						</fieldset>

						<fieldset>
							<legend>挂载ECS</legend>

							<div style="height: 20em; overflow: auto;">

								<table
									class="table table-striped table-bordered table-condensed">
									<colgroup>
										<col class="span2">
										<col class="span2">
										<col class="span4">
									</colgroup>
								
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
												<td>
													<input type="checkbox" name="ids" value="${item.id }"
														<c:if test="${not empty checkedComputeItem }">
															<c:forEach var="checked" items="${checkedComputeItem }">
																<c:if test="${checked[0] == item.id }">checked="checked"</c:if>
															</c:forEach>
														</c:if>
	
													/> ${item.identifier}
												</td>

											<td>
												<c:forEach var="map" items="${osTypeMap}">
													<c:if test="${item.osType == map.key  }">
														<c:out value="${map.value}" />
													</c:if>
												</c:forEach>
												
													 &nbsp;
													 
												 <c:forEach var="map" items="${osBitMap}">
													<c:if test="${item.osBit == map.key  }">
														<c:out value="${map.value}" />
													</c:if>
												</c:forEach>
												 
											</td>
											
											<td>
												<c:forEach var="map" items="${serverTypeMap}">
													<c:if test="${item.serverType == map.key  }">
														<c:out value="${map.value}" />
													</c:if>
												</c:forEach>
											</td>
										</tr>

										</c:forEach>

									</tbody>
								</table>

							</div>

						</fieldset>

						<hr />
						<div>
							<a id="backStep" class="btn">返回</a> <a id="nextStep"
								class="btn btn-info">下一步</a>
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
										<td>资源类型</td>
										<td id="td_resourceType"></td>
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
										<td>容量空间</td>
										<td id="td_storage"></td>
									</tr>

								</tbody>
							</table>
						</fieldset>

						<hr>
						<div>
							<a id="backStep" class="btn ">返回</a>
							<button class="btn btn-primary">保存</button>
						</div>
					</div>

				</div>

			</form:form>
		</div>
	</div>



</body>
</html>
