<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<!-- Bootstrap core CSS -->
   
    <!-- Custom styles for this template -->    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pepper-grinder/jquery-ui-1.10.3.custom.min.css"/> 
   
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/blueprint/screen.css"/> 
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/bootstrap-theme.min.css" rel="stylesheet"/>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery-ui-1.10.3.custom.min.js"></script> 
       
    <style>
     
      body {       
        padding-bottom: 40px;
        background:url('${pageContext.request.contextPath}/images/fraud_shield_logo.png') no-repeat fixed center;
      }
    
    		.form-control {
		      display: block;
		      width: 100%;
		      height: 25px;
		      padding: 2px 2px;
		      font-size: 14px;
		      line-height: 1.428571429;
		      color: #555555;
		      vertical-align: middle;
		      background-color: #ffffff;
		      border: 1px solid #aaaaaa;
		      border-radius: 4px;
		      -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
		              box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
		      -webkit-transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
		              transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s;
		
			}
			.navbar-inverse {
			
				background-color: #ddddff;
				border-color: #080808;
			}
			.logoutbox {
				position: absolute;
				top: 5%;
				right: 15%;
			}
			.adminbox {
				position: absolute;
				top: 5%;
				right: 20%;
			}
			.center {
			    margin-left: auto;
			    margin-right: auto;
			    width: 100%;
			  
			}
    
    </style>
    
    <script>
	var contextPath;
	var requestCode;
	var roomNumber;
	var callTimer;
	var statusTimer;
	
  	$.ajaxSetup({
		cache: false 
	});
	
	$(document).ready(function() {
		contextPath = $("#contextPath").val();
		requestCode = $("#requestCode").val();		
		checkRequestStatus();		
	});
	
	function checkRequestStatus() {
		var url = contextPath+"/servlet/renew/checkRequestStatus?requestCode=" + requestCode;
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			cache: false,
			async: false, 
			success: function(data) {								
				if(data.status == 'INVALID') {			
					appConfirmDialog('This FraudShield Registration renew request code is invalid or expired. Please start a new request or  visit your local office');
				}
				if(data.status == 'DECLINED') {			
					appConfirmDialog('We cannot renew your FraudShield Registration online. Please visit your local office.');
				}
				if(data.status == 'PENDING PAYMENT') {			
					appConfirmDialog('This FraudShield Registration renew request was approved. Please make payment to complete the registration');
					document.getElementById('paymentPanel').style.display = 'block';
				}
				if(data.status == 'COMPLETED') {			
					appConfirmDialog('Thank you for renew your FraudShield Registration. Your new FraudShield ID is ' + data.fid);					}
							
				if(data.status == 'NEW') {			
					document.getElementById('divWaitingPanel').style.display = 'block';
					checkCall();
					callTimer = setInterval(function(){ 
						 checkCall(); }, 
						3000);
				} 
				
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});
	}
	
	

	function checkCall() {
		var url = contextPath+"/servlet/renew/checkInterviewRequest?requestCode=" + requestCode;
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			cache: false,
			async: false, 
			success: function(data) {				
				if(data.sessionStart == 'yes') {			
					//var redurl = "https://apprtc.appspot.com/r/" + data.roomNumber;
					var redurl = contextPath+"/conference.jsp?roomNumber=" + data.roomNumber;
					//window.location = redurl;
					document.getElementById('divWaitingPanel').style.display = 'none';
					document.getElementById('divVideo').style.display = 'block';
					document.getElementById('videoFrame').src = redurl;
					//window.open(redurl, 'FraudShield', 'width=800px,height=650px,scroll=no;location:no');
					clearInterval(callTimer);
					statusTimer = setInterval(function(){ 
						checkApprovalStatus(); }, 3000);
				} else {
					$("#waitOrder").html(data.order);
				}
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});
	}
	
	function hangup() {
		var url = contextPath+"/servlet/renew/customerHangeup?requestCode=" + requestCode;
		$.ajax({
			type: "GET",
			url: url,
			cache: false,
			async: false, 
			success: function(data) {			
				document.getElementById('divWait').style.display = 'none';
				document.getElementById('divRedial').style.display = 'block';
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});		
		return false;
	}
	
	function  reDail() {
		checkRequestStatus();		
		document.getElementById('divWait').style.display = 'block';
		document.getElementById('divRedial').style.display = 'none';
	}

	function  checkApprovalStatus() {
		var url = contextPath+"/servlet/renew/checkApprovalStatus?requestCode=" + requestCode;
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			cache: false,
			async: false, 
			success: function(data) {			
				
				if(data.status == 'PENDING PAYMENT') {
					clearInterval(statusTimer);
					//Goto payment page 
					document.getElementById('divVideo').style.display = 'none';		
					document.getElementById('paymentPanel').style.display = 'block';					
				}
				
				if(data.status == 'DECLINED') {
					clearInterval(statusTimer);
					//Show message explain
					document.getElementById('divVideo').style.display = 'none';					
					appConfirmDialog('We cannot renew your FraudShield Registration online. Please visit your local office');
				}
				
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});		
	}
	
	function pay() {
		var url = contextPath+"/servlet/renew/makePayment?requestCode=" + requestCode;
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			cache: false,
			async: false, 
			success: function(data) {							
				document.getElementById('paymentPanel').style.display = 'none';	
				appConfirmDialog('Thank you for renew your FraudShield Registration. Your new FraudShield ID is ' + data.fid);				
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});		
	}
	
	
	function appConfirmDialog(message, title, focusElmtIdOnClose, onConfirmFunction, onCancelFunction) {
		if (title == null) {
			title = 'Message';
		}
		$("<div></div>").appendTo('body').html(
				'<div tabindex="0" style="text-decoration:none">' + message + '</div>').dialog({
			draggable : true,
			show : "blind",
			modal : true,
			title : title,
			zIndex : 10005,
			resizable : false,
			buttons : {
				Ok : function() {
					$(this).dialog("close");
					if (onConfirmFunction) {
						onConfirmFunction();
					}
				},
				Cancel : function() {
					$(this).dialog("close");
					if (onCancelFunction) {
						onCancelFunction();
					}
				}
			},
			open : function() {
				//$('.ui-dialog-buttonpane button:contains(Ok)').focus();
				// Reset inactivity timeout
				resetTimer();
			},
			close : function(event, ui) {
				$(this).remove();
				if (focusElmtIdOnClose) {
					var element = $('#' + focusElmtIdOnClose);
					if (element)
						element.focus();
				}
			}
		});
	
		return;
	};
	
