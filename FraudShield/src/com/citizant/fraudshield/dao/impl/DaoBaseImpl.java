package com.citizant.fraudshield.dao.impl;



import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.citizant.fraudshield.domain.DomainBase;

public abstract class DaoBaseImpl {
		private static final Logger log = Logger.getLogger(DaoBaseImpl.class);
	
	  
	     
	    public abstract void init();

	  
}
