package com.citizant.fraudshield.exception;

public class DocumentServiceException extends ExceptionBase {

	private static final long serialVersionUID = 1L;

	public DocumentServiceException(Throwable t) {
		super(t);
	}
	
	public DocumentServiceException(String shortDesc, String longDesc) {
		super(shortDesc, longDesc);
	}
}
