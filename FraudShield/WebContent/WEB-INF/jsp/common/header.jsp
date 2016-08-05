<%@page import="com.citizant.fraudshield.domain.TsoUser"%>
<%@page import="com.citizant.fraudshield.common.AppConstants"%>
<%
	boolean isAdmin = false;
	boolean isTso = false;
	boolean isCcTso = false;
	TsoUser user = (TsoUser)session.getAttribute(AppConstants.LOGIN_USER);
	if (user != null && user.getUserRole().equalsIgnoreCase("admin"))
	{
		isAdmin = true;
	}
	if (user != null && user.getUserRole().equalsIgnoreCase("tso"))
	{
		isTso = true;
	}
	if (user != null && user.getUserRole().equalsIgnoreCase("cctso"))
	{
		isCcTso = true;
	}
%>

<style type="text/css">
      div.header01 {       		
		font-size: 22px;
		color: darkblue;
		padding-top: 50px;
		margin-left: 250px;
	}
   	.menubox {
		position: relative;
		float:right;
		padding-top: 40px;
		margin-right: 20px;;
	}
	.menuItem {
		font-size:14px;
		width: 300px;
		margin-right: 30px;;
	}
</style>

        <div class="container">
        	<img style="float: left;" src="${pageContext.request.contextPath}/images/flash.jpg"/>          
     		<div class="header01">Identity Verification System</div>
     		<div class="menubox">
	     		<% if (isAdmin) {%>	        
		         	<a href="${pageContext.request.contextPath}/servlet/admin/start" class="menuItem">Manage TSO Users</a> 
		         	&nbsp;&nbsp;&nbsp;	     				
		         	<a href="${pageContext.request.contextPath}/servlet/search/start" class="menuItem">Search Customer</a>	
		         	&nbsp;&nbsp;&nbsp;
		         	<a href="${pageContext.request.contextPath}/servlet/report/start" class="menuItem">Reports</a>	
		         	&nbsp;&nbsp;&nbsp;		         	
		        <%} %>
		        
		        <% if (isCcTso) {%>    	
		         	<a href="${pageContext.request.contextPath}/servlet/renew/cctsopanel" class="menuItem">CCTSO Dashboard</a>
		         	&nbsp;&nbsp;&nbsp;
	     		<%} %>
	     		<%if(user!=null){ %>
	         	<a href="${pageContext.request.contextPath}/servlet/home/logout" class="menuItem">Logout</a>
	         	<%} %>
			</div>
       </div>
