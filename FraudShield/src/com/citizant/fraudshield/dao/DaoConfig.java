package com.citizant.fraudshield.dao;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.citizant.fraudshield.dao.impl.ActivityLogDaoImpl;
import com.citizant.fraudshield.dao.impl.CustomerServiceDaoImpl;
import com.citizant.fraudshield.dao.impl.CustomerDaoImpl;
import com.citizant.fraudshield.dao.impl.ReferenceDataDaoImpl;
import com.citizant.fraudshield.dao.impl.RenewDaoImpl;
import com.citizant.fraudshield.dao.impl.TsoUserDaoImpl;


@Configuration
public class DaoConfig {
	
	@Bean
	public TsoUserDao tsoUserDao() {
		return new TsoUserDaoImpl();
	}
	
	@Bean
	public CustomerServiceDao customerServiceDao() {
		return new CustomerServiceDaoImpl();
	}
	
	@Bean
	public CustomerDao customerDao(){
		return new CustomerDaoImpl();
	}
	
	@Bean
	public ActivityLogDao activityLogDao(){
		return new ActivityLogDaoImpl();
	}
	
	@Bean
	public ReferenceDataDao referenceDataDao(){
		return new ReferenceDataDaoImpl();
	}

	@Bean
	public RenewDao renewDao() {
		return new RenewDaoImpl();
	}
}
