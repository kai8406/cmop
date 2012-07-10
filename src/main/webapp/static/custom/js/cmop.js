
/**
 * 将输入的各种参数插入到最后的详情table中
 * 注意对比table中td 的ID
 */
function displayInputParameter(){
	
	//标题
	titile = "服务申请名+当前用户名+创建时间";
	$("#td_title").html(titile);
	
	//申请用途
	$("#td_usage").html($("#usage").val());
	
	//资源类型
	resourceType = $("input[name='resourceType']:checked").val();
	if(resourceType == 1){
		$("#td_resourceType").html("生产资源");
	}else if(resourceType ==2){
		$("#td_resourceType").html("测试/演示资源 ");
	}else{
		$("#td_resourceType").html("公测资源 ");
	}
	
	//起始日期
	$("#td_time").html( $("#serviceStart").val() + '&nbsp;至&nbsp;' + $("#serviceEnd").val());
	
	
	
	/*存储资源*/
	if($("#otherSpace").is(":checked")){
		$("#otherSpace").val($("#otherSpaceValue").val());
	}
	space = $("input[name='storageSpace']:checked").val();	//容量空间
	$("#td_storage").html(space);
	
	
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
	instancesNum = $("#instancesNum").val();
	
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
	
	//$("#td_osType").html(osName+" &mdash;"+osBit);
	
	$("#td_serverType").html("<code>服务器类型:</code>"+serverTypeName+"<code>虚拟机数量:</code>"+instancesNum);
	
	/*网络资源*/
	 typeStr ="";
     $("input[name='networkType']:checked").each(function(){
   	  if($(this).val()==1){
   		  typeStr  += ",电信CTC";
   	  }else{
   		  typeStr += ",联通CNC";
   	  }
     });
 	  networkType = typeStr.substring(1, typeStr.length); //接入链路
     
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
     
     
     $("#td_network").html("<code>接入链路:</code>"+networkType+"<code>接入速率:</code>"+band+"M");
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
		
		//
		/*将已选择的资源显示在详情列表中*/ 
		$("tr.ResourcesDetail").remove();
		var str = "";
		$("#selectedResources #singleResources").each(function(){
			str += "<tr class='ResourcesDetail'>" +
					"<td>计算资源</td>" +
					"<td>"+
						$(this).children("#osName_SingleResources").text()+"&nbsp;"+ $(this).children("#osBitName__SingleResources").text()+
						"<code>规格:</code>"+$(this).children("#serverTypeName__SingleResources").text()+
						"<code>数量:</code>"+$(this).children("#serverCount__SingleResources").text()+"" +
					"</td>" +
					"</tr>"; 
			
		});
		
		//最后插入
		$("#formDetail tbody:last-child").append(str);
		
		//
		
		var osTypes= [];
		 var bits= [];
		 var serverTypeIds = [];
		 var serverCount = [];
		 
		$("#selectedResources #singleResources").each(function() {
			 var osId = $(this).find("#osId_SingleResources").text();
			 var checkOsBit = $(this).find("#osBitId__SingleResources").text();
			 var middleTypeId = $(this).find("#serverTypeId__SingleResources").text();
			 var middleCount = $(this).find("#serverCount__SingleResources").text();
			 
			
			 osTypes.push(osId);                
			 bits.push(checkOsBit);             
			 serverTypeIds.push(middleTypeId);   
			 serverCount.push(middleCount);      
			 
		});
		
	 	$("#osTypes").val(osTypes);
	  	$("#bits").val(bits);
	  	$("#serverTypeIds").val(serverTypeIds);
	  	$("#serverCount").val(serverCount);

	
		
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


/**
 *  根据bitID获得对应的Bit名称
 * @param bitId . 1-32 Bit ; 2-64 Bit
 * @returns {String}
 */
function getBitName(bitId){
	if(bitId == 1){
		return "32 Bit";
	}else{
		return "64 Bit";
	}
}


/**
 * 根据服务器类型ID获得服务器类型信息
 * @param serverTypeId 服务器类型ID. 1-Small；2-Middle；3-Large
 * @returns {String}
 */
function getServerTypeNameByServerTypeId(serverTypeId){
	
	if(serverTypeId == 1){
		return "Small &mdash;CPU[单核] Memory[1GB] Disk[20GB]";
	}else if(serverTypeId == 2 ){
		return "Middle &mdash; CPU[双核] Memory[2GB] Disk[20GB]";
	}else{
		return "Large &mdash; CPU[四核] Memory[4GB] Disk[20GB]";
	}
}

/**
 *  根据操作系统的ID获得操作系统的名称
 * @param osId 操作系统ID
 * @returns {String}
 */
function getOSNameByOSId(osId){
	if(osId == 1){
		return "Windwos2003R2";
	}else if(osId ==2){
		return "Windwos2008R2";
	}else if(osId ==3){
		return "Centos5.6";
	}else{
		return "Centos6.3";
	}
}

/**
 * 填入参数,获得插入至文本的div字符串.
 * @param osId 操作系统ID
 * @param bitValue 选中Bit的value
 * @param serverTypeId 服务器类型ID
 * @param serverCount 服务器数量
 * @returns
 */
function appendSingleResourcesStr(osId,bitValue,serverTypeId,serverCount){
	
	 str = "<div class='row alert' id='singleResources'>";
	 str +="<div class='span1' id='osName_SingleResources'>"+getOSNameByOSId(osId)+"</div>";
	 str +="<div class='hidden' id='osId_SingleResources'>"+osId+"</div>";
	 
	 str +="<div class='hidden' id='osBitId__SingleResources'>"+bitValue+"</div>";
	 str +="<div class='span1' id='osBitName__SingleResources'>"+getBitName(bitValue)+"</div>";
	 
	 str +="<div class='hidden' id='serverTypeId__SingleResources'>"+serverTypeId+"</div>";
	 str +="<div class='span4' id='serverTypeName__SingleResources'>"+getServerTypeNameByServerTypeId(serverTypeId)+"</div>";
	 
	 str +="<div class='span1' id='serverCount__SingleResources'>"+serverCount+"</div>";
	 
	 str +="<button class='close' data-dismiss='alert'>&times;</button></div>";
	 return str;
}

/**
 * 选择服务器.
 * @param object
 */
function selectServer(object,modalObject){
	
	 	modalId =  modalObject.parent().parent().attr("id"); //弹出框ID
	
	//所选择的OS
	var osId =  object.parent().parent().find("#osId").text();
	
	//所选择的bit:第一个OS下的位数.
	var checkOsBit = object.parent().parent().find("input[name^='osBit']:checked").val();
	
	
	var smallCount = modalObject.parent().parent().find("#smallServerCount").val();//所选规格small的数量
	 if(smallCount != ""){//数量框不为空
		 
		var smallTypeId =  $("#smallTypeId").text(); //服务器类型ID; 1 
		
		 //如果已有相同的OS,Bit和规格,则删除已有的.
		 $("#selectedResources #singleResources").each(function(){
			 
			 if($(this).children("#osId_SingleResources").text()== osId && $(this).children("#osBitId__SingleResources").text() == checkOsBit 
					 && $(this).children("#serverTypeId__SingleResources").text() == smallTypeId ){
				 $(this).remove();
			 }
		});
		 
		 $("#selectedResources ").append(appendSingleResourcesStr(osId,checkOsBit,smallTypeId,smallCount));
	 }    	 
	      	 
	      	 
	 var middleCount = modalObject.parent().parent().find("#middleServerCount").val();//所选规格middle的数量
	 if(middleCount != ""){//数量框不为空
		 
		 var middleTypeId =  $("#middleTypeId").text(); //服务器类型ID; 2 
		 
		 //如果已有相同的OS,Bit和规格,则删除已有的.
		 $("#selectedResources #singleResources").each(function(){
			 if($(this).children("#osId_SingleResources").text()== osId && $(this).children("#osBitId__SingleResources").text() == checkOsBit 
					 && $(this).children("#serverTypeId__SingleResources").text() == middleTypeId ){
				 $(this).remove();
			 }
		});
		 
		 $("#selectedResources ").append(appendSingleResourcesStr(osId,checkOsBit,middleTypeId,middleCount));
		 
	 }
	 
	 var largeCount = modalObject.parent().parent().find("#largeServerCount").val();;//所选规格large的数量
	 if(largeCount != ""){//数量框不为空
		 
		 
		 var largeTypeId =  $("#largeTypeId").text(); //服务器类型ID; 3 
		 
		 //如果已有相同的OS,Bit和规格,则删除已有的.
		 $("#selectedResources #singleResources").each(function(){
			 if($(this).children("#osId_SingleResources").text()== osId && $(this).children("#osBitId__SingleResources").text() == checkOsBit 
					 && $(this).children("#serverTypeId__SingleResources").text() == largeTypeId ){
				 $(this).remove();
			 }
		});
		 
		 $("#selectedResources ").append(appendSingleResourcesStr(osId,checkOsBit,largeTypeId,largeCount));
	 }
	 
	  
	
}




/**
 * 获得指定月份后的日期.
 * @param monthNum 指定多少月后
 * @returns {String}
 */
function getDateByMonthNum(monthNum){
	 var CurrentDate = new Date();
	 CurrentDate.setMonth(CurrentDate.getMonth()+monthNum+1);
	 
	 var year = CurrentDate.getFullYear();   
	 var month = CurrentDate.getMonth();
	 if(month <= 9){
    	 month = "0" + month;
    }
	 var day = CurrentDate.getDate();
	if (day <= 9) {
		day = "0" + day;
	}
	return year + "-" + month + "-" + day;
	 
}

/**
 * 为起始时间设置默认值.
 * 测试资源:默认从申请日起到1个月
 * 公测资源:默认从申请日起到3个月
 * 生产资源:默认从申请日起到6个月
 */
function inputServiceDate()   
{   
	$("input[name='resourceType']").click(function(){
		resourceType = $("input[name='resourceType']:checked").val();
		if (resourceType == 1) {
			// 生产资源
			$("#serviceEnd").val(getDateByMonthNum(6) ); // 6个月后的日期
		} else if (resourceType == 2) {//测试资源
			$("#serviceEnd").val(getDateByMonthNum(1)); // 一个月后的日期
		} else {
			// 公测资源
			$("#serviceEnd").val(getDateByMonthNum(3)); // 3个月后的日期
		}
	});
    
}
















