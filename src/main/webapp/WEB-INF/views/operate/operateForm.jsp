<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>工单管理管理</title>
	
<script>
$(document).ready(function() {
	$("ul#navbar li#operate").addClass("active");
	
	$("#inputForm").validate({
		errorClass: "help-inline",
		errorElement: "span",
		highlight:function(element, errorClass, validClass) {
	   		$(element).closest('.control-group').addClass('error');
		},
		unhighlight: function(element, errorClass, validClass) {
			$(element).closest('.control-group').removeClass('error');
		}
	});
	
	$('.datepicker').datepicker();

	//IP改变事件
    $("select[name=ipPool]").change(function(){
    	var ip = $(this).val();
    	if (ip!="") {
        	var isUsed = false;
        	$("#updateDiv #osStorageDiv").each(function () {
        		if ($(this).find("#innerIp").val()==ip) {
        			isUsed = true;
        		}
        	});
        	if (isUsed) {
        		alert("您选择的IP已被选用，请重新选择！");
        	} else {
    	    	$(this).parent().find("#innerIp").val(ip);
        	}        	
        }
    });
});

function checkValid() {
	//alert($('#issueId').val()+","+$('#projectId').val()+","+$('#priority').val()+","+ $('#assignTo').val()+","+ $('#dueDate').val()+","+$('#estimatedHours').val()+","+$('#doneRatio').val()+","+$('#note').val());
	if ($('#estimatedHours').val()=="" || $('#estimatedHours').val()=="0") { 
        alert("Estimated time不能为空且应大于0！");
        $('#estimatedHours').focus();
        return false;
    }
	if ($('#note').val()=="") { 
        alert("Description不能为空！");
        $('#note').focus();
        return false;
    }	
	//if ($('#estimatedHours').val()<${issue.estimatedHours}) {
	//	alert("估计耗时不能小于当前值！");
    //    return false;
	//}
	//alert('${issue.startDate}'+","+$('#dueDate').val());
	var startDate=Date.parse($('#startDate').val().replace(/-/g,"/"));
	var dueDate=Date.parse($('#dueDate').val().replace(/-/g,"/"));
	//alert(startDate+","+dueDate);
	var today = new Date();
	var year = today.getFullYear(); 
    var month = today.getMonth() + 1;   
    if(month<10) month = "0"+month;
    var day = today.getDate(); 
	if(day<10) day = "0"+day; 
    var dateNow=Date.parse((year+"-"+month+"-"+day).replace(/-/g,"/"));
    if(dateNow>dueDate) {
		alert("Due date不能小于当前日期！");
		$('#dueDate').focus();
		return false;
	}
	if(startDate>dueDate) {
		alert("Due date不能小于Start date！");
		$('#dueDate').focus();
		return false;
	}

	//增加只有余波才能选择100%完成的限制
	//alert('${user.redmineUserId}');
	if (($('#operator').val()==null && '${user.redmineUserId}'!='${issue.assignee.id}' && $('#doneRatio').val()==100) || 
		($('#operator').val()!=null && '${user.redmineUserId}'!=$('#operator').val() && $('#doneRatio').val()==100)) {
		alert("您不是第一接收人，完成率不能选择100%！");
		$('#doneRatio').focus();
        return false;
	}
	if (parseInt($('#doneRatio').val())<parseInt('${issue.doneRatio}')) {
		alert("完成率不能小于当前值！");
		$('#doneRatio').focus();
        return false;
	}
	
	//拼装计算和存储资源相关属性
	var computes="",storages="",hostNames="",serverAlias="",osStorageAlias="",controllerAlias="",volumes="",sep=",";
	var innerIps="",eipIds="",eipAddresss="";
	$("#updateDiv #computeDiv").each(function () {
		computes = computes+$(this).find("#computeId").val()+sep;
		hostNames = hostNames+$(this).find("#hostName").val()+sep;
		serverAlias = serverAlias+$(this).find("#server").val()+sep;
    });
    var flag = true;
	$("#updateDiv #osStorageDiv").each(function () {
		osStorageAlias = osStorageAlias+$(this).find("#osStorage").val()+sep;
		innerIps = innerIps+$(this).find("#innerIp").val()+sep;
		if ($(this).find("#innerIp").val()=="") {
			flag = false;
		}
    });
	/*
    if (!flag) {
    	alert("计算资源的内网IP不能为空！");
    	return false;
    }*/
	$("#updateDiv #storageDiv").each(function () {
		storages = storages+$(this).find("#storageId").val()+sep;
		controllerAlias = controllerAlias+$(this).find("#controller").val()+sep;
		volumes = volumes+$(this).find("#volume").val()+" "+sep;
    });
	$("#updateDiv #eipDiv").each(function () {
		eipIds = eipIds+$(this).find("#eipId").val()+sep;
		eipAddresss = eipAddresss+$(this).find("#eipAddress").val()+sep;
    });
	$('#computes').val(computes);
	$('#storages').val(storages);
	$('#hostNames').val(hostNames);
	$('#serverAlias').val(serverAlias);
	$('#osStorageAlias').val(osStorageAlias);
	$('#controllerAlias').val(controllerAlias);
	$('#volumes').val(volumes);
	$('#innerIps').val(innerIps);
	$('#eipIds').val(eipIds);
	$('#eipAddresss').val(eipAddresss);
	if ($('#location').length>0) {
		$('#locationAlias').val($('#location').val());
	}
	//if (${isShow} && $('#hostNames').val()=="") {
	//	alert("主机名不能为空！");
	//	$('#hostNames').focus();
    //    return false;
	//}
	//alert($('#computes').val());
	//alert($('#storages').val());
	//alert($('#hostNames').val());
	//alert($('#serverAlias').val());
	//alert($('#osStorageAlias').val());
	//alert($('#controllerAlias').val());
	//alert($('#volumes').val());
	//alert($('#innerIps').val());
	//alert($('#eipIds').val());
	//alert($('#eipAddresss').val());
	//alert($('#locationAlias').val());
	//return false;

	$("#updateBtn").click(function(){
		$(this).modalmanager('loading');
	});
}

