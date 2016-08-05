package com.citizant.fraudshield.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reference_data")
public class ReferenceData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8510793299844203356L;
	
	@Id
	@GeneratedValue
	@Column(name="REFERENCE_DATA_ID")
	private Long refDataId;
	
	@Column(name="REFERENCE_CATEGORY")
	private String refCategory;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="VALUE")
	private String value;

	public Long getRefDataId() {
		return refDataId;
	}

	public void setRefDataId(Long refDataId) {
		this.refDataId = refDataId;
	}

	public String getRefCategory() {
		return refCategory;
	}

	public void setRefCategory(String refCategory) {
		this.refCategory = refCategory;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
