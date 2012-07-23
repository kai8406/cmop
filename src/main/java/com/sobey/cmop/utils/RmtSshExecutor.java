package com.sobey.cmop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.sobey.framework.utils.DateUtil;
import com.sobey.framework.utils.StringUtil;


import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 远程执行shell脚本类
 * @author wenlp
 */
public class RmtSshExecutor {
    /** 连接 */
    private Connection conn;
    /** 远程机器IP */
    private String ip;
    /** 用户名 */
    private String user;
    /** 密码 */
    private String password;
    private String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5; 

    /**
     * 构造函数
     * 
     * @param ip
     * @param user
     * @param password
     */
    public RmtSshExecutor(String ip, String user, String password) {
        this.ip = ip;
        this.user = user;
        this.password = password;
    }

    /**
     * 登录
     * 
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(user, password);
    }

    /**
     * 执行脚本
     * 
     * @param cmds
     * @return
     * @throws Exception
     */
    public String exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int rtnCode = -1;
        try {
            if (login()) {
                // Open a new {@link Session} on this connection
                Session session = conn.openSession();
                // Execute a command on the remote machine.
//              for (int i = 0; i < 10; i++) {
//                	session.execCommand(cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds+";"+cmds);
//				}
                session.execCommand(cmds);
                
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);
                
                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
                
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                
//                System.out.println("outStr=" + outStr);
//                System.out.println("outErr=" + outErr);
                
                rtnCode = session.getExitStatus();
            } else {
            	System.out.println("无法登录，创建远程连接失败！");
            }
        } catch (Exception e) {
			e.printStackTrace();
			System.out.println("RmtSshExecutor.exec() error...");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return outStr;
    }

    /**
     * @param in
     * @param charset
     * @return
     * @throws Exception
     */
    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
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

    public static void main(String args[]) throws Exception {
//      RmtSshExecutor rmtExecutor = new RmtSshExecutor("172.20.0.6", "root", "1q2w3e4r@88");
//      exe.exec("uname -a && date && uptime && who");
        //System.out.println(rmtExecutor.exec("rrdtool fetch /var/www/cacti/rra/2/854.rrd AVERAGE -s now-600"));
        
//    	for (int i = 0; i < 20; i++) {
    		System.out.println("\n--->Started Time："+DateUtil.getCurrentTime());
        	RmtSshExecutor rmtExecutor = new RmtSshExecutor("172.30.0.6", "admin", "1qaz!QAZ");
        	rmtExecutor.exec("execute ping-options repeat-count 1");
        	rmtExecutor.exec("execute ping-options timeout 3");
    		String rtnMessage = rmtExecutor.exec("execute ping 10.224.219.249");
//        	rmtExecutor.exec("execute ping-options repeat-count 1;execute ping-options timeout 3");
//        	String rtnMessage = rmtExecutor.exec("execute ping 10.224.219.249;execute ping 172.22.77.1");
//          System.out.println(rmtExecutor.parseIdcNetworkInfo(rtnMessage));
//          rtnMessage = rmtExecutor.exec("execute ping 172.22.77.1");
//          System.out.println(rmtExecutor.parseIdcNetworkInfo(rtnMessage));
//          Thread.sleep(12000);
            System.out.println("--->End Time："+DateUtil.getCurrentTime());
//		}
    }
}

