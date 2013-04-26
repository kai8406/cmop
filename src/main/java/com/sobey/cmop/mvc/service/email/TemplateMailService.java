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
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.sobey.cmop.mvc.constant.ApplyConstant;
import com.sobey.cmop.mvc.constant.AuditConstant;
import com.sobey.cmop.mvc.constant.CPConstant;
import com.sobey.cmop.mvc.constant.ComputeConstant;
import com.sobey.cmop.mvc.constant.IpPoolConstant;
import com.sobey.cmop.mvc.constant.MdnConstant;
import com.sobey.cmop.mvc.constant.MonitorConstant;
import com.sobey.cmop.mvc.constant.NetworkConstant;
import com.sobey.cmop.mvc.constant.RedmineConstant;
import com.sobey.cmop.mvc.constant.StorageConstant;
import com.sobey.cmop.mvc.entity.Apply;
import com.sobey.cmop.mvc.entity.Audit;
import com.sobey.cmop.mvc.entity.AuditFlow;
import com.sobey.cmop.mvc.entity.ComputeItem;
import com.sobey.cmop.mvc.entity.CpItem;
import com.sobey.cmop.mvc.entity.Failure;
import com.sobey.cmop.mvc.entity.MdnItem;
import com.sobey.cmop.mvc.entity.MonitorCompute;
import com.sobey.cmop.mvc.entity.MonitorElb;
import com.sobey.cmop.mvc.entity.NetworkDnsItem;
import com.sobey.cmop.mvc.entity.NetworkEipItem;
import com.sobey.cmop.mvc.entity.NetworkElbItem;
import com.sobey.cmop.mvc.entity.ServiceTag;
import com.sobey.cmop.mvc.entity.StorageItem;
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
public class TemplateMailService extends BaseSevcie {

	public static final String DEFAULT_ENCODING = "utf-8";

	private static Logger logger = LoggerFactory.getLogger(TemplateMailService.class);

	private JavaMailSender mailSender;

	private Template detailTemplate;

	private Template changeTemplate;

