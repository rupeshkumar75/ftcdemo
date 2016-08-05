<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<table id="requestListTable" class="display">
		<thead>
			<tr>
				<th>Customer Name</th>
				<th>Old ID</th>
				<th>New ID</th>
				<th>Request Date</th>
				<th>Interview Date</th>
				<th>Status</th>
				<th>Agent</th>
				<th>Comments</th>
			</tr>
		</thead>
		<tbody>		
		<c:forEach var="req" items="${requestList}" varStatus="loopCounter">
			
				<tr>
					<td>${req.firstName} ${req.lastName}</td>
					<td>${req.origFid }</td>
					<td>${req.newFid }</td>
					<td>${req.requestDate}</td>
					<td>${req.interviewDate}</td>
					<td>${req.status}</td>
					<td>${req.agent}</td>
					<td>${req.comments}</td>
				</tr>
	
		</c:forEach>
		</tbody>
	</table>

<script>
	var contextPath;
	$(document).ready(function() {
		contextPath = $("#contextPath").val();
		$(document).ready(function() {
			$("#requestListTable").dataTable( { 
				"bJQueryUI" : true,
				"bAutoWidth" : true,
				"aaSorting":[[1, "asc"]],
				"iDisplayLength" : 10,	
				"oLanguage": {"sSearch": "Filter: "} 
			});	
		});
	});
	
	
	function pickupCall() {
		var url = contextPath+"/servlet/renew/pickupCall";
		window.location = url;
	}

</script>