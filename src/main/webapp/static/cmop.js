
/**
 * 将输入的各种参数插入到最后的详情table中
 * 注意对比table中td 的ID
 */
function displayInputParameter(){
	
	
	//标题
	$("#td_title").html($("#title").val());
	
	//起始日期
	$("#td_time").html( $("#serviceStart").val() + '&nbsp;至&nbsp;' + $("#serviceEnd").val());
	
	//申请用途
	$("#td_description").html($("#description").val());
	
	//资源类型
	resourceType = $("input[name='resourceType']:checked").val();
	if(resourceType == 1){
		$("#td_resourceType").html("生产资源");
	}else if(resourceType ==2){
		$("#td_resourceType").html("测试/演示资源 ");
	}else{
		$("#td_resourceType").html("公测资源 ");
	}
	
	/*接入服务*/
	account = $("#account").val() ;
	user = $("#accountUser").val();
	visitHost = $("#visitHost").val();
	$("#td_inVpnItem").html("<code>账号:</code>"+account+"<code>使用人:</code>"+user+"<code>需要访问主机:</code>"+visitHost);
	
	
	/*存储资源*/
	//存储资源:数据存储
	if($('#dataStorageType').is(":checked")){
		
		$("#tr_dataStorage").show();
		
		//容量空间
		space = $("input[name='dataSorageSpace']:checked").val();
		
		//Throughput（吞吐量）
	 	if($("input[name='dataStorageThroughput']:checked").val() === 1){
	 		throughput = "50 Mbps以内"; 
	 	}else{
	 		throughput = "50 Mbps以上"; 
	 	}
		
		//IOPS（每秒进行读写（I/O）操作的次数）
		iops = $("#dataStorageIops").val();
		
	 	$("#td_dataStorage").html("<code>存储类型:</code>数据存储<code>容量空间:</code>"+space+"G<code>吞吐量:</code>"+throughput+"<code>IOPS:</code>"+iops);
	 	
	}else{
		$("#tr_dataStorage").hide();
	}
	
	//存储资源:业务存储
	if($('#businessStorageType').is(":checked")){
		
		$("#tr_businessStorage").show();
		
		//容量空间
		space = $("input[name='businessStorageSpace']:checked").val();
		
		//Throughput（吞吐量）
	 	if($("input[name='businessStorageThroughput']:checked").val() === 1){
	 		throughput = "50 Mbps以内"; 
	 	}else{
	 		throughput = "50 Mbps以上"; 
	 	}
		
		//IOPS（每秒进行读写（I/O）操作的次数）
		iops = $("#businessStorageIops").val();
		
	 	$("#td_businessStorage").html("<code>存储类型:</code>业务存储<code>容量空间:</code>"+space+"G<code>吞吐量:</code>"+throughput+"<code>IOPS:</code>"+iops);
	}else{
		$("#tr_businessStorage").hide();
	}
	
	
	/*计算资源*/
	//服务器类型
	serverType = $("input[name='serverType']:checked").val();
	if(serverType == 1){
		serverTypeName = "Small &mdash; CPU[单核] Memory[1GB] Disk[20GB]";
	}else if(serverType ==2){
		serverTypeName = "Middle &mdash; CPU[双核] Memory[2GB] Disk[20GB]";
	}else{
		serverTypeName = "Large &mdash; CPU[四核] Memory[4GB] Disk[20GB]";
	}
	
	//实例数量
	serverCount = $("#serverCount").val();
	
	//操作系统
	if($("input[name='osBit']:checked")){
		
		//获得radio的父节点,用于取得最近的serverId
		osNode = $("input[name='osBit']:checked").parent().parent().parent();
		
		//给隐藏域osType赋上选中的操作类型ID用于提交到后台.
	 	$("#osType").val(osNode.find("#osId").val());
		
		osName = osNode.find("#osName").text();//操作系统名
	}
	
	//操作系统位数
 	if($("input[name='osBit']:checked").val() == 1){
 		osBit = "32 Bit"; 
 	}else{
 		osBit = "64 Bit"; 
 	}
	
	$("#td_osType").html(osName+" &mdash;"+osBit);
	
	$("#td_serverType").html("<code>服务器类型:</code>"+serverTypeName+"<code>实例数量:</code>"+serverCount);
	
	/*网络资源*/
	 typeStr ="";
	 var networkTypeChecked =  $("input[name='networkTypeCheckbox']:checked"); //获得"接入链路"的选中状态
	 var networkTypeCheckedLength = networkTypeChecked.length; //选中的个数.
	 
	 if(networkTypeCheckedLength == 1){//选中一个的情况下
		 
		 networkTypeChecked.each(function(){
		   	  if($(this).val()==1){
		   		  typeStr  += ",电信CTC";
		   		$("#networkType").val(1); //将值插入页面ID=networkType 的隐藏域中.
		   	  }else{
		   		  typeStr += ",联通CNC";
		   		$("#networkType").val(2);
		   	  }
		     });
		 
	 }else if(networkTypeCheckedLength == 2){//两个全选
		 $("#networkType").val(3);
	 }
 	  networkTypeStr = typeStr.substring(1, typeStr.length); //接入链路
     
     //接入速率
     band = $("input[name='networkBand']:checked").val();
     
   
     portStr ="";
     $("input[name='networkPort']:checked").each(function(){
   	  if($(this).val()==1){
   		  portStr  += ",FTP-21";
   	  }else if($(this).val()==2){
   		  portStr += ",Telnet-23";
   	  }else if($(this).val()==3){
   		  portStr += ",DNS-32 ";
   	  }else if($(this).val()==4){
   		  portStr += ",Http-80 ";
   	  }else if($(this).val()==5){
   		  portStr += ",Https-443 ";
   	  }else{
   		  portStr += ",www-8080 ";
   	  }
     });
     networkPort = portStr.substring(1, portStr.length);  //开放端口
     
     //公网IP
     networkOutIp = $("#networkOutIp").val();
     
     //解析类型
      first = $("input[name='analyseTypeFirst']:checked").val();
     if(first==1){
   	  analyseTypeFirst = "NS";
     }else if(first ==2){
   	  analyseTypeFirst = "MX";
   	  
     }else if(first == 3){
   	  analyseTypeFirst = "A";
     }else{
   	  analyseTypeFirst ="CHAME";
     }
     
      sec = $("input[name='analyseTypeSec']:checked").val();
      if(sec==1){
   	  analyseTypeSec = "NS";
     }else if(sec ==2){
   	  analyseTypeSec = "MX";
   	  
     }else if(sec == 3){
   	  analyseTypeSec = "A";
     }else{
   	  analyseTypeSec = "CHAME";
     }
     
     //解析完整域名
     domainFirst = $("#domainFirst").val();
     domainSec = $("#domainSec").val();
     
     //目标IP地址
     ipFirst = $("#ipFirst").val();
     ipSec = $("#ipSec").val();
     
     
     $("#td_network").html("<code>接入链路:</code>"+networkTypeStr+"<code>接入速率:</code>"+band+"M");
     $("#td_networkPort").html(networkPort);
     $("#td_networkOutIp").html(networkOutIp);
     
     $("#td_netDomainFirst").html("<code>解析类型:</code>"+analyseTypeFirst+"<code>解析完整域名:</code>"+domainFirst+"<code>目标IP地址:</code>"+ipFirst);
     $("#td_netDomainSec").html("<code>解析类型:</code>"+analyseTypeSec+"<code>解析完整域名:</code>"+domainSec+"<code>目标IP地址:</code>"+ipSec);
     
}


/**
 * 申请页面中,点击"下一步"和"后退"时切换Tab
 * ul的ID	:		myTab
 * 下一步按钮ID:	nextStep
 * 返回按钮ID:		backStep
 */
function switchTab(){
	
	var nextSteps = $("a[id^='nextStep']");
	nextSteps.click(function(){
		$('#myTab li:eq('+ (nextSteps.index(this) + 1) +') a').tab('show'); 
		displayInputParameter();
	 });
	
	var backSteps = $("a[id^='backStep']");
	backSteps.click(function(){
		
		$('#myTab li:eq('+backSteps.index(this)+') a').tab('show'); // Select 1 tab (0-indexed)
		
		displayInputParameter();
	 });
	
	var tabStep = $("#myTab li a");
	tabStep.click(function(){
		displayInputParameter();
	 });
	
}