function changeLocation(){
	$.ajax({
        type: "GET",
        url: "${ctx}/ajax/getVlanByLocationAlias?locationAlias=" + $('#location').val(),
       	dataType: "json",
        success: function (data) {
        	$("#vlan").empty();
        	var html = "";
        	for (var key in data) {  
        		html += ("<option value='"+key+"'>"+data[key]+"</option>");
       	    }
        	 $("#vlan").append(html);
        	changeVlan();
        }
    });
}

function changeVlan(){
	$.ajax({
        type: "GET",
       	url: "${ctx}/ajax/getIpPoolByVlan?vlanAlias=" + $("#vlan").val(),
       	dataType: "json",
        success: function (data) {
        	var html = "<option value=''></option>";
        	for (var i = 0; i < data.length; i++) {  
       	        html += "<option value='"+data[i].ipAddress+"'>"+data[i].ipAddress+"</option>";
       	    }
        	$("#updateDiv #osStorageDiv").each(function () {
        		$(this).find("#ipPool").empty();
        		$(this).find("#ipPool").append(html);
        	});
        }
    });
}

function changeServer(obj){
	var server = $(obj).val();
	$.ajax({
        type: "GET",
       	url: "${ctx}/ajax/checkServerIsUsed?serverAlias=" + server,
       	dataType: "text",
        success: function (data) {
            if (data.length>0) {
            	alert("您选择的Server已被选用，请重新选择！");
            }
        }
    });	
}
</script>
</head>

