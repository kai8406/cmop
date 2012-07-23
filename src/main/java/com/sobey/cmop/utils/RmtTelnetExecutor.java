package com.sobey.cmop.utils;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

import com.sobey.framework.utils.StringUtil;


public class RmtTelnetExecutor {
	private TelnetClient telnet = new TelnetClient();
	private InputStream in;
	private PrintStream out;
	private char prompt = '$';// 普通用户结束

	public RmtTelnetExecutor(String ip, int port, String user, String password) {
		try {
			System.err.println("--->Telnet connect...");
			telnet.connect(ip, port);
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());
			// 根据root用户设置结束符
			this.prompt = (user.equals("root") || (user.equals("admin"))) ? '#' : '>';
			login(user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 登录
	 * 
	 * @param user
	 * @param password
	 */
	public void login(String user, String password) {
		System.err.println("--->login..."+user);
		if (user.equals("admin")) {
			readUntil("FGT1KB3909601339 login:");
		} else {
			readUntil("Username:");
		}  
		write(user);
		readUntil("Password:");
		write(password);
		readUntil(prompt + "");
	}

	/**
	 * 读取分析结果
	 * 
	 * @param pattern
	 * @return
	 */
	public String readUntil(String pattern) {
		try {
			System.err.println("--->readUntil..."+pattern);
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			while (true) {
				sb.append(ch);
				if (ch == lastChar) {
//					System.out.println("--->while..."+sb.toString()+", "+lastChar+","+ch);
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}
				ch = (char) in.read();
//				System.out.print(ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 写操作
	 * 
	 * @param value
	 */
	public void write(String value) {
		try {
			out.println(value);
			out.flush();
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	/**
	 * 向目标发送命令字符串
	 * 
	 * @param command
	 * @return
	 */
	public String sendCommand(String command) {
		try {
			write(command);
			return readUntil(prompt + "");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("RmtTelnetExecutor.sendCommand() error...");
		}
		return null;
	}

	/**
	 * 关闭连接
	 */
	public void disconnect() {
		try {
			telnet.disconnect();
			System.err.println("--->Telnet disconnect...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析返回值
	 */
	public String parseIdcNetworkInfo(String rtnMessage) {
		try {
			if (!StringUtil.isNullEmpty(rtnMessage)) {
				int packetLossStart = rtnMessage.indexOf("received")+9;
				int packetLossEnd = rtnMessage.indexOf("packet loss");
				String packetLoss = rtnMessage.substring(packetLossStart, packetLossEnd).trim();
				System.out.println("--->packetLoss:"+packetLoss);
				
				if (packetLoss.equals("100.00%")) {
					return "100.00%,100";
				}
				int avgPingStart = rtnMessage.indexOf("min/avg/max")+13;
				int avgPingEnd = rtnMessage.lastIndexOf("ms");
				String avgPing = rtnMessage.substring(avgPingStart, avgPingEnd).trim();
				System.out.println("--->avgPing:"+avgPing+"-->"+avgPing.split("/")[1]+"ms");
				return packetLoss+","+avgPing.split("/")[1];
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "100.00%,100";
	}

	public static void main(String[] args) {
		String ip = "172.30.0.6";
		String user = "admin";
		String pass = "1qaz!QAZ";
		RmtTelnetExecutor executor = new RmtTelnetExecutor(ip, 23, user, pass);
		try {
			if (user.equals("admin")) {
				executor.sendCommand("execute ping-options repeat-count 3");
				executor.sendCommand("execute ping-options timeout 10");
			}
			String rtnMessage = executor.sendCommand("execute ping 10.224.219.249");
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
//			executor.sendCommand("#");
			rtnMessage = executor.sendCommand("execute ping 172.22.77.1");
//			Thread.sleep(5000);
//			rtnMessage = executor.sendCommand("#");
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("--->出现异常:"+e.getMessage());
		}
		executor.disconnect();
		/**/
		
/*		try {
			String ip = "192.168.100.254";
			String user = "h3cadmin";
			String pass = "L!@!T8Xdthinkpad";
			RmtTelnetExecutor executor = new RmtTelnetExecutor(ip, 23, user, pass);
			String rtnMessage = executor.sendCommand("ping -c 3 192.168.100.253");
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 192.168.100.252");
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 192.168.100.251");
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			executor.disconnect();
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("--->出现异常:"+e.getMessage());
		}*/	
		
	}
	
	public static void test3() {
		try {
			String ip = "172.30.0.1";
			String user = "h3cadmin";
			String pass = "L!@!T8Xdthinkpad@s0bey";
			RmtTelnetExecutor executor = new RmtTelnetExecutor(ip, 23, user, pass);
			String rtnMessage = executor.sendCommand("ping -c 3 172.30.0.2"); //C机柜-负载-主
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.3"); //C机柜-负载-备
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.4"); //C机柜-负载DNS-主备
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.22"); //D机柜
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.23"); //E机柜
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.32"); //F机柜-O
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
	
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.42"); //F机柜-M
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.25"); //G机柜
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.26"); //H机柜
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.27"); //I机柜
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.28"); //J机柜-I
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			rtnMessage = executor.sendCommand("ping -c 3 172.30.0.46"); //J机柜-M
//			System.out.println("--->rtnMessage:\n"+rtnMessage);
			executor.parseIdcNetworkInfo(rtnMessage);
			
			executor.disconnect();
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("--->出现异常:"+e.getMessage());
		}	
	}
	
}
