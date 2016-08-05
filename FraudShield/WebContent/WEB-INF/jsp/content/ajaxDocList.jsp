<%@  page language="java" import="java.io.*"%>
<%@page import="com.citizant.fraudshield.bean.ScannedIdentityBean"%>
<%@page import="com.citizant.fraudshield.bean.ScannedDocHolder"%>
<%@page import="com.citizant.fraudshield.common.AppConstants"%>



<%	int docCounts = 0;
	int picCount = 0; 
	ScannedDocHolder holder = (ScannedDocHolder)session.getAttribute(AppConstants.DOC_HOLDER);
	if (holder!=null){
		docCounts = holder.getDocs().size();
		int count = 0;
		for(ScannedIdentityBean doc : holder.getDocs()){
			String docType = doc.getDocType();
			if (docType == null) docType = "";
			double random = Math.random();
			if (doc.getDocType().equals("Picture"))
			{
				picCount++;
				docCounts--;
			}
%>
			<div style="float:left;padding:20px;">
				<div style="background-color:gray;border-radius: 15px;">					
						<img src="${pageContext.request.contextPath}/servlet/customer/thumb?index=<%=count%>&random=<%=random%>" 
							width="150" style="padding-top:15px;padding-bottom:15px;padding-left:5px;padding-right:5px;"/>					
				</div>
				<div>			
					<%=docType%>
					<br/>
					<% if (!docType.equals(AppConstants.PICTURE_ID_TYPE)) { %> 
						Expires:<%=doc.getDocExpirationDateAsString()%>
					<%} %>
					<br/>
					<a href="#" onclick="javascript:viewDoc(<%=count%>);"><span class="glyphicon glyphicon-zoom-in"></span></a>&nbsp; &nbsp;
					<div class="actionLinks">
						<a href="#" onclick="javascript:confirmRemoveDoc(<%=doc.getId()%>, <%=count%>, '<%=doc.getDocType()%>');">
							<span class="glyphicon glyphicon-trash"></span>
						</a>
					</div>
				</div>
			</div>
<%
		count++;
		} }%>

<input type="hidden" name="numOfIds" id="numOfIds" value="<%=docCounts%>"/>
<input type="hidden" name="numOfPics" id="numOfPics" value="<%=picCount%>"/>