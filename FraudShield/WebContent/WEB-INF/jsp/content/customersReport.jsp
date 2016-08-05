<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<style>
body {
	background: #ddd !important;
}
table { 
    border-collapse: separate; 
}
table tr{
    border: thin solid;
}
table th,
table td {
	padding:1px;
    border-top: thin solid; 
    border-bottom: thin solid;
    border-left: thin solid;
    border-right: thin solid;
}
</style>

<%
	String imgName = "sort_asc.png";
	String sortOrder = (String)request.getAttribute("sortOrder");
	if (sortOrder != null && sortOrder.equals("desc"))
	{
		imgName = "sort_desc.png";
	}
%>

<script type="text/javascript">

	function sort(col) {
		var prevSort = $('#sortCol').val();
		var prevSortOrder = $('#sortOrder').val();
		var url = "sortCustomersReport?sort=" + col + "&prevSort=" + prevSort + "&prevSortOrder=" + prevSortOrder;
		document.location=url;
	}
	
	function printReport() {
		var docprint=window.open("about:blank", "_blank"); 
		var oTable = document.getElementById("results_tbl");
		docprint.document.open(); 
		docprint.document.writeln('<html><head><title>Customers Report</title>'); 
		docprint.document.writeln('</head>');
		docprint.document.writeln('<style>');
		docprint.document.writeln('body {background: #ffffff !important;}');
		docprint.document.writeln('table {border-collapse: collapse;}');
		docprint.document.writeln('table tr{border: thin solid;}');
		docprint.document.writeln('table th, table td {border: thin solid;}');
		docprint.document.writeln('</style>');
		docprint.document.writeln('<body><center>');
		docprint.document.writeln('<h2><u>Customers Report</u></h3>');
		docprint.document.writeln(oTable.parentNode.innerHTML);
		docprint.document.writeln('</center></body></html>'); 
		docprint.document.close(); 
		docprint.print();
		docprint.close();
	}
	
</script>

<input type="hidden" id="sortCol" name="sortCol" value="${sortCol}" />
<input type="hidden" id="sortOrder" name="sortOrder" value="${sortOrder}" />

<div class="panel panel-default" style="margin-top:25px;">
	<div class="panel-heading">
		<span class="panel-title">Customers Report</span>
		<span style="float:right;right: 10px;display:inline-block;font-size:9">
			<a href="#" title="Print Report" onclick="javascript:printReport();">
				<img src="${pageContext.request.contextPath}/images/printer.png" />&nbsp;Print Report
			</a>
		</span>
	</div>
	<div class="panel-body">
		<table id="results_tbl">
			<thead>
				<tr>
					<th width=10"><a href="#" onclick="javascript:sort('fraudShieldID');">FraudShield ID</a><c:if test="${sortCol eq 'fraudShieldID'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="1%">Prefix</th>
					<th width="15%"><a href="#" onclick="javascript:sort('Name');">Name</a><c:if test="${sortCol eq 'Name'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="6%"><a href="#" onclick="javascript:sort('dob');">Date of Birth</a><c:if test="${sortCol eq 'dob'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="8%"><a href="#" onclick="javascript:sort('ssn');">SSN</a><c:if test="${sortCol eq 'ssn'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="12%"><a href="#" onclick="javascript:sort('personAddress.address1');">Address 1</a><c:if test="${sortCol eq 'personAddress.address1'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="6%">Address 2</th>
					<th width="8%"><a href="#" onclick="javascript:sort('personAddress.city');">City</a><c:if test="${sortCol eq 'personAddress.city'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="1%"><a href="#" onclick="javascript:sort('personAddress.state');">State</a><c:if test="${sortCol eq 'personAddress.state'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="7%"><a href="#" onclick="javascript:sort('Zip');">Zip</a><c:if test="${sortCol eq 'Zip'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="8%"><a href="#" onclick="javascript:sort('phone1');">Primary Phone</a><c:if test="${sortCol eq 'phone1'}"> <img src="${pageContext.request.contextPath}/images/<%=imgName%>"/></c:if></th>
					<th width="8%">Secondary Phone</th>
					<th width="10%">Email</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="customer" items="${customersList}">
					<tr>
						<td width="10%">${customer.fraudShieldID}</td>
						<td width="1%">${customer.prefix}</td>
						<td width="15%">${customer.firstName} ${customer.middleInitial} ${customer.lastName} ${customer.suffix}</td>
						<td width="6%">${customer.dob}</td>
						<td width="8%">${customer.ssn}</td>
						<td width="12%">${customer.address}</td>
						<td width="6%">${customer.address2}</td>
						<td width="8%">${customer.city}</td>
						<td width="1%">${customer.state}</td>
						<td width="7%">${customer.zip}<c:if test="${not empty customer.zip4}"> - ${customer.zip4}</c:if></td>
						<td width="8%">${customer.phone1}</td>
						<td width="8%">${customer.phone2}</td>
						<td width="10%">${customer.email}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
</div>
