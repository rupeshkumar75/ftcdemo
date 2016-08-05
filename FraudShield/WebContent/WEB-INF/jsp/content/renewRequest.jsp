<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div class="container" align="center">

<div class="panel panel-default" style="width:50%">
	<div class="panel-heading">
		<h3 class="panel-title">Request Renew FraudShield Registration</h3>
	</div>
	<div class="panel-body">
	
		<div id="inputPanel" style="display:block">
		<form method="post" id="requestForm" action="${pageContext.request.contextPath}/servlet/renew/requestRenew">
		<table>
			<tr>
				<td>FraudShiled ID : </td>
				<td><input type="text" id="fraudShieldId" name="fraudShieldId"></td>
			</tr>
			<tr>
				<td>SSN :</td>
				<td><input type="text" id="ssn" name="ssn"></td>
			</tr>
				<tr>
				<td>First Name :</td>
				<td><input type="text" id="firstName" name="firstName"></td>
			</tr>
				<tr>
				<td>Last Name :</td>
				<td><input type="text" id="lastName" name="lastName"></td>
			</tr>
		</table>
		
		
			 <br/>
			 <br/>
			
			<button class="btn btn-large btn-primary" type="button" onclick="submitForm();">Submit</button>
		</form>
		</div>
		
		<div id="confirmPanel" style="display:none">
				Thank you for renew your FraudShiled registration. Please use this link below to 
				start the video conference. This link was also sent to your email address. This link 
				is time sensitive, it will expire after 48 hours.
				<br>					
				<a href="#" onclick="startInterview();">Start Interview</a> 
		</div>
	</div>
</div>

</div>
<script>
	var contextPath;
	var requestCode;
	$(document).ready(function() {
		contextPath = $("#contextPath").val();
		$.mask.definitions['~']='[+-]';
		$("#ssn").mask("999-99-9999");	
	});
	
	function startInterview() {
		var url = contextPath + '/servlet/renew/requestInterview?requestCode=' + requestCode;
		window.location = url;
	}
	
	function submitForm() {
		var myForm = $('#requestForm').serialize();
		var url = contextPath + '/servlet/renew/requestRenew';
	
		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "POST",
				url: url,
				data: myForm,
				dataType: "json",
				cache: false,
				async: false, // to set local variable
				success: function(data) {
					if(data.status == "INVALID") {
						appConfirmDialog('The information you entered don\'t match. Please check the data your entered.');
					}
					if(data.status == "SUCCESS") {
						requestCode = data.requestCode;
						document.getElementById('inputPanel').style.display = 'none';
						document.getElementById('confirmPanel').style.display = 'block';
					}
				},
				error: function(xhr, textStatus, errorThrown) {
					appConfirmDialog('Error to Saved Customer information');
				}
			});
		},500);		
	}
	
</script>