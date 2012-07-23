<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
<title>帐号管理</title>
<script>
	$(document)
			.ready(
					function() {
						//聚焦第一个输入框
						$("#email").focus();

						//为inputForm注册validate函数
						$("#inputForm")
								.validate(
										{
											rules : {
												email : {
													remote : "${ctx}/account/user/checkEmail?oldEmail="
															+ encodeURIComponent('${user.email}')
												},
												groupList : "required"
											},
											messages : {
												email : {
													remote : "登录邮箱已存在"
												},
												passwordConfirm : {
													equalTo : "输入与上面相同的密码"
												}
											},
											errorContainer : "#messageBox",
											errorPlacement : function(error,
													element) {
												if (element.is(":checkbox"))
													error.appendTo(element
															.parent().next());
												else
													error.insertAfter(element);
											}
										});
					});
</script>
</head>

<body>
	<form:form id="inputForm" modelAttribute="user"
		action="${ctx }/account/user/regist" method="post"
		cssClass="form-horizontal">

		<div class="tab-content">
			<div class="span6 offset2">
				<fieldset>
					<legend>
						<small>注册</small>
					</legend>

					<div id="messageBox" class="alert alert-error"
						style="display: none">输入有误，请先更正。</div>

					<div class="control-group">
						<label for="email" class="control-label">登录邮箱</label>
						<div class="controls">
							<input type="text" id="email" name="email" size="50"
								value="${user.email}" class="required email" />
						</div>
					</div>

					<div class="control-group">
						<label for="name" class="control-label">用户真实姓名</label>
						<div class="controls">
							<input type="text" id="name" name="name" size="50"
								value="${user.name}" class="required" />
						</div>
					</div>

					<div class="control-group">
						<label for="password" class="control-label">密码</label>
						<div class="controls">
							<input type="password" id="password" name="password" size="50"
								value="${user.password}" class="required" minlength="3" />
						</div>
					</div>

					<div class="control-group">
						<label for="passwordConfirm" class="control-label">确认密码</label>
						<div class="controls">
							<input type="password" id="passwordConfirm"
								name="passwordConfirm" size="50" value="${user.password}"
								equalTo="#password" />
						</div>
					</div>

					<div class="control-group">
						<label for="phonenum" class="control-label">联系电话</label>
						<div class="controls">
							<input type="text" id="phonenum" name="phonenum" size="50"
								value="${user.phonenum}" class="" />
						</div>
					</div>


					<div class="control-group">
						<label for="department" class="control-label">直属领导</label>
						<div class="controls">

							<select id="leaderId" name="leaderId" class="input-large">
								<c:forEach var="item" items="${leaderList }">
									<option value="${item.id }">
										${item.name } &minus;
										<c:forEach var="map" items="${departmentMap }">
											<c:if test="${item.department == map.key}">
												<c:out value="${map.value}" />
											</c:if>
										</c:forEach>
									</option>
								</c:forEach>

							</select>

						</div>
					</div>

					<div class="form-actions">
						<button class="btn btn-primary">保存</button>
						&nbsp; <input id="cancel" class="btn" type="button" value="返回"
							onclick="history.back()" />
					</div>

				</fieldset>
			</div>
		</div>
	</form:form>
</body>
</html>
