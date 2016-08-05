package com.citizant.fraudshield.service;


import java.util.Map;

public interface ReferenceService {
	 
	
	public Map<String, String> getNameSuffix();

    public Map<String, String> getNamePrefix();

    public Map<String, String> getStates();
	
    public Map<String, String> getIdentityDocTypes();
}
