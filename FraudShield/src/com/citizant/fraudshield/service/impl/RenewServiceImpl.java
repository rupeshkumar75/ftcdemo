package com.citizant.fraudshield.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;


import com.citizant.fraudshield.bean.CustomerBean;
import com.citizant.fraudshield.bean.RenewRequestBean;
import com.citizant.fraudshield.bean.ScannedIdentityBean;
import com.citizant.fraudshield.bean.SearchBean;
import com.citizant.fraudshield.dao.CustomerServiceDao;
import com.citizant.fraudshield.dao.RenewDao;
import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.domain.RenewRequest;
import com.citizant.fraudshield.exception.CustomerServiceException;
import com.citizant.fraudshield.security.RandomUniqueNumberGenarator;
import com.citizant.fraudshield.service.CustomerService;
import com.citizant.fraudshield.service.EncryptionService;
import com.citizant.fraudshield.service.RenewService;


public class RenewServiceImpl implements RenewService {
	
	@Autowired
	CustomerServiceDao customerServiceDao;
	
	@Autowired
	RenewDao renewDao;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	EncryptionService encryptionService;
	
	@Override
	public Person getRegistedPerson(SearchBean searchBean) {
		try {
			String encryptedSsn =  encryptionService.encryptString(searchBean.getSsn());
			Person p = customerServiceDao.retrieveCustomer(searchBean.getFirstName(), 
						searchBean.getLastName(), encryptedSsn, null);
			if(p != null) {
				if(searchBean.getFraudShiledId().equals(p.getFraudShieldID())) {
					return p;
				}
			}
			
		} catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public RenewRequest createRenewRequest(Person p) {
		RenewRequest request = new RenewRequest();
		request.setOrigFid(p.getFraudShieldID());
		request.setRequestCode(RandomUniqueNumberGenarator.generateRequestCode());
		request.setStatus("NEW");
		request.setCustomerId(p.getPersonId());
		request.setRequestDate(new Date());
		renewDao.saveRequest(request);
		return request;
	}
	
	@Override
	public RenewRequest getRenewRequestByCode(String requestCode){
		return renewDao.getRenewRequestByCode(requestCode);
	}
	
	@Override
	public RenewRequest getRenewRequestByFid(String fid) {
		return renewDao.getRenewRequestByFid(fid);
	}
	
	/*
	 * This is the logic to create a new customer registration
	 */
	
	@Override
	public void renewCustomer(String requestCode,  CustomerBean frmBean, String agent) {
		
		//Get the original scanned docs
		CustomerBean oldReg = customerService.getCustomerById(frmBean.getPersonId());
		
		List<ScannedIdentityBean> docs = oldReg.getIdDocs();
		for(ScannedIdentityBean docBean : docs) {
			docBean.setId(null);
		}
		//Create a new Registration from old one. Copy over all documents
		//Issue a new FraudShield ID
		frmBean.setPersonId(null);
		frmBean.setFraudShieldID(customerService.createFraudShieldID(frmBean.getSsn()));
		frmBean.setIdDocs(docs);
		try {
			customerService.saveCustomer(frmBean);
		} catch (CustomerServiceException e) {
			
		}
		
		//Mark old registration inactive
		customerService.setCustomerRecordInactive(oldReg.getPersonId());
				
		RenewRequest request = getRenewRequestByCode(requestCode);
		request.setNewFid(frmBean.getFraudShieldID());
		request.setInterviewDate(new Date());
		request.setAgent(agent);
		request.setStatus("PENDING PAYMENT");
		renewDao.saveRequest(request);
	}
	
	@Override
	public void declineRenew (String requestCode,  String comments, String agent) {
		RenewRequest request = getRenewRequestByCode(requestCode);
		request.setInterviewDate(new Date());
		request.setStatus("DECLINED");
		request.setAgent(agent);
		request.setComments(comments);
		renewDao.saveRequest(request);
	}
	
	@Override
	public List<RenewRequestBean> getRequestList() {
		List<RenewRequest> reqs = renewDao.getAllRequests();
		
		List<RenewRequestBean> beans = new ArrayList<RenewRequestBean>();
		
		for(RenewRequest req : reqs) {
			RenewRequestBean bean = new RenewRequestBean();
			try {
				BeanUtils.copyProperties(req, bean);
			}catch(Exception e){
				
			}
			CustomerBean p = customerService.getCustomerById(req.getCustomerId());
			bean.setFirstName(p.getFirstName());
			bean.setLastName(p.getLastName());
			beans.add(bean);
		}
		
		return beans;
	}
	
	@Override
	public boolean makePayment (String requestCode) {
		//Place holder, just mark request as paid
		RenewRequest request = getRenewRequestByCode(requestCode);
		request.setStatus("COMPLETED");
		renewDao.saveRequest(request);
		return true;
	}
	
	
	private String getEncodedImage(BufferedImage image) {
		try {
			//ImageIO.write(image, "png", new File("c:/temp/face9100220.png"));
			BASE64Encoder ecoder = new BASE64Encoder();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( image, "png", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			return ecoder.encode(imageInByte);		
		}catch(Exception e){
			return "";
		}
	}
	
}
