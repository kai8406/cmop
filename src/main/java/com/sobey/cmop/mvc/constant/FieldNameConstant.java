package com.sobey.cmop.mvc.constant;

/**
 * 资源自身的参数. 用于变更项的说明.<br>
 * 和页面,freemarker模板某些参数对应!!!<br>
 * 
 * @author liukai
 * 
 */
public class FieldNameConstant {

	/**
	 * entity ComputeItem 里的参数<br>
	 * 
	 * <pre>
	 * 操作系统, 操作位数, 规格, 用途信息, ESG, 应用信息
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Compate {
		操作系统, 操作位数, 规格, 用途信息, ESG, 应用信息;
	}

	/**
	 * entity StorageItem 里的参数<br>
	 * 
	 * <pre>
	 * 存储类型, 容量空间, 挂载实例
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Storage {
		存储类型, 容量空间, 挂载实例;
	}

	/**
	 * entity NetworkElbItem 里的参数<br>
	 * 
	 * <pre>
	 * 是否保持会话, 端口信息, 关联实例;
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Elb {
		是否保持会话, 端口信息, 关联实例;
	}

}
