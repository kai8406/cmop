package com.sobey.cmop.mvc.constant;

import java.util.Map;

import com.google.common.collect.Maps;

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
	public enum UserStatus implements CommonEnum {

		ENABLED(0), DISABLED(1);

		private int code;

		// 构造器只能私有private,绝对不允许有public构造器.

		private UserStatus(int code) {
			this.code = code;
		}

		// 该方法根据传入的参数,返回enum中的文本. 实现该方法后,需要声明一个private
		// Map集合,将enum中的值迭代至Map集合中,key为enum的输入参数.value为enum的值.最后用Map集合的get方法获得获得value

		private static final Map<Integer, String> lookup = Maps.newHashMap();
		static {
			for (UserStatus status : UserStatus.values()) {

				lookup.put(status.code, status.name());

			}
		}

		public static String get(Integer code) {
			return lookup.get(code);
		}

		// 覆写toString 方法，在该方法中返回从构造函数中传入的参数

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

		// 同toString方法,不过返回的是Integer类型.可查看toInteger的说明得到更详细的信息

		@Override
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
	public enum DefaultGroups implements CommonEnum {

		admin(1), apply(2), audit(3);

		private int code;

		private DefaultGroups(int code) {
			this.code = code;
		}

		private static final Map<Integer, String> lookup = Maps.newHashMap();
		static {
			for (DefaultGroups group : DefaultGroups.values()) {

				lookup.put(group.code, group.name());

			}
		}

		public static String get(Integer code) {
			return lookup.get(code);
		}

		@Override
		public String toString() {
			return String.valueOf(this.code);
		}

		@Override
		public Integer toInteger() {
			return this.code;
		}

	}

	public static void main(String[] args) {
		System.out.println(UserStatus.get(1));
		System.out.println(DefaultGroups.get(3));
		System.out.println(DefaultGroups.admin);
		System.out.println(DefaultGroups.admin.name());
	}
}
