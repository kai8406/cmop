package com.sobey.cmop.mvc.web.account;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sobey.cmop.mvc.entity.Group;
import com.sobey.cmop.mvc.service.account.AccountManager;
import com.sobey.framework.utils.Collections3;

/**
 * 用于转换用户表单中复杂对象Group的checkbox的关联。
 * 
 */
@Component
public class GroupListEditor extends PropertyEditorSupport {

	@Autowired
	private AccountManager accountManager;

	/**
	 * Back From Page
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] ids = StringUtils.split(text, ",");
		List<Group> groups = new ArrayList<Group>();
		for (String id : ids) {
			Group group = accountManager.getGroup(Integer.valueOf(id));
			groups.add(group);
		}
		setValue(groups);
	}

	/**
	 * Set to page
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getAsText() {
		return Collections3.extractToString((List) getValue(), "id", ",");
	}
}
