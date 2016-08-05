/********************************************************************** 
 *
 *            Copyright (c) 2014 Citizant, Inc.
 * 
 **********************************************************************/
package com.citizant.fraudshield.bean;

import java.sql.Timestamp;
import java.util.Date;

public class ScannedIdentityBean {
	private Long id = 0L;
	private String docType;
	private Date docExpirationDate;
	private String imageFormat = "jpeg";
	private byte[] imageData;
	private byte[] thumbData;
	private String docDesc;
	private String docId;
	
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getImageFormat() {
		return imageFormat;
	}
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public byte[] getThumbData() {
		return thumbData;
	}
	public void setThumbData(byte[] thumbData) {
		this.thumbData = thumbData;
	}
	public String getDocDesc() {
		return docDesc;
	}
	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}
	public Date getDocExpirationDate() {
		return docExpirationDate;
	}
	public void setDocExpirationDate(Date docExpirationDate) {
		this.docExpirationDate = docExpirationDate;
	}
	public String getDocExpirationDateAsString() {
		if (docExpirationDate != null)
		{
			return String.format("%1$tm/%1$td/%1$tY", docExpirationDate);
		}
		else
		{
			return "N/A";
		}
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	
}
