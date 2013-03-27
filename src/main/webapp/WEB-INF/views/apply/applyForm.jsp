<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active");
			
			$("#serviceTag").focus();
			
			$("#inputForm").validate();
			
			// 初始化服务开始和结束时间,结束时间默认为开始时间3个月后
			$("#serviceStart").val(getDatePlusMonthNum(0));
			$("#serviceEnd").val(getDatePlusMonthNum(3));
			
			$("#serviceStart").datepicker({
				changeMonth: true,
				onClose: function(selectedDate) {
					$("#serviceEnd").datepicker("option", "minDate", selectedDate);
				}
			});
			$("#serviceEnd").datepicker({
				changeMonth: true,
				onClose: function(selectedDate) {
					$("#serviceStart").datepicker("option", "maxDate", selectedDate);
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
								<a href="#deleteStorageModal${item.id}" data-toggle="modal">删除</a>
								<div id="deleteStorageModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
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
								<c:if test="${compute.networkElbItem.id == item.id }">${compute.identifier}(${compute.innerIp})&nbsp;&nbsp;</c:if>
							</c:forEach>
						</dd>
						
						<dd><em>端口映射（协议、源端口、目标端口）</em></dd>
						
						<c:forEach var="port" items="${item.elbPortItems }">
							<dd>&nbsp;&nbsp;${port.protocol}&nbsp;,&nbsp;${port.sourcePort}&nbsp;,&nbsp;${port.targetPort}</dd>
						</c:forEach>
						
						<span class="pull-right">
							<a href="${ctx}/apply/elb/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
							<a href="#deleteElbModal${item.id}" data-toggle="modal">删除</a>
							<div id="deleteElbModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
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
							<a href="#deleteEipModal${item.id}" data-toggle="modal">删除</a>
							<div id="deleteEipModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
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
				<c:if test="${not empty apply.networkDnsItems}">
				
					<hr>
					<dt>DNS域名映射</dt>
					<c:forEach var="item" items="${apply.networkDnsItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>域名</em>&nbsp;&nbsp;${item.domainName }</dd>
						
						<dd><em>域名类型</em>&nbsp;&nbsp;<c:forEach var="map" items="${domainTypeMap}"><c:if test="${item.domainType == map.key }">${map.value}</c:if></c:forEach></dd>
						
						<dd>
							<c:choose>
								<c:when test="${item.domainType != 3 }"><em>目标IP</em>&nbsp;&nbsp;${item.mountElbs }</c:when>
								<c:otherwise><em>CNAME域名</em>&nbsp;&nbsp;${item.cnameDomain }</c:otherwise>
							</c:choose>
							
							<span class="pull-right">
								<a href="${ctx}/apply/dns/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
								<a href="#deleteDnsModal${item.id}" data-toggle="modal">删除</a>
								<div id="deleteDnsModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
									<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
									<div class="modal-body">是否删除?</div>
									<div class="modal-footer">
										<button class="btn" data-dismiss="modal">关闭</button>
										<a href="${ctx}/apply/dns/delete/${item.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
									</div>
								</div>
							</span>
						
						</dd>
						
						<br>
						
					</c:forEach>
					
				</c:if>
				
				<!-- 监控邮件列表 -->
				<c:if test="${not empty apply.monitorMails}">
					<hr>
					<dt>监控邮件列表</dt>
					<c:forEach var="item" items="${apply.monitorMails}"><dd>${item.email }</dd></c:forEach>
					<dd>
						<span class="pull-right">
							<a href="${ctx}/apply/monitorEmail/update/applyId/${apply.id}">修改</a>&nbsp;
						</span>
					</dd>
				</c:if>
				
				<!-- 监控手机列表 -->
				<c:if test="${not empty apply.monitorPhones}">
					<hr>
					<dt>监控手机列表</dt>
					<c:forEach var="item" items="${apply.monitorPhones}"><dd>${item.telephone }</dd></c:forEach>
					<dd>
						<span class="pull-right">
							<a href="${ctx}/apply/monitorPhone/update/applyId/${apply.id}">修改</a>&nbsp;
						</span>
					</dd>
				</c:if>
				
				<!-- 服务器监控monitorCompute -->
				<c:if test="${not empty apply.monitorComputes}">
				
					<hr>
					<dt>实例监控</dt>
					<c:forEach var="item" items="${apply.monitorComputes}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>IP地址</em>&nbsp;&nbsp;${item.ipAddress}</dd>
						
						<dd><em>CPU占用率</em>
							&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${item.cpuWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
							&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${item.cpuCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						</dd>
						
						<dd><em>内存占用率</em>
							&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${item.memoryWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
							&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${item.memoryCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						</dd>
						
						<dd><em>网络丢包率</em>
							&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${item.pingLossWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
							&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdGtMap}"><c:if test="${item.pingLossCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						</dd>
						
						<dd><em>硬盘可用率</em>
							&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdLtMap}"><c:if test="${item.diskWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
							&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdLtMap}"><c:if test="${item.diskCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						</dd>
						
						<dd><em>网络延时率</em>
							&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${thresholdNetGtMap}"><c:if test="${item.pingDelayWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
							&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${thresholdNetGtMap}"><c:if test="${item.pingDelayCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						</dd>
						
						<dd><em>最大进程数</em>
							&nbsp;&nbsp;报警阀值&nbsp;<c:forEach var="map" items="${maxProcessMap}"><c:if test="${item.maxProcessWarn == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
							&nbsp;&nbsp;警告阀值&nbsp;<c:forEach var="map" items="${maxProcessMap}"><c:if test="${item.maxProcessCritical == map.key }"><strong>${map.value }</strong></c:if></c:forEach>
						</dd>
						
						<dd><em>监控端口</em>&nbsp;&nbsp;${item.port}</dd>
						<dd><em>监控进程</em>&nbsp;&nbsp;${item.process}</dd>
						<dd><em>挂载路径</em>&nbsp;&nbsp;${item.mountPoint}
							<span class="pull-right">
								<a href="${ctx}/apply/monitor/compute/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
								<a href="#deleteComputeModal${item.id}" data-toggle="modal">删除</a>
								<div id="deleteComputeModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
									<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
									<div class="modal-body">是否删除?</div>
									<div class="modal-footer">
										<button class="btn" data-dismiss="modal">关闭</button>
										<a href="${ctx}/apply/monitor/compute/delete/${item.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
									</div>
								</div>
							</span>
						</dd>
							
						<br>
					</c:forEach>
				</c:if>
				
				<!-- ELB监控monitorElb -->
				<c:if test="${not empty apply.monitorElbs}">
				
					<hr>
					<dt>ELB监控</dt>
					<c:forEach var="item" items="${apply.monitorElbs}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>监控ELB</em>&nbsp;&nbsp;${item.networkElbItem.identifier }(${item.networkElbItem.virtualIp})
						
							<span class="pull-right">
								<a href="${ctx}/apply/monitor/elb/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
								<a href="#deleteElbModal${item.id}" data-toggle="modal">删除</a>
								<div id="deleteElbModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
									<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
									<div class="modal-body">是否删除?</div>
									<div class="modal-footer">
										<button class="btn" data-dismiss="modal">关闭</button>
										<a href="${ctx}/apply/monitor/elb/delete/${item.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
									</div>
								</div>
							</span>
						
						</dd>
							
						<br>
						
					</c:forEach>
				</c:if>
				
				<!-- MDN -->
				<c:if test="${not empty apply.mdnItems }">
					<hr>
					<dt>MDN</dt>
					<c:forEach var="item" items="${apply.mdnItems}">
					
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						
						<dd><em>重点覆盖地域</em>&nbsp;&nbsp;${item.coverArea}</dd>
						
						<dd><em>重点覆盖ISP</em>&nbsp;&nbsp;
							<c:forEach var="coverIsp" items="${fn:split(item.coverIsp,',')}">
								<c:forEach var="map" items="${ispTypeMap }">
									<c:if test="${map.key == coverIsp }">${map.value }</c:if>
								</c:forEach>
						    </c:forEach>
						    <span class="pull-right">
								<a href="${ctx}/apply/mdn/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
							</span>
					 	</dd>
						
						<c:if test="${not empty item.mdnVodItems }">
							<br>
							<dt>MDN点播加速</dt>
							<c:forEach var="vod" items="${item.mdnVodItems}">
								<dd><em>服务域名</em>&nbsp;&nbsp;${vod.vodDomain}</dd>
								<dd><em>加速服务带宽</em>&nbsp;&nbsp;<c:forEach var="map" items="${bandwidthMap }"><c:if test="${map.key == vod.vodBandwidth }">${map.value }</c:if></c:forEach></dd>
								<dd><em>播放协议选择</em>&nbsp;&nbsp;${vod.vodProtocol}</dd>
								<dd><em>出口带宽</em>&nbsp;&nbsp;${vod.sourceOutBandwidth}</dd>
								<dd><em>Streamer地址</em>&nbsp;&nbsp;${vod.sourceStreamerUrl}
									<span class="pull-right">
										<a href="${ctx}/apply/mdn/mdnVod/update/${vod.id}/applyId/${apply.id}">修改</a>&nbsp;
										<a href="#deleteVodModal${item.id}" data-toggle="modal">删除</a>
										<div id="deleteVodModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
											<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
											<div class="modal-body">是否删除?</div>
											<div class="modal-footer">
												<button class="btn" data-dismiss="modal">关闭</button>
												<a href="${ctx}/apply/mdn/mdnVod/delete/${vod.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
											</div>
										</div>
									</span>
								</dd>
								<br>
							</c:forEach>
						</c:if>
						
						<c:if test="${not empty item.mdnLiveItems }">
							<br>
							<dt>MDN直播加速</dt>
							<c:forEach var="live" items="${item.mdnLiveItems}">
								<dd><em>服务域名</em>&nbsp;&nbsp;${live.liveDomain}</dd>
								<dd><em>加速服务带宽</em>&nbsp;&nbsp;<c:forEach var="map" items="${bandwidthMap }"><c:if test="${map.key == live.liveBandwidth }">${map.value }</c:if></c:forEach></dd>
								<dd><em>播放协议选择</em>&nbsp;&nbsp;${live.liveProtocol}</dd>
								<dd><em>出口带宽</em>&nbsp;&nbsp;${live.bandwidth}</dd>
								<dd><em>频道名称</em>&nbsp;&nbsp;${live.name}</dd>
								<dd><em>频道GUID</em>&nbsp;&nbsp;${live.guid}</dd>
								<dd><em>直播流输出模式</em>&nbsp;&nbsp;<c:forEach var="map" items="${outputModeMap }"><c:if test="${map.key == live.streamOutMode }">${map.value }</c:if></c:forEach></dd>
								<c:choose>
									<c:when test="${live.streamOutMode == 1  }">
										<dd><em>编码器模式</em>&nbsp;&nbsp;<c:forEach var="map" items="${encoderModeMap }"><c:if test="${map.key == live.encoderMode }">${map.value }</c:if></c:forEach></dd>
										<c:choose>
											<c:when test="${live.encoderMode == 1 }">
												<dd><em>HTTP流地址</em>&nbsp;&nbsp;${live.httpUrl}</dd>
												<dd><em>HTTP流混合码率</em>&nbsp;&nbsp;${live.httpBitrate}</dd>
											</c:when>
											<c:otherwise>
												<dd><em>M3U8流地址</em>&nbsp;&nbsp;${live.hlsUrl}</dd>
												<dd><em>M3U8流混合码率</em>&nbsp;&nbsp;${live.hlsBitrate}</dd>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<dd><em>HTTP流地址</em>&nbsp;&nbsp;${live.httpUrl}</dd>
										<dd><em>HTTP流混合码率</em>&nbsp;&nbsp;${live.httpBitrate}</dd>
										
										<dd><em>M3U8流地址</em>&nbsp;&nbsp;${live.hlsUrl}</dd>
										<dd><em>M3U8流混合码率</em>&nbsp;&nbsp;${live.hlsBitrate}</dd>
										
										<dd><em>RTSP流地址</em>&nbsp;&nbsp;${live.rtspUrl}</dd>
										<dd><em>RTSP流混合码率</em>&nbsp;&nbsp;${live.rtspBitrate}</dd>
									</c:otherwise>
								</c:choose>
								<dd>
									<span class="pull-right">
										<a href="${ctx}/apply/mdn/mdnLive/update/${live.id}/applyId/${apply.id}">修改</a>&nbsp;
										<a href="#deleteLiveModal${item.id}" data-toggle="modal">删除</a>
										<div id="deleteLiveModal${item.id }" class="modal hide fade" tabindex="-1" data-width="250">
											<div class="modal-header"><button type="button" class="close" data-dismiss="modal">×</button><h3>提示</h3></div>
											<div class="modal-body">是否删除?</div>
											<div class="modal-footer">
												<button class="btn" data-dismiss="modal">关闭</button>
												<a href="${ctx}/apply/mdn/mdnLive/delete/${live.id}/applyId/${apply.id}" class="btn btn-primary">确定</a>
											</div>
										</div>
									</span>
								</dd>
								<br>
							</c:forEach>
						</c:if>
						<br>
					</c:forEach>
				</c:if>
				
				<!-- 云生产CP -->
				<c:if test="${not empty apply.cpItems}">
					<hr>
					<dt>CP云生产</dt>
					<c:forEach var="item" items="${apply.cpItems}">
						<dd><em>标识符</em>&nbsp;&nbsp;${item.identifier}</dd>
						<dd><em>收录流URL</em>&nbsp;&nbsp;${item.recordStreamUrl}</dd>
						<dd><em>收录码率</em>&nbsp;&nbsp;<c:forEach var="map" items="${recordBitrateMap}"><c:if test="${map.key == item.recordBitrate }">${map.value }</c:if></c:forEach></dd>
						<dd><em>输出编码</em>&nbsp;&nbsp;
							<c:forEach var="exportEncode" items="${fn:split(item.exportEncode,',')}">
								<c:forEach var="map" items="${exportEncodeMap }">
									<c:if test="${map.key == exportEncode }"><br>${map.value }</c:if>
								</c:forEach>
						    </c:forEach>
					 	</dd>
						<dd><em>收录类型</em>&nbsp;&nbsp;<c:forEach var="map" items="${recordTypeMap}"><c:if test="${map.key == item.recordType }">${map.value }</c:if></c:forEach></dd>
						<dd><em>收录时段</em>&nbsp;&nbsp;${item.recordTime}</dd>
						<c:if test="${not empty item.publishUrl }">
							<dd><em>发布接口地址</em>&nbsp;&nbsp;${item.publishUrl}</dd>
						</c:if>
						<dd><em>是否推送内容交易平台</em>&nbsp;&nbsp;<c:forEach var="map" items="${isPushCtpMap}"><c:if test="${map.key == item.isPushCtp }">${map.value }</c:if></c:forEach></dd>
						<br>
						<dd><strong>视频配置</strong></dd>
						<dd><em>FTP上传IP</em>&nbsp;&nbsp;${item.videoFtpIp}</dd>
						<dd><em>端口</em>&nbsp;&nbsp;${item.videoFtpPort}</dd>
						<dd><em>FTP用户名</em>&nbsp;&nbsp;${item.videoFtpUsername}</dd>
						<dd><em>FTP密码</em>&nbsp;&nbsp;${item.videoFtpPassword}</dd>
						<dd><em>FTP根路径</em>&nbsp;&nbsp;${item.videoFtpRootpath}</dd>
						<dd><em>FTP上传路径</em>&nbsp;&nbsp;${item.videoFtpUploadpath}</dd>
						<dd><em>输出组类型</em>&nbsp;&nbsp;${item.videoOutputGroup}</dd>
						<dd><em>输出方式配置</em>&nbsp;&nbsp;<c:forEach var="map" items="${videoOutputWayMap}"><c:if test="${map.key == item.videoOutputWay }">${map.value }</c:if></c:forEach></dd>
						<br>
						<dd><strong>图片配置</strong></dd>
						<dd><em>FTP上传IP</em>&nbsp;&nbsp;${item.pictrueFtpIp}</dd>
						<dd><em>端口</em>&nbsp;&nbsp;${item.pictrueFtpPort}</dd>
						<dd><em>FTP用户名</em>&nbsp;&nbsp;${item.pictrueFtpUsername}</dd>
						<dd><em>FTP密码</em>&nbsp;&nbsp;${item.pictrueFtpPassword}</dd>
						<dd><em>FTP根路径</em>&nbsp;&nbsp;${item.pictrueFtpRootpath}</dd>
						<dd><em>FTP上传路径</em>&nbsp;&nbsp;${item.pictrueFtpUploadpath}</dd>
						<dd><em>输出组类型</em>&nbsp;&nbsp;${item.pictrueOutputGroup}</dd>
						<dd><em>输出媒体类型</em>&nbsp;&nbsp;${item.pictrueOutputMedia}</dd>
						<c:if test="${not empty item.cpProgramItems }">
							<br>
							<dd><strong>拆条节目单</strong></dd>
							<c:forEach var="program" items="${ item.cpProgramItems}">
								<dd><a>${program.name }&nbsp;&nbsp;${program.size }K</a></dd>
							</c:forEach>
						</c:if>
						<dd>
							<span class="pull-right">
								<a href="${ctx}/apply/cp/update/${item.id}/applyId/${apply.id}">修改</a>&nbsp;
							</span>
						</dd>
					</c:forEach>
				</c:if>
				
			</dl>
			
			<div class="form-actions">
				<a class="btn" href="${ctx}/apply/">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
