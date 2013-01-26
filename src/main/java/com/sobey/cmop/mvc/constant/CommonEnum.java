package com.sobey.cmop.mvc.constant;

/**
 * 构造Enum的接口. 所有用于定义常量的Enum都必须实现该接口.<br>
 * 
 * <strong> 注意:一定要在Enum内写一个static的get方法 </strong> 该方法根据传入的参数,返回enum中的文本. <br>
 * 实现该方法后,需要声明一个 Map集合,将enum中的值迭代至Map集合中,key为enum的输入参数.value为enum的值. <br>
 * 最后用Map集合的get方法获得获得value.同时该Map也可以直接获得enum的Map
 * <p>
 * eg: <br>
 * <code>                                                                                                       
 * public enum EnumTest{ <br>                                                                                   
 * 		ONE,TWO; <br>                                                                                           
 * 		public static final Map<Integer, String> map = Maps.newHashMap();<br>                               
 * 		static {<br>                                                                                            
 * 			for (EnumTest enumTest : EnumTest.values()) {<br>                                                   
 * 				map.put(enumTest.code, enumTest.name());<br>                                                 
 * 			}<br>                                                                                               
 * 		}<br>                                                                                                   
 * public String get(Integer code) {<br>                                                                  
 * 		return map.get(code);<br>                                                                        
 * }<br>                                                                                                       
 * }<br>                                                                                                        
 * EnumTest.get(1) = ONE                                                                                        
 * </code>
 * </p>
 * 
 * @param code
 * 
 * @author liukai
 * 
 */
public interface CommonEnum {

	/**
	 * 在该方法中返回从构造函数中传入的参数,并且参数是Integer类型<br>
	 * 注意:Enum必须实现一个私有private的构造器,绝对不允许有public构造器.
	 * <p>
	 * eg: <br>
	 * <code> public enum EnumTest{ 
	 * ONE,TWO;  <br>
	 * private int code; <br>
	 * private EnumTest(int code) { <br>
	 * 		this.code = code; <br>
	 * 	} <br>
	 * } <br>
	 * EnumTest.ONE = 1 </code>
	 * </p>
	 * 
	 * @return
	 */
	public Integer toInteger();

}
