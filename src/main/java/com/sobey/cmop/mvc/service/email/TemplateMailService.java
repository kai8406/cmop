package com.sobey.cmop.mvc.service.email;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.framework.utils.PropertiesLoader;

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
@Service
@Transactional(readOnly = true)
public class TemplateMailService {

	private static final String DEFAULT_ENCODING = "utf-8";

	// 加载propertie文件

	public static PropertiesLoader loader = new PropertiesLoader("classpath:/config.properties");

	private static Logger logger = LoggerFactory.getLogger(TemplateMailService.class);

	private JavaMailSender mailSender;

	private Template applyTemplate;

	/**
	 * 注入Freemarker引擎配置,构造Freemarker 邮件内容模板.<br>
	 * 实现一个Template类,然后加载指定路劲(查看applicationContext-email.xml)的后缀为.ftl模板.<br>
	 * 可实现多个不同的模板.
	 * 
	 */
	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
		// 根据freemarkerConfiguration的templateLoaderPath载入文件.
		applyTemplate = freemarkerConfiguration.getTemplate("applyMailTemplate.ftl", DEFAULT_ENCODING);
	}

	/**
	 * 发送MIME格式的服务申请审批通知邮件.
	 */
	public void sendApplyNotificationMail(Apply apply, AuditFlow auditFlow, List<ComputeItem> computes) {

		MimeMessage msg = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

			// 发件人.通过读取配置文件获得.
			String sendFrom = loader.getProperty("SENDFROM_EMAIL");

			// 收件人.生成环境使用
			String sendTo = auditFlow.getUser().getEmail();

			// 收件人.测试使用
			String sendToTest = loader.getProperty("TEST_SENDTO_EMAIL");

			// 邮件标题
			String sendSubject = "资源申请审批邮件";

			Map<String, Object> map = Maps.newHashMap();

			// 服务申请Apply

			map.put("apply", apply);
			map.put("auditFlow", auditFlow);
			map.put("priorityMap", ApplyConstant.Priority.mapKeyStr);

			// 实例Compute

			map.put("computes", computes);
			map.put("osTypeMap", ComputeConstant.OS_TYPE_STRING_MAP);
			map.put("osBitMap", ComputeConstant.OS_BIT_STRING_MAP);
			map.put("computeTypeMap", ComputeConstant.ComputeType.mapKeyStr);
			map.put("pcsServerTypeMap", ComputeConstant.PCSServerType.mapKeyStr);
			map.put("ecsServerTypeMap", ComputeConstant.ECSServerType.mapKeyStr);

			// 审批Audit

			String content = this.generateMailContent(applyTemplate, map);

			// 邮件相关信息

			helper.setFrom(sendFrom);
			helper.setTo(sendToTest); // 测试环境使用.
			// helper.setTo(sendTo); //生产环境使用.
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
	 * 使用Freemarker生成html格式内容.<br>
	 * 
	 * @param template
	 *            freemarker的模板
	 * @param map
	 *            传入freemarker的参数.Key为参数名,value为传入参数的对象.
	 * 
	 * @return 通过模板得到的HTML内容
	 * 
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
