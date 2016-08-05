package com.citizant.fraudshield.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.citizant.fraudshield.dao.ReferenceDataDao;
import com.citizant.fraudshield.domain.ReferenceData;

@Transactional
@Repository
public class ReferenceDataDaoImpl extends DaoBaseImpl implements
		ReferenceDataDao {

	@Override
	public List<ReferenceData> getAllReferenceData() {
		return new ArrayList<ReferenceData>();
	}

	@Override
	public void init() {


	}

	@Override
	public List<ReferenceData> getReferenceDataByCategory(String category) {
		return new ArrayList<ReferenceData>();
	}

}
