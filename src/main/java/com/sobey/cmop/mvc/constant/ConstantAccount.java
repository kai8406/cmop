package com.sobey.cmop.mvc.constant;

/**
 * Account 模块的静态常量
 * 
 * @author liukai
 * 
 */
public class ConstantAccount {

	/**
	 * 用户状态 <br>
	 * 0:enabled 有效 <br>
	 * 1:disabled 无效
	 * 
	 * @author liukai
	 * 
	 */
	public enum userStatus {

		ENABLED(0), DISABLED(1);

		private int code;

		private userStatus(int code) {
			this.code = code;
		}

		// 覆写toString 方法，在该方法中返回从构造函数中传入的参数
		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

		// 同toString方法,不过返回的是Integer类型.
		public Integer toInteger() {
			return this.code;
		}

	}

	/**
	 * 默认的Group类型
	 * <p>
	 * 1.admin 超级管理员<br>
	 * 2.apply 申请人 <br>
	 * 3.audit 审批人 <br>
	 * </P>
	 * 
	 * @author liukai
	 * 
	 */
	public enum defaultGroups {

		admin(1), apply(2), audit(3);

		private int code;

		private defaultGroups(int code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

		public Integer toInteger() {
			return this.code;
		}
	}

}
