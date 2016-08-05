<%@ page import="com.citizant.fraudshield.domain.TsoUser" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% 
	TsoUser user = (TsoUser)request.getAttribute("tsoUser");
%>

<% if (user != null) { %>
	<table id="tsoUserDetailsTable"  class="display">
		<thead>
			<tr>
				<th>Property</th>
				<th>Value</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>First Name</td>
				<td><%= user.getFirstName() %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>Last Name</td>
				<td><%= user.getLastName() %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>Contact Phone</td>
				<td><%= user.getContactPhone() %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>Contact Email</td>
				<td><%= user.getContactEmail() %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>Role</td>
				<td><%= user.getUserRole() %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>Active</td>
				<td><%=Boolean.toString(user.isActive()) %></td>
				<td>
				
					<%if(user.isActive()) {%>
						<input type="button"  name="cmdCancel"   class="btn btn-large btn-primary"  style="width:150px" id="cmdCancel" value="Lock Account" onclick="javascript:changeAccount(true, '<%=user.getUsername()%>');" />				
					
					<%} else { %>
						<input type="button"  name="cmdCancel"   class="btn btn-large btn-primary"  style="width:150px" id="cmdCancel" value="Unlock Account" onclick="javascript:changeAccount(false, '<%=user.getUsername()%>');" />										
					<%} %>
				
				</td>
			</tr>
		</tbody>
	</table>
<% } else {%>
		<p>No matching TSO User was found.</p>
<% } %>