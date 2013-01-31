<html>
<head>你好,请审批以下内容.</head>
<body>
	<ul>
		<li>
			服务申请单基本信息
			<ul>
				<li>申请人：${apply.user.name} </li>
				<li>申请标题：${apply.title}</li>
				<li>申请时间：${apply.createTime}</li>
					
				<li>优先级： 
					<#list priorityMap?keys as k >
						<#if apply.priority?string == k>
							${priorityMap[k]}
						</#if>
					</#list>
				</li>
				
				<li>服务起止日期：${apply.serviceStart} 至 ${apply.serviceEnd}</li>
				<li>用途描述：${apply.description}  </li>
			</ul>	
		</li>
		
		
		<#if computes?exists>
		<li>
			实例信息(PCS,ECS)
			<#list computes as compute>
				<ul>
					<li>标识符：${compute.identifier}(${compute.remark})，${compute.osType}(${compute.osBit})${compute.serverType}</li>
					<li>关联ESG：${compute.networkEsgItem.identifier}(${compute.networkEsgItem.description})  内网IP:${compute.innerIp}</li>
					<br>
				</ul>
			</#list>
			
		</li>
		</#if>
		
		<li>
			审批操作
			<ul>
				<li><a href="#">1.同意</a></li>
				<li><a href="#">2.不通过但继续</a></li>
				<li><a href="#">3.不通过且退回</a></li>
			</ul>
		</li>
		
	</ul>

</body>
</html>


