package com.citizant.fraudshield.service.impl;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.citizant.fraudshield.common.AppConstants;
import com.citizant.fraudshield.dao.ReferenceDataDao;
import com.citizant.fraudshield.domain.ReferenceData;
import com.citizant.fraudshield.service.ReferenceService;

public class ReferenceServiceImpl implements ReferenceService {
	
	@Autowired
	ReferenceDataDao referenceDataDao;

	@Override
	public Map<String, String> getNameSuffix() {
		List<ReferenceData> list = referenceDataDao.getReferenceDataByCategory(AppConstants.NAME_SUFFIX);
		Map<String, String> referenceDataMap = new  LinkedHashMap<String, String>();
		referenceDataMap.put("", "");
		for (ReferenceData rec : list)
		{
			referenceDataMap.put(rec.getCode(), rec.getValue());
		}
		
		return referenceDataMap;
	}

	@Override
	public Map<String, String> getNamePrefix() {
		List<ReferenceData> list = referenceDataDao.getReferenceDataByCategory(AppConstants.NAME_PREFIX);
		Map<String, String> referenceDataMap = new LinkedHashMap<String, String>();
		referenceDataMap.put("", "");
		for (ReferenceData rec : list)
		{
			referenceDataMap.put(rec.getCode(), rec.getValue());
		}
		
		return referenceDataMap;
	}

	@Override
	public Map<String, String> getStates() {
		List<ReferenceData> list = referenceDataDao.getReferenceDataByCategory(AppConstants.STATE_CODE);
		Map<String, String> referenceDataMap = new LinkedHashMap<String, String>();
		referenceDataMap.put("", "");
		for (ReferenceData rec : list)
		{
			referenceDataMap.put(rec.getCode(), rec.getValue());
		}
		
		return referenceDataMap;
	}

	@Override
	public Map<String, String> getIdentityDocTypes() {
		List<ReferenceData> list = referenceDataDao.getReferenceDataByCategory(AppConstants.ID_TYPE);
		Map<String, String> referenceDataMap = new LinkedHashMap<String, String>();
		referenceDataMap.put("", "");
		for (ReferenceData rec : list)
		{
			referenceDataMap.put(rec.getCode(), rec.getValue());
		}
		
		return referenceDataMap;
	}

}
