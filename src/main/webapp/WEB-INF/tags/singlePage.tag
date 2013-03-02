<%@tag pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>

<div class="pagination pull-right"	style="margin-top: 0px; margin-bottom: 0px;">
	<ul>
		<li class="disabled"><a href='#'>共${page.totalElements}条</a></li>
	</ul>
</div>