<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>网络资源申请</title>
	
	 <script>
	$(document).ready(function() {
		
		//聚焦指定的Tab
		$("#support-tab").addClass("active");
		
		switchTab();
		
		//起始时间
		$("#serviceStart").datepicker();
		$("#serviceEnd").datepicker();

	});
</script>
</head>

<body>
	<div class="row">

			<!-- Guide -->
			<div id="Guide" class="span2">
				<div class="tabbable tabs-left">
					<ul class="nav nav-tabs" id="myTab">
						<li class="active"><a href="#profile1" data-toggle="tab">1.网络资源申请</a></li>
						<li><a href="#profile2" data-toggle="tab">2.网络资源</a></li>
						<li><a href="#profile3" data-toggle="tab">3.提交</a></li>
					</ul>
				</div>
			</div>
			
			<div id="main" class="span10">
			
				<form:form id="inputForm" modelAttribute="apply" action="${ctx}/apply/support/network/save/${apply.id}" method="post">
					<input type="hidden" id="networkType"	name="networkType" value="${networkType }"  /> 
					<div class="tab-content">
					
						<!-- 第1步 -->
						<div class="tab-pane fade in active span6 offset1" id="profile1">
							<fieldset>
								<legend>网络资源申请</legend>
								
								<div class="control-group">
									<label class="control-label">网络资源申请主题</label>
									<div class="controls">
										<input type="text" id="title" name="title" value="${title }"  class="input-large" />
									</div>
								</div>
								
								
								<div class="control-group">
									<label class="control-label">申请起始时间</label>
									<div class="controls">
										<input type="text" id="serviceStart" name="serviceStart" value="${serviceStart }" class="input-medium" />&mdash;
										<input type="text" id="serviceEnd" name="serviceEnd" value="${serviceEnd }" class="input-medium" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">申请用途</label>
									<div class="controls">
										<textarea rows="3" id="description" name="description" class="input-xlarge">${description}</textarea>
									</div>
								</div>

								<div class="control-group">
									<label class="control-label">资源类型</label>
									<div class="controls">
										<label class="radio"> 
											<input type="radio"  value="1" name="resourceType" />生产资源
										</label> 
										<label class="radio"> 
											<input type="radio" value=" 2"	name="resourceType" /> 测试/演示资源
										</label> 
										<label class="radio"> 
											<input type="radio" value="3" name="resourceType" /> 公测资源
										</label>
									</div>
								</div>
							</fieldset>
							
							<div class="form-actions">
								<a id="nextStep" class="btn btn-primary">下一步</a>
							</div>
						</div>

						<!-- 第2步 -->
						<div class="tab-pane fade span6 offset1" id="profile2">

							<fieldset>
								<legend>网络资源</legend>

								<div class="control-group ">
									<label class="control-label">接入链路</label>

									<div class="controls">
										<label class="checkbox"> 
											<input type="checkbox" name="networkTypeCheckbox" value="1"  />电信CTC
										</label> 
										<label class="checkbox"> 
											<input type="checkbox" name="networkTypeCheckbox" value="2"  />联通CNC
										</label> 
									</div>

								</div>

								<div class="page-header"></div>

								<div class="control-group ">
									<label class="control-label">接入速率</label>
									<div class="controls">
										<label class="radio"> 
											<input type="radio" id="networkBand" name="networkBand"  value="1" /> 1M
										</label>
										<label class="radio"> 
											<input type="radio" id="networkBand" name="networkBand"  value="5" /> 5M
										</label>
										<label class="radio"> 
											<input type="radio" id="networkBand" name="networkBand"  value="10" /> 10M
										</label>
										<label class="radio"> 
											<input type="radio" id="networkBand" name="networkBand"  value="20" /> 20M
										</label>
										<label class="radio"> 
											<input type="radio" id="networkBand" name="networkBand"  value="50" /> 50M
										</label>
										<label class="radio"> 
											<input type="radio" id="networkBand" name="networkBand"  value="100" /> 100M
										</label>
									</div>
								</div>

								<div class="page-header"></div>

								<div class="control-group">
									<label class="control-label">开放端口</label>
									<div class="controls">
										<label class="checkbox"> 
											<input type="checkbox"  name="networkPort" value="FTP-21"/> FTP-21
										</label> 
										<label class="checkbox"> 
											<input type="checkbox"  name="networkPort" value="Telnet-23"/> Telnet-23
										</label> 
										<label class="checkbox"> 
											<input type="checkbox"  name="networkPort" value="DNS-32"/> DNS-32
										</label> 
										<label class="checkbox"> 
											<input type="checkbox"  name="networkPort" value="Http-80"/> Http-80
										</label> 
										<label class="checkbox"> 
											<input type="checkbox"  name="networkPort" value=" https-443"/> https-443
										</label> 
										<label class="checkbox"> 
											<input type="checkbox"  name="networkPort" value="www-8080"/> www-8080
										</label> 
										
										<label class="checkbox"> 
											<input type="checkbox" id="networkPortOtherCheckbox" name="" 	value="" /> 其它服务端 
											<input type="text" id="networkPortOther" name="networkPortOther" value=""  class="input-medium" />
										</label> 
										
									</div>
								</div>

								<div class="page-header"></div>

								<div class="control-group ">
									<label class="control-label">公网IP</label>

									<div class="controls">
										<textarea rows="3" id="networkOutIp" name="networkOutIp"  class="input-xlarge">${networkOutIp}</textarea>
									</div>

								</div>

								<div class="page-header"></div>

								<label class="control-label">域名解析</label>

								<table class="table">
									<colgroup>
										<col class="span4">
										<col class="span1">
										<col class="span1">
									</colgroup>
									<thead>
										<tr>
											<th>解析类型</th>
											<th>解析完整域名</th>
											<th>目标IP地址</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>
												<input type="radio" name="analyseTypeFirst" value="1"/> NS 
												<input type="radio" name="analyseTypeFirst" value="2"/>MX 
												<input type="radio" name="analyseTypeFirst" value="3"/>A 
												<input type="radio" name="analyseTypeFirst" value="4"/>CNAME
											</td>
											<td><input type="text" id="domainFirst" name="domainFirst" value="" class="input-medium"/></td>
											<td><input type="text" id="ipFirst" name="ipFirst" value="" class="input-medium"/></td>
										</tr>
										<tr>
											<td>
												<input type="radio" name="analyseTypeSec" value="1"/> NS 
												<input type="radio" name="analyseTypeSec" value="2"/>MX 
												<input type="radio" name="analyseTypeSec" value="3"/>A 
												<input type="radio" name="analyseTypeSec" value="4"/>CNAME
											</td>
											<td><input type="text" id="domainSec" name="domainSec" value="" class="input-medium"/></td>
											<td><input type="text" id="ipSec" name="ipSec" value="" class="input-medium"/></td>
										</tr>
									</tbody>
								</table>

							</fieldset>

							<div class="form-actions">
								<a id="backStep" class="btn">返回</a>
								<a id="nextStep" class="btn btn-primary">下一步</a>
							</div>

						</div>


						<!-- 第3步 -->
						<div class="tab-pane fade span8 offset1" id="profile3">
							<fieldset>
								<legend>网络资源申请详情</legend>
								<table class="table table-bordered table-striped">
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
											<td id="td_description"></td>
										</tr>
										<tr>
											<td>资源类型</td>
											<td id="td_resourceType"></td>
										</tr>
										<tr>
											<td>网络接入</td>
											<td id="td_network"></td>
										</tr>
										<tr>
											<td>开放端口</td>
											<td id="td_networkPort"></td>
										</tr>
										<tr>
											<td>公网IP申请</td>
											<td id="td_networkOutIp"></td>
										</tr>
										<tr>
											<td>域名解析</td>
											<td id="td_netDomainFirst"></td>
										</tr>
										<tr>
											<td>域名解析</td>
											<td id="td_netDomainSec"></td>
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
