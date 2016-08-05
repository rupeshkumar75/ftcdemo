package com.citizant.fraudshield.service;

import java.awt.image.BufferedImage;
import java.util.List;

import com.citizant.fraudshield.bean.CustomerBean;
import com.citizant.fraudshield.bean.RenewRequestBean;
import com.citizant.fraudshield.bean.SearchBean;
import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.domain.RenewRequest;


public interface RenewService {
	public Person getRegistedPerson(SearchBean searchBean);
	
	public RenewRequest createRenewRequest(Person p);
	
	public RenewRequest getRenewRequestByCode(String requestCode);
	
	public RenewRequest getRenewRequestByFid(String fid);
	
	public void renewCustomer(String requestCode,  CustomerBean frmBean, String agent);
	
	public void declineRenew (String requestCode,  String coments, String agent);
	
	public boolean makePayment (String requestCode);
	
	public List<RenewRequestBean> getRequestList();
		
}
