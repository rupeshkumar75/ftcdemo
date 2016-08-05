package com.citizant.fraudshield.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class DomainBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor provided here for completeness, but in practice
     * should not be used.
     */
    protected DomainBase() {
    }


    /**
     * Every Domain object must override this method
     * 
     * @return the integer primary key.
     */
    public abstract Long getId();


}