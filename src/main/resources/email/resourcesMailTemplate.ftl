<html>
<head>

<style type="text/css">
/* td 背景色 */
td.is-visible {
	color: #468847;
	background-color: #dff0d8 !important;
}

td.is-hidden { /* 	color: #ccc; */
	background-color: #f9f9f9 !important;
}


table {
  max-width: 80%;
  background-color: transparent;
  border-collapse: collapse;
  border-spacing: 0;
}

.table {
  width: 80%;
  margin-bottom: 20px;
}

.table th,
.table td {
  padding: 8px;
  line-height: 20px;
  text-align: left;
  vertical-align: top;
  border-top: 1px solid #dddddd;
}

.table th {
  font-weight: bold;
}

.table thead th {
  vertical-align: bottom;
}


.table tbody + tbody {
  border-top: 2px solid #dddddd;
}

.table .table {
  background-color: #ffffff;
}

.table-bordered {
  border: 1px solid #dddddd;
  border-collapse: separate;
  *border-collapse: collapse;
  border-left: 0;
  -webkit-border-radius: 4px;
     -moz-border-radius: 4px;
          border-radius: 4px;
}

.table-bordered th,
.table-bordered td {
  border-left: 1px solid #dddddd;
}
</style>
	
</head>
<body>
	
	<p><#if operateUrl?exists>${operateUrl}<#elseif operateDoneStr?exists>${operateDoneStr}<#else>你好,请审批以下内容.</#if></p>
	
	<ul>
		
		<!--服务标签 ServiceTag -->
		<li>
			<strong>服务标签基本信息</strong>
			<ul> 
				<li><em>申请人</em>&nbsp;: ${serviceTag.user.name} </li>
				<li><em>标签名</em>&nbsp;: ${serviceTag.name}</li>
				<li><em>申请时间</em>&nbsp;: ${serviceTag.createTime}</li>
				
				<li><em>优先级</em>&nbsp;: 
					<#list priorityMap?keys as k >
						<#if serviceTag.priority?string == k>
							${priorityMap[k]}
						</#if>
					</#list>
				</li>
				
				<li><em>服务起止日期</em>&nbsp;: ${serviceTag.serviceStart} <em>至</em>&nbsp; ${serviceTag.serviceEnd}</li>
				<li><em>用途描述</em>&nbsp;: ${serviceTag.description}</li>
			</ul>	
		</li> <!--服务标签 ServiceTag End -->
		
		<hr>
		
		<#if (resourcesList?exists) && (resourcesList?size > 0) >
		<li>
			<#list resourcesList as resource>
			
				<#list resource.changes as change>
				
					<p>变更资源标识符&nbsp;<strong>${resource.serviceIdentifier}</strong> &nbsp;&nbsp; 变更描述${change.description }</p>
					
					<table class="table table-bordered">
						<thead>
							<tr>
								<th style="width: 20%">变更项</th>
								<th style="width: 40%">旧值</th>
								<th style="width: 40%">新值</th>
							</tr>
						</thead>
						<tbody>
							
						<#list change.changeItems as item >
						
							<tr>
								<td>${item.fieldName}</td>
								
								<#if resource.serviceType == 1 || resource.serviceType == 2 ><!-- 实例Compute  -->
								
									<td class="is-hidden">
										${item.oldString}
									</td>
									
									<td class="is-visible">
										 ${item.newString}
									</td>
								
								<#elseif resource.serviceType == 3 ><!-- 存储 storage  -->
								
									<td class="is-hidden">
										${item.oldString}
									</td>
									
									<td class="is-visible">
										 ${item.newString}
									</td>
								
								<#elseif resource.serviceType == 4 ><!--   ELB  -->
									
									<td class="is-hidden">
										${item.oldString}
									</td>
									
									<td class="is-visible">
										 ${item.newString}
									</td>
									
								<#elseif resource.serviceType == 5 ><!--   EIP  -->
									
									<td class="is-hidden">
										${item.oldString}
									</td>
									
									<td class="is-visible">
										 ${item.newString}
									</td>
									
								<#elseif resource.serviceType == 6 ><!--   DNS  -->
								
									<td class="is-hidden">
										${item.oldString}
									</td>
									
									<td class="is-visible">
										 ${item.newString}
									</td>
									
								<#elseif resource.serviceType == 9 ><!--   monitorCompute  -->
									
									<td class="is-hidden">
									
										<#if item.fieldName == 'CPU占用率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == 'CPU占用率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>										
										<#elseif item.fieldName == 'CPU占用率警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '内存占用率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '内存占用率警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络丢包率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络丢包率警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '硬盘可用率报警阀值' >
											<#list thresholdLtMap?keys as k ><#if item.oldValue?string == k>${thresholdLtMap[k]}</#if></#list>
										<#elseif item.fieldName == '硬盘可用率警告阀值' >
											<#list thresholdLtMap?keys as k ><#if item.oldValue?string == k>${thresholdLtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络延时率报警阀值' >
											<#list thresholdNetGtMap?keys as k ><#if item.oldValue?string == k>${thresholdNetGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络延时率警告阀值' >
											<#list thresholdNetGtMap?keys as k ><#if item.oldValue?string == k>${thresholdNetGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '最大进程数报警阀值' >
											<#list maxProcessMap?keys as k ><#if item.oldValue?string == k>${maxProcessMap[k]}</#if></#list>
										<#elseif item.fieldName == '最大进程数警告阀值' >
											<#list maxProcessMap?keys as k ><#if item.oldValue?string == k>${maxProcessMap[k]}</#if></#list>
										<#elseif item.fieldName == '网卡流量报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网卡流量警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.oldValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#else>${item.oldValue}</#if>
										
									</td>
									
									<td class="is-visible">
									
										<#if item.fieldName == 'CPU占用率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == 'CPU占用率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>										
										<#elseif item.fieldName == 'CPU占用率警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '内存占用率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '内存占用率警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络丢包率报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络丢包率警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '硬盘可用率报警阀值' >
											<#list thresholdLtMap?keys as k ><#if item.newValue?string == k>${thresholdLtMap[k]}</#if></#list>
										<#elseif item.fieldName == '硬盘可用率警告阀值' >
											<#list thresholdLtMap?keys as k ><#if item.newValue?string == k>${thresholdLtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络延时率报警阀值' >
											<#list thresholdNetGtMap?keys as k ><#if item.newValue?string == k>${thresholdNetGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网络延时率警告阀值' >
											<#list thresholdNetGtMap?keys as k ><#if item.newValue?string == k>${thresholdNetGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '最大进程数报警阀值' >
											<#list maxProcessMap?keys as k ><#if item.newValue?string == k>${maxProcessMap[k]}</#if></#list>
										<#elseif item.fieldName == '最大进程数警告阀值' >
											<#list maxProcessMap?keys as k ><#if item.newValue?string == k>${maxProcessMap[k]}</#if></#list>
										<#elseif item.fieldName == '网卡流量报警阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#elseif item.fieldName == '网卡流量警告阀值' >
											<#list thresholdGtMap?keys as k ><#if item.newValue?string == k>${thresholdGtMap[k]}</#if></#list>
										<#else>${item.newValue}</#if>
										 
									</td>
									
								<#elseif resource.serviceType == 10 ><!--   monitorElb  -->
								
									<td class="is-hidden">
										${item.oldString}
									</td>
									
									<td class="is-visible">
										 ${item.newString}
									</td>
									
								<#else>
								
								
								</#if>
								
							</tr>
							
						</#list>
						
						</tbody>
					</table>
				
				</#list> 
			 
			</#list> 
			
		</li>
		</#if>
		
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


