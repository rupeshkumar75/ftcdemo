package com.citizant.fraudshield.dao;

import java.util.List;

import com.citizant.fraudshield.domain.RenewRequest;

public interface RenewDao {
	
	public void saveRequest(RenewRequest request);
	
	public RenewRequest getRenewRequestByCode(String requestCode);
	
	public RenewRequest getRenewRequestByFid(String fid);
	
	public List<RenewRequest> getAllRequests();
}
