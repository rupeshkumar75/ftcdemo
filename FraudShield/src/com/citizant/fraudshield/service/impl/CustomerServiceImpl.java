package com.citizant.fraudshield.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.List;

import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

import org.hibernate.criterion.Order;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import com.citizant.fraudshield.common.ActivityType;
import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.dao.CustomerServiceDao;
import com.citizant.fraudshield.domain.Person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;

import com.citizant.fraudshield.bean.CustomerBean;
import com.citizant.fraudshield.bean.ScannedIdentityBean;
import com.citizant.fraudshield.dao.CustomerDao;
import com.citizant.fraudshield.domain.PersonAddress;
import com.citizant.fraudshield.domain.PersonIdentification;
import com.citizant.fraudshield.exception.CustomerServiceException;
import com.citizant.fraudshield.exception.DocumentServiceException;
import com.citizant.fraudshield.service.ActivityLogService;
import com.citizant.fraudshield.service.CustomerService;
import com.citizant.fraudshield.service.DocumentService;
import com.citizant.fraudshield.service.EncryptionService;
import com.citizant.fraudshield.util.ImageResizer;
import com.citizant.fraudshield.util.ImageUtil;

public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerServiceDao customerServiceDao;
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired 
	DocumentService documentService;
	
	@Autowired
	EncryptionService encryptionService;
	
	@Autowired
	ActivityLogService activityLogService;

	public List<Person> searchPerson(
			String fName, 
			String lName, 
			String ssn, 
			String address,
			String docType) throws NoSuchPaddingException, GeneralSecurityException{
		String encSsn = null;
		if(ssn.length()>=10){
			encSsn = encryptionService.encryptString(ssn);
		}
		return customerServiceDao.searchPerson(fName, lName, encSsn, address, docType);
	}
	
	public Person retrieveCustomer (
			String fName, 
			String lName, 
			String ssn, 
			String dob) throws NoSuchPaddingException, GeneralSecurityException {
		
		return customerServiceDao.retrieveCustomer(fName, lName, 
					encryptionService.encryptString(ssn), 
					encryptionService.encryptString(dob));
	}
	
	
	@Override
	public CustomerBean getCustomerById(Long id) {
		Person p = customerDao.getCustomerById(id);
		return convertPersonToCustomerBean(p, true);
	}

	@Override
	/*
	 * To save the entire record in a single transaction, 
	 * Save files to S3 first, if succeed, Create record. 
	 * 
	 */
	public Boolean saveCustomer(CustomerBean bean) throws CustomerServiceException{
		
		//Push files to S3
		if(bean.getIdDocs()!=null){
			try {
				documentService.setupDocumentService();
			} catch (DocumentServiceException de) {
				throw new CustomerServiceException ( de.getShortDesc(), de.getLongDesc() );
			}
			for(ScannedIdentityBean doc : bean.getIdDocs()){			
				
				try {
					String fileId = documentService.saveDocument(doc.getImageData());
					doc.setDocId(fileId);
				} catch (DocumentServiceException de) {
					throw new CustomerServiceException ( de.getShortDesc(), de.getLongDesc() );
				}				
			}
		}
		
		Person p = null;	
		String originalSSN = "";
		if(bean.getPersonId()!=null && bean.getPersonId()>0){
			p = customerDao.getCustomerById(bean.getPersonId());
		}
		
		if(p==null){
			p = new Person();	
		}else{
			try{
				originalSSN = encryptionService.decryptString(p.getSsn());
			}catch(Exception e){
				
			}
		}
					
		try{
			BeanUtils.copyProperties(bean, p);			
			p.setSsn(encryptionService.encryptString(bean.getSsn()));
			p.setDob(encryptionService.encryptString(bean.getDob()));
			//New customer or customer's SSN changed, generate a new ID
			if(p.getFraudShieldID()==null 
					|| p.getFraudShieldID().length()==0 
					|| !originalSSN.equals(bean.getSsn())){
				p.setFraudShieldID(createFraudShieldID(bean.getSsn()));
				p.setCreatedDate(
						new Timestamp(Calendar.getInstance().getTimeInMillis()));
			}
			p.setActive(true);
		}catch(Exception e){
			
		}
		PersonAddress add = new PersonAddress();
		if(bean.getAddressId()!=null && bean.getAddressId()!=0){
			add.setPersonAddressId(bean.getAddressId());
		}
		add.setAddress1(bean.getAddress());
		add.setAddress2(bean.getAddress2());
		add.setCity(bean.getCity());
		add.setState(bean.getState());
		add.setZip(bean.getZip());
		add.setZipPlusFour(bean.getZip4());
		add.setActive(true);
		add.setPerson(p);

		p.setPersonAddress(add);
		
		if(customerDao.saveCustomer(p)){		
			if(bean.getIdDocs()!=null){
				for(ScannedIdentityBean doc : bean.getIdDocs()){
					PersonIdentification pid = new PersonIdentification();			
					if(doc.getId()==null || doc.getId()==0){
						pid.setIdDescription(doc.getDocDesc());
						pid.setIdentificationType(doc.getDocType());
						pid.setIdentificationExpDate(doc.getDocExpirationDate());
						pid.setIdentification(doc.getDocId());
						pid.setPersonId(p.getId());
						pid.setActive(true);
						customerDao.saveDocument(pid);
						doc.setId(pid.getId());
					}
				}				
			}	
		}
		
		bean.setPersonId(p.getPersonId());
		bean.setFraudShieldID(p.getFraudShieldID());
		bean.setAddressId(p.getPersonAddress().getPersonAddressId());
		return Boolean.TRUE;
	}

	public String createFraudShieldID(String ssn){
		//String s4 = ssn.substring(ssn.length()-4);
		//String fsId = s4 + RandomUniqueNumberGenarator.getKey();
		/*
		String fsId = RandomUniqueNumberGenarator.getKey();
		while(!customerDao.isFraudShieldIDUnique(fsId))
		{
			fsId = RandomUniqueNumberGenarator.getKey();
		}*/
		String fsId = null;
		try {
			JSONParser parser=new JSONParser();
			String res = getServerResponse(SystemConfig.FID_SERVICE_URL);
			Object obj = parser.parse(res);
			JSONObject jsonObject = (JSONObject)obj;
			fsId = (String)jsonObject.get("fid");
			System.out.println("New Fraud Shield ID : " + fsId);
		}catch(Exception e) {
			
		}
		
		return fsId;
	}
	
	public void deleteDocument(Long docId){
		customerDao.deleteDocument(docId);
	}
	
	@Override
	public List<CustomerBean> getAllCustomers(List<Order> sort) {
		
		List<CustomerBean> results = new ArrayList<CustomerBean>();
		List<Person> customers = customerDao.getAllCustomers(sort);
		for (Person customer : customers){
			results.add(convertPersonToCustomerBean(customer, false));
		}
		return results;
	}
	
	private CustomerBean convertPersonToCustomerBean(Person p, boolean loadDocs)
	{
		CustomerBean bean = new CustomerBean();
		
		try{
			BeanUtils.copyProperties(p, bean);	
			bean.setPersonId(p.getId());
			bean.setSsn(encryptionService.decryptString(p.getSsn()));
			bean.setDob(encryptionService.decryptString(p.getDob()));
		}catch(Exception e){
			e.printStackTrace();
		}
		PersonAddress add = p.getPersonAddress();
		
		if(add!=null){
			bean.setAddressId(add.getPersonAddressId());
			bean.setAddress(add.getAddress1());
			bean.setAddress2(add.getAddress2());
			bean.setCity(add.getCity());
			bean.setState(add.getState());
			bean.setZip(add.getZip());
			bean.setZip4(add.getZipPlusFour());
		}
		
		if (loadDocs)
		{
			List<PersonIdentification> pids = customerDao.getPersonIds(p.getId());
			
			if(pids!=null){
				List<ScannedIdentityBean> idDocs = new ArrayList<ScannedIdentityBean>();
				for(PersonIdentification pid : pids){
					ScannedIdentityBean sb = new ScannedIdentityBean();
					sb.setDocType(pid.getIdentificationType());
					sb.setId(pid.getPersonIdentificationId());
					sb.setDocExpirationDate(pid.getIdentificationExpDate());
				
					try {
						byte[] data = documentService.getDocument(pid.getIdentification());
						sb.setImageData(data);
						sb.setThumbData(ImageResizer.resizeImage(data, 120));
					} catch ( DocumentServiceException e) {
						sb.setImageData(encryptionService.decryptBinary(pid.getIdentification()));
						sb.setThumbData(ImageResizer.resizeImage(sb.getImageData(), 120));
					}					
					idDocs.add(sb);
				}
						
				bean.setIdDocs(idDocs);
			}
		}
		return bean;
	}
	
	
	public void setCustomerRecordInactive(Long id) {
		Person p = customerDao.getCustomerById(id);
		p.setActive(false);
		customerDao.saveCustomer(p);
	}
	
	public Person isSSNExits(String ssn) {
		try{
			List<Person> persons = customerDao.getCustomerBySsn(ssn);
			if(persons!=null && persons.size()>0) {
				return (Person)persons.get(0);
			}
		}catch(Exception e){
			
		}
		
		return null;
	}
	
	@Override
	public String getCustomerPictureById(Long personId) {
		CustomerBean customerBean = getCustomerById(personId);
		List<ScannedIdentityBean> docs = customerBean.getIdDocs();
		BufferedImage refImg = null;	
		String pic = null;
		for(ScannedIdentityBean doc : docs) {
			if("Picture".equalsIgnoreCase(doc.getDocType())) {
				try {
					refImg = ImageIO.read(new ByteArrayInputStream(doc.getImageData()));
					pic = ImageUtil.encodeImage (refImg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				break;
			}
		}
		return pic;
	}
	
	
	@Scheduled(cron="0 15 0 ? * *")
	public void checkForExpiredRegistration() {
	    // Check for expired FraudShield registration, if a registration is
		// more than 365 days after creation, it will marked as inactive
		HashMap<String, String> dups = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1); // to get previous year add -1
		Date endDay = cal.getTime();
		List<Person> expiredCustomers = customerDao.getExpiredActiveCustomers(endDay);
		for(Person p : expiredCustomers) {
			p.setActive(false);
			customerDao.saveCustomer(p);
			dups.put("" + p.getPersonId(), "");
			activityLogService.log("SYSTEM", ActivityType.EXPIRE_REGISTRATION, "Expired Registration", p.getId());
		}
		
		//Check for expired documents
		List<PersonIdentification> docs =  customerDao.getExpiredDocuments(endDay);
		
		
		for(PersonIdentification doc : docs) {
			if(dups.get("" + doc.getPersonId()) == null) {
				Person p = customerDao.getCustomerById(doc.getPersonId());
				p.setActive(false);
				customerDao.saveCustomer(p);
				dups.put("" + doc.getPersonId(), "");
				activityLogService.log("SYSTEM", ActivityType.EXPIRE_REGISTRATION, "Expired Document", p.getId());
			}
		}
		
		System.out.println("Scheduler running ..." + endDay.toGMTString());
	}
	
	
	// Get server response
	private String getServerResponse(String serverurl) throws Exception {
		URL url;
		// get URL content
		url = new URL(serverurl);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(5000);
		// open the stream and put it into BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer sbuff = new StringBuffer();
		String inputLine;
		while ((inputLine = br.readLine()) != null) {
			sbuff.append(inputLine);
		}
		br.close();
		return sbuff.toString();
	}
	
	public Person getPersonById(Long id) {
		return customerDao.getCustomerById(id);
	}
	
	
	public List<Person> getAllRegistrations(Date start, Date end) {
		return customerDao.getRegistrationsInTimeFrame( start,  end);
	}
}
