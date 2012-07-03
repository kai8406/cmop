<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>
<html>
<head>
	<title>服务变更</title>
	
	 <script>
	$(document).ready(function() {
		
		//聚焦指定的Tab
		$("#feature-tab").addClass("active");
		
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
						<li class="active"><a href="#profile1" data-toggle="tab">1.服务申请</a></li>
						<li><a href="#profile2" data-toggle="tab">2.计算资源申请</a></li>
						<li><a href="#profile3" data-toggle="tab">3.存储资源申请</a></li>
						<li><a href="#profile4" data-toggle="tab">4.接入服务申请</a></li>
						<li><a href="#profile5" data-toggle="tab">5.网络资源申请</a></li>
						<li><a href="#profile6" data-toggle="tab">6.提交</a></li>
					</ul>
				</div>
			</div>
			
			<div id="main" class="span10">
			
				<form:form id="inputForm" modelAttribute="apply" action="${ctx}/apply/feature/save/${apply.id}" method="post">
					<input type="hidden" id="networkType"	name="networkType" value="${networkType }"  /> 
					<input type="hidden" id="osType" name="osType" value="${osType }" />
					<input type="text" id="applyId" name="applyId" value="${applyId }" />
					
					<div class="tab-content">
					
						<!-- 第1步 -->
						<div class="tab-pane fade in active span6 offset1" id="profile1">
							<fieldset>
								<legend>服务变更</legend>
								
								<div class="control-group">
									<label class="control-label">服务变更主题</label>
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
										<textarea rows="2" id="description" name="description" class="input-xlarge">${description}</textarea>
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
							<hr/>
							<div>
								<a id="nextStep" class="btn btn-info">下一步</a>
							</div>
						</div>

						<!-- 第2步 -->
						<div class="tab-pane fade span6 offset1" id="profile2">
							<fieldset>
								<legend>计算资源</legend>

								<div class="control-group">
									<label for="focusedInput" class="control-label">服务器类型</label>
									<div class="controls">
										<label class="radio"> 
											<input type="radio"  value="1" name="serverType" />Small &mdash; CPU[单核] Memory[1GB] Disk[20GB]
										</label> 
										<label class="radio"> 
											<input type="radio" value=" 2"	name="serverType" />Middle &mdash; CPU[双核] Memory[2GB] Disk[20GB]
										</label> 
										<label class="radio"> 
											<input type="radio" value="3" name="serverType" />Large &mdash; CPU[四核] Memory[4GB] Disk[20GB]
										</label>
									</div>
								</div>
								
								<div class="page-header"></div>
								
								<div class="control-group">
									<label for="focusedInput" class="control-label">实例数量</label>
									<div class="controls">
										<input type="text"  id="serverCount" name="serverCount" value="${serverCount }" class="input-medium" />
									</div>
								</div>
							</fieldset>

							<fieldset>
								<legend>操作系统</legend>
								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="windows OS" src="./img/windows_logo.png" />
											</div>
											<div class="span4">
											
												<input type="hidden" id="osId" value="1" />
												
												<h3 id="osName">Windwos2003R2</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
									</div>
								</div>

								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="windows OS" src="./img/windows_logo.png" />
											</div>
											<div class="span4">
												<input type="hidden" id="osId" value="2" />
												<h3 id="osName">Windwos2008R2</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
									</div>
								</div>

								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="centOS" src="./img/centos-logo.png" />
											</div>
											<div class="span4">
												<input type="hidden" id="osId" value="3" />
												<h3 id="osName">Centos6.3</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
									</div>
								</div>

								<div class="control-group">
									<div class="controls">
										<div class="row show-grid">
											<div class="span1">
												<img alt="centOS" src="./img/centos-logo.png" />
											</div>
											<div class="span4">
												<input type="hidden" id="osId" value="4" />
												<h3 id="osName" >Centos5.6</h3>
												<p>Nullam quis risus eget urna mollis ornare vel eu leo.
													ultricies vehicula.</p>
											</div>
											<div class="span1">
												<label class="radio">
													<input type="radio"  value="1" name="osBit" /> 32 Bit
												</label> 
												<label class="radio"> 
													<input type="radio" value="2" name="osBit" /> 64 Bit
												</label>
											</div>
										</div>
									</div>
								</div>
							</fieldset>
							
							<hr/>
							<div>
								<a id="backStep" class="btn">返回</a> 
								<a id="nextStep" class="btn btn-info">下一步</a>
							</div>

						</div>

						<!-- 第3步 -->
						<div class="tab-pane fade span7 offset1" id="profile3">

							<fieldset>
								<legend>存储资源</legend>

								<div class="row">

									<div class="span3" id="dataStorage">
										<fieldset>
											<legend>
												<input type="checkbox" id="dataStorageType" name="dataStorageType" value="1" />
												<small>数据存储</small>
											</legend>

											<div class="control-group">
												<label class="control-label">容量空间</label>
												<div class="controls">
												
													<label class="radio"> 
														<input type="radio"	value="20" name="dataSorageSpace" /> 20GB
													</label>
													<label class="radio"> 
														<input type="radio" value="30" name="dataSorageSpace" /> 30GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="50" name="dataSorageSpace" /> 50GB
													</label> 
													<label class="radio">
														<input type="radio" value="100" name="dataSorageSpace" /> 100GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="200" name="dataSorageSpace" /> 200GB
													</label> 
													<label class="radio">
														<input type="radio" value="" name="" />其它 
														<input type="text" value="" class="input-medium" />
													</label>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">Throughput（吞吐量）</label>
													<div class="controls">
														<label class="radio"> 
															<input type="radio" value="1" name="dataStorageThroughput" /> 50 Mbps以内
														</label> 
														<label class="radio"> 
															<input type="radio" value="2" name="dataStorageThroughput" /> 50 Mbps以上
														</label>
													</div>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">IOPS（每秒进行读写（I/O）操作的次数）</label>
													<div class="controls">
														<input type="text" id="dataStorageIops" name="dataStorageIops" value="" class="input-medium" />
													</div>
												</div>

											</div>
										</fieldset>
									</div>
									<div class="span3 offset1" id="businessStorage">
										<fieldset>
											<legend>
												<input type="checkbox" id="businessStorageType" name="businessStorageType" value="2" />
												<small>业务存储</small>
											</legend>

											<div class="control-group">
												<label class="control-label">容量空间</label>
												<div class="controls">
													<label class="radio"> 
														<input type="radio"	value="20" name="businessStorageSpace" /> 20GB
													</label>
													<label class="radio"> 
														<input type="radio" value="30" name="businessStorageSpace" /> 30GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="50" name="businessStorageSpace" /> 50GB
													</label> 
													<label class="radio">
														<input type="radio" value="100" name="businessStorageSpace" /> 100GB
													</label> 
													<label class="radio"> 
														<input type="radio" value="200" name="businessStorageSpace" /> 200GB
													</label> 
													<label class="radio">
														<input type="radio" value="" name="businessStorageSpace" />其它 
														<input type="text" value="" class="input-medium" />
													</label>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">Throughput（吞吐量）</label>
													<div class="controls">
														<label class="radio"> 
															<input type="radio" value="1" name="businessStorageThroughput" /> 50 Mbps以内
														</label> 
														<label class="radio"> 
															<input type="radio" value="2" name="businessStorageThroughput" /> 50 Mbps以上
														</label>
													</div>
												</div>

												<div class="page-header"></div>

												<div class="control-group">
													<label class="control-label">IOPS（每秒进行读写（I/O）操作的次数）</label>
													<div class="controls">
														<input type="text" id="businessStorageIops" name="businessStorageIops" value="" class="input-medium" />
													</div>
												</div>

											</div>
										</fieldset>

									</div>
								</div>
							</fieldset>
							<hr/>
							<div>
								<a id="backStep" class="btn">返回</a>
								<a id="nextStep" class="btn btn-info">下一步</a>
							</div>
						</div>

						<!-- 第4步 -->
						<div class="tab-pane fade span6 offset1" id="profile4">
							<fieldset>
								<legend>接入服务</legend>

								<div class="control-group">
									<label class="control-label">账号</label>
									<div class="controls">
										<input type="text" id="account" name="account" value="${account }" class="input-large" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">使用人</label>
									<div class="controls">
										<input type="text" id="accountUser" name="accountUser" value="${accountUser }" class="input-large" />
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label">需要访问主机</label>
									<div class="controls">
										<input type="text" id="visitHost" name="visitHost" value="${visitHost }" class="input-large" />
									</div>
								</div>

							</fieldset>
							<hr/>
							<div>
								<a id="backStep" class="btn">返回</a> 
								<a id="nextStep" class="btn btn-info">下一步</a>
							</div>
						</div>

						<!-- 第5步 -->
						<div class="tab-pane fade span6 offset1 " id="profile5">

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
							<hr/>
							<div>
								<a id="backStep" class="btn">返回</a>
								<a id="nextStep" class="btn btn-info">下一步</a>
							</div>

						</div>


						<!-- 第6步 -->
						<div class="tab-pane fade span8 offset1" id="profile6">
							<fieldset>
								<legend>服务变更详情</legend>
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
											<td id="td_usage"></td>
										</tr>
										<tr>
											<td>资源类型</td>
											<td id="td_resourceType"></td>
										</tr>
										<tr>
											<td>操作系统</td>
											<td id="td_osType"></td>
										</tr>
										<tr>
											<td>计算机资源</td>
											<td id="td_serverType"></td>
										</tr>
										<tr id="tr_dataStorage" style="display: none;">
											<td>存储资源</td>
											<td id="td_dataStorage"></td>
										</tr>
										<tr id="tr_businessStorage" style="display: none;">
											<td>存储资源</td>
											<td id="td_businessStorage"></td>
										</tr>
										<tr>
											<td>接入服务</td>
											<td id="td_inVpnItem"></td>
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

							<hr/>
							<div>
								<a id="backStep" class="btn ">返回</a>
								<button class="btn btn-primary">保存修改</button>
							</div>
							
						</div>

					</div>

				</form:form>
			</div>
		</div>
</body>
</html>
