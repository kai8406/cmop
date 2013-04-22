<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<div id="basicdata-tab">
	<ul class="nav nav-tabs">
		<li id="location" class="tip-bottom" title="IDC管理"><a href="${ctx}/basicdata/location/">IDC管理</a></li>
		<li id="vlan" class="tip-bottom" title="Vlan管理"><a href="${ctx}/basicdata/vlan/">Vlan管理</a></li>
		<li id="ip" class="tip-bottom" title="IP管理"><a href="${ctx}/basicdata/ippool/">IP管理</a></li>
		<li id="serverModel" class="tip-bottom" title="服务器型号管理"><a href="${ctx}/basicdata/serverModel/">型号管理</a></li>
		<li id="hostServer" class="tip-bottom" title="服务器管理"><a href="${ctx}/basicdata/host/">服务器管理</a></li>
		<li id="excel" class="tip-bottom" title="Excel导入"><a href="${ctx}/basicdata/import/">Excel导入</a></li>
	</ul>
</div>