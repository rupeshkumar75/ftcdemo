package com.citizant.fraudshield.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.scheduling.annotation.EnableScheduling;

import com.citizant.fraudshield.service.impl.ActivityLogServiceImpl;
import com.citizant.fraudshield.service.impl.AwsEmailServiceImpl;
import com.citizant.fraudshield.service.impl.AwsEncrytionServiceImpl;
import com.citizant.fraudshield.service.impl.CustomerServiceImpl;
import com.citizant.fraudshield.service.impl.FaceCompareServiceImpl;
import com.citizant.fraudshield.service.impl.LocalEncryptionServiceImpl;
import com.citizant.fraudshield.service.impl.S3DocumentServiceImpl;
import com.citizant.fraudshield.service.impl.ReferenceServiceImpl;
import com.citizant.fraudshield.service.impl.RenewServiceImpl;
import com.citizant.fraudshield.service.impl.TsoUserServiceImpl;

@Configuration
@EnableScheduling
public class ServiceConfig {
	
	@Bean
	public TsoUserService tsoUserService() {
		return new TsoUserServiceImpl();
	}
	
	@Bean
	public CustomerService customerService(){
		return new CustomerServiceImpl();
	}
	
	@Bean
	public ReferenceService referenceService() {
		return new ReferenceServiceImpl();
	}
	
	@Bean
	public ActivityLogService activityLogService() {
		return new ActivityLogServiceImpl();
	}
	
	@Bean
	public RenewService RenewService() {
		return new RenewServiceImpl();
	}
	
	@Bean
	public DocumentService documentService() {
		return new S3DocumentServiceImpl();
	}
	
	@Bean
	public EncryptionService encryptionService() {
		return new LocalEncryptionServiceImpl ();
		//return new AwsEncrytionServiceImpl ();
	}
	
	@Bean
	public EmailService emailService() {
		return new AwsEmailServiceImpl ();
	}
	
	@Bean
	public FaceRegService faceService() {
		return new FaceCompareServiceImpl ();
	}
}
