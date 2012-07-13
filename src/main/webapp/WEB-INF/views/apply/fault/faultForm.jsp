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
	<form:form id="inputForm" modelAttribute="fault" action="."
		class="form-horizontal" method="post">
		<div class="tab-content">
			<div class="tab-pane fade in active span6 offset2">
				<fieldset>
					<legend>创建故障申报</legend>
					<div class="control-group">
						<label class="control-label">主题</label>
						<div class="controls">
							<input type="text" id="title" name="title" value="${title}"
								class="input-large" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">优先级</label>
						<div class="controls">
							<select id="level" name="level" class="input-medium">
								<option value="1">低</option>
								<option value="2">普通</option>
								<option value="3" selected>高</option>
								<option value="4">紧急</option>
								<option value="5">立刻</option>
							</select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">故障描述</label>
						<div class="controls">
							<textarea rows="2" id="description" name="description"
								class="input-xlarge">${description}</textarea>
						</div>
					</div>
				</fieldset>

				<div class="form-actions">
					<button class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>
