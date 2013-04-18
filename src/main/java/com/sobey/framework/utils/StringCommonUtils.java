package com.sobey.framework.utils;

import org.apache.commons.lang3.StringUtils;

public class StringCommonUtils {

	/**
	 * 替换字符串中指定的字符串,并将最后一个字符去掉. eg.
	 * 
	 * <pre>
	 * StringCommonUtils.replaceAndSubstringText(&quot;1-2-&quot;, &quot;-&quot;, &quot;,&quot;) = &quot;1,2&quot;
	 * 
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAndSubstringText(String str, String symbol, String replaceSymbol) {

		if (StringUtils.isNotBlank(str)) {

			String tempStr = str.substring(0, str.length() - 1);

			return StringUtils.replace(tempStr, symbol, replaceSymbol);
		}

		return null;
	}

}
