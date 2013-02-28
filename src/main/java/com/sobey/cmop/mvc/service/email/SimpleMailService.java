package com.sobey.cmop.mvc.service.email;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sobey.cmop.mvc.comm.BaseSevcie;

/**
 * 纯文本邮件服务类.
 * 
 * @author liukai
 */
public class SimpleMailService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(SimpleMailService.class);

	private JavaMailSender mailSender;

	/**
	 * 发送纯文本的通知邮件.<br>
	 * 
	 * <pre>
	 * to	 收件人邮箱
	 * subject	 邮件标题
	 * contentText 邮件Text文本内容
	 * </pre>
	 * 
	 * 
	 * @param map
	 */
	public void sendNotificationMail(String to, String subject, String contentText) {

		StringBuffer html = new StringBuffer();

		html.append("<html>");
		html.append("<html>");
		html.append("<head></head>");
		html.append("<body>");
		html.append(contentText);
		html.append("</body>");
		html.append("</html>");

		// 发件人.通过读取配置文件获得.
		String sendFrom = CONFIG_LOADER.getProperty("SENDFROM_EMAIL");

		// 收件人.测试使用
		String sendToTest = CONFIG_LOADER.getProperty("TEST_SENDTO_EMAIL");

		try {

			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, TemplateMailService.DEFAULT_ENCODING);

			helper.setFrom(sendFrom);
			helper.setTo(sendToTest); // 测试环境使用.
			// helper.setTo(sendTo); //生产环境使用.
			helper.setSubject(subject);
			helper.setText(contentText, true);

			mailSender.send(msg);

			if (logger.isInfoEnabled()) {
				logger.info("纯文本邮件已发送至 " + sendToTest);
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
