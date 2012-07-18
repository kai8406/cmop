package com.sobey.mvc.util;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sobey.framework.utils.PropertiesLoader;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManager.INCLUDE;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;

public class RedmineUtil {
	private static final Logger logger = LoggerFactory.getLogger(RedmineUtil.class);

	/**
	 * 解析redmine.properties 获得redmine api 配置信息
	 */
	private static PropertiesLoader loader = new PropertiesLoader("classpath:/config.properties");

	private static RedmineManager mgr = new RedmineManager(loader.getProperty("redmineHost"), loader.getProperty("apiAccessKey"));

	public static void main(String[] args) {
		try {
			createIssue(new Issue());
			// changeIssue(getIssue(3));
			// getIssue(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建Issue
	 * 
	 * @param mgr
	 * @throws RedmineException
	 */
	public static boolean createIssue(Issue issue) {
		try {
			// issue.setTracker(new Tracker(1, "错误")); //目前两种类型：1-错误；3-支持
			// issue.setSubject("test_20120711-2");
			// issue.setDescription("具体的申请描述信息：description20GB");
			issue.setStatusName("新建"); // 默认：新建
			issue.setPriorityId(5); // 默认：高
			issue.setAssignee(mgr.getCurrentUser()); // 默认：指派给余波mgr.getUserById(3)
			issue.setStartDate(new Date());
			issue.setDueDate(DateUtil.addDate(null));
			issue.setDoneRatio(0);
			issue.setAuthor(mgr.getCurrentUser()); // 默认：程序创建者
			issue.setCreatedOn(new Date());

			mgr.createIssue(loader.getProperty("projectKey"), issue); // 默认：Sobey
																		// Cloud运营-->Iaas
			logger.info("--->创建Issue成功！");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("--->创建Issue失败！");
			return false;
		}
	}

	/**
	 * 更新Issue
	 * 
	 * @param mgr
	 * @throws RedmineException
	 */
	public static boolean changeIssue(Issue issue) throws RedmineException {
		try {
			// issue.setNotes("内容再次更新。。。");
			issue.setStatusId(1);
			issue.setDueDate(new Date());
			issue.setDoneRatio(0);

			mgr.update(issue); // 状态不能为已关闭
			logger.info("--->更新Issue成功！");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("--->更新Issue失败！");
			return false;
		}
	}

	/**
	 * 获取Issue
	 * 
	 * @param mgr
	 * @throws RedmineException
	 */
	@SuppressWarnings("rawtypes")
	public static Issue getIssue(int issueId) throws Exception {
		try {
			Issue issue = mgr.getIssueById(issueId, INCLUDE.journals);
			List list = issue.getJournals();
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				Journal journal = (Journal) list.get(i);
				System.out.print(journal.getUser() + ": " + journal.getNotes());// +" "+journal.getCreatedOn()
			}
			logger.info("--->获取Issue成功！");
			return issue;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("--->获取Issue失败！");
			return null;
		}
	}

}
