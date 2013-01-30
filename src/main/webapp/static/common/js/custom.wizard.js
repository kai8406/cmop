/**
 * 表单引导.点击"下一步"和"后退"时切换DIV
 * 
 * 切换的DIV的class 必须为 step.
 * 下一步按钮的class 必须为 nextStep
 * 返回按钮的class 必须为 backStep
 * 
 */
$(document).ready(function() {
	
	$(".step:not(:first)").addClass("hidden");//不是第一个的全部隐藏
	
	
	//下一步
	
	$(".nextStep").click(function() {
		
		var $this = $(this);
		
		if (!$this.closest("form").valid()) {return false;}//验证form.
		
		var index = $this.index(this); //索引
		
		$("div.step:eq(" + index + ")").removeClass("show").addClass("hidden");
		$("div.step:eq(" + (index + 1) + ")").removeClass("hidden").addClass("show");
		
	});
	
	
	//退回
	
	$(".backStep").click(function() {
		
		var index = $(this).index(this);
		
		$("div.step:eq(" + index + ")").removeClass("hidden").addClass("show");
		$("div.step:eq(" + (index + 1) + ")").removeClass("show").addClass("hidden");
		
		$("#resourcesList").empty();//清空各个资源申请的汇总信息
		
	});
});