</script>
    
</head>
<body style="background:#c0c0c0;" onunload="hangup();">
 <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}"/>
 <input type="hidden" id="requestCode" value="${requestCode}"/>
 <div class="container" align="center">
	<div class="panel panel-default" style="width:50%">
		<div class="panel-heading">
			<h3 class="panel-title">Request Renew FraudShield Registration</h3>			
		</div>
		<div class="panel-body" align="center">

				<div id="divWaitingPanel"  style="display:none">
					All agents are currently assisting other customers. We will be with you as soon as an agent is available.
				
				   Your patience is greatly appreciated.<br/>
					
					There are <span id="waitOrder" style="inline">1</span> callers in front of you.
					
					
					<br/>
					<br/>
					<div id="divWait" style="display:block">
						<img alt="" src="${pageContext.request.contextPath}/images/ajax_loader_blue_128.gif">				
						<br/>
						<br/>
						<button id="hangupBtn" class="btn btn-large btn-primary" type="button" onclick="javascript: hangup();">Hangup Call</button>
					</div>
					<div id="divRedial" style="display:none">
						<button id="redailBtn" class="btn btn-large btn-primary" type="button" onclick="javascript: reDail();">Redial</button>
					</div>		
				</div>
				
				<div id="paymentPanel"  style="display:none">
					You online renew was approved, please make payment to complete the process.
					
					<table>
						<tr>
							<td>Name on Card</td>
							<td><input type="text" name="name"/></td>
						</tr>
						<tr>
							<td>Card Number</td>
							<td><input type="text" name="name"/></td>
						</tr>
						<tr>
							<td>Expiration Date</td>
							<td><input type="text" name="name" size="5"/> / <input type="text" name="name" size="10"/></td>
						</tr>
						<tr>
							<td><button id="hangupBtn" class="btn btn-large btn-primary" type="button" onclick="javascript: pay();">Make Payment</button></td>					
						</tr>
					</table>								
				</div>
			</div>
		</div>
		<div id="divVideo"  style="display:none">
			<iframe id="videoFrame" src="" width="1200" height="800"/>
		</div>
	</div>

</body>
</html>






