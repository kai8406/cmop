<html>
<head>

</head>
<body>

	<p><#if operateUrl?exists>${operateUrl}<#elseif operateDoneStr?exists>${operateDoneStr}<#else>你好,请审批以下内容.</#if></p>
	
	<ul>
		
		<#if failure?exists ><!-- 故障申报 Failure -->
			<li>
				<strong>故障申报基本信息</strong>
				<ul> 
					<li><em>申报人</em>&nbsp;: ${failure.user.name} </li>
					<li><em>申报标题</em>&nbsp;: ${failure.title}</li>
					<li><em>申报时间</em>&nbsp;: ${failure.createTime?string('yyyy-MM-dd HH:mm:ss')}</li>
					
					<li><em>故障类型</em>&nbsp;: 
						<#list applyServiceTypeMap?keys as k >
							<#if failure.faultType?string == k>
								${applyServiceTypeMap[k]}
							</#if>
						</#list>
					</li>
					
					<li><em>优先级</em>&nbsp;: 
						<#list priorityMap?keys as k >
							<#if failure.level?string == k>
								${priorityMap[k]}
							</#if>
						</#list>
					</li>
					
					<li><em> 故障现象及描述</em>&nbsp;: ${failure.description}</li>
				</ul>	
			</li>
		</#if> <!-- 故障申报 failure End -->
		
		<li>
			<!-- 实例Compute -->
			<#if (computes?exists) && (computes?size > 0) >
				<strong>计算资源(PCS,ECS)</strong>
				<#list computes as compute>
					<ul>
					
						<li><em>标识符</em>&nbsp;:${compute.identifier}</li>
						<li><em>用途信息</em>&nbsp;:${compute.remark}</li>
						<li><em>基本信息</em>&nbsp;:
						
							<#list osTypeMap?keys as k >
								<#if compute.osType?string == k>${osTypeMap[k]}</#if>
							</#list>
						
							<#list osBitMap?keys as k >
								<#if compute.osBit?string == k>${osBitMap[k]}</#if>
							</#list>
						
							<!-- 规格 1.PCS 2.ECS -->
							<#if compute.computeType == 1 >
								<#list pcsServerTypeMap?keys as k >
									<#if compute.serverType?string == k>${pcsServerTypeMap[k]}</#if>
								</#list>
							<#else>
								<#list ecsServerTypeMap?keys as k >
									<#if compute.serverType?string == k>${ecsServerTypeMap[k]}</#if>
								</#list>
							</#if>
						
					  </li>
						
						<li><em>关联ESG</em>&nbsp;:${compute.networkEsgItem.identifier}(${compute.networkEsgItem.description})</li>
						
						<br>
						
					</ul>
				</#list>
				
			</#if><!-- 实例Compute End-->
		
			<!-- 存储 storage  -->
			<#if (storages?exists) && (storages?size > 0) >
				我是存储~~
			</#if><!-- 存储 storage End-->
			
		</li>
		
	</ul>

</body>
</html>


