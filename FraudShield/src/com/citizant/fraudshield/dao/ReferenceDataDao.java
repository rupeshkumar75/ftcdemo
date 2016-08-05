package com.citizant.fraudshield.dao;

import java.util.List;

import com.citizant.fraudshield.domain.ReferenceData;

public interface ReferenceDataDao {
	public List<ReferenceData> getAllReferenceData();
	public List<ReferenceData> getReferenceDataByCategory(String category);
}
