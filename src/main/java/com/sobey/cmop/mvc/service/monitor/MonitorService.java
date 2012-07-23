package com.sobey.cmop.mvc.service.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sobey.cmop.utils.RmtSshExecutor;
import com.sobey.cmop.utils.RmtTelnetExecutor;
import com.sobey.framework.utils.DateUtil;
import com.sobey.framework.utils.StringUtil;

@Component
public class MonitorService {
	private static Logger logger = LoggerFactory.getLogger(MonitorService.class);
	
	/**
	 * 接口方法
	 * @param idc
	 * @return
	 */
	public List getIdcNetworkInfo(String idc) {
        return IDCNetDevice.idcNetworkInfo;
	}
	
	/**
	 * 定时器执行方法：获取两两设备之间的网络质量
	 */
	public void idcNetworkInfo() {
        try {
        	String type;
        	String ip;
        	int port;
        	String user;
        	String pass;
        	System.out.println("\n--->Started Time："+DateUtil.getCurrentTime()+" "+IDCNetDevice.deviceList.size());
        	int count = 0;
        	for(int i=0; i<IDCNetDevice.deviceList.size(); i++) {
        		Map map = (HashMap)IDCNetDevice.deviceList.get(i);
        		type = (String) map.get(IDCNetDevice.TYPE);
        		ip = (String) map.get(IDCNetDevice.IP);
        		port = (Integer) map.get(IDCNetDevice.PORT);
        		user = (String) map.get(IDCNetDevice.USER);
        		pass = (String) map.get(IDCNetDevice.PASSWORD);
        		System.out.println("第"+(i+1)+"组："+ip+" "+user+" "+pass+" "+port);
         		
        		RmtTelnetExecutor telnetExecutor = null;
        		if (type.equals(IDCNetDevice.SWITCH)) { //交换机使用Telnet协议
        			telnetExecutor = new RmtTelnetExecutor(ip, port, user, pass);
        		}
         		String rtnMessage = null;
         		String rtnValue = null;
         		int lineNum;
         		List list = (ArrayList)map.get(IDCNetDevice.TARGET_DEVICE);
         		if(list!=null && list.size()!=0) {
         			for(int j=0; j<list.size(); j++){
         	  			Map subMap = (HashMap)list.get(j);
         	  			lineNum = Integer.parseInt(subMap.get(IDCNetDevice.LINE_NUMBER)+"");
         	  			ip = (String) subMap.get(IDCNetDevice.IP);
             			System.out.println("---设备"+(j+1)+"："+ip);
             			
             			if (type.equals(IDCNetDevice.FIREWALL)) { //防火墙使用SSH协议
             				RmtSshExecutor sshExecutor = new RmtSshExecutor("172.30.0.6", "admin", "1qaz!QAZ");
             				try {
                    			sshExecutor.exec("execute ping-options repeat-count 3");
                    			sshExecutor.exec("execute ping-options timeout 3");
                        		rtnMessage = sshExecutor.exec("execute ping "+ip);
//                        		System.out.println("--->rtnMessage:\n"+rtnMessage);
                			} catch (Exception e) {
							}
                     		rtnValue = sshExecutor.parseIdcNetworkInfo(rtnMessage);
                		} else { //交换机使用Telnet协议
                			try {
                    			rtnMessage = telnetExecutor.sendCommand("ping -c 3 "+ip);
                    			//System.out.println("--->rtnMessage:\n"+rtnMessage);
                			} catch (Exception e) {
							}
                			rtnValue = telnetExecutor.parseIdcNetworkInfo(rtnMessage);
                		}
                 		if (!StringUtil.isNullEmpty(rtnValue)) {
                 			System.out.println("--->更新第"+(i+1)+"("+lineNum+")条链路Ping值");
                 			updateIdcNetworkInfo(count, rtnValue);
                 		}
                 		count++;
         			}
             	}
         		if (telnetExecutor!=null) {
         			telnetExecutor.disconnect();
         		}
        	}
        	System.err.println("--->End Time："+DateUtil.getCurrentTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public void updateIdcNetworkInfo(int lineNum, String rtnValue) {
		String pingValue = rtnValue.split(",")[1];
		IDCNetDevice.idcNetworkInfo.set(lineNum, pingValue);
	}
	
}
