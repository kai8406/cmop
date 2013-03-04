<html>
<head>

</head>
<body>

	<p><#if operateUrl?exists>${operateUrl}<#elseif operateDoneStr?exists>${operateDoneStr}<#else>你好,请审批以下内容.</#if></p>
	
	<ul>
		
		<!--服务申请Apply -->
		<li>
			<strong>服务申请单基本信息</strong>
			<ul> 
				<li><em>申请人</em>&nbsp;: ${apply.user.name} </li>
				<li><em>申请标题</em>&nbsp;: ${apply.title}</li>
				<li><em>申请时间</em>&nbsp;: ${apply.createTime}</li>
				
				<li><em>优先级</em>&nbsp;: 
					<#list priorityMap?keys as k >
						<#if apply.priority?string == k>${priorityMap[k]}</#if>
					</#list>
				</li>
				
				<li><em>服务起止日期</em>&nbsp;: ${apply.serviceStart} <em>至</em>&nbsp; ${apply.serviceEnd}</li>
				<li><em>用途描述</em>&nbsp;: ${apply.description}</li>
			</ul>	
		</li> <!--服务申请Apply End -->
		<li>
			<!-- 实例Compute -->
			<#if (computes?exists) && (computes?size > 0) >
				<strong>计算资源(PCS & ECS)</strong>
				
				<#list computes as compute>
					<ul>
						<li><em>标识符</em>&nbsp;:${compute.identifier}</li>
						<li><em>用途信息</em>&nbsp;:${compute.remark}</li>
						<li><em>基本信息</em>&nbsp;:
						
							<#list osTypeMap?keys as k ><#if compute.osType?string == k>${osTypeMap[k]}</#if></#list>
						
							<#list osBitMap?keys as k ><#if compute.osBit?string == k>${osBitMap[k]}</#if></#list>
						
							<!-- 规格 1.PCS 2.ECS -->
							<#if compute.computeType == 1 >
								<#list pcsServerTypeMap?keys as k ><#if compute.serverType?string == k>${pcsServerTypeMap[k]}</#if></#list>
							<#else>
								<#list ecsServerTypeMap?keys as k ><#if compute.serverType?string == k>${ecsServerTypeMap[k]}</#if></#list>
							</#if>
						
					  	</li>
						<li><em>关联ESG</em>&nbsp;:${compute.networkEsgItem.identifier}(${compute.networkEsgItem.description})</li>
						<br>
					</ul>
				</#list>
				
			</#if><!-- 实例Compute End-->
		
			<!-- 存储 storage  -->
			<#if (storages?exists) && (storages?size > 0) >
				<strong>ES3存储空间</strong>
				
				<#list storages as storage>
					<ul>
						<li><em>标识符</em>&nbsp;:${storage.identifier}</li>
						<li><em>存储类型</em>&nbsp;:
							<#list storageTypeMap?keys as k ><#if storage.storageType?string == k>${storageTypeMap[k]}</#if></#list>
						</li>
						<li><em>容量空间</em>&nbsp;:${storage.space}GB</li>
						<li><em>挂载实例</em>&nbsp;:${storage.mountComputes}</li>
						<br>
					</ul>
				</#list>
				
			</#if><!-- 存储 storage End-->
			
			<!-- 负载均衡器ELB -->
			<#if (elbs?exists) && (elbs?size > 0) >
				<strong>负载均衡器ELB</strong>
				
				<#list elbs as elb>
					<ul>
						<li><em>标识符</em>&nbsp;:${elb.identifier}</li>
						<li><em>是否保持会话</em>&nbsp;:
							<#list KeepSessionMap?keys as k ><#if elb.keepSession?string == k>${KeepSessionMap[k]}</#if></#list>
						</li>
						<li><em>关联实例</em>&nbsp;: 待完成 </li>
						<li><em>端口映射(协议、负载端口、实例端口)</em></li>
						
						<#list elb.elbPortItems as port>
							<ul>
								<li>&nbsp;&nbsp;${port.protocol}&nbsp;,&nbsp;${port.sourcePort}&nbsp;,&nbsp;${port.targetPort}</li>
							</ul>
						</#list>
						
						<br>
					</ul>
				</#list>
				
			</#if><!-- 负载均衡器ELB End -->
			
		</li>
		
		<#if passUrl?exists>
			<li>
				<strong>审批操作</strong>
				<ul>
					<li><a href="${passUrl}">1.同意</a></li>
					<li><a href="${disagreeContinueUrl}">2.不通过但继续</a></li>
					<li><a href="${disagreeReturnUrl}">3.不通过且退回</a></li>
				</ul>
			</li>
		</#if>
	</ul>

</body>
</html>


