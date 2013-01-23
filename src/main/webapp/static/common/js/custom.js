
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

 	// === reset ===//
 	
 	$("button.reset").click(function(){
 		var $form = $(this).closest("form"); 
 		$form.find("input[type=text]").val('');
 		$form.submit();
 	});
	
});