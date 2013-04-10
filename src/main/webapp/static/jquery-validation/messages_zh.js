/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
 */
(function ($) {
	$.extend($.validator.messages, {
		required: "必选字段",
		remote: "请修正该字段",
		email: "请输入正确格式的电子邮件",
		url: "请输入合法的网址",
		date: "请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		equalTo: "请再次输入相同的值",
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: $.validator.format("请输入一个长度最多是 {0} 的字符串"),
		minlength: $.validator.format("请输入一个长度最少是 {0} 的字符串"),
		rangelength: $.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
		range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		max: $.validator.format("请输入一个最大为 {0} 的值"),
		min: $.validator.format("请输入一个最小为 {0} 的值")
	});
	
	$.extend($.validator.defaults,{
		errorClass: "text-error",
		errorElement: "span",
		highlight:function(element, errorClass, validClass) {
			$(element).closest('.control-group').addClass('error');
		},
		unhighlight: function(element, errorClass, validClass) {
			$(element).closest('.control-group').removeClass('error');
		}
	});
}(jQuery));


//增加IP或IP段的验证  eg. 192.168.1.1 & 192.168.1.1/255 & 192.168.1.1,192.168.1.2,192.168.1.3
jQuery.validator.addMethod("ipValidate", function(value, element) {
	var rExpression = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\/(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))?$|^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(\,((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)))*$/;
	return this.optional(element) || rExpression.test(value);
}, "IP格式不正确");

//增加IP的验证  eg. 192.168.1.1 
jQuery.validator.addMethod("ipAddressValidate", function(value, element) {
	var rExpression = /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))?$/;
	return this.optional(element) || rExpression.test(value);
}, "IP格式不正确");

//增加port的验证
jQuery.validator.addMethod("portValidate", function(value, element) {
	//eg. 192.168.1.1 & 192.168.1.1/255
	var rExpression = /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])(\/([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5]))?$|^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])((,([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5]))*)$/;
	return this.optional(element) || rExpression.test(value);
}, "端口格式不正确");

//增加hostName的验证
jQuery.validator.addMethod("hostNameValidate", function(value, element) {
	//eg. 192.168.1.1 & 192.168.1.1/255
	var rExpression = /^[\u0391-\uFFE5A-Za-z0-9]*[ \t][\u0391-\uFFE5A-Za-z0-9]*[ \t][0-9]*[\-][0-9]*([\-][0-9]*)?\d$/;
	return this.optional(element) || rExpression.test(value);
}, "请按 Company Model Rack-Site 命名");