package com.sobey.cmop.interfaces.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sobey.cmop.mvc.service.monitor.IDCNetDevice;
import com.sobey.cmop.mvc.service.monitor.MonitorService;

@Component("idcNetLinkServlet")
public class IDCNetLinkServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(IDCNetLinkServlet.class);
	
	@Resource
	private MonitorService monitorService;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String serverClassId = request.getParameter("serverClassId");
		try {
			logger.info("--->serverClassId: " + serverClassId);
			if (serverClassId==null || serverClassId.equals("")) {
				this.printString(response, "0:serverClassId is null");
			}
			
	    	List idcNetLinkList = monitorService.getIdcNetworkInfo("");
	    	System.out.println("--->"+idcNetLinkList.size());
	    	
	    	StringBuffer sb_xml = new StringBuffer();
	    	sb_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    	sb_xml.append("<idcNetLink id=\""+serverClassId+"\">");
	    	
	    	int index = 0;
        	for(int i=0; i<IDCNetDevice.deviceList.size(); i++) {
        		Map map = (HashMap)IDCNetDevice.deviceList.get(i);
        		String sourceIp = (String) map.get(IDCNetDevice.IP);
         		List list = (ArrayList)map.get(IDCNetDevice.TARGET_DEVICE);
         		if(list!=null && list.size()!=0){
         			for(int j=0; j<list.size(); j++){
         	  			Map subMap = (HashMap)list.get(j);
         	  			String targetIp = (String) subMap.get(IDCNetDevice.IP);
        	    		sb_xml.append("<link>");
        	    		sb_xml.append("    <source>").append(sourceIp).append("</source>");
        	    		sb_xml.append("    <target>").append(targetIp).append("</target>");
        	    		sb_xml.append("    <pingValue>").append(idcNetLinkList.get(index)).append("</pingValue>");
        	    		sb_xml.append("</link>");
        	    		index++;
         			}
         		}
        	}
	    	sb_xml.append("</idcNetLink>");
	    	System.out.println("--->"+sb_xml.toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.printString(response, "0:"+e.getMessage());
		}
	}

	protected void printString(HttpServletResponse response, String string) {
		response.setContentType("text/xml; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			out.write(string);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
