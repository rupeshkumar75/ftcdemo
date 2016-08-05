<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div class="container">

	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Online Callers</h3>
		</div>
		<div class="panel-body" id="divCallerList">
		
			
		</div>
	</div>
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Renew Requests</h3>
		</div>
		<div class="panel-body" id="divRequestList">
		
						
		</div>
	</div>
	
</div>

<script>
	var contextPath;
	var requestCode;
	$(document).ready(function() {
		contextPath = $("#contextPath").val();
		requestCode = $("#requestCode").val();
		checkCallers();
		checkRequests();
		setInterval(function(){ 
			checkCallers(); }, 
			3000);
		setInterval(function(){ 
			checkRequests(); }, 
			10000);	
	});

	function checkCallers() {

		var url = contextPath+"/servlet/renew/getCallerList";
		$.ajax({
			type: "GET",
			url: url,
			cache: false,
			async: false, // to set local variable
			success: function(data) {		
				$('#divCallerList').html(data);
			},
			error: function(xhr, textStatus, errorThrown) {
				alert('error');
			}
		});
	}
	
	function checkRequests() {

		var url = contextPath+"/servlet/renew/getRequestList";
		$.ajax({
			type: "GET",
			url: url,
			cache: false,
			async: false, // to set local variable
			success: function(data) {		
				$('#divRequestList').html(data);
			},
			error: function(xhr, textStatus, errorThrown) {
				alert('error');
			}
		});
	}
</script>