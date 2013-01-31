package com.sobey.cmop.mvc.service.email;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.User;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * MIME邮件服务类.
 * 
 * 由Freemarker引擎生成的的html格式邮件, 并带有附件.
 * 
 * @author liukai
 */
public class TemplateMailService {

	private static final String DEFAULT_ENCODING = "utf-8";

	private static Logger logger = LoggerFactory.getLogger(TemplateMailService.class);

	private JavaMailSender mailSender;

	private Template template;

	private Template applyTemplate;

	/**
	 * 注入Freemarker引擎配置,构造Freemarker 邮件内容模板.
	 */
	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
		// 根据freemarkerConfiguration的templateLoaderPath载入文件.
		template = freemarkerConfiguration.getTemplate("mailTemplate.ftl", DEFAULT_ENCODING);
		applyTemplate = freemarkerConfiguration.getTemplate("applyTemplate.ftl", DEFAULT_ENCODING);
	}

	public void sendApplyMail(Apply apply, AuditFlow auditFlow, List<ComputeItem> computes) {

		MimeMessage msg = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

			String sendFrom = "cmop_public@163.com"; // 发件人.以后通过读取配置文件获得.
			String sendTo = auditFlow.getUser().getEmail(); // 收件人
			String sendSubject = "资源申请审批邮件"; // 邮件标题

			Map<String, Object> map = Maps.newHashMap();
			map.put("apply", apply);
			map.put("auditFlow", auditFlow);
			map.put("computes", computes);
			
			map.put("serviceTypeMap", ApplyConstant.ServiceType.map);
			map.put("priorityMap", ApplyConstant.Priority.mapStr);
			
			map.put("computeTypeMap", ComputeConstant.ComputeType.map);
			map.put("ecsServerTypeMap", ComputeConstant.ECSServerType.map);
			map.put("pcsServerTypeMap", ComputeConstant.PCSServerType.map);
			

			String content = this.generateMailContent(applyTemplate, map);

			System.out.println("content:" + content);

			helper.setFrom(sendFrom);
			helper.setTo("sobey_public@163.com"); // 收件人.测试使用
			helper.setSubject(sendSubject);

			helper.setText(content, true);

			mailSender.send(msg);

			logger.info("HTML版邮件已发送至 " + sendTo);

		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}

	}

	/**
	 * 发送MIME格式的用户修改通知邮件.
	 */
	public void sendUserNotificationMail(User user) {

		try {
			MimeMessage msg = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

			helper.setFrom("cmop_public@163.com"); // 发件人
			helper.setTo("sobey_public@163.com"); // 收件人
			helper.setSubject("用户修改通知");

			Map<String, Object> map = Maps.newHashMap();
			map.put("user", user);
			String content = this.generateMailContent(template, map);

			helper.setText(content, true);

			mailSender.send(msg);

			logger.info("HTML版邮件已发送至sobey_public@163.com");

		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}

	/**
	 * 使用Freemarker生成html格式内容.
	 * 
	 * @param map
	 *            实现Collections.singletonMap()的方法.
	 * @return
	 * @throws MessagingException
	 */
	private String generateMailContent(Template template, Map<String, Object> map) throws MessagingException {
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
		} catch (IOException e) {
			logger.error("生成邮件内容失败, FreeMarker模板不存在", e);
			throw new MessagingException("FreeMarker模板不存在", e);
		} catch (TemplateException e) {
			logger.error("生成邮件内容失败, FreeMarker处理失败", e);
			throw new MessagingException("FreeMarker处理失败", e);
		}
	}

	/**
	 * Spring的MailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