<body>
	
	<form id="inputForm" action="." method="post" class="form-horizontal input-form" style="max-width: 800px">
		<input type="hidden" id="issueId" name="issueId" value="${issue.id}"/>
		<input type="hidden" id="authorId" name="authorId" value="${redmineIssue.assignee}"/>
		<input type="hidden" id="startDate" value='<fmt:formatDate value="${issue.startDate}" pattern="yyyy-MM-dd"/>'/>
		
		<div class="accordion-group">
			<div class="accordion-heading">
				<div class="accordion-toggle" data-toggle="collapse" href="#collapseOne"><b>工单信息</b></div>
			</div>
			<div id="collapseOne" class="accordion-body collapse in" style="padding-bottom: 10px;">
				<div class="accordion-inner">
					<div class="row-fluid">
					    <div class="row-fluid" style="padding-bottom: 5px;">
							<div class="span8"><em>Subject:</em>&nbsp;${issue.project.name}>>${issue.subject}</p></div>
						</div>
					    <div class="row-fluid" style="padding-bottom: 5px;">
							<div class="span4"><em>Priority:</em>&nbsp;${issue.priorityText}</div>
							<div class="span4"><em>Due Date:</em>&nbsp;<fmt:formatDate value="${issue.dueDate}" pattern="yyyy-MM-dd"/></div>	
						</div>
					    <div class="row-fluid" style="padding-bottom: 5px;">
							<div class="span4"><em>Status:</em>&nbsp;${issue.statusName}</div>
							<div class="span4"><em>Start Date:</em>&nbsp;<fmt:formatDate value="${issue.startDate}" pattern="yyyy-MM-dd"/></div>	
						</div>
					    <div class="row-fluid" style="padding-bottom: 5px;">
							<div class="span4"><em>Tracker:</em>&nbsp;${issue.tracker.name}</div>
							<div class="span4"><em>Done Ratio:</em>&nbsp;${issue.doneRatio}%</div>	
						</div>
					    <div class="row-fluid">
							<div class="span8"><em>详细描述:</em></div>	
						</div>
					    <div class="row-fluid" >
							<div class="span12">${issue.description}</div>	
						</div>
					</div>		
				</div>
			</div>
		</div>

		<div class="accordion-group">
			<div class="accordion-heading">
				<div class="accordion-toggle" data-toggle="collapse" href="#collapseTwo"><b>操作历史</b></div>
			</div>
			<div id="collapseTwo" class="accordion-body collapse in" style="padding-bottom: 0px;">
				<div class="accordion-inner">
				    <div class="span8">
			    		<c:forEach var="journal" items="${issue.journals}" varStatus="status">
			    			<input type="hidden" id="operator" name="operator" value="${journal.user.id}"/>
				    		<p class="help-inline plain-text span8" style="margin-left: 0px;">
								<strong>#${status.index+1} Updated by:</strong>&nbsp;${journal.user}&nbsp;&nbsp;<fmt:formatDate value="${journal.createdOn}" pattern="yyyy-MM-dd HH:mm:ss" />
				    			<label class="span7" style="margin-bottom: 0px; margin-left: 25px;">描述：${journal.notes}</label><br>
				    			<ul>
									<c:forEach var="detail" items="${journal.details}">
		   								<li>
		   									${detail.name}:&nbsp;
			   								<c:if test="${detail.name=='status_id'}">
												<c:forEach var="map" items="${operateStatusMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach> &#8594; 
												<c:forEach var="map" items="${operateStatusMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
											</c:if>
											<c:if test="${detail.name=='assigned_to_id'}">
												<c:forEach var="map" items="${assigneeMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach>&#8594;
												<c:forEach var="map" items="${assigneeMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
											</c:if>
											<c:if test="${detail.name=='priority_id'}">
												<c:forEach var="map" items="${priorityMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach>&#8594;
												<c:forEach var="map" items="${priorityMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
											</c:if>
											<c:if test="${detail.name=='project_id'}">
												<c:forEach var="map" items="${projectMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach>&#8594;
												<c:forEach var="map" items="${projectMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
											</c:if>
											<c:if test="${detail.name=='estimated_hours'}">
												<c:choose><c:when test="${not empty detail.oldValue }">detail.oldValue</c:when><c:otherwise>0.00</c:otherwise></c:choose>
											</c:if>
											<c:if test="${detail.name!='priority_id' && detail.name!='assigned_to_id' && detail.name!='project_id' && detail.name!='status_id'}">
												${detail.oldValue}&#8594;${detail.newValue}
											</c:if>
										</li>
									</c:forEach>
								</ul>
					    	</p>
				    	</c:forEach>
				    </div>
				 </div>
			</div>
		</div>	
		    
		<div class="accordion-group">
			<div class="accordion-heading">
				<div class="accordion-toggle" data-toggle="collapse" href="#collapseThree"><b>操作</b></div>
			</div>
			<div id="collapseThree" class="accordion-body collapse in" style="padding-bottom: 0px;">
				<div class="accordion-inner" id="updateDiv">		    
			    	<div class="row-fluid">
					    <div class="row-fluid" style="padding-bottom: 5px;">
							<div class="span2">Priority</div>
							<div class="span4">
								<select id="priority" name="priority" class="required">
									<c:forEach var="map" items="${priorityMap}">
										<option value="<c:out value='${map.key}' />" 
											<c:if test="${issue.priorityId==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
										</option>
									</c:forEach>
								</select>						
							</div>
							<div class="span2">Project</div>
							<div class="span4">
								<select id="projectId" name="projectId" class="required">
									<c:forEach var="map" items="${projectMap}">
										<option value="<c:out value='${map.key}' />" 
											<c:if test="${issue.project.id==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
										</option>
									</c:forEach>
								</select>				
							</div>
						</div>	 
					    <div class="row-fluid" style="padding-bottom: 5px;">
							<div class="span2">Assignee</div>
							<div class="span4">
								<select id="assignTo" name="assignTo" class="required">
									<c:forEach var="map" items="${assigneeMap}">
										<option value="<c:out value='${map.key}' />" 
											<c:if test="${issue.assignee.id==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
										</option>
									</c:forEach>
								</select>						
							</div>				    
							<div class="span2">Done Ratio</div>
							<div class="span4">
								<select id="doneRatio" name="doneRatio" class="required">
									<c:forEach var="map" items="${doneRatioMap}">
										<option value="<c:out value='${map.key}' />" 
											<c:if test="${issue.doneRatio==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
										</option>
									</c:forEach>
								</select>					
							</div>
						</div>	 					
					    <div class="row-fluid" style="padding-bottom: 5px;">
							<div class="span2">Due Date</div>
							<div class="span4">
								<input type="text" id="dueDate" name="dueDate" readonly="readonly" class="datepicker required" value="<fmt:formatDate value="${issue.dueDate}" pattern="yyyy-MM-dd"/>">			
							</div>
							<div class="span2">Estimated time</div>
							<div class="span4">
								<input type="text" id="estimatedHours" name="estimatedHours" value="${issue.estimatedHours}" class="required number" min="0" placeholder="...预计完成所需时间" style="width: 212px;">				
							</div>
						</div>	 
						<div class="row-fluid">	
							<div class="span2">Note</div>
							<div class="span10">
								<textarea rows="3" id="note" name="note" class="required input-large" placeholder="...详细的操作描述" style="width: 600px;"></textarea>			
							</div>
						</div>						
					
						<!-- 下面是写入OneCMDB时需要人工选择填入的关联项，暂时只考虑服务申请时才有 -->
						<input type="hidden" id="computes" name="computes"/>
						<input type="hidden" id="storages" name="storages"/>
						<input type="hidden" id="hostNames" name="hostNames"/>
						<input type="hidden" id="serverAlias" name="serverAlias"/>
						<input type="hidden" id="osStorageAlias" name="osStorageAlias"/>
						<input type="hidden" id="controllerAlias" name="controllerAlias"/>
						<input type="hidden" id="volumes" name="volumes"/>
						<input type="hidden" id="innerIps" name="innerIps"/>
						<input type="hidden" id="eipIds" name="eipIds"/>
						<input type="hidden" id="eipAddresss" name="eipAddresss"/>
						<input type="hidden" id="locationAlias" name="locationAlias"/>
						
					    <c:if test="${not empty computeList || not empty networkEipList}">
					    	<div class="row-fluid" style="padding-bottom: 5px; padding-top: 5px;">
								<div class="span2">IDC和VLAN</div>
								<div class="span10" id="locationDiv">
									<select id="location" name="location" style="width:104px" onchange="changeLocation()">
										<c:forEach var="map" items="${location}">
											<option value="<c:out value='${map.key}' />" 
												<c:if test="${map.key=='Location-2'}">selected="selected"</c:if>><c:out value="${map.value}" />
											</option>
										</c:forEach>
									</select>
									<select id="vlan" name="vlan" class="input-small" onchange="changeVlan()">
										<option value=""></option>
										<c:forEach var="map" items="${vlan}">
											<option value="<c:out value='${map.key}' />"><c:out value="${map.value}" /></option>
										</c:forEach>
									</select>				
								</div>
							</div>	
						</c:if>	
						
						<c:forEach var="compute" items="${computeList}">
							<div class="row-fluid" style="padding-bottom: 5px;">					
								<div class="span2">${compute.identifier}</div>
								<div class="span5" id="computeDiv">
									<input type="hidden" id="computeId" name="computeId" value="${compute.id}"/>
									<input type="text" id="hostName" name="hostName" value="${compute.hostName}" class="input-small" placeholder="Host Name">
									<c:if test="${compute.computeType==1}">
										<select id="server" name="server" class="input-small" onchange="changeServer(this)">
											<c:forEach var="map" items="${server}">
												<option value="<c:out value='${map.key}' />" 
													<c:if test="${compute.serverAlias==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
												</option>
											</c:forEach>
										</select> &nbsp;Server    
									</c:if>
									<c:if test="${compute.computeType==2}">
										<select id="server" name="server" class="input-small">
											<c:forEach var="map" items="${hostServer}">
												<option value="<c:out value='${map.key}' />" 
													<c:if test="${compute.hostServerAlias==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
												</option>
											</c:forEach>
										</select> &nbsp;Host Server
									</c:if>
								</div>
								<div class="span5" id="osStorageDiv">
									<input type="text" id="innerIp" name="innerIp" readonly="readonly" value="${compute.innerIp}" class="input-small" placeholder="内网IP">
									<select id="ipPool" name="ipPool" class="input-small">
									</select>
									<c:if test="${compute.computeType==1}">
								    	<input type="hidden" id="osStorage" name="osStorage"/>
								    </c:if>
								    <c:if test="${compute.computeType==2}">
										<select id="osStorage" name="osStorage" class="input-small" >
											<c:forEach var="map" items="${osStorage}">
												<option value="<c:out value='${map.key}' />" 
													<c:if test="${compute.osStorageAlias==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
												</option>
											</c:forEach>
										</select>
									</c:if>
								</div>
							</div>
						</c:forEach>
							
						<c:forEach var="storage" items="${storageList}">
							<div class="row-fluid" style="padding-bottom: 5px;" id="storageDiv">
								<div class="span2">${storage.identifier}</div>
								<div class="span5">
							    	<input type="hidden" id="storageId" name="storageId" value="${storage.id}"/>
							    	<c:if test="${storage.storageType==1}">
										<select id="controller" name="controller" style="width: 197px;">
											<c:forEach var="map" items="${fimasController}">
												<option value="<c:out value='${map.key}' />" 
													<c:if test="${storage.controllerAlias==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
												</option>
											</c:forEach>
										</select> &nbsp;Fimas
									</c:if>					 
									<c:if test="${storage.storageType==2}"> 
										<select id="controller" name="controller" style="width: 197px;">
											<c:forEach var="map" items="${netappController}">
												<option value="<c:out value='${map.key}' />" 
													<c:if test="${storage.controllerAlias==map.key}">selected="selected"</c:if>><c:out value="${map.value}" />
												</option>
											</c:forEach>
										</select> &nbsp;Netapp
									</c:if>	  
								</div>
								<div class="span5">
									<input type="text" id="volume" name="volume" value="${storage.volume}" style="width: 185px;" placeholder="Volume">					    
								</div>
							</div>
						</c:forEach>
							
						<c:forEach var="eip" items="${eipList}">
							<div class="row-fluid" style="padding-bottom: 5px;">
								<div class="span2">${eip.identifier}</div>
								<div class="span10" id="eipDiv">
							    	<input type="hidden" id="eipId" name="eipId" value="${eip.id}"/>
							    	<c:if test="${eip.ispType==1}">
										<select id="eipAddress" name="eipAddress" style="width:104px">
											<c:forEach var="map" items="${telecomIpPool}">
												<option value="<c:out value='${map.ipAddress}' />"
													<c:if test="${eip.ipAddress==map.ipAddress}">selected="selected"</c:if>><c:out value="${map.ipAddress}" />
												</option>
											</c:forEach>
										</select>
									</c:if>					 
									<c:if test="${eip.ispType==2}"> 
										<select id="eipAddress" name="eipAddress" style="width:104px">
											<c:forEach var="map" items="${telecomIpPool}">
												<option value="<c:out value='${map.ipAddress}' />" 
													<c:if test="${eip.ipAddress==map.ipAddress}">selected="selected"</c:if>><c:out value="${map.ipAddress}" />
												</option>
											</c:forEach>
										</select>
									</c:if>
									<c:forEach var="map" items="${ispTypeMap}">
											<c:if test="${eip.ispType==map.key}"><c:out value="${map.value}" /></c:if>
									</c:forEach>
								</div>
							</div>
						</c:forEach>
							
						<!-- ELB 暂时不处理 -->
					</div>

					<div class="row-fluid">	
						<div class="form-actions">
							<input id="cancel" class="btn" type="button" value="返回" onclick="history.back()"/>
							<c:if test="${issue!=null && issue.doneRatio!=100 && issue.assignee.id==user.redmineUserId}">
								<button id="updateBtn" class="btn btn-info" onclick="return checkValid();" style="margin-left: 30px;">&nbsp;提&nbsp;交&nbsp;</button>
							</c:if>
						</div>
					</div>					
				</div>
			</div>
		</div>
		
	</form>
	
</body>
</html>
