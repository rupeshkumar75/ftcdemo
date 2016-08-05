<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<script type="text/javascript">
	var contextPath;
	var docId;
	var docCount;
	var contextPath;

	$(document).ready(function() {
		$.mask.definitions['~']='[+-]';
		$("#zip").mask("?99999");
		$("#zip4").mask("?9999");
		$("#ssn").mask("999-99-9999");
		$("#dob").mask("99/99/9999");
		$("#phone1").mask("(9?99) 999-9999");
		$("#phone2").mask("(9?99) 999-9999");
		contextPath = $("#contextPath").val();
		if($("#showDocs").val()){
			listDocs();
		}
		//var conferenceUrl = "https://apprtc.appspot.com/r/" + ${caller.roomNumber};
		var conferenceUrl = contextPath+"/cctsoConference.jsp?requestCode=" + ${caller.requestCode} + "&roomNumber=" + ${caller.roomNumber};
		window.open(conferenceUrl, 'Customer Name', 'width=900px,height=800px,scroll=no;location:no');
	});
	
	function saveCustomer(){
		if( !validateForm() ){
			return;
		}
		var myForm = $('#customerForm').serialize();
		var url = contextPath+"/servlet/renew/approveRenew";

		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "POST",
				url: url,
				data: myForm,
				dataType: "json",
				cache: false,
				async: false, // to set local variable
				success: function(data) {
					//$("#fraudShieldID").val(data.fsId);
					//$("#personId").val(data.personId);
					//$("#addresId").val(data.addressId);
					//appConfirmDialog('Customer information Saved!');
					back();
				},
				error: function(xhr, textStatus, errorThrown) {
					appConfirmDialog('Error to Saved Customer information');
				}
			});
		},500);		
	}
	
	function addIdentity(){
		var url = contextPath + "/scanController.jsp?sid=<%=session.getId()%>";
		window.open(url, '', 'width=1000px,height=650px,scroll=no;location:no');
		//window.showModalDialog(url, '', 'dialogWidth:1000px;dialogHeight:650px;scroll:no;location:no');
	}
	
	function listDocs(){
		var url = contextPath + "/servlet/customer/doclist";
		$.ajax({
			type: "GET",
			url: url,
			cache: false,
			async: false, // to set local variable
			success: function(response) { 				 
			 	$("#identityList").html("");  
			 	$("#identityList").html(response); 
			},
            error: function(xhr, textStatus, errorThrown) {
              alert("error");
            }    
		});				
	}
	
	function back(){
		var url = contextPath + "/servlet/renew/cctsopanel";
		document.location=url;
	}
	
	function confirmRemoveDoc(id, count){
		docId = id;
		docCount = count;
		var message = "Do you want to remove the scanned documnet?";
		appConfirmDialog(message,"Confirmation Message", '', deleteImage );
	}
	
	function deleteImage(){		
		var url = contextPath + "/servlet/customer/deletedoc?docId=" + docId + "&count=" + docCount;
		$.ajax({
			type: "GET",
			url: url,
			cache: false,
			async: false, // to set local variable
			success: function(response) { 				 
				listDocs();
			},
            error: function(xhr, textStatus, errorThrown) {
              alert("error to delete");
            }    
		});				
	}
	
	function validateForm(){
		var firstName = $("#firstName").val();
		var lastName = $("#lastName").val();
		var ssn = $("#ssn").val();
		var dob = $("#dob").val();
		var address = $("#address").val();
		var city = $("#city").val();
		var state = $("#state").val();
		var zip = $("#zip").val();
		var phone1 = $("#phone1").val();
		var numOfIds = $("#numOfIds").val();
		
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
		if(address == ''){
			msg = msg + 'Address is required' + "<br>";
		}
		if(city == ''){
			msg = msg + 'City is required' + "<br>";
		}
		if(state == ''){
			msg = msg + 'State is required' + "<br>";
		}
		if(zip == '' || zip.length<5){
			msg = msg + 'Valid zip code is required' + "<br>";
		}
		if(phone1 == ''){
			msg = msg + 'Valid primary phone number is required' + "<br>";
		}
		if(numOfIds == '' || parseInt(numOfIds)<2){
			msg = msg + 'At least 2 forms of identification are required' + "<br>";
		}
		
		if(msg!=''){			
			appConfirmDialog(msg, "Error" );
			return false;
		}
		return true;
	}
	
	function viewDoc(count){
		var url = contextPath + "/servlet/customer/image?index=" + count + "&random=" + Math.random();	
		$('#docImage').attr('src',url);
		$("#imageViewer").dialog({
	     	title: "Document Image",
	        autoOpen: false,
	        draggable: "true",
	        modal: true,
	        height: 600,
	        width: 600
	    }); 
		$("#imageViewer").dialog('open');		
	}
	
	function declineRenew(){
		$("#commentsDialog").dialog({
	     	title: "Decline Renew",
	        autoOpen: false,
	        draggable: "true",
	        modal: true,
	        height: 400,
	        width: 600
	    }); 
		$("#commentsDialog").dialog('open');	
	}
	
	function confirmDecline() {
		
		var myForm = $('#declineCommentsForm').serialize();
		var url = contextPath+"/servlet/renew/declineRenew";

		setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
			$.ajax({
				type: "POST",
				url: url,
				data: myForm,
				dataType: "json",
				cache: false,
				async: false, // to set local variable
				success: function(data) {				
					back();
				},
				error: function(xhr, textStatus, errorThrown) {
					appConfirmDialog('Error to Saved Customer information');
				}
			});
		},500);		
		
		$("#commentsDialog").dialog('close');	
	}
	
	function cancelDecline() {
		$("#commentsDialog").dialog('close');	
	}
	
