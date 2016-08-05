package com.citizant.fraudshield.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="person_identification")
public class PersonIdentification extends DomainBase implements Serializable {

	private static final long serialVersionUID = 7810652881998932003L;

	@Id
	@GeneratedValue
	@Column(name="PERSON_IDENTIFICATION_ID")
	private Long personIdentificationId;
	

    @Column(name="PERSON_ID")
	private Long personId;
	
	@Column(name="IDENTIFICATION")
	private String identification;
	
	@Column(name="IDENTIFICATION_TYPE")
	private String identificationType;
	
	@Column(name="IDENTIFICATION_EXP_DATE")
	private Date identificationExpDate;
	
	@Column(name="ID_DESC")
	private String idDescription;

	@Column(name="ACTIVE_IND")
	private boolean active = true;

	public Long getPersonIdentificationId() {
		return personIdentificationId;
	}

	public void setPersonIdentificationId(Long personIdentificationId) {
		this.personIdentificationId = personIdentificationId;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}
	
	public String getIdDescription() {
		return idDescription;
	}

	public void setIdDescription(String idDescription) {
		this.idDescription = idDescription;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	
	public Date getIdentificationExpDate() {
		return identificationExpDate;
	}

	public void setIdentificationExpDate(Date identificationExpDate) {
		this.identificationExpDate = identificationExpDate;
	}

	@Override
	public Long getId() {
	
		return this.personIdentificationId;
	}
}
