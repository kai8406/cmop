package com.sobey.cmop.mvc.utilities.jms.simple;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sobey.cmop.mvc.utilities.email.MimeMailService;
import com.sobey.cmop.mvc.utilities.email.SimpleMailService;

/**
 * 消息的异步被动接收者.
 * 
 * 使用Spring的MessageListenerContainer侦听消息并调用本Listener进行处理.
 * 
 * @author calvin
 * 
 */
public class NotifyMessageListener implements MessageListener {

	private static Logger logger = LoggerFactory.getLogger(NotifyMessageListener.class);

	@Autowired(required = false)
	private SimpleMailService simpleMailService;

	@Autowired(required = false)
	private MimeMailService mimeMailService;

	/**
	 * MessageListener回调函数.
	 */
	@Override
	public void onMessage(Message message) {
		try {
			MapMessage mapMessage = (MapMessage) message;
			// 打印消息详情
			logger.info("Name:{}, Email:{}", mapMessage.getString("name"), mapMessage.getString("email"));

			System.out.println("mimeMailService:" + mimeMailService);
			System.out.println(mapMessage.getString("name"));
			System.out.println("-----");

			// 发送邮件
			// if (mimeMailService != null) {
			// mimeMailService.sendNotificationMail(mapMessage.getString("name"));
			// }

			// 发送邮件
			if (simpleMailService != null) {
				simpleMailService.sendNotificationMail(mapMessage.getString("name"));
			}
		} catch (Exception e) {
			logger.error("处理消息时发生异常.", e);
		}
	}
}
