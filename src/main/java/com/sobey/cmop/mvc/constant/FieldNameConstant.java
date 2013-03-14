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

	/**
	 * entity NetworkEipItem 里的参数<br>
	 * 
	 * <pre>
	 * ISP, 关联实例orELB, 端口信息
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Eip {
		ISP, 关联实例, 关联ELB, 端口信息
	}

	/**
	 * entity NetworkDnsItem 里的参数<br>
	 * 
	 * <pre>
	 * 域名, 域名类型, 目标IP,CNAME域名;
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum Dns {
		域名, 域名类型, 目标IP, CNAME域名;
	}

	/**
	 * entity MonitorElb 里的参数
	 * 
	 * <pre>
	 * 监控Elb
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum monitorElb {
		监控ELB;
	}

	/**
	 * entity MonitorCompute 里的参数
	 * 
	 * <pre>
	 * 监控端口, 监控进程, 挂载路径, 
	 * 监控实例, 
	 * CPU占用率报警阀值, CPU占用率警告阀值,
	 * 内存占用率报警阀值, 内存占用率警告阀值,
	 * 网络丢包率报警阀值, 网络丢包率警告阀值,
	 * 硬盘可用率报警阀值, 硬盘可用率警告阀值,
	 * 网络延时率报警阀值, 网络延时率警告阀值,
	 * 最大进程数报警阀值, 最大进程数警告阀值,
	 * 网卡流量报警阀值, 网卡流量警告阀值;
	 * </pre>
	 * 
	 * @author liukai
	 * 
	 */
	public enum monitorCompute {
		监控实例, 监控端口, 监控进程, 挂载路径,

		CPU占用率报警阀值, CPU占用率警告阀值,

		内存占用率报警阀值, 内存占用率警告阀值,

		网络丢包率报警阀值, 网络丢包率警告阀值,

		硬盘可用率报警阀值, 硬盘可用率警告阀值,

		网络延时率报警阀值, 网络延时率警告阀值,

		最大进程数报警阀值, 最大进程数警告阀值,

		网卡流量报警阀值, 网卡流量警告阀值;
	}

}
