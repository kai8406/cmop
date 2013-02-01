<html>
<head>你好,请审批以下内容.</head>
<body>

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
						<#if apply.priority?string == k>
							${priorityMap[k]}
						</#if>
					</#list>
				</li>
				
				<li><em>服务起止日期</em>&nbsp;: ${apply.serviceStart} <em>至</em>&nbsp; ${apply.serviceEnd}</li>
				<li><em>用途描述</em>&nbsp;: ${apply.description}</li>
			</ul>	
		</li> <!--服务申请Apply End -->
		
		
		<!--实例Compute -->
		<#if (computes?exists) && (computes?size > 0) >
		<li>
			<strong>计算资源(PCS,ECS)</strong>
			<#list computes as compute>
				<ul>
				
					<li><em>标识符</em>&nbsp;:${compute.identifier}(${compute.remark})</li>
					
					<li><em>基本信息</em>&nbsp;:
					
						<#list osTypeMap?keys as k >
							<#if compute.osType?string == k>
								${osTypeMap[k]}
							</#if>
						</#list>
					
						<#list osBitMap?keys as k >
							<#if compute.osBit?string == k>
								${osBitMap[k]}
							</#if>
						</#list>
					
						<!-- 规格 1.PCS 2.ECS -->
						<#if compute.computeType == 1 >
							<#list pcsServerTypeMap?keys as k >
								<#if compute.serverType?string == k>
									${pcsServerTypeMap[k]}
								</#if>
							</#list>
						<#else>
							<#list ecsServerTypeMap?keys as k >
								<#if compute.serverType?string == k>
									${ecsServerTypeMap[k]}
								</#if>
							</#list>
						</#if>
					
				  </li>
					
					<li><em>关联ESG</em>&nbsp;:${compute.networkEsgItem.identifier}(${compute.networkEsgItem.description})  <em>内网IP</em>&nbsp;:${compute.innerIp}</li>
					
					<br>
					
				</ul>
			</#list>
			
		</li>
		</#if><!--实例Compute End-->
		
		<li>
			<strong>审批操作</strong>
			<ul>
				<li><a href="#">1.同意</a></li>
				<li><a href="#">2.不通过但继续</a></li>
				<li><a href="#">3.不通过且退回</a></li>
			</ul>
		</li>
		
	</ul>

</body>
</html>


