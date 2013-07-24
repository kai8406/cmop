package com.sobey.cmop.mvc.constant;

/**
 * 构造Enum的接口. 所有用于定义常量的Enum都必须实现该接口.
 * 
 * <strong> 注意:一定要在Enum内写一个static的get方法 </strong> 该方法根据传入的参数,返回enum中的文本. 实现该方法后,需要声明一个
 * Map集合,将enum中的值迭代至Map集合中,key为enum的输入参数.value为enum的值. 最后用Map集合的get方法获得获得value.同时该map方法也可以直接获得enum的Map
 * mapKeyStr方法主要是用于页面Freemarker 遍历的(Freemarker 遍历HashMap的数据,key不能为非String的值!)
 * 
 * <pre>
 * eg:
 * public enum EnumTest{                                                                                 
 * 		ONE,TWO;                                                                                         
 * 		public static final Map<Integer, String> map = Maps.newLinkedHashMap();                            
 * 		static {                                                                                         
 * 			for (EnumTest enumTest : EnumTest.values()) {                                                
 * 				map.put(enumTest.code, enumTest.name());                                              
 * 			}                                                                                            
 * 		}                                                                                                 
 * public String get(Integer code) {                                                               
 * 		return map.get(code);                                                                     
 * }                                                                                                      
 * }                                                                                                     
 * EnumTest.get(1) = ONE
 * </pre>
 * 
 * @param code
 * 
 * @author liukai
 * 
 */
public interface ICommonEnum {

	/**
	 * 在该方法中返回从构造函数中传入的参数,并且参数是Integer类型 注意:Enum必须实现一个私有private的构造器,绝对不允许有public构造器.
	 * 
	 * @return
	 */
	public Integer toInteger();

}
