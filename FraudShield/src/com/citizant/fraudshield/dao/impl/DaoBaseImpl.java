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
	
	  	@Autowired
	    private SessionFactory sessionFactory;

	    /**
	     * Accessor method use when wiring beans.
	     * 
	     * @param sessionFactory
	     */
	    @Autowired
	    protected void setSessionFactory(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }

	    /**
	     * Force all subclasses to create this method so that each DAO has a method
	     * that can be executed at startup.
	     */
	    public abstract void init();

	    /**
	     * This is the primary means of acquiring a database connection. All
	     * Hibernate API calls should use this method.
	     * 
	     * @return a database session.
	     * @throws HibernateException if it can't get a database session.
	     */
	    protected Session getCurrentSession() {
	    	Session se = null;
	        try {
	        	se = sessionFactory.getCurrentSession();
	        	
	            //return sessionFactory.getCurrentSession();
	        } catch (HibernateException he) {
	           // log.error("Unable to get current session!", he);
	            throw he;
	            //se = openSession();
	        }
	        return se;
	    }

	    /**
	     * This method of opening a database session should be avoided as it
	     * requires you to explicitly cloese the session. If you don't, then you'll
	     * open too many connections and hit either the pool size limit or the
	     * Oracle per user connection limit.
	     * 
	     * @return a database session that must be explicitly closed.
	     * @throws HibernateException if it can't get a database session.
	     */
	    protected Session openSession() {
	        try {
	            return sessionFactory.openSession();
	        } catch (HibernateException he) {
	            log.error("Unable to open new session!", he);
	            throw he;
	        }
	    }



	    /**
	     * This is the only way to update an domain object as it correctly sets the
	     * user that is perfoming the update.
	     * 
	     * @param entity the object to be updated.
	     * @param userName the user updating the database record.
	     * @return the updated object.
	     */
	    protected <T extends DomainBase> T update(T entity) {
	        this.getCurrentSession().update(entity);
	        return entity;
	    }

	    /**
	     * This is the method to be used when adding a record to the database.
	     * 
	     * @param entity a new object to be saved.
	     */
	    protected <T extends DomainBase> void add(T entity) {
	    
	        this.getCurrentSession().save(entity);
	    }

	    /**
	     * Save or update a domain object
	     * 
	     * @param entity new or exist domain object.
	     */
	    public <T extends DomainBase> void saveOrUpdate(T entity) {
	        this.getCurrentSession().saveOrUpdate(entity);
	    }

	    /**
	     * This is the method that should be used when performing a hard delete of a
	     * table row, i.e. domain object, as it performs the necessary steps
	     * required for auditing.
	     * 
	     * @param entity the domain object you want to delete.
	     * @param userName the user performing the action.
	     */
	    protected <T extends DomainBase> void delete(T entity) {
	        // set the delete flag	
	        // update the record so that it
	        // forces the trigger to be updated
	        this.update(entity);
	        // now delete the record
	        // template.delete(entity);
	        this.getCurrentSession().delete(entity);
	    }


	    /**
	     * Deletes a collection of domain objects.
	     * 
	     * @param entities collection of domain objects.
	     * @param userName the user performing the action.
	     */
	    protected void delete(Collection<? extends DomainBase> entities) {
	        if (entities == null) {
	            return;
	        }

	        for (DomainBase entity : entities) {
	            if (entity.getId() == null) {
	                continue;
	            }

	            this.delete(entity);
	        }
	    }

	    /**
	     * Convenience method that determines if a query returned any rows.
	     * 
	     * @param results an arbitrary list of objects.
	     * @return true if at lease one row was found.
	     */
	    protected boolean hasResults(List<? extends DomainBase> results) {
	        if (results == null || results.size() == 0 || results.get(0) == null) {
	            return false;
	        } else {
	            return true;
	        }
	    }

	    /**
	     * Convenience method that returns the first element (row) of a list of
	     * objects returned by a database query.
	     * 
	     * @param results a list of objects returned by hibernate.
	     * @return an object or null.
	     */
	    protected Object getFirstRow(List<? extends DomainBase> results) {
	        return hasResults(results) ? results.get(0) : null;
	    }

	    /**
	     * Force Hibernate to perform a save of all of it's data.
	     */
	    protected void sync() {
	    
	    }

	    /**
	     * Manually calling flush() followed by clear() make Hibernate flushing all 
	     * managed objects to the DB, and decouple these objects from the EntityManager.
	     * This should only use in batch processing.
	     */
	    public void flushAndClear() {
	    	this.getCurrentSession().flush();
	    	this.getCurrentSession().clear();
	        
	    }
}