</script>
<style>
	

</style>
<div class="container">

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Renew Customer Registration</h3>
	</div>
	<div class="panel-body">
	<form:form commandName="frmBean" id="customerForm" action="" >
		<input type="hidden" name="mode" id="mode"/>		
		<input type="hidden" name="customerId" id="customerId"/>
		<input type="hidden" name="requestCode" id="requestCode" value="${caller.requestCode}"/>
		<input type="hidden" name="showDocs" id="showDocs" value="${showDocs}"/>
		<form:input type="hidden" path="personId" id="personId"/>
		<form:input type="hidden" path="addressId" id="addressId"/>
		<table style="border-spacing: 10px;">
		<tr>
			<td style="valign:top;">
				<table  style="border-spacing: 10px;padding: 10px;">
					<tr>
						<td>Name Prefix/Salutation:</td>
						<td>
							<form:select path="prefix" id="prefix" title="Name Prefix/Salutation" 
								maxlength="10" tabindex="222"  class="form-control">
								<form:options items="${namePrefixes}" />
							</form:select>
						</td>
					</tr>
					<tr>
						<td>First Name: <span class="glyphicon glyphicon-star"></span></td>			
						<td>
							<form:input path="firstName" id="firstName" title="First Name" maxlength="13" 
							placeholder="first name" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Middle Initial:</td>
						<td>
							<form:input path="middleInitial" id="middleInitial" title="Middle Initial" maxlength="13"
							 placeholder="middle name" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Last Name: <span class="glyphicon glyphicon-star"></span></td>
						<td>
							<form:input path="lastName" id="lastName" title="last Name" maxlength="13"
							 placeholder="last name" class="form-control"/>
						</td>
					</tr>
				
					<tr>
					    <td>Name Suffix:</td>
						<td>
						    <form:select path="suffix" id="suffix" title="Name Prefix/Salutation" 
								maxlength="10" tabindex="222"  class="form-control">
								<form:options items="${nameSuffixes}" />
							</form:select>
						</td>
					</tr>
					<tr>
						<td>SSN # : <span class="glyphicon glyphicon-star"></span></td>
						<td>
							<form:input path="ssn" id="ssn" title="SSN#" maxlength="13"
							 placeholder="SSN#" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Date of Birth: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="dob" id="dob" title="Date of Birth" maxlength="15"
							 placeholder="Date of Birth" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Fraud Shield ID:</td>
						<td>
							<form:input path="fraudShieldID" id="fraudShieldID" title="Fraud Shield ID" maxlength="15"
							 	placeholder="Fraud Shield ID" class="form-control"  readonly="true"/>			
						</td>
					</tr>
					<tr>
						<td>
								
						</td>
						<td style="align:right;"><span class="glyphicon glyphicon-star"></span> - Required Field</td>
					</tr>
				</table>
			</td>
			<td width="150">&nbsp;</td>
			<td>
				<table>
					<tr>
						<td>Address: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="address" id="address" title="street address" maxlength="50"
							 placeholder="street address" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Address 2:</td>
						<td>
						<form:input path="address2" id="address2" title="street address2" maxlength="50"
							 placeholder="street address2" class="form-control"/>
						</td>
					
					</tr>
					<tr>
						<td>City: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="city" id="city" title="city" maxlength="50"
							 placeholder="city" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>State : <span class="glyphicon glyphicon-star"></span></td>
						<td>
						    <form:select path="state" id="state" title="Name Prefix/Salutation" 
								maxlength="10" tabindex="222"  class="form-control">
								<form:options items="${states}" />
							</form:select>
							
						</td>
					</tr>
					<tr>
					<td>Zipcode: <span class="glyphicon glyphicon-star"></span></td>
						<td>
							<form:input path="zip" id="zip" title="Zip Code" maxlength="5"
							 placeholder="Zip Code" class="form-control"/>
						</td>	
					</tr>
					<tr>
						<td>Zip code +4 :</td>
						<td><form:input path="zip4" id="zip4" title="Zip Code +4" maxlength="5"
							 placeholder="Zip Code +4" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Primary Phone: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="phone1" id="phone1" title="primary phone" maxlength="15"
							 placeholder="primary phone" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Secondary Phone:</td>
						<td>
						<form:input path="phone2" id="phone2" title="secondary phone" maxlength="15"
							 placeholder="secondary phone" class="form-control"/>
						</td>
	
					</tr>
					<tr>
						<td>Email:</td>
						<td>
						<form:input path="email" id="email" title="email" maxlength="15"
							 placeholder="email" class="form-control"/>
						</td>
					</tr>
				</table>			
			</td>	
			
			</tr>
			</table>	
		</form:form>
	</div>
		<div class="center" align="center">
			<input type="button"  name="cmdSave"  class="btn btn-large btn-primary"  style="width:100px" id="cmdSave" value="Approve" class="button" onclick="javascript:saveCustomer();" />
			&nbsp;&nbsp;
			<input type="button"  name="cmdCancel"  class="btn btn-large btn-primary"  style="width:100px" id="cmdCancel" value="Decline" onclick="javascript:declineRenew();" />
			&nbsp;&nbsp;
			<input type="button"  name="cmdCancel"   class="btn btn-large btn-primary"  style="width:100px" id="cmdCancel" value="Back" onclick="javascript:back();" />
			
			
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">
			<span>Identity Documents <span class="glyphicon glyphicon-star"></span></span>		
		</div>
		<div class="panel-body" id="identityList"></div>			
	</div>
</div>

	<div id="imageViewer" style="display:none">
		<img id="docImage" src="" />
		
	</div>

	<div id="commentsDialog" style="display:none">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Decline Request Renew</h3>
			</div>
			<div class="panel-body">
		<form id="declineCommentsForm">
			<input type="hidden" name="requestCode" value="${caller.requestCode}"/>
			Comments : 
				<textarea id="declineComments" name="declineComments" rows="4" cols="200"></textarea>
			 <br/>
			 <br/>
			
			<button class="btn btn-large btn-primary" type="button" onclick="confirmDecline();">OK</button>
			<button class="btn btn-large btn-primary" type="button" onclick="cancelDecline();">Close</button>
		</form>
			</div>
		</div>		
	</div>
	
</div>