	/**
	 * 根据freemarkerConfiguration的templateLoaderPath载入文件.
	 * 
	 * <pre>
	 * 注入Freemarker引擎配置,构造Freemarker 邮件内容模板. 
	 * 实现一个Template类,然后加载指定路劲(查看applicationContext-email.xml)的后缀为.ftl模板. 
	 * 可实现多个不同的模板.
	 * </pre>
	 * 
	 */
	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
		detailTemplate = freemarkerConfiguration.getTemplate("detailMailTemplate.ftl", DEFAULT_ENCODING);
		changeTemplate = freemarkerConfiguration.getTemplate("changeMailTemplate.ftl", DEFAULT_ENCODING);
	}

	/**
	 * freemarker 中使用的常量的Map
	 * 
	 * @return
	 */
	private Map<String, Object> freemarkerParameterMap() {

		Map<String, Object> map = Maps.newHashMap();

		map.put("osTypeMap", ComputeConstant.OS_TYPE_STRING_MAP);
		map.put("osBitMap", ComputeConstant.OS_BIT_STRING_MAP);

		map.put("priorityMap", RedmineConstant.Priority.mapKeyStr);
		map.put("computeTypeMap", ComputeConstant.ComputeType.mapKeyStr);
		map.put("pcsServerTypeMap", ComputeConstant.PCSServerType.mapKeyStr);
		map.put("ecsServerTypeMap", ComputeConstant.ECSServerType.mapKeyStr);
		map.put("applyServiceTypeMap", ApplyConstant.ServiceType.mapKeyStr);
		map.put("storageTypeMap", StorageConstant.StorageType.mapKeyStr);
		map.put("KeepSessionMap", NetworkConstant.KeepSession.mapKeyStr);
		map.put("ispTypeMap", NetworkConstant.ISPType.mapKeyStr);
		map.put("domainTypeMap", NetworkConstant.DomainType.mapKeyStr);
		map.put("thresholdGtMap", MonitorConstant.THRESHOLD_GT_STRING_KEY);
		map.put("thresholdLtMap", MonitorConstant.THRESHOLD_LT_STRING_KEY);
		map.put("thresholdNetGtMap", MonitorConstant.THRESHOLD_NET_GT_STRING_KEY);
		map.put("maxProcessMap", MonitorConstant.MAX_PROCESS_STRING_KEY);

		map.put("encoderModeMap", MdnConstant.EncoderMode.mapKeyStr);
		map.put("outputModeMap", MdnConstant.OutputMode.mapKeyStr);
		map.put("bandwidthMap", MdnConstant.BANDWIDTH_MAP_STRING_KEY);

		map.put("videoOutputWayMap", CPConstant.VideoOutputWay.mapKeyStr);
		map.put("recordTypeMap", CPConstant.RecordType.mapKeyStr);
		map.put("exportEncodeMap", CPConstant.EXPORTENCODE_MAP_STRING_KEY);
		map.put("recordBitrateMap", CPConstant.RECORDBITRATE_MAP_STRING_KEY);
		map.put("isPushCtpMap", CPConstant.IsPushCtp.mapKeyStr);

		map.put("DEFAULT_IPADDRESS", IpPoolConstant.DEFAULT_IPADDRESS);

		return map;

	}

	// ==========================//
	// ========= Apply ==========//
	// ==========================//

	/**
	 * 发送MIME格式的服务申请审批通知邮件.
	 */
	public void sendApplyNotificationMail(Apply apply, AuditFlow auditFlow) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		// 服务申请Apply

		map.put("apply", apply);

		map.put("computes", apply.getComputeItems());
		map.put("storages", apply.getStorageItems());
		map.put("elbs", apply.getNetworkElbItems());
		map.put("eips", apply.getNetworkEipItems());
		map.put("dnses", apply.getNetworkDnsItems());
		map.put("monitorComputes", apply.getMonitorComputes());
		map.put("monitorElbs", apply.getMonitorElbs());
		map.put("monitorMails", apply.getMonitorMails());
		map.put("monitorPhones", apply.getMonitorPhones());
		map.put("mdns", apply.getMdnItems());
		map.put("cps", apply.getCpItems());

		// 申请 审批Audit

		String passUrl = CONFIG_LOADER.getProperty("APPLY_PASS_URL") + "?applyId=" + apply.getId() + "&userId=" + auditFlow.getUser().getId() + "&result=" + AuditConstant.Result.同意;
		String disagreeContinueUrl = CONFIG_LOADER.getProperty("APPLY_DISAGREE_URL") + "/" + apply.getId() + "?userId=" + auditFlow.getUser().getId() + "&result=" + AuditConstant.Result.不同意但继续;
		String disagreeReturnUrl = CONFIG_LOADER.getProperty("APPLY_DISAGREE_URL") + "/" + apply.getId() + "?userId=" + auditFlow.getUser().getId() + "&result=" + AuditConstant.Result.不同意且退回;

		map.put("passUrl", passUrl);
		map.put("disagreeContinueUrl", disagreeContinueUrl);
		map.put("disagreeReturnUrl", disagreeReturnUrl);

		// 邮件标题
		String sendSubject = "资源申请审批邮件";

		this.sendMailConfig(detailTemplate, map, auditFlow.getUser(), sendSubject);

	}

	/**
	 * 发送工单处理邮件(服务申请Apply)
	 */
	public void sendApplyOperateNotificationMail(Apply apply, User assigneeUser) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		// 服务申请Apply

		map.put("apply", apply);

		map.put("computes", apply.getComputeItems());
		map.put("storages", apply.getStorageItems());
		map.put("elbs", apply.getNetworkElbItems());
		map.put("eips", apply.getNetworkEipItems());
		map.put("dnses", apply.getNetworkDnsItems());
		map.put("monitorComputes", apply.getMonitorComputes());
		map.put("monitorElbs", apply.getMonitorElbs());
		map.put("monitorMails", apply.getMonitorMails());
		map.put("monitorPhones", apply.getMonitorPhones());
		map.put("mdns", apply.getMdnItems());
		map.put("cps", apply.getCpItems());

		// 工单处理URL

		String operateUrl = "你有新的服务申请处理工单. <a href=\"" + CONFIG_LOADER.getProperty("OPERATE_URL") + "\">&#8594点击进行处理</a><br>";

		map.put("operateUrl", operateUrl);

		// 邮件标题
		String sendSubject = "工单处理邮件";

		this.sendMailConfig(detailTemplate, map, assigneeUser, sendSubject);

	}

	/**
	 * 工单处理结束,通知申请人邮件(服务申请Apply)
	 */
	public void sendApplyOperateDoneNotificationMail(Apply apply) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		// 服务申请Apply

		map.put("apply", apply);
		map.put("computes", apply.getComputeItems());
		map.put("storages", apply.getStorageItems());
		map.put("elbs", apply.getNetworkElbItems());
		map.put("eips", apply.getNetworkEipItems());
		map.put("dnses", apply.getNetworkDnsItems());
		map.put("monitorComputes", apply.getMonitorComputes());
		map.put("monitorElbs", apply.getMonitorElbs());
		map.put("monitorMails", apply.getMonitorMails());
		map.put("monitorPhones", apply.getMonitorPhones());
		map.put("mdns", apply.getMdnItems());
		map.put("cps", apply.getCpItems());

		// 工单处理完成提示文字

		String operateDoneStr = "工单处理流程已完成.如果申请了VPN账号,请向申请资源负责人索取.<a href=\"" + CONFIG_LOADER.getProperty("RESOURCE_URL") + "\">&#8594点击查看</a><br>";

		map.put("operateDoneStr", operateDoneStr);

		// 邮件标题
		String sendSubject = "服务申请工单处理邮件";

		this.sendMailConfig(detailTemplate, map, apply.getUser(), sendSubject);

	}

	// ==========================//
	// ======= Resources ========//
	// ==========================//

	/**
	 * 发送MIME格式的资源变更审批通知邮件.
	 */
	public void sendResourcesNotificationMail(ServiceTag serviceTag, AuditFlow auditFlow, Audit audit) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		// 服务标签ServiceTag

		map.put("serviceTag", serviceTag);

		map.put("resourcesList", comm.resourcesService.getCommitedResourcesListByServiceTagId(serviceTag.getId()));

		// 变更 审批Audit

		String passUrl = CONFIG_LOADER.getProperty("RESOURCES_PASS_URL") + "?serviceTagId=" + serviceTag.getId() + "&userId=" + auditFlow.getUser().getId() + "&result=" + AuditConstant.Result.同意;

		String disagreeContinueUrl = CONFIG_LOADER.getProperty("RESOURCES_DISAGREE_URL") + "/" + serviceTag.getId() + "?userId=" + auditFlow.getUser().getId() + "&result="
				+ AuditConstant.Result.不同意但继续 + "&auditId=" + audit.getId();

		String disagreeReturnUrl = CONFIG_LOADER.getProperty("RESOURCES_DISAGREE_URL") + "/" + serviceTag.getId() + "?userId=" + auditFlow.getUser().getId() + "&result=" + AuditConstant.Result.不同意且退回
				+ "&auditId=" + audit.getId();
		;

		map.put("passUrl", passUrl);
		map.put("disagreeContinueUrl", disagreeContinueUrl);
		map.put("disagreeReturnUrl", disagreeReturnUrl);

		// 邮件标题
		String sendSubject = "资源变更审批邮件";

		this.sendMailConfig(changeTemplate, map, auditFlow.getUser(), sendSubject);

	}

	/**
	 * 发送工单处理邮件(资源变更Resources)
	 */
	public void sendResourcesOperateNotificationMail(ServiceTag serviceTag, User assigneeUser) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		// 服务标签ServiceTag

		map.put("serviceTag", serviceTag);

		map.put("resourcesList", comm.resourcesService.getOperateResourcesListByServiceTagId(serviceTag.getId()));

		// 工单处理URL

		String operateUrl = "你有新的资源变更处理工单. <a href=\"" + CONFIG_LOADER.getProperty("OPERATE_URL") + "\">&#8594点击进行处理</a><br>";

		map.put("operateUrl", operateUrl);

		// 邮件标题
		String sendSubject = "工单处理邮件";

		this.sendMailConfig(changeTemplate, map, assigneeUser, sendSubject);

	}

	/**
	 * 工单处理结束,通知申请人邮件(资源变更Resources)
	 */
	public void sendResourcesOperateDoneNotificationMail(ServiceTag serviceTag) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		// 服务标签ServiceTag

		map.put("serviceTag", serviceTag);

		map.put("resourcesList", comm.resourcesService.getOperateResourcesListByServiceTagId(serviceTag.getId()));

		// 工单处理完成提示文字

		String operateDoneStr = "工单处理流程已完成.如果申请了VPN账号,请向申请资源负责人索取.<a href=\"" + CONFIG_LOADER.getProperty("RESOURCE_URL") + "\">&#8594点击查看</a><br>";

		map.put("operateDoneStr", operateDoneStr);

		// 邮件标题
		String sendSubject = "资源变更处理邮件";

		this.sendMailConfig(changeTemplate, map, serviceTag.getUser(), sendSubject);

	}

	// ==========================//
	// ========= Recycle ========//
	// ==========================//

	/**
	 * 发送工单处理邮件(资源回收)
	 */
	public void sendRecycleResourcesOperateNotificationMail(User user, List<ComputeItem> computeItems, List<StorageItem> storageItems, List<NetworkElbItem> elbItems, List<NetworkEipItem> eipItems,
			List<NetworkDnsItem> dnsItems, List<MonitorCompute> monitorComputes, List<MonitorElb> monitorElbs, List<MdnItem> mdnItems, List<CpItem> cpItems, User assigneeUser) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		map.put("computes", computeItems);
		map.put("storages", storageItems);
		map.put("elbs", elbItems);
		map.put("eips", eipItems);
		map.put("dnses", dnsItems);
		map.put("monitorComputes", monitorComputes);
		map.put("monitorElbs", monitorElbs);
		map.put("mdns", mdnItems);
		map.put("cps", cpItems);

		// 工单处理URL

		String operateUrl = "你有新的资源回收工单处理. <a href=\"" + CONFIG_LOADER.getProperty("OPERATE_URL") + "\">&#8594点击进行处理</a><br>";

		map.put("operateUrl", operateUrl);

		// 邮件标题
		String sendSubject = "资源回收工单处理邮件";

		this.sendMailConfig(detailTemplate, map, assigneeUser, sendSubject);

	}

	/**
	 * 工单处理结束,通知申请人邮件(资源回收)
	 * 
	 * @param sendToUser
	 *            收件人.生成环境使用
	 */
	public void sendRecycleResourcesOperateDoneNotificationMail(User sendToUser) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		// 工单处理完成提示文字

		String operateDoneStr = "资源回收工单处理流程已完成.<a href=\"" + CONFIG_LOADER.getProperty("RESOURCE_URL") + "\">&#8594点击查看</a><br>";

		map.put("operateDoneStr", operateDoneStr);

		// 邮件标题
		String sendSubject = "资源回收处理邮件";

		this.sendMailConfig(detailTemplate, map, sendToUser, sendSubject);

	}

	// ==========================//
	// ========= Failure ========//
	// ==========================//

	/**
	 * 发送MIME格式的工单处理邮件(故障申报Failure).
	 */
	public void sendFailureResourcesNotificationMail(Failure failure, List<ComputeItem> computeItems, List<StorageItem> storageItems, List<NetworkElbItem> elbItems, List<NetworkEipItem> eipItems,
			List<NetworkDnsItem> dnsItems, List<MonitorCompute> monitorComputes, List<MonitorElb> monitorElbs, List<MdnItem> mdnItems, List<CpItem> cpItems, User assigneeUser) {

		// 初始化数据,并将其放入一个HashMap中.

		Map<String, Object> map = this.freemarkerParameterMap();

		map.put("failure", failure);
		map.put("computes", computeItems);
		map.put("storages", storageItems);
		map.put("elbs", elbItems);
		map.put("eips", eipItems);
		map.put("dnses", dnsItems);
		map.put("monitorComputes", monitorComputes);
		map.put("monitorElbs", monitorElbs);
		map.put("mdns", mdnItems);
		map.put("cps", cpItems);

		// 工单处理URL

		String operateUrl = "你有新的故障处理工单工单处理. <a href=\"" + CONFIG_LOADER.getProperty("OPERATE_URL") + "\">&#8594点击进行处理</a><br>";

		map.put("operateUrl", operateUrl);

		// 邮件标题
		String sendSubject = "故障申报工单处理邮件";

		this.sendMailConfig(detailTemplate, map, assigneeUser, sendSubject);

	}

	/**
	 * 邮件发送配置的信息
	 * 
	 * @param template
	 *            采用的邮件模板.
	 * @param map
	 *            初始化的数据map
	 * @param sendToUser
	 *            收件人
	 * @param sendSubject
	 *            邮件标题
	 */
	private void sendMailConfig(Template template, Map<String, Object> map, User sendToUser, String sendSubject) {

		try {

			/****************** Step.1 将初始化的数据Map通过freemarker模板生成HTML格式内容. ******************/

			String content = this.generateMailContent(template, map);

			/****************** Step.2 完成邮件发送的几个参数后发送邮件. ******************/

			// 发件人.通过读取配置文件获得.
			String sendFrom = CONFIG_LOADER.getProperty("SENDFROM_EMAIL");

			// 收件人.生成环境使用
			String sendTo = sendToUser.getEmail();

			// 收件人.测试使用
			String sendToTest = CONFIG_LOADER.getProperty("TEST_SENDTO_EMAIL");

			MimeMessage msg = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

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

		/****************** Step.3 完成邮件发送的几个参数后发送邮件. ******************/

	}

	/**
	 * 使用Freemarker生成html格式内容.
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
