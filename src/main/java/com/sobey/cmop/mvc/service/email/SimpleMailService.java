package com.sobey.cmop.mvc.service.email;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 纯文本邮件服务类.
 * 
 * @author liukai
 */
public class SimpleMailService {

	private static Logger logger = LoggerFactory.getLogger(SimpleMailService.class);

	private JavaMailSender mailSender;

	/**
	 * 发送纯文本的通知邮件.<br>
	 * 
	 * <pre>
	 * 
	 * from		 发件人邮箱.
	 * to		 收件人邮箱
	 * subject		 邮件标题
	 * contentText	 邮件Text文本内容
	 * 
	 * </pre>
	 * 
	 * 
	 * @param map
	 */
	public void sendNotificationMail(String from, String to, String subject, String contentText) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(contentText);

		try {

			mailSender.send(msg);

			if (logger.isInfoEnabled()) {
				logger.info("纯文本邮件已发送至{}", StringUtils.join(msg.getTo(), ","));
			}

		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}

	/**
	 * Spring的MailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
