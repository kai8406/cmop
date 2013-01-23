
$(document).ready(function() {
	
	// === uniform === //
	
	 $("input:radio,input:checkbox,input:file").uniform();
	
	// === Tooltips === //
	 
	$('.tip').tooltip();	
	$('.tip-left').tooltip({ placement: 'left' });	
	$('.tip-right').tooltip({ placement: 'right' });	
	$('.tip-top').tooltip({ placement: 'top' });	
	$('.tip-bottom').tooltip({ placement: 'bottom' });	
	
	// === datepicker === //
	
 	$('.datepicker').datepicker();

 	// === Reset 点击reset按钮,form里的输入框情况,并提交form执行一次查询. ===//
 	
 	$("button.reset").click(function(){
 		var $form = $(this).closest("form"); 
 		$form.find("input[type=text]").val('');
 		$form.submit();
 	});
 	
 	// === option 点击More,得到更多的搜索条件===//
 	
 	$("button.options").click(function(){
 		
 		//图标切换
 		
 		$(this).find("i").toggleClass("icon-resize-small icon-resize-full");
 		
 		//搜索框切换+清空文本.
 		
 		var $options =$("div.options"); 
 		$options.find("select option:first").prop('selected', true);
 		$options.toggle(300).find("input[type=text]").val('');
 	});
 	
	
});