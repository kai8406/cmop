<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>
<title>审批管理</title>
<script>
$(document).ready(function() {
	$("ul#navbar li#resourceAudit").addClass("active");
});

function setResult(result) {
    if (result != "1" && $('#opinion').val() == "") {
        alert("请填写审批意见！");
        $('#opinion').focus();
        return false;
    }
    $('#result').val(result);
    return true;
}
</script>
</head>

<body>

<style>body{background-color: #f5f5f5;}</style>

	<form action="#" method="post" class="form-horizontal input-form">
		<input type="hidden" name="id" value="${apply.id}">
		<input type="hidden" name="userId" value="${userId}">
		<c:if test="${empty result}">
			<input type="hidden" name="result" id="result">
		</c:if>
		
		<fieldset>
			<legend><small>资源变更详情</small></legend>
			
			<dl class="dl-horizontal">
				<dt>标签名</dt>
				<dd>${serviceTag.name}&nbsp;</dd>
				
				<dt>状态</dt>
				<dd>
					<c:forEach var="map" items="${resourcesStatusMap }">
					 	<c:if test="${map.key == serviceTag.status }">${map.value }</c:if>
					</c:forEach>&nbsp;
				</dd>
				
				<dt>优先级</dt>
				<dd>
					<c:forEach var="map" items="${priorityMap }">
					 	<c:if test="${map.key == serviceTag.priority }">${map.value }</c:if>
					</c:forEach>&nbsp;
				</dd>
				
				<dt>服务开始时间</dt>
				<dd>${serviceTag.serviceStart}&nbsp;</dd>
				
				<dt>服务结束时间</dt>
				<dd>${serviceTag.serviceEnd}&nbsp;</dd>
				
				<dt>用途描述</dt>
				<dd>${serviceTag.description}&nbsp;</dd>
				
				<dt>创建日期</dt>
				<dd><fmt:formatDate value="${serviceTag.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />&nbsp;</dd>
				</dl>
		
				<c:forEach var="resource" items="${resourcesList }">
					<c:forEach var="change" items="${resource.changes }">
						<p>变更资源标识符&nbsp;<strong>${resource.serviceIdentifier}<c:if test="${resource.ipAddress != DEFAULT_IPADDRESS }">(${ resource.ipAddress })</c:if></strong> &nbsp;&nbsp; 变更描述&nbsp;${change.description }</p>
						<table class="table table-bordered">
				            <thead><tr><th class="span2">变更项</th><th class="span2">旧值</th><th class="span2">新值</th></tr></thead>
				            <tbody>
					            <c:forEach var="item" items="${change.changeItems }">
									<tr>
										<td>${item.fieldName}</td>
										<td class="is-hidden">${item.oldString}</td>
										<td class="is-visible">${item.newString}</td>
									</tr>
								</c:forEach>
				            </tbody>
			            </table>
					</c:forEach>
					<hr>
				</c:forEach>
			</dl>
			
			<hr>
			
			<!-- 审批意见 -->
			
			<c:if test="${not empty audits  }">
				<table class="table">
					<thead>
						<tr>
							<th>审批人</th>
							<th>审批决定</th>
							<th>审批意见</th>
							<th>审批时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${audits}" var="item">
							<c:if test="${item.status != 0}">	
							<tr>
								<td>${item.auditFlow.user.name}</td>
								<td>
									<c:forEach var="map" items="${auditResultMap}">
										<c:if test="${item.result == map.key}">
											<span class="label label-success">${map.value }</span>
										</c:if>
									</c:forEach>
								</td>
								<td>${item.opinion}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy年MM月dd日  HH时mm分ss秒" /></td>
							</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
			
			<form id="inputForm" action="${ctx}/audit/apply/${apply.id}" method="post">
				<c:if test="${empty view}">
					<div class="control-group">
						<label class="control-label" for="opinion">审批意见</label>
						<div class="controls">
							<textarea rows="3" id="opinion" name="opinion" placeholder="...审批意见" maxlength="45" class="required"></textarea>
						</div>
					</div>
				</c:if>
			
				<div class="form-actions">
					<c:if test="${empty result}">
						<input class="btn" type="button" value="返回" onclick="history.back()">
						<c:if test="${empty view}">
							<c:forEach var="map" items="${auditResultMap}">
								<button class="btn btn-primary" onclick="return setResult('${map.key}')">${map.value}</button>
							</c:forEach>
						</c:if>
					</c:if>

					<c:if test="${not empty result}">
						<button class="btn" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</button>
						<c:forEach var="map" items="${auditResultMap}">
							<c:if test="${result==map.key}">
								<button class="btn btn-primary" onclick="return setResult('${map.key}')">${map.value}</button>
							</c:if>
						</c:forEach>
					</c:if>
				</div>
			</form>
		
		</fieldset>
	</form>
	
</body>
</html>
