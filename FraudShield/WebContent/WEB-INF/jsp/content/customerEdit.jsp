<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<script type="text/javascript">
	var contextPath;
	var docId;
	var docCount;
	var duplicateUser = false;
	var mode;
	$(document).ready(function() {
		$.mask.definitions['~']='[+-]';
		$("#zip").mask("?99999");
		$("#zip4").mask("?9999");
		$("#ssn").mask("999-99-9999");
		$("#reenterSSN").mask("999-99-9999");
		$("#dob").mask("99/99/9999");
		$("#phone1").mask("(9?99) 999-9999");
		$("#phone2").mask("(9?99) 999-9999");
		
		contextPath = $("#contextPath").val();
		if($("#showDocs").val()){
			listDocs();
		}
		
		//check if this is edit mode
		var personId = $("#personId").val();
		if(personId != "") {
			pageReadOnly();
     		document.getElementById("printButton").style.display='none';             
		} else {
			setInterval(function(){
				checkNewDocs();}, 1500);
		}
	});
	
	
	function pageReadOnly() {
		//Scan Page, disable all input fields
		var elems = document.getElementsByClassName('form-control');
		for(var i = 0; i < elems.length; i++) {
			elems[i].disabled = true;
		}
		             
		var elems = document.getElementsByClassName('actionLinks');
		for(var i = 0; i < elems.length; i++) {
			elems[i].style.display = "none";
		}
		             			
		//Hide action links
		document.getElementById("cmdClear").style.display='none';
		document.getElementById("cmdSave").style.display='none';     		
	}
	
	function saveCustomer(){
		
		var ssn = $("#ssn").val();		
		var url = contextPath + "/servlet/customer/checkDuplicate";	
		$.ajax({
			type: "POST",
			url: url,
			data: {
				'ssn' : ssn
			},
			dataType: "json",
			cache: false,
			async: false, // to set local variable
			success: function(response) {
				if(response.status == 'DUP') {		
					appConfirmDialog('There is an existing Fraud Shield Registration for this SSN.');
					return false;
				} else {
					if( !validateForm() ){
						return;
					}
					
					var myForm = $('#customerForm').serialize();
					var url = contextPath+"/servlet/customer/save";

					setTimeout(function(){ // this gives the UI time to update before AJAX locks it up
						$.ajax({
							type: "POST",
							url: url,
							data: myForm,
							dataType: "json",
							cache: false,
							async: false, // to set local variable
							success: function(data) {					
								if(data.status == 'OK') {					
									$("#fraudShieldID").val(data.fsId);
									$("#personId").val(data.personId);
									$("#addressId").val(data.addressId);
									$("#previousSSN").val(data.ssn);
									document.getElementById("printButton").disabled = false;
									pageReadOnly();
									appConfirmDialog('Customer information Saved!');
								} else {					
									appConfirmDialog(data.errorMsg);
								}					
							},
							error: function(xhr, textStatus, errorThrown) {
								appConfirmDialog('Error saving Customer information');
							}
						});
					},500);
				}	
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});
			
	}
	
	function addIdentity(){
		var url = contextPath + "/webtwain/webscan.jsp?sid=<%=session.getId()%>";
		window.open(url, 'ScanDoc', 'width=1000px,height=700px,scroll=no;location:no');
		//window.showModalDialog(url, '', 'dialogWidth:1000px;dialogHeight:650px;scroll:no;location:no');
	}
	
	function capturePicture() {
		var url = contextPath + "/webtwain/webcam.jsp?sid=<%=session.getId()%>";
		window.open(url, 'TakePicture', 'width=1000px,height=750px,scroll=no;location:no');
		//window.open(url);
		//document.location = url;
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
	
	
	function checkNewDocs(){
		var url = contextPath + "/servlet/customer/checkNewDocs";
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			cache: false,
			async: false, // to set local variable
			success: function(data) { 				 
			 	if(data.status == 'yes') {
			 		listDocs();
			 	}
			},
            error: function(xhr, textStatus, errorThrown) {
             
            }    
		});				
	}
	
	
	function back(){
		var url = contextPath + "/servlet/customer/back";
		document.location=url;
	}
	
	function newCustomer(){
		var url = contextPath + "/servlet/customer/new";
		document.location=url;	
	}
	
	function confirmRemoveDoc(id, count, type){
		docId = id;
		docCount = count;
		var numOfIds = $("#numOfIds").val();
		var numOfPics = $("#numOfPics").val();
		if (type == "Picture"){
			if (parseInt(numOfPics)==1) {
				appConfirmDialog("There is only Picture for this customer.  This Picture cannot be deleted at this time.", "Error" );
				return false;
			}
		}
		else if (parseInt(numOfIds)==2) {
			appConfirmDialog("There are only two identification documents for this customer.  These documents cannot be deleted at this time.", "Error" );
			return false;
		}
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
		
		var personId = $("#personId").val();
		var firstName = $("#firstName").val();
		var lastName = $("#lastName").val();
		var ssn = $("#ssn").val();
		var reSSN = $("#reenterSSN").val();
		var dob = $("#dob").val();
		var address = $("#address").val();
		var city = $("#city").val();
		var state = $("#state").val();
		var zip = $("#zip").val();
		var phone1 = $("#phone1").val();
		var numOfIds = $("#numOfIds").val();
		var numOfPics = $("#numOfPics").val();
		var email = $("#email").val();
		
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
		if(reSSN == '' || reSSN.length<11){
			msg = msg + 'Please re-enter SSN' + "<br>";
		}
		if(dob == '' || dob.length<9){
			msg = msg + 'Valid date of birth is required' + "<br>";
		} else {
			 var birthday = moment(dob, ["MM/DD/YYYY"], true);
			 if (!birthday.isValid()) {
				 msg = msg + "Invalid date of birth" + "<br>";    
			 } else {
				 var now = moment();
				 if(birthday.isAfter(now)) {
					 msg = msg + "Date of birth is a future date" + "<br>";   
				 }
			 }
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
		if(numOfPics == '' || parseInt(numOfPics)<1){
			msg = msg + 'Identification Picture is required' + "<br>";
		}
		if (ssn != '' && reSSN != '' && ssn != reSSN) {
			msg = msg + 'SSNs do not match' + "<br>";
		}
		if (email == '' || !validateEmail(email)) {
			msg = msg + 'Valid email address is required' + "<br>";
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
	        position: 'top',
	        width: 800,
	        height: 700
	    }); 
		$("#imageViewer").dialog('open');		
	}
	
	function checkDuplicate(ssn) {
		var url = contextPath + "/servlet/customer/checkDuplicate";	
		$.ajax({
			type: "POST",
			url: url,
			data: {
				'ssn' : ssn
			},
			dataType: "json",
			cache: false,
			async: false, // to set local variable
			success: function(response) {
				if(response.status == 'DUP') {		
					return true;					
				}	
			},
			error: function(xhr, textStatus, errorThrown) {
				
			}
		});
		
		return false;
	}
	
	
	function checkSSN(ssn) {
		if(ssn.length >= 11) {			
			var url = contextPath + "/servlet/customer/checkDuplicate";	
			$.ajax({
				type: "POST",
				url: url,
				data: {
					'ssn' : ssn
				},
				dataType: "json",
				cache: false,
				async: false, // to set local variable
				success: function(response) {
					if(response.status == 'DUP') {		
						$("#oldRegDate").html(response.regDate);
						$("#customerId").val(response.personId);
						document.getElementById('snapImage').setAttribute('src', 'data:image/png;base64,' + response.picture);
						$("#duplicateWarningDialog").dialog({
				          	title: "Duplicated Registration",
				              autoOpen: false,
				              draggable: "true",
				              modal: true,             
				              width: 800,
				              height: 700
				         }); 
						 $("#duplicateWarningDialog").dialog('open');						
					}	
				},
				error: function(xhr, textStatus, errorThrown) {
					
				}
			});			
		}
	} 
	
	function confirmContinueReg() {
		var message = 'Are you sure you want to invalidate the existing Fraud Shield Registration?';
		$("<div></div>").appendTo('body').html(
				'<div tabindex="0" style="text-decoration:none">' + message + '</div>').dialog({
			draggable : true,
			show : "blind",
			modal : true,
			title : "Confirm",
			zIndex : 10005,
			resizable : false,
			buttons : {
				Ok : function() {
					$(this).dialog("close");
					invalidateOldReq();
					$("#duplicateWarningDialog").dialog('close');
					$("#duplicateWarningDialog").dialog('destroy');
				},
				Cancel : function() {
					$(this).dialog("close");					
				}
			},
			open : function() {
				
			},
			close : function(event, ui) {
				$(this).remove();
			}
		});	
	}
	
	function invalidateOldReq() {
		var personId = $("#customerId").val();
		var url = contextPath + "/servlet/customer/invalidate?personId=" + personId;	
		$.ajax({
			type: "GET",
			url: url,
			dataType: "json",
			cache: false,
			async: false, // to set local variable
			success: function(response) {
				if(response.status == 'OK') {		
					appConfirmDialog("Existing Fraud Shield Registration was invalidated", "Confirm" );
				}	
			},
			error: function(xhr, textStatus, errorThrown) {
				appConfirmDialog("Failed to invalidate existing Fraud Shield Registration", "Error" );
			}
		});
	}
	
	function printIdCard() {
		var personId = $("#personId").val();
		var url = "${pageContext.request.contextPath}/servlet/customer/printcard?personId=" + personId;
		window.open(url, 'FraudShiledID', 'width=800px,height=800px,scroll=no;location:no');
	}
