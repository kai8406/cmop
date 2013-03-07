<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>申请管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#serviceTag").focus();
			
			
			// 初始化服务开始和结束时间,结束时间默认为开始时间3个月后
			
			$("#serviceStart").val(getDatePlusMonthNum(0));
			$("#serviceEnd").val(getDatePlusMonthNum(3));
			
			$( "#serviceStart" ).datepicker({
				changeMonth: true,
				onClose: function( selectedDate ) {
				$( "#serviceEnd" ).datepicker( "option", "minDate", selectedDate );
				}
			});
			
			$( "#serviceEnd" ).datepicker({
				changeMonth: true,
				onClose: function( selectedDate ) {
				$( "#serviceStart" ).datepicker( "option", "maxDate", selectedDate );
				}
			});
				
			
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
			
		});
	</script>
	
</head>

<body>
	
	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="form-horizontal input-form">
	
		<input type="hidden" name="id" value="${apply.id}">
		
		<fieldset>
			<legend><small>
				<c:choose>
					<c:when test="${not empty apply }">修改服务申请单</c:when>
					<c:otherwise>创建服务申请单</c:otherwise>
				</c:choose>
			</small></legend>
			
			<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
			
			<c:if test="${not empty apply}">
			
				<div class="control-group">
					<label class="control-label" for="title">标题</label>
					<div class="controls">
						<p class="help-inline plain-text">${apply.title}</p>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="createTime">申请日期</label>
					<div class="controls">
						<p class="help-inline plain-text"><fmt:formatDate value="${apply.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></p>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="status">状态</label>
					<div class="controls">
						<p class="help-inline plain-text">
						 <c:forEach var="map" items="${applyStatusMap }">
						 	<c:if test="${map.key == apply.status }">${map.value }</c:if>
						</c:forEach>
						</p>
					</div>
				</div>
				
			</c:if>
			
			<div class="control-group">
				<label class="control-label" for="serviceTag">服务标签</label>
				<div class="controls">
					<input type="text" id="serviceTag" name="serviceTag" value="${apply.serviceTag }" class="required" maxlength="45" placeholder="...服务标签">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="priority">优先级</label>
				<div class="controls">
					<select id="priority" name="priority">
						<c:forEach var="map" items="${priorityMap }">
							<option value="${map.key }" 
								<c:if test="${map.key == apply.priority }">
									selected="selected"
								</c:if>
							>${map.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="serviceStart">服务开始时间</label>
				<div class="controls">
					<input type="text" id="serviceStart" name="serviceStart" value="${apply.serviceStart }" readonly="readonly" class="datepicker required"  placeholder="...服务开始时间">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="serviceEnd">服务结束时间</label>
				<div class="controls">
					<input type="text" id=serviceEnd name="serviceEnd" value="${apply.serviceEnd }" readonly="readonly" class="datepicker required"  placeholder="...服务结束时间">
				</div>
			</div>
		
			<div class="control-group">
				<label class="control-label" for="description">用途描述</label>
				<div class="controls">
					<textarea rows="3" id="description" name="description" placeholder="...用途描述"
						maxlength="500" class="required ">${apply.description }</textarea>
				</div>
			</div>
			
			<dl class="dl-horizontal">
			
				<!-- 实例Compute -->
				<c:if test="${not empty apply.computeItems}">
					<hr>
					<dt>PCS & ECS实例</dt>
					<c:forEach var="item" items="${apply.computeItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>用途信息</em>&nbsp;&nbsp;${item.remark}</dd>
						
						<dd>
							<em>基本信息</em>
							&nbsp;&nbsp;<c:forEach var="map" items="${osTypeMap}"><c:if test="${item.osType == map.key}">${map.value}</c:if></c:forEach>
							&nbsp;&nbsp;<c:forEach var="map" items="${osBitMap}"><c:if test="${item.osBit == map.key}">${map.value}</c:if></c:forEach>
							&nbsp;&nbsp;
							<c:choose>
								<c:when test="${item.computeType == 1}"><c:forEach var="map" items="${pcsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:when>
								<c:otherwise><c:forEach var="map" items="${ecsServerTypeMap}"><c:if test="${item.serverType == map.key}">${map.value}</c:if></c:forEach></c:otherwise>
							</c:choose>
						</dd>
						
						<dd>
							<em>关联ESG</em>&nbsp;&nbsp;${item.networkEsgItem.identifier}(${item.networkEsgItem.description})
							<span class="pull-right">
								<a href="${ctx}/apply/compute/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
								<a href="#deleteComputeModal${item.id}" data-toggle="modal">删除</a>
								<div id="deleteComputeModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
									<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
									<div class="modal-body">是否删除?</div>
									<div class="modal-footer">
										<button class="btn" data-dismiss="modal">关闭</button>
										<a href="${ctx}/apply/compute/delete/${item.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
									</div>
								</div>
							</span>
						</dd>
						
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- 存储空间ES3 -->
				<c:if test="${not empty apply.storageItems}">
					<hr>
					<dt>ES3存储空间</dt>
					<c:forEach var="item" items="${apply.storageItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>存储类型</em>&nbsp;&nbsp;<c:forEach var="map" items="${storageTypeMap}"><c:if test="${item.storageType == map.key}">${map.value}</c:if></c:forEach></dd>
						
						<dd><em>容量空间</em>&nbsp;&nbsp;${item.space}&nbsp;GB</dd>
						
						<dd>
							<em>挂载实例</em>&nbsp;&nbsp;${item.mountComputes}
							<span class="pull-right">
								<a href="${ctx}/apply/es3/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
								<a href="#deleteComputeModal${item.id}" data-toggle="modal">删除</a>
								<div id="deleteComputeModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
									<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
									<div class="modal-body">是否删除?</div>
									<div class="modal-footer">
										<button class="btn" data-dismiss="modal">关闭</button>
										<a href="${ctx}/apply/es3/delete/${item.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
									</div>
								</div>
							</span>
						</dd>
						
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- 负载均衡器ELB -->
				<c:if test="${not empty apply.networkElbItems}">
					<hr>
					<dt>负载均衡器ELB</dt>
					<c:forEach var="item" items="${apply.networkElbItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>是否保持会话</em>&nbsp;<c:forEach var="map" items="${keepSessionMap}"><c:if test="${item.keepSession == map.key }">${map.value}</c:if></c:forEach></dd>
						
						<dd><em>关联实例</em>&nbsp; 
							<c:forEach var="compute" items="${allComputes}">
								<c:if test="${compute.networkElbItem.id == item.id }">${compute.identifier}
									<c:if test="${not empty compute.innerIp }">(${compute.innerIp})</c:if>&nbsp;&nbsp;
								</c:if>
							</c:forEach>
						</dd>
						
						<dd><em>端口映射（协议、源端口、目标端口）</em></dd>
						
						<c:forEach var="port" items="${item.elbPortItems }">
							<dd>&nbsp;&nbsp;${port.protocol}&nbsp;,&nbsp;${port.sourcePort}&nbsp;,&nbsp;${port.targetPort}</dd>
						</c:forEach>
						
						<span class="pull-right">
							<a href="${ctx}/apply/elb/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
							<a href="#deleteComputeModal${item.id}" data-toggle="modal">删除</a>
							<div id="deleteComputeModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
								<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
								<div class="modal-body">是否删除?</div>
								<div class="modal-footer">
									<button class="btn" data-dismiss="modal">关闭</button>
									<a href="${ctx}/apply/elb/delete/${item.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
								</div>
							</div>
						</span>
						
							
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- IP地址EIP -->
				<c:if test="${not empty apply.networkEipItems}">
				
					<hr>
					<dt>EIP</dt>
					<c:forEach var="item" items="${apply.networkEipItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>ISP运营商</em>&nbsp;&nbsp;<c:forEach var="map" items="${ispTypeMap}"><c:if test="${item.ispType == map.key }">${map.value}</c:if></c:forEach></dd>
						
						<dd>
							<c:choose>
								<c:when test="${not empty item.computeItem }"><em>关联实例</em>&nbsp;&nbsp;${item.computeItem.identifier }(${item.computeItem.innerIp })</c:when>
								<c:otherwise><em>关联ELB</em>&nbsp;&nbsp;${item.networkElbItem.identifier }(${item.networkElbItem.virtualIp })</c:otherwise>
							</c:choose>
						</dd>
						
						<dd><em>端口映射（协议、源端口、目标端口）</em></dd>
						
						<c:forEach var="port" items="${item.eipPortItems }">
							<dd>&nbsp;&nbsp;${port.protocol}&nbsp;,&nbsp;${port.sourcePort}&nbsp;,&nbsp;${port.targetPort}</dd>
						</c:forEach>
						
						<span class="pull-right">
							<a href="${ctx}/apply/eip/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
							<a href="#deleteComputeModal${item.id}" data-toggle="modal">删除</a>
							<div id="deleteComputeModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
								<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
								<div class="modal-body">是否删除?</div>
								<div class="modal-footer">
									<button class="btn" data-dismiss="modal">关闭</button>
									<a href="${ctx}/apply/eip/delete/${item.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
								</div>
							</div>
						</span>
							
						<br>
						
					</c:forEach>
				
				</c:if>
				
				<!-- DNS -->
				<c:if test="${not empty apply.networkDnsItems}"></c:if>
				
				<!-- 服务器监控monitorCompute -->
				<c:if test="${not empty apply.monitorComputes}"></c:if>
				
				<!-- ELB监控monitorElb -->
				<c:if test="${not empty apply.monitorElbs}"></c:if>
				
			</dl>
			
			<div class="form-actions">
				<a class="btn" href="${ctx}/apply/">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
