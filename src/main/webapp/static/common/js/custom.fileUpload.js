/**
 * 删除上传的附件 
 * 
 * @param obja标签链接对象.
 */
function deleteUpdateLoad(obj) {
	$.ajax({
		type: "GET",
		url: obj.href,
		dataType: "json",
		success: function(data) {
			if (data.success == true) {
				obj.parentNode.parentNode.removeChild(obj.parentNode); //javascript 原生函数找到父节点,删除子节点.
			} else {
				alert(data.message);
			}
		}
	});
}