</script>
<style>
	

</style>
<div class="container">

<div class="panel panel-default" style="background: #eff8fe; background-size: coverd; margin: 15px;">
	<div class="panel-heading">
		<h3 class="panel-title">Customer Information</h3>
	</div>
	<div class="panel-body">
	<form:form commandName="frmBean" id="customerForm" action="" >
		<input type="hidden" name="mode" id="mode"/>		
		<input type="hidden" name="customerId" id="customerId"/>
		<input type="hidden" name="showDocs" id="showDocs" value="${showDocs}"/>
		<form:input type="hidden" path="personId" id="personId"/>
		<form:input type="hidden" path="addressId" id="addressId"/>
		<input type="hidden" name="previousSSN" id="previousSSN" value="${frmBean.ssn}"/>
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
							placeholder="first name" class="form-control"  tabindex="223"/>
						</td>
					</tr>
					<tr>
						<td>Middle Initial:</td>
						<td>
							<form:input path="middleInitial" id="middleInitial" title="Middle Initial" maxlength="13"
							 placeholder="middle name" class="form-control"  tabindex="224"/>
						</td>
					</tr>
					<tr>
						<td>Last Name: <span class="glyphicon glyphicon-star"></span></td>
						<td>
							<form:input path="lastName" id="lastName" title="last Name" maxlength="13"
							 placeholder="last name" class="form-control"  tabindex="225"/>
						</td>
					</tr>
				
					<tr>
					    <td>Name Suffix:</td>
						<td>
						    <form:select path="suffix" id="suffix" title="Name Prefix/Salutation" 
								maxlength="10" tabindex="226"  class="form-control">
								<form:options items="${nameSuffixes}" />
							</form:select>
						</td>
					</tr>
					<tr>
						<td>SSN # : <span class="glyphicon glyphicon-star"></span></td>
						<td>
							<form:input path="ssn" id="ssn" title="SSN#" maxlength="13"  tabindex="227"
							 placeholder="SSN#" class="form-control" onchange="javascript:checkSSN(this.value);"/>
						</td>
					</tr>
					<tr>
						<td>Reenter SSN # : <span class="glyphicon glyphicon-star"></span></td>
						<td>
							<input type="text" id="reenterSSN" name="reenterSSN" title="SSN#" value="${frmBean.ssn}" 
								maxlength="13" placeholder="SSN#" class="form-control"  tabindex="228"/>
						</td>
					</tr>
					<tr>
						<td>Date of Birth: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="dob" id="dob" title="Date of Birth" maxlength="15"  tabindex="229"
							 placeholder="Date of Birth" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Fraud Shield ID:</td>
						<td>
							<form:input path="fraudShieldID" id="fraudShieldID" title="Fraud Shield ID" maxlength="15"
							 	placeholder="Fraud Shield ID" class="form-control"  disabled="true"/>			
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
						<form:input path="address" id="address" title="street address" maxlength="50"  tabindex="230"
							 placeholder="street address" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Address 2:</td>
						<td>
						<form:input path="address2" id="address2" title="street address2" maxlength="50" tabindex="231"
							 placeholder="street address2" class="form-control"/>
						</td>
					
					</tr>
					<tr>
						<td>City: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="city" id="city" title="city" maxlength="50"  tabindex="232"
							 placeholder="city" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>State : <span class="glyphicon glyphicon-star"></span></td>
						<td>
						    <form:select path="state" id="state" title="Name Prefix/Salutation" 
								maxlength="10" tabindex="233"  class="form-control">
								<form:options items="${states}" />
							</form:select>
							
						</td>
					</tr>
					<tr>
					<td>Zipcode: <span class="glyphicon glyphicon-star"></span></td>
						<td>
							<form:input path="zip" id="zip" title="Zip Code" maxlength="5"  tabindex="234"
							 placeholder="Zip Code" class="form-control"/>
						</td>	
					</tr>
					<tr>
						<td>Zip code +4 :</td>
						<td><form:input path="zip4" id="zip4" title="Zip Code +4" maxlength="5"  tabindex="235"
							 placeholder="Zip Code +4" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Primary Phone: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="phone1" id="phone1" title="primary phone" maxlength="15"  tabindex="236"
							 placeholder="primary phone" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>Secondary Phone:</td>
						<td>
						<form:input path="phone2" id="phone2" title="secondary phone" maxlength="15"  tabindex="237"
							 placeholder="secondary phone" class="form-control"/>
						</td>
	
					</tr>
					<tr>
						<td>Email: <span class="glyphicon glyphicon-star"></span></td>
						<td>
						<form:input path="email" id="email" title="email" maxlength="200"  tabindex="238"
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
		<input type="button"  name="cmdSave"  class="btn btn-large btn-primary" style="width:100px" id="cmdSave" value="Save" class="button" onclick="javascript:saveCustomer();"  tabindex="239"/>
		&nbsp;&nbsp;
		<button class="btn btn-large btn-primary" id="printButton" type="button" style="width:100px" disabled="true" onclick="printIdCard();">Print ID</button>
		&nbsp;&nbsp;
		<input type="button"  name="cmdClear"   class="btn btn-large btn-primary"  style="width:100px" id="cmdClear" value="Clear" onclick="javascript:newCustomer();"  tabindex="240"/>
		&nbsp;&nbsp;
		<input type="button"  name="cmdBack"   class="btn btn-large btn-primary"  style="width:100px" id="cmdCancel" value="Back" onclick="javascript:back();"  tabindex="241"/>
		
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">
			<span>Identity Documents <span class="glyphicon glyphicon-star"></span></span>
			<div class="actionLinks">
				<span style="display:inline-block;font-size:9">
			    	<a href="#" title="Scan Identity Document" onclick="javascript:addIdentity();">
						<span class="glyphicon glyphicon-hdd"></span> Scan Identity Document
					</a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="#" title="Scan Identity Document" onclick="javascript:capturePicture();">
						<span class="glyphicon glyphicon-camera"></span> Capture Picture
					</a>
				</span>
			</div>
		</div>
		<div class="panel-body" id="identityList"></div>			
	</div>
</div>
<div id="imageViewer" style="display:none">
	<img id="docImage" src="" width="700px"/>	
</div>

<div id="duplicateWarningDialog" style="display:none">
	<div>	
		<div align="center" style="margin: 15px;">
			<img id="snapImage" width="700px"/>
		</div>
	There is an existing Fraud Shield Registration for this SSN. If you choose continue to create new registration, 
	this registration will be removed.<br/>
	Existing Registration created at : <span id="oldRegDate">12/2/2014</span>
	</div>
	
	<div class="center" align="center" style="height:100px;line-height: 100px;">
		<input type="button"  name="cmdSave"  class="btn btn-large btn-primary" style="width:100px" id="cmdSave" value="Continue" class="button" onclick="javascript:confirmContinueReg();" />
			&nbsp;&nbsp;
		<input type="button"  name="cmdCancel"   class="btn btn-large btn-primary"  style="width:100px" id="cmdCancel" value="Cancel" onclick="javascript:back();" />		
	</div>
	
	
</div>

</div>