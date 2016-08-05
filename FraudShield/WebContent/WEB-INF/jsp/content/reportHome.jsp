<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<style>


.rptr{
    border: thin solid;
}

.rptd {
	padding:1px;
    border-top: thin solid; 
    border-bottom: thin solid;
    border-left: thin solid;
    border-right: thin solid;
    align:center;
}
</style>


<div class="container" style="margin-top:100px;width:60%;">

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Fraud Shield System Reports</h3>
	</div>
	<div class="panel-body">
			<ul>
		
					<span style="display:inline-block;font-size:9">
						<span class="glyphicon glyphicon-list-alt"></span>
						<a href="#" onclick="javascript:generateCustomersReport();">All Customers Report</a>
					</span>
					<p>&nbsp;</p>
					<span style="display:inline-block;font-size:9">
						<span class="glyphicon glyphicon-list-alt"></span>
						<a href="#" onclick="javascript:startStatusReport();">Status Report</a>
					</span>
							
			</ul>		
	</div>
</div>
</div>

<div id="statusReportDiv" style="display:none;">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">Fraud Shield Status Reports</h3>
		</div>
		<div class="panel-body">
		
			<form id="searchForm" method="post">		
					<table>
						<tr>
							<td>Time Frame :</td>
							<td>
								<select class="form-control" name="timeFrame" id="timeFrame" onchange="setTimeFrame(this.value);">
									<option value="0"></option>
									<option value="1">1 Day</option>
									<option value="2">3 Days</option>
									<option value="3">1 week</option>
									<option value="4">30 days</option>
									<option value="5">60 days</option>
									<option value="6">6 month</option>
									<option value="7">1 year</option>
								</select>
							</td>
						</tr>
				
						<tr>
							<td>From : (MM/dd/yyyy)</td>
							<td><input type="text" style="width: 180px;" maxlength="20"
								class="form-control" name="fromDate" id="fromDate" value=""
								placeholder="From" />
							</td>
						</tr>
						<tr>
							<td>To : (MM/dd/yyyy)</td>
							<td><input type="text" style="width: 180px;" maxlength="120"
								class="form-control" name="toDate" id="toDate" value=""
								placeholder="To" />
							</td>
						</tr>
				</table>	
				<div class="center" align="center">
				<input type="button" name="cmdSearch" class="btn btn-large btn-primary"  style="width:100px" id="cmdSearch" value="Generate" class="btn-default"  style="width:100px"
					onclick="javascript:generateStatusReport();" />
				<input type="button" name="cmdClear"  class="btn btn-large btn-primary"  style="width:100px" id="cmdClear" value="Close" class="btn-default"  style="width:100px"
					onclick="javascript:closeReportDialog();" />
			</div>
			</form>
		
		<div class="panel panel-default">
			<div class="panel-heading">
				<span class="panel-title">Status Report</span>
			</div>
			<div class="panel-body" id="customerList">
				
				<table id="statusTable" style="cellpadding:10px;callspacing=10px;">
				<thead>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</thead>
				<tbody>
					<tr class="rptr">
						<td width="60%" class="rptd">Number of New Registrations</td>
						<td class="rptd"><div id="numOfReg"></div></td>
					</tr>
					<tr>
						<td class="rptd">Number of Expired Registrations</td>
						<td class="rptd"><div id="numOfExp"></div></td>
					</tr>
					<tr class="rptr">
						<td class="rptd">Number of Renewed Registrations</td>
						<td class="rptd"><div id="numOfRen"></div></td>
					</tr>
					<tr class="rptr">
						<td class="rptd">Number of TSO users</td>
						<td class="rptd"><div id="numOfTso"></div></td>
					</tr>
					<tr class="rptr">
						<td class="rptd">Total number of TSO logins</td>
						<td class="rptd"><div id="numOfLogin"></div></td>
					</tr>
					<tr class="rptr">
						<td class="rptd">Number of Fraud Shield ID Printed</td>
						<td class="rptd"><div id="numOfPrint"></div></td>
					</tr>	
					<tr class="rptr">
						<td class="rptd">Number of TSO Account Locked</td>
						<td class="rptd"><div id="numOfLock"></div></td>
					</tr>	
					
					<tr class="rptr">
						<td class="rptd">Number of Success TSO logins</td>
						<td class="rptd"><div id="numOfSuccess"></div></td>
					</tr>	
					<tr class="rptr">
						<td class="rptd">Number of Failed TSO logins</td>
						<td class="rptd"><div id="numOfFail"></div></td>
					</tr>	
				</tbody>																		
				</table>
			</div>
		</div>	
		
	</div>	
</div>


<script>
var oTable;

function generateStatusReport() {
	var url = "${pageContext.request.contextPath}/servlet/report/generateStatusReport";
	var myForm = $('#searchForm').serialize();
	$.ajax({
		type: "POST",
		url: url,
		data: myForm,
		dataType: "json",
		cache: false,
		async: false, // to set local variable
		success: function(data) {					
			$("#numOfReg").html(data.numOfReg);
			$("#numOfExp").html(data.numOfExp);
			$("#numOfRen").html(data.numOfRen);
			$("#numOfTso").html(data.numOfTso);
			$("#numOfLogin").html(data.numOfLogin);
			$("#numOfFail").html(data.numOfFail);
			$("#numOfSuccess").html(data.numOfSuccess);
			$("#numOfPrint").html(data.numOfPrint);
			$("#numOfLock").html(data.numOfLock);
		},
		error: function(xhr, textStatus, errorThrown) {
			appConfirmDialog('Error get status report');
		}
	});
}

function closeReportDialog(){
	$("#statusReportDiv").dialog('close');
	
}

$(document).ready(function() {
	$( "#fromDate" ).datepicker();
	$( "#toDate" ).datepicker();
	
	$.mask.definitions['~']='[+-]';
	$("#fromDate").mask("99/99/9999");
	$("#toDate").mask("99/99/9999");
	
	
});

function generateCustomersReport(){
	var url = "${pageContext.request.contextPath}/servlet/report/generateCustomersReport";
	document.location=url;
}

function startStatusReport() {
	 $("#statusReportDiv").dialog({
      	title: "Status Report",
          autoOpen: false,
          draggable: "true",
          modal: true,
          height: 650,
          width: 600
      }); 
	
	 $("#statusReportDiv").dialog('open');
}

function setTimeFrame(frameId) {
	
	$("#numOfReg").html('');
	$("#numOfExp").html('');
	$("#numOfRen").html('');
	$("#numOfTso").html('');
	$("#numOfLogin").html('');
	$("#numOfFail").html('');
	$("#numOfSuccess").html('');
	$("#numOfPrint").html('');
	$("#numOfLock").html('');
	
	var url = "${pageContext.request.contextPath}/servlet/report/getTimeframe?frameId=" + frameId;
	$.ajax({
		type: "GET",
		url: url,
		dataType: "json",
		cache: false,
		async: false, // to set local variable
		success: function(data) {												
			$("#fromDate").val(data.fromDate);
			$("#toDate").val(data.toDate);
		},
		error: function(xhr, textStatus, errorThrown) {
			appConfirmDialog('Error !');
		}
	});
}
</script>