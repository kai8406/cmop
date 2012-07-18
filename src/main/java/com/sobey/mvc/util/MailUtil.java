package com.sobey.mvc.util;

import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sobey.framework.utils.PropertiesLoader;
import com.sobey.mvc.Constant;
import com.sobey.mvc.entity.Apply;

public class MailUtil {
	private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

	private static final String MAIL_ADDRESS = "kai8406@163.com";

	/**
	 * 解析mail.properties 获得mail配置信息
	 */
	private static PropertiesLoader loader = new PropertiesLoader("classpath:/config.properties");

	public static void main(String[] args) {
		send(1, "", 1, MAIL_ADDRESS);
	}

	/**
	 * 发送邮件
	 * 
	 * @param applyId
	 * @param content
	 * @param userId
	 * @param sendTo
	 * @return
	 */
	public static boolean send(int applyId, String content, int userId, String sendTo) {
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		// 设定 Mail Server
		senderImpl.setHost("smtp.163.com");
		Properties property = new Properties();
		property.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		property.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(property);
		// SMTP验证时，需要用户名和密码
		senderImpl.setUsername("wlp_oo");
		senderImpl.setPassword("roadmap");
		// senderImpl.setUsername("wlp_1");
		// senderImpl.setPassword("I2loveyou6");

		// 建立邮件讯息
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(mailMessage, true, "GBK");
			// 设定寄件人、收件人、主题与内文
			messageHelper.setFrom("wlp_oo@163.com");
			// messageHelper.setTo("wenluping@sobey.com"); //sendTo
			messageHelper.setTo(MAIL_ADDRESS); // sendTo
			messageHelper.setSubject("资源申请审批邮件");

			StringBuffer html = new StringBuffer();
			html.append("<html><head></head>");
			html.append("<style type=\"text/css\">");
			html.append("body {font-family: 微软雅黑;font-size: 12px;}");
			html.append("a {font-family: 微软雅黑;font-size: 12px;}");
			html.append("div {font-family: 微软雅黑;font-size: 12px;}");
			html.append("</style>");
			html.append("<body>");

			html.append("你好！请审核以下内容：").append(MAIL_ADDRESS);
			html.append(content);
			html.append("<li><b>审批操作</b></li><br>");
			html.append(Constant.BLANK_SPACE);
			html.append("<a href=\"" + loader.getProperty("PASS_URL") + applyId + "&user_id=" + userId + "&result=1&opinion=\">1.同意</a>").append(Constant.BLANK_SPACE);
			html.append("<a href=\"" + loader.getProperty("DISAGREE_URL") + applyId + "&user_id=" + userId + "\">2.不通过但继续</a>").append(Constant.BLANK_SPACE);
			html.append("<a href=\"" + loader.getProperty("DISAGREE_URL") + applyId + "\">3.不通过且退回</a><br>");
			html.append("</p></body></html>");
			messageHelper.setText(html.toString(), true);

			// 传送邮件
			senderImpl.send(mailMessage);
			logger.info("--->发送邮件成功！");
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.info("--->发送邮件失败！");
			return false;
		}
	}

	/**
	 * 拼装邮件用的申请内容
	 * 
	 * @param apply
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String buildMailDesc(Apply apply, List list) {
		StringBuffer content = new StringBuffer();
		content.append("<li><b>申请的详细信息</b></li>").append("<br>");
		content.append(Constant.BLANK_SPACE).append("<u><b>1. 基本信息</b></u>").append("<br>");
		content.append(Constant.BLANK_SPACE).append(Constant.BLANK_SPACE).append("申请人：").append(apply.getUser().getName()).append("<br>");
		content.append(Constant.BLANK_SPACE).append(Constant.BLANK_SPACE).append("申请时间：").append(apply.getCreateTime()).append("<br>");
		content.append(Constant.BLANK_SPACE).append(Constant.BLANK_SPACE).append("申请标题：").append(apply.getTitle()).append("<br>");
		content.append(Constant.BLANK_SPACE).append(Constant.BLANK_SPACE).append("资源类型：").append(Constant.RESOURCE_TYPE.get(apply.getResourceType())).append("<br>");
		content.append(Constant.BLANK_SPACE).append(Constant.BLANK_SPACE).append("服务起止日期：").append(apply.getServiceStart()).append(" 至 ").append(apply.getServiceEnd()).append("<br>");
		content.append(Constant.BLANK_SPACE).append(Constant.BLANK_SPACE).append("用途描述：").append(apply.getDescription()).append("<br>");

		content.append(Constant.BLANK_SPACE).append("<u><b>2. 计算资源信息</b></u>").append("<br>");
		for (int i = 0; i < list.size(); i++) {
			Object[] objecct = (Object[]) list.get(i);
			content.append(Constant.BLANK_SPACE).append(Constant.BLANK_SPACE).append(Constant.OS_TYPE.get(objecct[1])).append("&nbsp;&nbsp;");
			content.append(Constant.OS_BIT.get(objecct[2])).append("&nbsp;&nbsp;");
			content.append(Constant.SERVER_TYPE.get(objecct[3])).append("&nbsp;&nbsp;");
			content.append("数量:" + objecct[4]).append("<br>");
		}

		content.append(Constant.BLANK_SPACE).append("<u><b>3. 存储资源信息</b></u>").append("<br>");
		content.append(Constant.BLANK_SPACE).append("<u><b>4. 网络资源信息</b></u>").append("<br><br>");
		return content.toString();
	}

}
