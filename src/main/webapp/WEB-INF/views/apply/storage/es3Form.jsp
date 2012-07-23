<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>

<head>
<style type="text/css">
.alert {
	margin-bottom: 0px;
	color: black;
	background-color: #fff;
	border: 1px solid #fff;
}
</style>
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
		
		$("#inputForm").validate({
			rules:{	space:"required",resourceType:"required"	}
		});
		/////////////////////
		
		
		//点击增加,动态生成一行 存储空间+挂载按钮
		$("#createES3Mount").click(function(){
			
			var space ;
			
			var storeageTypeId =$("input[name='storeageType']:checked").val();//获得选中radio的值.
			
			//ES3验证
			if($("#otherSpace").is(":checked")){//如果选中"其它"
				
				if($.trim($("#otherSpaceValue").val()) == "" ){//如果输入框为空
					$("#es3MessageBox").modal("toggle");
					return false; 
				}else{
					space = $("#otherSpaceValue").val();
				}
			}else{
				 space = $("input[name='space']:checked").val();//获得选中radio的值.
			}
			
			//插入挂载行
			 str = "<div class='row alert' id='ES3Mount'>";
			 str +="<div class='span1' id='space'>"+space+"&nbsp;GB</div>";
			 str +="<div class='hidden' id='space_Id'>0</div>";
			 str +="<div class='hidden' id='space_value'>"+space+"</div>";
			 str +="<div class='span1' id='storeageType_value'>"+fGetStoreageTypeValueById(storeageTypeId)+"</div>";
			 str +="<div class='hidden' id='storeageType_id'>"+storeageTypeId+"</div>";
			 str +="<div class='span1 '><a id='ES3MountBtn' name='' class='btn btn-warning bES3Tmp' data-toggle='modal' href='#ecsListModal'>挂载</a></div>";
			 str +="<div id='showIdentifier' class='span4'></div>";
			 str +="<div class='hidden' id='checkedES3Id'></div>";
			 str +="<button class='close' data-dismiss='alert'>&times;</button></div>";
			$("#ES3MountList").append(str);
			
		});
		
		
		//点击挂载按钮触发
		$("#ES3MountBtn").live("click",function(){
			
			 $("a.bES3Tmp").attr("name","");//将class为bES3Tmp的a标签name初始化为""
			
			//为了区分存储空间挂载,设置当前点击a标签的name为 sTmp
			$(this).attr("name","sTmp");
			
			//默认清空checkbox
			$("input:checkbox[name='ids']").removeAttr('checked');

		});
		
		
		//点击modal确定按钮
		$("#ecsListModalSave").click(function() {
			
				//清空当前行显示的已挂载实例的标识符(Identifier)
			  $("a.bES3Tmp[name='sTmp']").parent().parent().find("#showIdentifier").empty();
			
				var escId = "";
			//将选中的实例显示在a标签name为bES3Tmp所在的挂载行中.多个实例的话用"/"区分
			  $(this).parent().parent().find("tbody #ids:checked").each(function(){
				  escId += $(this).val()+"/";
				 $("a.bES3Tmp[name='sTmp']").parent().parent().find("#showIdentifier").append( $.trim($(this).parent().parent().find("#esc_identifier").html()) +" , ");
			  });
				  $("a.bES3Tmp[name='sTmp']").parent().parent().find("#checkedES3Id").html(escId);
			  
			  $("a.bES3Tmp").attr("name","");//将class为mountBtn的a标签name 设置为""
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
				<input type="hidden" name="title" value="${apply.title}" />
				<input type="hidden" name="ecsIds" id="ecsIds" value="" />
				<input type="hidden" name="spaces" id="spaces" value="" />
				<input type="hidden" name="spaceIds" id="spaceIds" value="" />
				<input type="hidden" name="storeageTypes" id="storeageTypes" value="" />

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
									<input type="text" id="serviceStart" name="serviceStart" readonly="readonly"
										value="${apply.serviceStart }" class="input-medium" />&mdash;
									<input type="text" id="serviceEnd" name="serviceEnd" readonly="readonly"
										value="${apply.serviceEnd }" class="input-medium" />
								</div>
							</div>

							<div class="control-group">
								<label class="control-label">申请用途</label>
								<div class="controls">
									<textarea rows="2" id="description" name="description" maxlength="100"
										class="input-xlarge required ">${apply.description}</textarea>
								</div>
							</div>

						</fieldset>

						<div class="form-actions">
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
								
										<c:forEach var="map" items="${spaceMap }" varStatus="index" >
											<label class="radio inline	"> 
												<input type="radio" value="<c:out value='${map.key}'/>"	name="space"
													<c:if test="${index.index == 0 }"> checked="checked" </c:if>
													<c:if test="${storageItem.space == map.key }"> checked="checked" </c:if>
												 />
												<c:out value="${map.value}" />
											</label>
										</c:forEach>
									
									
									  <label class="radio inline"> 
										  <input type="radio" value=""	id="otherSpace" name="space"
												<c:if test="${
													storageItem.space != 20 && 
													storageItem.space != 30 && 
													storageItem.space != 50 && 
													storageItem.space != 100 && 
													storageItem.space != null
												}">
													checked="checked" 
												</c:if>
											 />
										 
											其它 
										
											<input type="text" id="otherSpaceValue" name="otherSpaceValue"	value="${storageItem.space}"
												<c:if test="${
													storageItem.space != 20 &&
													storageItem.space != 30 &&
													storageItem.space != 50 &&
													storageItem.space != 100  
												 }">
												  name="${storageItem.space}"
											  </c:if>
											class="input-mini digits" maxlength="5" />
									</label>
									 <a class="btn" id="createES3Mount" >增加</a>
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label">存储类型</label>
								<div class="controls">
									<c:forEach var="map" items="${storeageTypeMap }">
										<label class="radio"> 
											<input type="radio" value="<c:out value='${map.key}'/>"	name="storeageType"
												<c:if test="${ map.key == 1}">checked="checked"</c:if>
											 />
											<c:out value="${map.value}" />
										</label>
									</c:forEach>
								</div>
							</div>
							
						</fieldset>

						<fieldset>
							<legend>挂载ECS</legend>
							<div id="ES3MountList" class="span8">
							
							<c:forEach var="item" items="${checkedComputeItem }">

									<div id="ES3Mount" class="row alert">
										<div id="space" class="span1">${item[2] }&nbsp;GB</div>
										<div class='hidden' id='space_Id'>${item[0]}</div> 
										<div id="space_value" class="hidden">${item[2] }</div>
										
										<div id="storeageType_id" class="hidden">${item[3] }</div>
										<div class="span1" id="storeageType_value">
										
										<c:forEach var="map" items="${storeageTypeMap }">
											
											<c:if test="${ map.key == item[3]}">
												<c:out value="${map.value}" />
											</c:if>
											
										</c:forEach>
										
										
										</div>
										
			 
										<div class="span1 ">
											<a href="#ecsListModal" data-toggle="modal"
												class="btn btn-warning bES3Tmp" name="" id="ES3MountBtn">挂载</a>
										</div>
										<div class="span4" id="showIdentifier">${item[5]}</div>
										<div id="checkedES3Id" class="hidden">${item[4]}/</div>
										<button data-dismiss="alert" class="close">×</button>
									</div>

								</c:forEach>
							
							
							</div>
						</fieldset>

						<div class="form-actions">
							<a id="backStep" class="btn">返回</a> <a id="nextStep" 
								class="btn btn-info">下一步</a>
						</div>
					</div>


					<!-- 第3步 -->
					<div class="tab-pane fade span8" id="profile3">
						<fieldset>
							<legend>ES3资源申请详情</legend>
							
							 <dl class="dl-horizontal">
									<dt>申请类型:</dt>
									<dd>ECS申请</dd>
					
									<dt>资源类型:</dt>
									<dd id="dd_resourceType"></dd>
									
									<dt>申请起始时间:</dt>
									<dd id="dd_time"></dd>
									
									<dt>申请用途:</dt>
									<dd id="dd_description"></dd>
									
							 
									<div id="mountDetail"></div>
									
								</dl>
							 
							 
						</fieldset>
						
						
						<div class="form-actions">
							<a id="backStep" class="btn ">返回</a>
							<button class="btn btn-primary">保存</button>
						</div>
			
					</div>

				</div>

			</form:form>
		</div>
	</div>
	
	<div id="ecsListModal" class="modal hide fade form-horizontal ">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3>实例列表</h3>
			</div>
			<div class="modal-body">
								<table
									class="table table-striped table-bordered table-condensed">
									<colgroup>
										<col class="span1">
										<col class="span1">
										<col class="span3">
										<col class="span3">
									</colgroup>
								
									<thead>
										<tr>
											<th>#</th>
											<th>标识符</th>
											<th>操作系统</th>
											<th>ECS规格</th>
										</tr>
									</thead>
									<tbody>

										<c:forEach var="item" items="${AllComputeItem }">

											<tr>
												<td>
													<input type="checkbox" id="ids" name="ids" value="${item.id }" />  
												</td>
												<td id="esc_identifier">${item.identifier}</td>
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
			<div class="modal-footer">
				<a href="#" class="btn" data-dismiss="modal">取消</a> <a href="#"
					id="ecsListModalSave" data-dismiss="modal" name="" class="btn btn-primary">确定</a>
			</div>
		</div>
		
		
		<div id="es3MessageBox" class="modal hide  form-horizontal">
			<div class="modal-body">请输入容量空间.</div>
			<div class="modal-footer">
				<a href="#" class="btn" data-dismiss="modal">确定</a> 
			</div>
		</div>


</body>
</html>
