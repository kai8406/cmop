<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>故障申报</title>
<script>
	$(document).ready(function() {
		//聚焦指定的Tab
		$("#fault-tab").addClass("active");

	});
</script>
</head>

<body>
	<div class=" span6 offset2 form-horizontal">

			<legend>故障申报详细信息</legend>
			<dl class="dl-horizontal">

				<dt>申请人:</dt>
				<dd>${fault.user.name}</dd>

				<dt>申请时间:</dt>
				<dd><fmt:formatDate value="${fault.createTime}" pattern ="yyyy-MM-dd HH:mm:ss" /></dd>

				<dt>申请主题:</dt>
				<dd>${fault.title}</dd>

				<dt>优先级:</dt>
				<dd>

					<c:if test="${fault.level == 1}">低
				</c:if>
					<c:if test="${fault.level == 2}">普通
				</c:if>
					<c:if test="${fault.level == 3}">高
				</c:if>
					<c:if test="${fault.level == 4}">紧急
				</c:if>
					<c:if test="${fault.level == 5}">立刻
				</c:if>
				</dd>

				<dt>故障描述:</dt>
				<dd>${fault.description}</dd>


				<dt>Redmine状态:</dt>
				<dd>${redmineStatus}</dd>
			</dl>

		</fieldset>
		<div class="form-actions">
			<input id="cancel" class="btn" type="button" value="返回"
				onclick="history.back()" />
		</div>

	</div>
</body>
</html>
