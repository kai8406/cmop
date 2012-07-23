/**
 * 申请页面中,点击"下一步"和"后退"时切换Tab
 * ul的ID	:		myTab
 * 下一步按钮ID:	nextStep
 * 返回按钮ID:		backStep
 */
function switchTab(){
	
	//下一步
	var nextSteps = $("a[id^='nextStep']");
	nextSteps.click(function(){
		
		if(!$("#inputForm").valid()){
			return false; 
		}
		
		$('#myTab li:eq('+ (nextSteps.index(this) + 1) +') a').tab('show'); 
		
		displayInputParameter();
		
		selectResources();
		
		mountES3();
		
	 });
	
	//退回
	var backSteps = $("a[id^='backStep']");
	backSteps.click(function(){
		
		$('#myTab li:eq('+backSteps.index(this)+') a').tab('show'); // Select 1 tab (0-indexed)
		
		displayInputParameter();
	 });
	
	//Tab切换
	var tabStep = $("#myTab li a");
	tabStep.click(function(){
		displayInputParameter();
	 });
	
		
}


/**
 * 将输入的各种参数插入到最后的详情table中
 * 注意对比table中td 的ID
 */
function displayInputParameter(){
	
	//申请用途
	$("#dd_description").html($("#description").val());
	
	//资源类型
	resourceType = $("input[name='resourceType']:checked").val();
	if(resourceType == 1){
		$("#dd_resourceType").html("生产资源");
	}else if(resourceType ==2){
		$("#dd_resourceType").html("测试/演示资源 ");
	}else{
		$("#dd_resourceType").html("公测资源 ");
	}
	
	//起始日期
	$("#dd_time").html( $("#serviceStart").val() + '&nbsp;至&nbsp;' + $("#serviceEnd").val());
	
	$("#mountDetail").empty();
	var str="";
	$("#ES3MountList #ES3Mount").each(function(){
		var space = $(this).find("#space").text();
		var ecsIdentifier = $(this).find("#showIdentifier").text();
		var storeageType = $(this).find("#storeageType_value").text();
		
		 
		str += "<hr>";
		str += "<dt>存储空间:</dt>";
		str += "<dd>"+space+"</dd>";
		
		str += "<dt>存储类型:</dt>";
		str += "<dd>"+storeageType+"</dd>";
		
		str += "<dt>绑定计算资源:</dt>";
		str += "<dd>"+ecsIdentifier+"</dd>";
		
		 
		
	});
	$("#mountDetail").append(str);//最后插入
	
	
	
	
	/*存储资源*/
	if($("#otherSpace").is(":checked")){
		$("#otherSpace").val($("#otherSpaceValue").val());
	}
	space = $("input[name='space']:checked").val();	//容量空间
	$("#dd_storage").html(space+"GB");
	
	
 	
}

/**
 * 选择计算资源
 */
function selectResources(){
	//
	/*将已选择的资源显示在详情列表中*/ 
	$("#ResourcesDetail").empty();//清空显示列表
	var str = "";
	$("#selectedResources #singleResources").each(function(){
		str += "<dd>"+$(this).children("#osName_SingleResources").text()+"&nbsp;"+ $(this).children("#osBitName__SingleResources").text()+"<strong>规格:</strong>"+$(this).children("#serverTypeName__SingleResources").text()+"<strong>数量:</strong> "+$(this).children("#serverCount__SingleResources").text()+"</dd>";
	});
	$("#ResourcesDetail").append(str);//最后插入
	
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
}

function mountES3(){
	
	var ecsIds = [];
	var spaces=[];
	var spaceIds=[];
	var storeageTypes=[];
	$("#ES3MountList #ES3Mount").each(function(){
		var ecsId = $(this).find("#checkedES3Id").text();
		var space = $(this).find("#space_value").text();
		var spaceId = $(this).find("#space_Id").text();
		var storeageType = $(this).find("#storeageType_id").text();
		ecsIds.push(ecsId);
		spaces.push(space);
		spaceIds.push(spaceId);
		storeageTypes.push(storeageType);
	});
	
	$("#ecsIds").val(ecsIds);
	$("#spaces").val(spaces);
	$("#spaceIds").val(spaceIds);
	$("#storeageTypes").val(storeageTypes);
	
	
	
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
	
	
	var smallCount = modalObject.parent().parent().find("#small_ServerCount").val();//所选规格small的数量
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
	      	 
	      	 
	 var middleCount = modalObject.parent().parent().find("#middle_ServerCount").val();//所选规格middle的数量
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
	 
	 var largeCount = modalObject.parent().parent().find("#large_ServerCount").val();;//所选规格large的数量
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

function fGetStoreageTypeValueById(id){
	if(id == 1 ){
		return "Throughput";
	}else{
		
		return "IOPS";
	}
}
















