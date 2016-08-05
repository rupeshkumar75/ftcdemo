<%@ page import="java.util.List" %>
<%@ page import="com.citizant.fraudshield.domain.Person" %>
<%@ page import="com.citizant.fraudshield.util.StringUtil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">

	$(document).ready(function() {
		if($("#customerListTable")!=null) {
			$("#customerListTable").dataTable( { 
				"bJQueryUI" : true,
				"bAutoWidth" : true,
				"aaSorting":[[0, "asc"]],
				"iDisplayLength" : 10,	
				"oLanguage": {"sSearch": "Filter: "} 
			});	
		}
	});

</script>

<% 
	List<Person> results = (List<Person>)session.getAttribute("results");
	String errorMessage = (String)request.getAttribute("errorMessage");
%>

<div>
	<% if (!StringUtil.isEmpty(errorMessage)) {%>
   		<font color="red"><%= errorMessage %></font>
   		<br />
	<% } %>
</div>

<% if (results != null) { %>
	<% if (results.size() > 0) { %>
	<table id="customerListTable"  class="display">
		<thead>
			<tr>
				<th>Name</th>
				<th>Date of Birth</th>
				<th>SSN</th>
				<th>Fraud Shield ID</th>
				<th>Address</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<% for (Person person : results) { %>
				<tr>
					<td><%= person.getFullName() %></td>
					<td><%= person.getDecryptedDOB() %></td>
					<td><%= person.getDecryptedSSN() %></td>
					<td><b><%= person.getFraudShieldID() %></b></td>
					<td><%= person.getAddress() %></td>
					<td>
					<a href="${pageContext.request.contextPath}/servlet/customer/edit?customerId=<%=person.getPersonId()%>">
					<span class="glyphicon glyphicon-list-alt"></span>
					</a></td>
				</tr>
			<% } %>
		</tbody>
	</table>
	<script>
		document.getElementById('printLink').style.display = 'inline-block';
	</script>
	<% } else {%>
		<p>No records found matching the search criteria.</p>
	<% } %>
<% } %>