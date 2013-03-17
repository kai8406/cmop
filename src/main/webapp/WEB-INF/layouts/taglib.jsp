<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
