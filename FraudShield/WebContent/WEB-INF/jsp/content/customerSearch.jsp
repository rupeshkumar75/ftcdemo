<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@page import="com.citizant.fraudshield.domain.TsoUser"%>
<%@page import="com.citizant.fraudshield.common.AppConstants"%>

<%
	boolean isAdminFlag = false;
	TsoUser user = (TsoUser)session.getAttribute(AppConstants.LOGIN_USER);
	if (user != null && user.getUserRole().equalsIgnoreCase("admin"))
	{
		isAdminFlag = true;
	}
%>

<script type="text/javascript">
	var contextPath;
	
	$(document).ready(function() {
		contextPath = $("#contextPath").val();
		$.mask.definitions['~']='[+-]';
		$("#ssn").mask("999-99-9999");	
		contextPath = $("#contextPath").val();
		loadCustomerList();
	});
	
	function loadCustomerList(){
		var url = contextPath+"/servlet/search/reload";
		$.ajax({
			type: "GET",
			url: url,
			cache: false,
			async: false, // to set local variable
			success: function(data) {			
				$("#customerList").html(data);
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});
	}
	
	function searchCustomer(){
		//validate search criteria
		var fname = document.getElementById('firstName').value;
		var lname = document.getElementById('lastName').value;
		var ssn = document.getElementById('ssn').value;
		var address = document.getElementById('address').value;
		var docType = '';
		
		if (document.getElementById('idDocType')){
			docType = document.getElementById('idDocType').value;
		}
		
		if ($.trim(fname) == '' && $.trim(lname) == '' && 
			$.trim(ssn) == '' && $.trim(address) == '' && $.trim(docType) == '')
		{
			var message ='Please provide a search criteria.';
			appConfirmDialog(message,"Search Error" );
			return;
		}		
		
		document.getElementById('printLink').style.display = 'none';
		
		var myForm = $('#searchForm').serialize();
		var url = contextPath+"/servlet/search/dosearch";

		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "POST",
				url: url,
				data: myForm,
				cache: false,
				async: false, // to set local variable
				success: function(data) {
					$('#bodyId').css('cursor','default');
					$('#cmdSearch').css('cursor','default');
					$("#customerList").html(data);
				},
				error: function(xhr, textStatus, errorThrown) {
					$('#bodyId').css('cursor','default');
					$('#cmdSearch').css('cursor','default');
				}
			});
		},500);
	}
	
	function clearForm(){
		document.location.href=contextPath+"/servlet/search/start";
	}
	
	function printReport() {
		var docprint=window.open("about:blank", "_blank"); 
		var oTable = document.getElementById("customerListTable");
		docprint.document.open(); 
		docprint.document.writeln('<html><head><title>Search Results</title>'); 
		docprint.document.writeln('</head>');
		docprint.document.writeln('<style>');
		docprint.document.writeln('body {background: #ffffff !important;}');
		docprint.document.writeln('table {border-collapse: collapse;}');
		docprint.document.writeln('table tr{border: thin solid;}');
		docprint.document.writeln('table th, table td {border: thin solid;}');
		docprint.document.writeln('</style>');
		docprint.document.writeln('<body><center>');
		docprint.document.writeln('<h2><u>Search Results</u></h3>');
		docprint.document.writeln('<table>');
		docprint.document.writeln(oTable.innerHTML);
		docprint.document.writeln('</table>');
		docprint.document.writeln('</center></body></html>'); 
		docprint.document.close(); 
		docprint.print();
		docprint.close();
	}
</script>


 <div class="container">
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Search Customer</h3>
	</div>
	<div class="panel-body">
		<form id="searchForm">
		
		<table>
		
		<tr>
		<td>
		
			<table>
				<tr>
					<td>First Name:</td>
					<td><input type="text" style="width: 180px;" maxlength="120"
						class="form-control" name="firstName" id="firstName" value="${searchBean.firstName}"
						placeholder="first name" /></td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><input type="text" style="width: 180px;" maxlength="120"
						class="form-control" name="lastName" id="lastName" value="${searchBean.lastName}"
						placeholder="last name" /></td>
				</tr>
			</table>
		</td>
		<td>&nbsp;</td>
		<td>	
			<table>
				<tr>
					<td>SSN # :</td>
					<td><input type="text" style="width: 180px;" maxlength="20"
						class="form-control" name="ssn" id="ssn" value="${searchBean.ssn}"
						placeholder="SSN" /></td>
				</tr>
				<tr>
					<td>Address :</td>
					<td><input type="text" style="width: 180px;" maxlength="120"
						class="form-control" name="address" id="address" value="${searchBean.address}"
						placeholder="Address" /></td>
				</tr>
			</table>
		</td>
		</tr>
		<% if (isAdminFlag) {%>
		<tr>
			<td>
				<table>
					<tr>
						<td>Document Type :</td>
						<td>
							<select id="idDocType" name="idDocType" placeholder="Document Type" class="form-control" onchange="showDescription();" style="width: 275px">
								<c:forEach var="entry" items="${applicationScope.identityDocTypes}">
									<option value="${entry.key}"><c:out value="${entry.value}"/></option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<% } %>	
		</table>	
			<div class="center" align="center">
			<input type="button" name="cmdSearch" class="btn btn-large btn-primary"  style="width:100px" id="cmdSearch" value="Search" class="btn-default"  style="width:100px"
				onclick="javascript:searchCustomer();" />
			<input type="button" name="cmdClear"  class="btn btn-large btn-primary"  style="width:100px" id="cmdClear" value="Clear" class="btn-default"  style="width:100px"
				onclick="javascript:clearForm();" />
		</div>
		</form>
	</div>
	<div class="panel panel-default">
	<div class="panel-heading">
		<span class="panel-title">List of Customers</span>
		<div class="actionLinks">
			<span style="display:inline-block;">	
		    	<a href="${pageContext.request.contextPath}/servlet/customer/new" title="Add New Customer">
					<span class="glyphicon glyphicon-cloud-upload"></span>Add New Customer
				</a>		
			</span>
			<% if (isAdminFlag) {%>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<span style="display:none;" id="printLink">	
				<a href="#" title="Print" onclick="javascript:printReport();">
					<img src="${pageContext.request.contextPath}/images/printer.png" />&nbsp;Print
				</a>
			</span>
			<% } %>
		
		</div>
	</div>
	<div class="panel-body" id="customerList"></div>
</div>
		
</div>



</div>