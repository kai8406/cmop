<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>审批</title>
<script>
$(document).ready(function() {
	//聚焦指定的Tab
	$("#audit-tab").addClass("active");
});
function setResult(result) {
	//alert(result);
	if (result!="1" && $('#opinion').val()=="") {
		alert("请填写审批意见！");
		return false;
	}
	$('#result').val(result);
	return true;
}
</script>
</head>

<body>
	
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
	<div class="row">
		<div class="span8 offset2">
			<div class="accordion" id="accordion">
				<div class="accordion-group">
					<div class="accordion-heading">
						<div class="accordion-toggle" data-toggle="collapse" href="#collapseOne">申请详细信息</div>
					</div>
					<div id="collapseOne" class="accordion-body collapse">
						<div class="accordion-inner">
							<table class="table table-bordered">
								<colgroup>
									<col class="span2">
									<col class="span6">
								</colgroup>
								<!--
								<thead>
									<tr>
										<th>Tag</th>
										<th>Description</th>
									</tr>
								</thead>
								-->
								<tbody>
									<tr>
										<td>申请人</td>
										<td>${apply.user.name}</td>
									</tr>
									<tr>
										<td>申请时间</td>
										<td>${apply.createTime}</td>
									</tr>

									<tr>
										<td>申请主题</td>
										<td>${apply.title}</td>
									</tr>

									<tr>
										<td>申请起止时间</td>
										<td>${apply.serviceStart} 至 ${apply.serviceEnd}</td>
									</tr>
									<tr>
										<td>申请用途</td>
										<td>${apply.description}</td>
									</tr>
									<tr>
										<td>资源类型</td>
										<td>${apply.resourceType}</td>
									</tr>
									<!-- 
									<tr>
										<td>操作系统</td>
										<td>CentOs5.5 server &mdash; 64 Bit</td>
									</tr>
									<tr>
										<td>计算机资源</td>
										<td><code>服务器类型:</code>Small &mdash; CPU[单核] Memory[1GB]
											Disk[20GB].&nbsp; <code> 数量:</code>2个</td>
									</tr>
									<tr>
										<td>存储资源</td>
										<td><code>存储类型:</code>数据存储.&nbsp; <code>容量空间:</code>200G.&nbsp;
											<code>吞吐量:</code>50 Mbps以内.&nbsp; <code>IOPS:</code>100</td>
									</tr>
									<tr>
										<td>存储资源</td>
										<td><code>存储类型:</code>业务存储.&nbsp; <code>容量空间:</code>200G.&nbsp;
											<code>吞吐量:</code>50 Mbps以内.&nbsp; <code>IOPS:</code>100</td>
									</tr>
									<tr>
										<td>接入服务</td>
										<td><code>账号:</code>vpnAccout&nbsp; <code>使用人:</code>Vpner.&nbsp;
											<code>需要访问主机:</code>127.0.0.1&nbsp; <code>接入方式:</code>VPN</td>
									</tr>
									<tr>
										<td>公网IP申请</td>
										<td><code>接入链路:</code>电信CTC &nbsp; <code>接入速率:</code>100M
											&nbsp;</td>
									</tr>
									<tr>
										<td>开放端口</td>
										<td>telnet-23, &nbsp; http-80 , &nbsp; www-8080 &nbsp;</td>
									</tr>
									<tr>
										<td>域名解析</td>
										<td><code>解析类型:</code>NS &nbsp; <code>解析完整域名:</code>www.test.com
											&nbsp; <code>目标IP地址:</code>198.168.5.2 &nbsp;</td>
									</tr>
									-->
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="accordion-group">
					<div class="accordion-heading">
						<div class="accordion-toggle" data-toggle="collapse" href="#collapseTwo">审核历史</div>
					</div>
					<div id="collapseTwo" class="accordion-body collapse in">
						<div class="accordion-inner">
							<table class="table">
								<colgroup>
									<col class="span2">
									<col class="span5">
								</colgroup>
								<thead>
									<tr>
										<th>审核人</th>
										<th>审核决定</th>
										<th>审核意见</th>
										<th>审核时间</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${auditList}" var="audit">
									<tr>
										<td>${audit.auditFlow.user.name}</td>
										<td>
										<c:if test="${audit.result == 1 }">
										<span class="label label-success">同意</span>
										</c:if>						
										<c:if test="${audit.result == 2 }">
										<span class="label label-important">不同意但继续</span>
										</c:if>
										<c:if test="${audit.result == 3 }">
										<span class="label label-important">不同意且退回</span>
										</c:if>																					
										</td>
										<td>${audit.opinion}</td>
										<td>${audit.createTime}</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			
			<form id="inputForm" action="${ctx}/audit/save" method="post">
				<div class="control-group">
					<div>
						<h3>审批意见</h3>
					</div>
					<div class="controls">
						<textarea rows="2" id="opinion" name="opinion" class="input-xlarge"></textarea>
						<input type="hidden" id="result" name="result"/>
						<input type="hidden" id="result" name="applyId" value="${apply.id}"/>
					</div>
				</div>
				<hr>
				<div class="form-actions">
				
					<c:if test="${ empty result }">
						<button class="btn btn-success" onclick="setResult(1)">同意</button>
						<button class="btn btn-success" onclick="return setResult(2);">不同意但继续</button>
						<button class="btn btn-success" onclick="return setResult(3);">不同意且退回</button>
					</c:if>
					
					<c:if test="${not empty result }">
						<button class="btn btn-success" onclick="return setResult(${result})">保存</button>
						<button class="btn " onclick="window.close();">取消</button>
					</c:if>
				</div>				
			</form>
		</div>
	</div>
</body>
</html>
