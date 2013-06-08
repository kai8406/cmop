<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>服务申请</title>

	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#apply").addClass("active"); 
			
			/*禁用回车提交form表单.*/
			$("#inputForm").keypress(function(e) {
				if (e.which == 13) {
					return false;
				}
			});
			
			$("#inputForm").validate();
			
			// 初始化服务开始和结束时间,结束时间默认为开始时间3个月后
			$("#serviceStart").val(getDatePlusMonthNum(0));
			$("#serviceEnd").val(getDatePlusMonthNum(3));
			
			$("#serviceStart").datepicker({
				changeMonth: true,
				minDate: 'D',
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
			
			//点击直播输出流模式
			$("input:radio[name='outputMode']").click(function() {
				if ($(this).val() == 1) {
					$("#EncoderDiv").addClass("show").removeClass("hidden");
					$("#TransferDiv").addClass("hidden").removeClass("show");
				} else {
					$("#TransferDiv").addClass("show").removeClass("hidden");
					$("#EncoderDiv").addClass("hidden").removeClass("show");
				}
			});
			
			//点击编码器模式
			$("#encoderMode").change(function() {
				
				$("input.mdn-encoder").val("");
				
				if ($(this).val() == 1) {
					$("#HTTPDIV").addClass("show").removeClass("hidden");
					$("#HSLDIV").addClass("hidden").removeClass("show");
				} else if($(this).val() == 2) {
					$("#HSLDIV").addClass("show").removeClass("hidden");
					$("#HTTPDIV").addClass("hidden").removeClass("show");
				}else{
					$("#HTTPDIV").addClass("hidden").removeClass("show");
					$("#HSLDIV").addClass("hidden").removeClass("show");
				}
			});
			
			$('#createMDNBtn').click(function() {
				if (!$("#inputForm").valid()) {
					return false;
				}
				var domain = $("#domain").val();
				var speedBandwidth = $("#speedBandwidth").val();
				var speedBandwidthText = $("#speedBandwidth>option:selected").text();
				var $protocol = $("input[name='playProtocol']:checked");
				var exportBandwidth = $("#exportBandwidth").val();
				var protocolId = "",
					protocolText = "",
					html = "";
				$protocol.each(function() {
					protocolId += $(this).val() + "-";
					protocolText += $(this).val() + ",";
				});
				
				//处理掉字符串的尾巴","
				protocolId = protocolId.substring(0, protocolId.length - 1);
				protocolText = protocolText.substring(0, protocolText.length - 1);
				
				//判断是vod or live,和ul中的li顺序有关系.第一个是vod,第二个是live.注意.
				if ($("#mdn-tab").find("li:first").hasClass("active")) { /* vod */
					var vodStreamer = $("#vodStreamer").val();
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button data-dismiss="alert" class="close" type="button">×</button>';
					html += '<input type="hidden" name="vodDomains" value="' + domain + '">';
					html += '<input type="hidden" name="vodBandwidths" value="' + speedBandwidth + '">';
					html += '<input type="hidden" name="vodProtocols" value="' + protocolId + '">';
					html += '<input type="hidden" name="sourceOutBandwidths" value="' + exportBandwidth + '">';
					html += '<input type="hidden" name="sourceStreamerUrls" value="' + vodStreamer + '">';
					html += '<dd><em>服务域名</em>&nbsp;&nbsp;<strong>' + domain + '</strong></dd>';
					html += '<dd><em>加速服务带宽</em>&nbsp;&nbsp;<strong>' + speedBandwidthText + '</strong></dd>';
					html += '<dd><em>播放协议</em>&nbsp;&nbsp;<strong>' + protocolText + '</strong></dd>';
					html += '<dd><em>源站出口带宽</em>&nbsp;&nbsp;<strong>' + exportBandwidth + '</strong></dd>';
					html += '<dd><em>源站Streamer公网地址</em>&nbsp;&nbsp;<strong>' + vodStreamer + '</strong></dd>';
					html += '</div>';
				} else {/* live */
					var channelName = $("#channelName").val();
					var channelGUID = $("#channelGUID").val();
					
					//直播流输出模式
					var outputMode = $("input:radio[name='outputMode']:checked");
					var outputModeText = outputMode.closest("label.radio").find(".radioText").text();
					
					//编码器模式
					var encoderMode = $("#encoderMode");
					var encoderModeText = encoderMode.find("option:selected").text();
					var httpUrlEncoder = $("#httpUrlEncoder").val();
					var httpBitrateEncoder = $("#httpBitrateEncoder").val();
					var hlsUrlEncoder = $("#hlsUrlEncoder").val();
					var hlsBitrateEncoder = $("#hlsBitrateEncoder").val();
					var httpUrl = $("#httpUrl").val();
					var httpBitrate = $("#httpBitrate").val();
					var hlsUrl = $("#hlsUrl").val();
					var hlsBitrate = $("#hlsBitrate").val();
					
					html += '<div class="resources alert alert-block alert-info fade in">';
					html += '<button data-dismiss="alert" class="close" type="button">×</button>';
					html += '<input type="hidden" name="liveDomains" value="' + domain + '">';
					html += '<input type="hidden" name="liveBandwidths" value="' + speedBandwidth + '">';
					html += '<input type="hidden" name="liveProtocols" value="' + protocolId + '">';
					html += '<input type="hidden" name="bandwidths" value="' + exportBandwidth + '">';
					html += '<input type="hidden" name="channelNames" value="' + channelName + '">';
					html += '<input type="hidden" name="channelGUIDs" value="' + channelGUID + '">';
					html += '<input type="hidden" name="streamOutModes" value="' + outputMode.val() + '">';
					html += '<input type="hidden" name="encoderModes" value="' + encoderMode.val() + '">';
					html += '<dd><em>服务域名</em>&nbsp;&nbsp;<strong>' + domain + '</strong></dd>';
					html += '<dd><em>加速服务带宽</em>&nbsp;&nbsp;<strong>' + speedBandwidthText + '</strong></dd>';
					html += '<dd><em>播放协议</em>&nbsp;&nbsp;<strong>' + protocolText + '</strong></dd>';
					html += '<dd><em>源站出口带宽</em>&nbsp;&nbsp;<strong>' + exportBandwidth + '</strong></dd>';
					html += '<dd><em>频道名称</em>&nbsp;&nbsp;<strong>' + channelName + '</strong></dd>';
					html += '<dd><em>频道GUID</em>&nbsp;&nbsp;<strong>' + channelGUID + '</strong></dd>';
					html += '<dd><em>直播流输出模式</em>&nbsp;&nbsp;<strong>' + outputModeText + '</strong></dd>';
					if (outputMode.val() == 1) {
						//Encoder模式
						html += '<input type="hidden" name="httpUrls" value="' + httpUrlEncoder + '">';
						html += '<input type="hidden" name="httpBitrates" value="' + httpBitrateEncoder + '">';
						html += '<input type="hidden" name="hlsUrls" value="' + hlsUrlEncoder + '">';
						html += '<input type="hidden" name="hlsBitrates" value="' + hlsBitrateEncoder + '">';
						
						if(encoderMode.val() != undefined){//选择编码器模式
							html += '<dd><em>编码器模式</em>&nbsp;&nbsp;<strong>' + encoderModeText + '</strong></dd>';
							if (encoderMode.val() == 1) {
								//HTTP拉流模式
								html += '<dd><em>拉流地址</em>&nbsp;&nbsp;<strong>' + httpUrlEncoder + '</strong></dd>';
								html += '<dd><em>拉流混合码率</em>&nbsp;&nbsp;<strong>' + httpBitrateEncoder + '</strong></dd>';
							} else if(encoderMode.val() == 2) {
								//RTMP推流模式
								html += '<dd><em>推流地址</em>&nbsp;&nbsp;<strong>' + hlsUrlEncoder + '</strong></dd>';
								html += '<dd><em>推流混合码率</em>&nbsp;&nbsp;<strong>' + hlsBitrateEncoder + '</strong></dd>';
							}
						}
					} else {
						//Transfer模式
						html += '<input type="hidden" name="httpUrls" value="' + httpUrl + '">';
						html += '<input type="hidden" name="httpBitrates" value="' + httpBitrate + '">';
						html += '<input type="hidden" name="hlsUrls" value="' + hlsUrl + '">';
						html += '<input type="hidden" name="hlsBitrates" value="' + hlsBitrate + '">';
						//HTTP拉流模式
						html += '<dd><em>HTTP流地址</em>&nbsp;&nbsp;<strong>' + httpUrl + '</strong></dd>';
						html += '<dd><em>HTTP流混合码率</em>&nbsp;&nbsp;<strong>' + httpBitrate + '</strong></dd>';
						//RTMP推流模式
						html += '<dd><em>HSL流地址</em>&nbsp;&nbsp;<strong>' + hlsUrl + '</strong></dd>';
						html += '<dd><em>HSL流混合码率</em>&nbsp;&nbsp;<strong>' + hlsBitrate + '</strong></dd>';
					}
					html += '</div>';
				}
				
				//插入HTML文本至页面
				$("#resourcesDIV dl").append(html);
			});
			
		});
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>

	<form id="inputForm" action="." method="post" class="input-form form-horizontal">
		
		<fieldset>
		
			<legend><small>创建MDN</small></legend>
			
			<!-- Step.1 -->
			<div class="step">
			
				<div class="control-group">
					<label class="control-label" for="serviceTag">服务标签</label>
					<div class="controls">
						<input type="text"  id="serviceTag" name="serviceTag"  class="required" maxlength="45" placeholder="...服务标签">
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
						<input type="text" id="serviceStart" name="serviceStart" readonly class="datepicker required"  placeholder="...服务开始时间">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="serviceEnd">服务结束时间</label>
					<div class="controls">
						<input type="text" id=serviceEnd name="serviceEnd" readonly class="datepicker required"  placeholder="...服务结束时间">
					</div>
				</div>
			
				<div class="control-group">
					<label class="control-label" for="description">用途描述</label>
					<div class="controls">
						<textarea rows="3" id="description" name="description" placeholder="...用途描述"
							maxlength="2000" class="required"></textarea>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="coverArea">重点覆盖地域</label>
					<div class="controls">
						<input type="text" id="coverArea" name="coverArea" class="required" maxlength="45" placeholder="...重点覆盖地域">
					</div>
				</div>
				 
				<div class="control-group">
					<label class="control-label" for="coverIsp">重点覆盖ISP</label>
					<div class="controls">
						<c:forEach var="map" items="${ispTypeMap}">
							<label class="checkbox">
								<input type="checkbox" name="coverIsp" value="${map.key}" class="required"><span class="checkboxText ">${map.value}</span>
							</label>
						</c:forEach>
					</div>
				</div>
				
				<div class="form-actions">
					<input class="btn" type="button" value="返回" onClick="history.back()">
					<input class="btn btn-primary nextStep" type="button" value="下一步">
				</div>
			
			</div><!-- Step.1 End -->
			
			<!-- Step.2 -->
			<div class="step">
			
			    <ul id="mdn-tab" class="nav nav-tabs">
				    <li class="active"><a href="#vodTab" data-toggle="tab">MDN点播加速</a></li>
				    <li><a href="#liveTab" data-toggle="tab">MDN直播加速</a></li>
			    </ul>
			    
			    <div class="control-group">
					<label class="control-label" for="domain">服务域名</label>
					<div class="controls">
						<input type="text" id="domain" name="domain" class="required" maxlength="45" placeholder="...服务域名">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="speedBandwidth">加速服务带宽</label>
					<div class="controls">
						<select id="speedBandwidth" class="required">
							<c:forEach var="map" items="${bandwidthMap}"><option value="${map.key }">${map.value }</option></c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="playProtocol">播放协议选择</label>
					<div class="controls">
						<c:forEach var="map" items="${palyProtocolMap}">
					 		<label class="checkbox inline">
								<input type="checkbox" id="playProtocol" name="playProtocol" class="required" value="${map.key }"><span class="checkboxText">${map.value }</span>
							</label>	
						</c:forEach>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="exportBandwidth">源站出口带宽</label>
					<div class="controls">
						<input type="text" id="exportBandwidth" class="required" maxlength="45" placeholder="...源站出口带宽">
					</div>
				</div>
				    
			    <div class="tab-content">
			    
				    <!-- MDN点播加速 vodTab -->
				    <div class="tab-pane active" id="vodTab">
						
						<div class="control-group">
							<label class="control-label" for="vodStreamer">Streamer地址</label>
							<div class="controls">
								<input type="text" id="vodStreamer" name="vodStreamer" class="required" maxlength="45" placeholder="...源站Streamer公网地址">
							</div>
						</div>
						
				    </div><!-- MDN点播加速 vodTab End -->
				    
			    	<!-- MDN直播加速 liveTab -->
			    	<div class="tab-pane" id="liveTab">
			    	
			    		<div class="control-group">
							<label class="control-label" for="channelName">频道名称</label>
							<div class="controls">
								<input type="text" id="channelName" name="channelName" class="required" maxlength="45" placeholder="...频道名称">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="channelGUID">频道GUID</label>
							<div class="controls">
								<input type="text" id="channelGUID" name="channelGUID" class="required" maxlength="45" placeholder="...频道GUID">
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="outputMode">直播流输出模式</label>
							<div class="controls">
								<c:forEach var="map" items="${outputModeMap }">
									<label class="radio inline">
										<input type="radio" id="outputMode" name="outputMode" value="${map.key }"
											 <c:if test="${map.key == 1 }">checked="checked" </c:if>
											><span class="radioText">${map.value }</span>
									</label>
								</c:forEach>
							</div>
						</div>
						
						<!-- 选择Encoder -->
						<div id="EncoderDiv" class="show">
						
							<div class="control-group">
								<label class="control-label" for="encoderMode">编码器模式</label>
								<div class="controls">
									<select id="encoderMode" name="encoderMode" class="required">
										<c:forEach var="map" items="${encoderModeMap}">
												<option value="${map.key }">${map.value }</option>
										</c:forEach>
									</select>
								</div>
							</div>
							
							<!-- HTTP拉流模式  -->
							<div id="HTTPDIV" class="hidden">
								<div class="control-group">
									<label class="control-label" for="httpUrlEncoder">流地址</label>
									<div class="controls">
										<input type="text" id="httpUrlEncoder" class="required mdn-encoder" maxlength="45" placeholder="...拉流地址">
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="httpBitrateEncoder">混合码率</label>
									<div class="controls">
										<input type="text" id="httpBitrateEncoder" class="required mdn-encoder" maxlength="45" placeholder="...拉流混合码率">
									</div>
								</div>
							</div><!-- HTTP拉流模式 End -->
							
							<!-- HSLDIV推流模式  -->
							<div id="HSLDIV" class="hidden">
								<div class="control-group">
									<label class="control-label" for="hlsUrlEncoder">流地址</label>
									<div class="controls">
										<input type="text" id="hlsUrlEncoder" class="mdn-encoder" maxlength="45" placeholder="...推流地址">
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="hlsBitrateEncoder">混合码率</label>
									<div class="controls">
										<input type="text" id="hlsBitrateEncoder" class="mdn-encoder" maxlength="45" placeholder="...推流混合码率">
									</div>
								</div>
							</div><!-- HSLDIV推流模式 End -->
						
						</div><!-- 选择Encoder End -->
						
						<!-- 选择 Transfer -->
						<div id="TransferDiv" class="hidden">
							
							<div class="control-group">
								<label class="control-label" for="httpUrl">HTTP流地址</label>
								<div class="controls">
									<input type="text" id="httpUrl" class="required" maxlength="45" placeholder="...HTTP流地址">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="httpBitrate">HTTP混合码率</label>
								<div class="controls">
									<input type="text" id="httpBitrate" class="required" maxlength="45" placeholder="...HTTP流混合码率">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="hlsUrl">HSL流地址</label>
								<div class="controls">
									<input type="text" id="hlsUrl" class="required" maxlength="45" placeholder="...HSL流地址">
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="hlsBitrate">HSL混合码率</label>
								<div class="controls">
									<input type="text" id="hlsBitrate"  class="required" maxlength="45" placeholder="...HSL流混合码率">
								</div>
							</div>
								
						</div><!-- 选择Transfer End -->
						
			    		 
				    </div><!--MDN直播加速 liveTab End -->
				    
				    <div class="control-group">
						<div class="controls">
							 <a id="createMDNBtn" class="btn btn-success">生成MDN</a>
						</div>
					</div>
				    
			    </div>
			    
			    <hr>
			
				<div id="resourcesDIV"><dl class="dl-horizontal"></dl></div>
				
				<div class="form-actions">
					<input class="btn backStep" type="button" value="上一步">
					<input class="btn btn-primary" type="submit" value="提交">
				</div>
				
			</div><!-- Step.2 End -->
			
		</fieldset>
		
	</form>
	
</body>
</html>
