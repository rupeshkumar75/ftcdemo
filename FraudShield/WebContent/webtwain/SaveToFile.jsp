<%@  page language="java" import="java.io.*"%>
<%@page import="com.citizant.fraudshield.bean.ScannedIdentityBean"%>
<%@page import="com.citizant.fraudshield.bean.ScannedDocHolder"%>
<%@page import="com.citizant.fraudshield.common.AppConstants"%>
<%@page import="com.citizant.fraudshield.util.ImageResizer"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%!
	boolean success = false;
	//find string
	int Find(byte[] src, int start, int length, String str)
	{
		if (start < 0) return -1;
		if (start >= src.length) return -1;
		
		int end = (start + length - 1);
		if (end < 0) end = 0;
		if (end <= start) end = start;
		
		int index = 0;
		int firstMatch = -1;
		for (int i = start; i <= end; i++)
		{
			if (src[i] == str.charAt(index))
			{
				if (firstMatch == -1)
					firstMatch = i; //save first match
				index ++;
			}
			else
			{
				index = 0; //try to find again
				if (firstMatch != -1 && (firstMatch + 1)!= i)
					i = firstMatch + 1; //reset index to find
				firstMatch = -1;
			}
			
			if (index == str.length())
				return firstMatch; // found
		}
		
		return -1;
	}
	
	int ReverseFind(byte[] src, int start, int length, String str)
	{
		if (start < 0) return -1;
		if (start >= src.length) return -1;
		
		int end = (start - length + 1);
		if (end < 0) end = 0;
		if (end >= start) end = start;

		int index = str.length() - 1;
		int firstMatch = -1;
		
		for (int i = start; i >= end; i--)
		{
			if (src[i] == str.charAt(index))
			{
				if (firstMatch == -1)
					firstMatch = i; //save first match
				index --;
			}
			else
			{
				index = str.length() - 1; //try to find again
				if (firstMatch != -1 && (firstMatch - 1)!= i)
					i = firstMatch -1; // reset index to find
				firstMatch = -1;
			}
			
			if (index < 0)
				return i; //found
		}
		
		return -1;
	}
	
	String ExtractBoundary(String line)
	{
		int index = line.indexOf("boundary=");
		if (index == -1) 
		{
			return null;
		}
		String boundary = line.substring(index + 9);  // 9 for "boundary="

		// The real boundary is always preceded by an extra "--"
		boundary = "--" + boundary;

		return boundary;
	}
%>


<%
	System.out.println("Request received!");
	String sessionId = request.getParameter("sid");
	String docType = request.getParameter("docType");
	String docExpDate = request.getParameter("docExpDate");
	String docDesc = request.getParameter("docDesc");
	

	String contentType = request.getContentType();
		
	if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0))
	{
		DataInputStream in = new DataInputStream(request.getInputStream());
		int formDataLength = request.getContentLength();
		byte dataBytes[] = new byte[formDataLength];
		int byteRead = 0;
		int totalBytesRead = 0;
		
		while (totalBytesRead < formDataLength)
		{
			byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
			totalBytesRead += byteRead;
		}
		in.close();
		
		String strEOL = "\r\n";
		String strDoubleEOL = "\r\n\r\n";
		String strBoundary = "Content-Type: application/octet-stream";
		String strFileNameParam = "filename=\"";
		int posStreamBoundary = Find(dataBytes, 0, formDataLength, strBoundary);
		String saveFile = "";
		int posFileStreamBegin = 0;
		int fileSize = 0;
		if (-1 != posStreamBoundary)
		{
			posFileStreamBegin = posStreamBoundary + strBoundary.length() + strEOL.length() * 2; // double eol
			
			//Try to find filename
			int posFileNameL = ReverseFind(dataBytes, posStreamBoundary, 512, strFileNameParam);
			if (-1 == posFileNameL)
			{// unknow request stream
				return;
			}
			if (posFileNameL != -1)
			{
				posFileNameL += strFileNameParam.length(); //
				int posFileNameR = ReverseFind(dataBytes, posStreamBoundary, 8, "\"");
				saveFile = new String(dataBytes, posFileNameL, posFileNameR - posFileNameL);
			}
			
			fileSize = (formDataLength - posFileStreamBegin);
		}
		else
		{	//data post from mac os
			String strContentType = request.getContentType();
			strBoundary = ExtractBoundary(strContentType);
			System.out.println(strBoundary);
			posStreamBoundary = Find(dataBytes, 0, formDataLength, strBoundary);
			if (-1 == posStreamBoundary)
				return; // unknow request stream
			
			//Try to find filename
			int posFileNameR = -1;
			int posFileNameL = Find(dataBytes, posStreamBoundary, 512, strFileNameParam);
			if (-1 == posFileNameL)
			{// unknow request stream
				return;
			}
			if (posFileNameL != -1)
			{
				posFileNameL += strFileNameParam.length(); //
				posFileNameR = Find(dataBytes, posFileNameL + 1, 512, "\"");
				saveFile = new String(dataBytes, posFileNameL, posFileNameR - posFileNameL);
				System.out.println(saveFile);
				//location stream begin pos
				posFileStreamBegin = Find(dataBytes, posFileNameR, 512, strDoubleEOL);
				if (-1 == posFileStreamBegin)
				{// unknow request stream
					return;
				}
				posFileStreamBegin += strDoubleEOL.length();
				int posFileStreamEnd = ReverseFind(dataBytes, formDataLength - 1, 512, strBoundary);
				if (-1 == posFileStreamEnd)
				{// unknow request stream
					return;
				}
			
				fileSize = (posFileStreamEnd - posFileStreamBegin); 
			}
		}
		System.out.println(sessionId + " --- " + docType + " --- " + docDesc + " --- " + fileSize);
		/*
		*	Check file size
		*	5242880
		*/		
		if(fileSize > 1024000 ) {
			
			success = false;
		} else {
			
			success = true;
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			outStream.write(dataBytes, posFileStreamBegin, fileSize);
			outStream.flush();
			outStream.close();
			
			byte[] imageData = Arrays.copyOfRange(dataBytes, posFileStreamBegin, fileSize);
			//System.out.println("File Size: " + imageData.length);
			//Create a doc bean
			imageData = outStream.toByteArray();
			//System.out.println("File Size from stream: " + imageData.length);
			SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
			Date t = sdf.parse(docExpDate);
			ScannedIdentityBean doc = new ScannedIdentityBean();
			doc.setImageData(imageData);
			doc.setDocType(docType);
			doc.setDocExpirationDate(t);
			doc.setDocDesc(docDesc);
			doc.setThumbData(ImageResizer.resizeImage(imageData, 120));
			
			ScannedDocHolder holder = (ScannedDocHolder)application.getAttribute(sessionId);
			if(holder==null){
				holder = new ScannedDocHolder();			
			}
			holder.addDoc(doc);
			application.setAttribute(sessionId, holder);
		}
	}
%>
<%if(!success){%>
	FILE_TOO_LARGE
<%}%>