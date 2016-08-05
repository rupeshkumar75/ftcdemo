<%@ page language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@page import="com.citizant.fraudshield.util.StringUtil"%>
<%@page import="com.citizant.fraudshield.domain.TsoUser"%>
<%@page import="com.citizant.fraudshield.common.AppConstants"%>
<%
	boolean isLogged = false;
	boolean isAdmin = false;
	TsoUser user = (TsoUser)session.getAttribute(AppConstants.LOGIN_USER);
	if(session.getAttribute("USER_ID")!=null){
		 isLogged = true;  
	}
	if (user != null && user.getUserRole().equalsIgnoreCase("admin"))
	{
		isAdmin = true;
	}
	String path = StringUtil.getServerURL(request) + "/";
%>

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.dataTables.css"/>   	  	   
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/demo_table_jui.css"/>  
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.dataTables.min.js"></script>  
    <script src="${pageContext.request.contextPath}/js/jquery-ui-1.10.3.custom.min.js"></script> 
    <script src="${pageContext.request.contextPath}/js/jquery.maskedinput-1.3.min.js"></script>   
    <script src="${pageContext.request.contextPath}/js/moment.min.js"></script>   
   
   
    <style>   
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
			.center {
			    margin-left: auto;
			    margin-right: auto;
			    width: 100%;
			  
			}
    		.actionLinks {
    			position: relative;
				float:right;
				
				margin-right: 20px;;
    		}
    </style>
    <script>
    	$.ajaxSetup({
    		cache: false 
    	});
    	
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
    	
    	function validateEmail(email) {
    	    var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    	    return re.test(email);
    	}
    	
    </script>
</head>
<body style="background:#d2ecfb;">
	<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}"/>
	<div class="container">		
 		<div id="loadingPopupId" class="loadingPopup" style="display:none">
             <div style="background-color: #FFFFF">
                <img src="${pageContext.request.contextPath}/images/loading.gif" id="loadingGifId" alt="Loading Page" title="Loading Page" />
            </div>
        </div>
  		<tiles:insertAttribute name="header" />  		
  		<tiles:insertAttribute name="body-content"/>			  		
  	</div> 	
</body>
</html>
