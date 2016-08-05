<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<table id="callerListTable">
	<thead>
			<tr>
				<th>Call Order</th>
				<th>Customer Name</th>
				<th>Request Time</th>
			</tr>
		</thead>
		<tbody>		
		<c:forEach var="caller" items="${callerList}" varStatus="loopCounter">
			<tr>	
				<td>${loopCounter.count}</td>	
				<td>${caller.firstName} ${caller.lastName}</td>	
				<td>${caller.callTime}</td>	
			</tr>
		</c:forEach>
		</tbody>
</table>
	 <br/>
	 <br/>	
	 
	<button class="btn btn-large btn-primary" type="button" onclick="javascript:pickupCall();">Pickup Call</button>

<script>
	var contextPath;
	$(document).ready(function() {
		contextPath = $("#contextPath").val();
		$("#callerListTable").dataTable( { 
			"bJQueryUI" : true,
			"bAutoWidth" : true,
			"aaSorting":[[1, "asc"]],
			"iDisplayLength" : 10,	
			"oLanguage": {"sSearch": "Filter: "} 
		});	
	});
	
	
	function pickupCall() {
		var url = contextPath+"/servlet/renew/pickupCall";
		window.location = url;
	}

</script>