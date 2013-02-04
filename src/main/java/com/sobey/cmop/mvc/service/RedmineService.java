package com.sobey.cmop.mvc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.sobey.cmop.mvc.comm.BaseSevcie;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManager.INCLUDE;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.User;

/**
 * 通过redmine-java-api操作Redmine 相关的管理类.
 * 
 * @author liukai
 */
@Service
@Transactional(readOnly = true)
public class RedmineService extends BaseSevcie {

	private static Logger logger = LoggerFactory.getLogger(ComputeService.class);

	/**
	 * 重置次数
	 */
	private static int COUNT = 0;

	/**
	 * 解析redmine.properties 获得redmine api 配置信息
	 */
	public static String HOST = CONFIG_LOADER.getProperty("redmineHost");

	/**
	 * 默认完成度
	 */
	private static Integer DEFAULT_DONERATIO = 0;

	/**
	 * 创建Issue
	 * 
	 * @param mgr
	 * @throws RedmineException
	 */
	@Transactional(readOnly = false)
	public static boolean createIssue(Issue issue, String projectId, RedmineManager mgr) {

		boolean result = false;

		try {

			issue.setStatusName("新建"); // 默认：新建
			issue.setDoneRatio(DEFAULT_DONERATIO);
			issue.setAssignee(mgr.getCurrentUser()); // 默认：登录者
			issue.setAuthor(mgr.getCurrentUser()); // 默认：登录者
			issue.setStartDate(new Date());
			issue.setDueDate(new Date());
			issue.setCreatedOn(new Date());

			mgr.createIssue(projectId, issue);

			result = true;

			logger.info("--->创建Issue成功！");

		} catch (RedmineException e) {

			e.printStackTrace();

			COUNT++;

			// 重新连接

			repeatConnect(mgr);

			if (COUNT <= 3) {

				result = createIssue(issue, projectId, mgr);

			} else {

				logger.info("--->创建Issue失败！");

			}

		}

		COUNT = 0; // 重置次数

		return result;
	}

	/**
	 * 更新Issue<br>
	 * 如果没有更新成功,用递归算法重复4次
	 * 
	 * @param issue
	 * @param mgr
	 * @return 成功返回true,失败返回false.
	 */
	@Transactional(readOnly = false)
	public static boolean changeIssue(Issue issue, RedmineManager mgr) {

		boolean result = false;

		try {

			logger.info("issueId--->" + issue.getId());

			issue.setUpdatedOn(new Date());

			mgr.update(issue); // 已关闭的更新会出错

			result = true;

			logger.info("--->更新Issue成功！");

		} catch (RedmineException e) {

			e.printStackTrace();

			COUNT++;

			repeatConnect(mgr);

			if (COUNT <= 3) {

				result = changeIssue(issue, mgr);

			} else {

				logger.info("--->更新Issue失败！");

			}
		}

		COUNT = 0; // 重置次数

		return result;
	}

	/**
	 * 根据issueId获取Issue<br>
	 * 如果没有值,用递归算法重复4次,
	 * 
	 * @param issueId
	 * @param mgr
	 * @return
	 */
	public static Issue getIssueById(int issueId, RedmineManager mgr) {

		Issue issue = null;

		try {

			issue = mgr.getIssueById(issueId, INCLUDE.journals);

			if (issue != null) {

				List<Journal> list = issue.getJournals();

				if (list != null) { // 必须向下循环一次，否则将不会被更新。。。感觉是懒加载

					System.out.println(list.size());
				}

			}

			logger.info("--->获取Issue成功！");

		} catch (RedmineException e) {

			e.printStackTrace();

			COUNT++;

			repeatConnect(mgr);

			if (COUNT <= 3) {

				issue = getIssueById(issueId, mgr);

			} else {

				logger.info("--->获取Issue失败！");

			}

		}

		COUNT = 0; // 重置次数

		return issue;

	}

	/**
	 * 根据subject获得Issue.<br>
	 * 如果没有值,用递归算法重复4次,如果最后还是获取不了值,则返回Null.
	 * 
	 * @param subject
	 * @param mgr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Issue getIssueBySubject(String subject, RedmineManager mgr) {

		List<Issue> list = null;

		try {

			Map<String, String> paraMap = Maps.newHashMap();

			paraMap.put("subject", subject);

			list = mgr.getIssues(paraMap);

		} catch (RedmineException e) {

			e.printStackTrace();

			COUNT++;

			repeatConnect(mgr);

			if (COUNT <= 3) {

				list = (List<Issue>) getIssueBySubject(subject, mgr);

			} else {

				logger.info("--->获取Issue失败！");

			}

		}

		COUNT = 0; // 重置次数

		if (list != null && list.size() > 0) {

			logger.info("--->获取Issue成功！");

			return list.get(0);

		}

		return null;

	}

	/**
	 * 解决连接Redmine失败的情况，每隔2秒钟重连3次
	 * 
	 * @return
	 */
	private static boolean repeatConnect(RedmineManager mgr) {

		logger.info("--->RedmineException..." + COUNT);

		try {

			// 延时2秒

			Thread.sleep(2000);

			User user = mgr.getCurrentUser();

			logger.info("--->重新连接Redmine成功！" + user.getFirstName());

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			logger.info("--->重新连接Redmine失败！");

		}

		return false;

	}

}
