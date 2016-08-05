package com.citizant.fraudshield.common;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class SystemConfig {
	
	public static String AWS_S3_BUCKET = "";
	public static String FID_SERVICE_URL = "";
	public static String FACE_SERVICE_URL = "";
	public static String SERVER_ID = "";
	public static String DATA_KEY_ID = "";
	public static String KMS_END_POINT = "";
	public static String SES_SENDER_EMAIL = "";
	public static String RESET_EMAIL_SUBJECT = "";
	public static String RESET_EMAIL_CONTENT = "";
	public static String CONFIRM_EMAIL_SUBJECT = "";
	public static String CONFIRM_EMAIL_CONTENT = "";
	public static String CARD_INSTRUCTION = "";
	public static String LOAD_BALANCER_ADDRESS = "";
	public static String JASPER_TEMPLATE = "";
	
	public static void loadConfig (String fileName) {
		try{
			SAXReader reader = new SAXReader();
			File metaFile = new File(fileName);
			Document doc = reader.read(metaFile);
			Element root = doc.getRootElement();
			AWS_S3_BUCKET = root.elementText("asw-s3-bucket");
			FID_SERVICE_URL = root.elementText("id-service-url");	
			SERVER_ID = root.elementText("server-id");	
			DATA_KEY_ID = root.elementText("data-key-id");
			KMS_END_POINT =  root.elementText("kms-end-point");		
			SES_SENDER_EMAIL =  root.elementText("ses-sender-email");	
			FACE_SERVICE_URL = root.elementText("face-service-url");	
			RESET_EMAIL_SUBJECT = root.elementText("reset-email-subject");	
			RESET_EMAIL_CONTENT = root.elementText("reset-email-content");	
			CONFIRM_EMAIL_SUBJECT = root.elementText("confirm-email-subject");	
			CONFIRM_EMAIL_CONTENT = root.elementText("confirm-email-content");
			CARD_INSTRUCTION = root.elementText("card-instruction");
			LOAD_BALANCER_ADDRESS = root.elementText("load-balancer-address");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }		
	}
}
