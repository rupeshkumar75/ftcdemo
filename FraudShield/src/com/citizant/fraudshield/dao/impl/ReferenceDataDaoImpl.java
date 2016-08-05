package com.citizant.fraudshield.dao.impl;

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
		Criteria criteria = super.getCurrentSession().createCriteria(ReferenceData.class);
		return criteria.list();
	}

	@Override
	public void init() {


	}

	@Override
	public List<ReferenceData> getReferenceDataByCategory(String category) {
		Criteria criteria = super.getCurrentSession().createCriteria(ReferenceData.class);
		criteria.add(Restrictions.eq("refCategory", category));
		return criteria.list();
	}

}
