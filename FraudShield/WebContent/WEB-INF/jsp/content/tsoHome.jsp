<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@page import="com.citizant.fraudshield.domain.TsoUser"%>
<%@page import="com.citizant.fraudshield.common.AppConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<input type="hidden" id="personId"/>
<div class="container">
	<div class="panel panel-default" style="margin: 15px;">
		<div class="panel-heading">
			<h3 class="panel-title">TSO Actions</h3>
		</div>
		<div class="panel-body" style="height:200px;line-height: 200px;" >
			
			
			<div class="center" align="center">
				<input type="button"  name="cmdSave"  class="btn btn-large btn-primary" style="width:150px" id="cmdSave" value="Existing Customer" class="button" onclick="javascript:existingCustomer();" />
				&nbsp;&nbsp;
				<input type="button"  name="cmdCancel"   class="btn btn-large btn-primary"  style="width:150px" id="cmdCancel" value="New Customer" onclick="javascript:newCustomer();" />
			</div>
			
		</div>
	</div>	
</div>


<div id="exCustomerDialog" style="display:none">
	<form method="post" id="requestForm">
		<table>			
			<tr>
				<td>SSN :<span class="glyphicon glyphicon-star"></span></td>
				<td><input type="text" id="ssn" name="ssn"></td>
			</tr>
				<tr>
				<td>First Name :<span class="glyphicon glyphicon-star"></span></td>
				<td><input type="text" id="firstName" name="firstName"></td>
			</tr>
				<tr>
				<td>Last Name :<span class="glyphicon glyphicon-star"></span></td>
				<td><input type="text" id="lastName" name="lastName"></td>
			</tr>
			<tr>
				<td>Date of Birth : <span class="glyphicon glyphicon-star"></span></td>
				<td><input type="text" id="dob" name="dob"></td>
			</tr>
		</table>	
		
			<div align="center" style="margin: 10px;">
			<button class="btn btn-large btn-primary" type="button" onclick="searchCustomer();">Search</button>
			&nbsp;&nbsp;
			<button class="btn btn-large btn-primary" type="button" onclick="closeDialog();">Close</button>
			</div>
		</form>
</div>

<div id="customerInfoDialog" style="display:none;align:center">
		<div align="center" style="margin: 15px;">
			<img id="snapImage" width="700px"/>
		</div>
		
		<div align="center" style="margin: 15px;">
		Registration Date : <span id="oldRegDate">12/2/2014</span>
		</div>
		<div align="center">
		<button class="btn btn-large btn-primary" type="button" onclick="printIdCard();">Print ID</button>
		&nbsp;&nbsp;
		<button class="btn btn-large btn-primary" type="button" onclick="closeInfoDialog();">Close</button>
		</div>
</div>




<script>

	function existingCustomer() {
		
		$.mask.definitions['~']='[+-]';	
		$("#ssn").mask("999-99-9999");
		$("#dob").mask("99/99/9999");
		
		$("#ssn").val('');
		$("#dob").val('');
		$("#lastName").val('');
		$("#firstName").val('');
		
		$("#exCustomerDialog").dialog({
          	title: "Search Existing Customer",
              autoOpen: false,
              draggable: "true",
              modal: true,
              height: 300,
              width: 400
         }); 
		
		 $("#exCustomerDialog").dialog('open');
	}
	function closeDialog() {
		$("#exCustomerDialog").dialog('close');
		$("#exCustomerDialog").dialog('destroy');
	}
	
	function searchCustomer() {
		
		
		var firstName = $("#firstName").val();
		var lastName = $("#lastName").val();
		var ssn = $("#ssn").val();
		var dob = $("#dob").val();
		
		var msg = '';
		if(firstName == ''){
			msg = msg + 'First Name is required' + "<br>";
		}
		if(lastName == ''){
			msg = msg + 'Last Name is required' + "<br>";
		}
		if(ssn == '' || ssn.length<11){
			msg = msg + 'Valid SSN is required' + "<br>";
		}
		if(dob == '' || dob.length<9){
			msg = msg + 'Valid date of birth is required' + "<br>";
		}
		
		if(msg!=''){			
			appConfirmDialog(msg, "Error" );
			return false;
		}
		
		var myForm = $("#requestForm").serialize();   
		
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/servlet/search/retrieveCustomer",
			data: myForm,
			dataType: "json",
			cache: false,
			async: false, // to set local variable
			success: function(data) {
				if(data.status == 'OK') {
					$("#oldRegDate").html(data.regDate);
					showCustomerInfo(data.picture);			
					$("#personId").val(data.personId);
					$("#exCustomerDialog").dialog('close');
				} else {
					appConfirmDialog('Cannot find Customer!');
				}				
			},
			error: function(xhr, textStatus, errorThrown) {
				appConfirmDialog('Error to retrieve Customer information');
			}
		});
	}
	
	
	function showCustomerInfo(newFace) {
		
		document.getElementById('snapImage').setAttribute('src', 'data:image/png;base64,' + newFace);
		$("#customerInfoDialog").dialog({
          	title: "Existing Customer",
              autoOpen: false,
              draggable: "true",
              modal: true,             
              width: 800,
              height: 650,
         }); 
		 $("#customerInfoDialog").dialog('open');
		
	}
	
	function closeInfoDialog() {
		 $("#customerInfoDialog").dialog('close');
		 $("#customerInfoDialog").dialog('destroy');
	}
	
	function printIdCard() {
		var personId = $("#personId").val();
		var url = "${pageContext.request.contextPath}/servlet/customer/printcard?personId=" + personId;
		window.open(url, 'FraudShiledID', 'width=800px,height=800px,scroll=no;location:no');
	}
	
	function newCustomer() {
		document.location = "${pageContext.request.contextPath}/servlet/customer/new"
	}
</script>