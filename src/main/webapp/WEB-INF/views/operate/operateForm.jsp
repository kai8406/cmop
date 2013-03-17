<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/layouts/taglib.jsp"%>

<html>
<head>

	<title>工单管理管理</title>
	
	<script>
		$(document).ready(function() {
			
			$("ul#navbar li#operate").addClass("active");
			
			$("#inputForm").validate();
			
			$('.datepicker').datepicker();
			
		});
	</script>
	
</head>

<body>

	<style>body{background-color: #f5f5f5;}</style>
	
	<form id="inputForm" action="." method="post" class="form-horizontal input-form" style="max-width: 800px">
	
		<input type="hidden" name="id" value="${issue.id}">
		<input type="hidden" name="authorId" value="${redmineIssue.assignee}">
		
		<fieldset>
			<legend><small>工单信息</small></legend>
			
			<c:if test="${not empty message}"><div id="message" class="alert alert-success fade in"><button data-dismiss="alert" class="close" type="button">×</button><span>${message }</span></div></c:if>
			
			<div class="row">
		 		<div class="span8"><p class="help-inline plain-text span8"><em>Subject:</em>&nbsp;${issue.project.name}>>${issue.subject}</p></div>
			 	<div class="span4">
					<p class="help-inline plain-text span3"><em>Priority:</em>&nbsp;${issue.priorityText}</p>
					<p class="help-inline plain-text span3"><em>Tracker:</em>&nbsp;${issue.tracker.name}</p>
					<p class="help-inline plain-text span3"><em>Status:</em>&nbsp;${issue.statusName}</p>
				</div>
				
			 	<div class="span4">
					<p class="help-inline plain-text span3"><em>Start Date:</em>&nbsp;<fmt:formatDate value="${issue.startDate}" pattern="yyyy-MM-dd"/></p>
					<p class="help-inline plain-text span3"><em>Due Date:</em>&nbsp;<fmt:formatDate value="${issue.dueDate}" pattern="yyyy-MM-dd"/></p>
					<p class="help-inline plain-text span3"><em>Done Ratio:</em>&nbsp;${issue.doneRatio}%</p>
				</div>
			</div>
			
			<div class="page-header"><em>详细描述:</em></div>
			 
			<div><p class="help-inline plain-text span8">${issue.description}</p></div>
			
		    <div class="page-header"><em>操作历史:</em></div>
		    
		    <div class="span8">
		    
	    		<c:forEach var="journal" items="${issue.journals}" varStatus="status">
	    			
	    			<input type="hidden" id="operator" name="operator" value="${journal.user.id}"/>
	    			
		    		<p class="help-inline plain-text span8">
		    			
						<strong>#${status.index+1} Updated by:</strong>&nbsp;${journal.user}&nbsp;&nbsp;<fmt:formatDate value="${journal.createdOn}" pattern="yyyy-MM-dd HH:mm:ss" />
		    			
		    			<ul>
							<c:forEach var="detail" items="${journal.details}">
   								<li>
   								
   									${detail.name}:&nbsp;
   									
	   								<c:if test="${detail.name=='status_id'}">
										<c:forEach var="map" items="${operateStatusMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach> &#8594; 
										<c:forEach var="map" items="${operateStatusMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
									</c:if>
									
									<c:if test="${detail.name=='assigned_to_id'}">
										<c:forEach var="map" items="${assigneeMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach>&#8594;
										<c:forEach var="map" items="${assigneeMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
									</c:if>
									
									<c:if test="${detail.name=='priority_id'}">
										<c:forEach var="map" items="${priorityMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach>&#8594;
										<c:forEach var="map" items="${priorityMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
									</c:if>
									
									<c:if test="${detail.name=='project_id'}">
										<c:forEach var="map" items="${projectMap}"><c:if test="${detail.oldValue==map.key}">${map.value}</c:if></c:forEach>&#8594;
										<c:forEach var="map" items="${projectMap}"><c:if test="${detail.newValue==map.key}">${map.value}</c:if></c:forEach>
									</c:if>
									
									<c:if test="${detail.name=='estimated_hours'}">
										<c:choose><c:when test="${not empty detail.oldValue }">detail.oldValue</c:when><c:otherwise>0.00</c:otherwise></c:choose>
									</c:if>
									
									<c:if test="${detail.name!='priority_id' && detail.name!='assigned_to_id' && detail.name!='project_id' && detail.name!='status_id'}">
										${detail.oldValue}&#8594;${detail.newValue}
									</c:if>
								
								</li>
							</c:forEach>
						</ul>
			    	</p>
		    	</c:forEach>
		    </div>
		    
		    <div class="page-header"><em>操作:</em></div>
		    
		
				<div class="control-group">
					<label class="control-label" for="Priority">Priority</label>
					<div class="controls">
						<select id="priority" name="priority" class="required">
							<c:forEach var="map" items="${priorityMap}"><option value="${map.key}">${map.value}</option></c:forEach>
						</select>
					</div>
				</div>
				 
				<div class="control-group">
					<label class="control-label" for="assignTo">Assignee</label>
					<div class="controls">
						<select id="assignTo" name="assignTo" class="required">
							<c:forEach var="map" items="${assigneeMap}"><option value="${map.key}">${map.value}</option></c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="projectId">Project</label>
					<div class="controls">
						<select id="projectId" name="projectId" class="required">
							<c:forEach var="map" items="${projectMap}"><option value="${map.key}">${map.value}</option></c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="doneRatio">Done Ratio</label>
					<div class="controls">
						<select id="doneRatio" name="doneRatio" class="required">
							<c:forEach var="map" items="${doneRatioMap}"><option value="${map.key}">${map.value}</option></c:forEach>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="dueDate">Due Date</label>
					<div class="controls">
						 <input type="text" id="dueDate" name="dueDate" readonly="readonly" class="datepicker required"  placeholder="...Due Date">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="estimatedHours">Estimated time</label>
					<div class="controls">
						<input type="text" id="estimatedHours" name="estimatedHours" value="1" class="required number" min="0" placeholder="...预计完成所需时间">
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="note">Note</label>
					<div class="controls">
						<textarea rows="3" id="note" name="note" class="required" placeholder="...详细的操作描述"></textarea>
					</div>
				</div>
			
			<div class="form-actions">
				<a href="${ctx}/apply/" class="btn">返回</a>
				<input class="btn btn-primary" type="submit" value="提交">
			</div>
		
		</fieldset>
		
	</form>
	
</body>
</html>
