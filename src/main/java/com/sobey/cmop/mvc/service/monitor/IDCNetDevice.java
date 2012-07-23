package com.sobey.cmop.mvc.service.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;

import com.sobey.framework.utils.XmlUtil;

/**
 * IDC内部网络设备
 * @author Jason
 */
public class IDCNetDevice {
	private static final IDCNetDevice instance = new IDCNetDevice();
	public static List deviceList = initDeviceMap();
	public static int deviceCount;
	public static List idcNetworkInfo = new ArrayList();
	static {
		System.out.println("--->inital idcNetworkInfo..."+deviceCount);
		for (int i=0; i<deviceCount; i++) { //从编号1开始存值
			idcNetworkInfo.add(0.1); //new Object[] {i, 1}
		}
	}
	
	private IDCNetDevice() {
		
	}
	
	public static IDCNetDevice getInstance() {
		return instance;
	}
	
	//设备列表消息
    public static final String DEVICES = "devices";
//    public static final String SOURCE_DEVICE = "sourceDevice";
    public static final String TARGET_DEVICE = "targetDevice";
    public static final String TYPE = "type";
    public static final String IP = "ip";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String PORT = "port";
    public static final String LINE_NUMBER = "lineNum";
    
    public static final String FIREWALL = "firewall";
    public static final String SWITCH = "switch";
    
    public static List initDeviceMap() {
    	System.out.println("--->初始化设备列表");
    	List list = new ArrayList();
    	Document document = null;
    	try {
	        ClassPathResource resource = new ClassPathResource("monitor/idcNetDevice.xml");
	        document = XmlUtil.getDocument(resource.getInputStream());
	        if (document == null) {
	        	throw new Exception();
	        }
	        Element root = document.getRootElement();
	        Iterator iterator = root.elementIterator();
	        while (iterator.hasNext()) {
	            Element element = (Element) iterator.next();
	            String type="", ip="", user="", password="", number="", subIp="";
	            int port=23;
	           	Map deviceMap = new HashMap();
//	            if (SOURCE_DEVICE.equals(element.getName())){
	            	type = element.attributeValue(TYPE);
	            	ip = element.attributeValue(IP);
	            	user =  element.attributeValue(USER);
	            	password = element.attributeValue(PASSWORD);
	            	port =  Integer.parseInt(element.attributeValue(PORT));
	            	System.out.println("--->[type,ip]:"+type+","+ip);
	                Iterator subIterator = element.elementIterator();
	                deviceMap.put(TYPE, type);
	                deviceMap.put(IP, ip);
	                deviceMap.put(USER, user);
	                deviceMap.put(PASSWORD, password);
	                deviceMap.put(PORT, port);
	                List subList = new ArrayList();
	                while (subIterator.hasNext()){
	                      Map subMap = new HashMap();
	                	  Element subElement = (Element) subIterator.next();
//	                      if(TARGET_DEVICE.equals(subElement.getName())){
	                    	  number = subElement.attributeValue(LINE_NUMBER);
	                    	  subIp = subElement.attributeValue(IP);
	                    	  System.out.println("--->[subIp]:"+subIp);
	                    	  subMap.put(LINE_NUMBER, number);
	                    	  subMap.put(IP, subIp);
	                    	  deviceCount++;
//	                      }
	                      subList.add(subMap);
	                }
	                deviceMap.put(TARGET_DEVICE, subList);
//	            }
	            list.add(deviceMap);
	        }
	        System.out.println("--->初始化设备列表文件成功！");
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println("--->初始化设备列表文件失败！");
		}
    	return list;
    }
    
}
