package com.sobey.cmop.mvc.service.iaas;

import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.StorageItem;
import com.sobey.cmop.mvc.entity.ToJson.ComputeJson;
import com.sobey.cmop.mvc.entity.ToJson.ElbJson;
import com.sobey.cmop.mvc.entity.ToJson.StorageJson;

/**
 * 将参数类型为Integer的字段转化成字符串后封装,以方便页面Json的操作.<br>
 * 比如 ComputeItem中的操作系统等字段是用Integer类型保存的.通过操作系统的值获得对应的操作系统文本后插入XXJson对象中<br>
 * 转后后的对象看entity文件下的ToJson中
 * 
 * @author liukai
 * 
 */
public interface ResourcesJsonService {

	/**
	 * 将ComputeItem转化成ComputeJson格式.
	 */
	public ComputeJson convertComputeJsonToComputeItem(ComputeItem computeItem);

	/**
	 * 将StorageItem转化成StorageJson格式.
	 * 
	 * @param storageItem
	 * @return
	 */
	public StorageJson convertStorageJsonToComputeItem(StorageItem storageItem);

	/**
	 * 将NetworkElbItem转化成ElbJson格式.
	 * 
	 * @param elbItem
	 * @return
	 */
	public ElbJson convertElbJsonToNetworkElbItem(NetworkElbItem elbItem);

}
