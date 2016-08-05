package com.citizant.fraudshield.service;

import com.citizant.fraudshield.exception.DocumentServiceException;

public interface DocumentService {
	
	public boolean setupDocumentService() throws DocumentServiceException;
	
	public String saveDocument (byte[] contents) throws DocumentServiceException;
	
	public byte[] getDocument (String documentId) throws DocumentServiceException;

}
