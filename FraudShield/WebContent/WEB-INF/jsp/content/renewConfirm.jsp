<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<div class="container">
	<center>
	<div class="panel panel-default" style="width:50%">
		<div class="panel-heading">
			<h3 class="panel-title">Request Renew FraudShield Registration</h3>
		</div>
		<div class="panel-body">
			<c:choose>
				<c:when test="${renewRequest.status eq 'INVALID'}">
					The information you entered don't match. Please check the data your entered. 
				</c:when>
				<c:otherwise>
					Thank you for renew your FraudShiled registration. Please use this link below to 
					start the video conference. This link was also sent to your email address. This link 
					is time sensentive, it will expire after 48 hours.
					<br>
					
					<a href="${pageContext.request.contextPath}/servlet/renew/requestInterview?requestCode=${renewRequest.requestCode}">Start Interview</a> 
				
				</c:otherwise>
			</c:choose>
	
		</div>
	</div>
	</center>
</div>