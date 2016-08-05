package com.citizant.fraudshield.callcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class CallCenterManager {
	
	private static CallCenterManager manager;
	
	
	private List<Caller> callerQueue = null;
	
	private HashMap<String, Caller> inSessionCallers = null;
	
	
	private CallCenterManager() {
		callerQueue = new ArrayList<Caller>();
		inSessionCallers = new HashMap<String, Caller>();
	}
	
	public static CallCenterManager getCallCenterManager() {
		if(manager == null ) {
			manager = new CallCenterManager();
		}
		return manager;
	}
	
	public synchronized void addCaller(Caller caller) {
		if(!isCallerInQueue (caller.getRequestCode())) {
			callerQueue.add(caller);
			inSessionCallers.remove(caller.getRequestCode());
		}
	}
	
	public synchronized Caller pickupCaller() {
		Caller caller = null;
		if(callerQueue.size() > 0) {
			caller = callerQueue.get(0);
			inSessionCallers.put(caller.getRequestCode(), caller);
			String roomNumber = "" + (int)(Math.random() * 10000000.0);
			caller.setRoomNumber(roomNumber);
			callerQueue.remove(0);
		}		
		return caller;
	}

	public Caller callerPickup(String requestCode) {
		Caller caller = inSessionCallers.get(requestCode);
		inSessionCallers.remove(requestCode);
		return caller;
	}
	
	//Customer hangup the phone
	public  synchronized void customerHangup(String requestCode) {
		//Customer hang up, remove from caller list
		for(int i=0; i<callerQueue.size(); i++) {
			Caller c = callerQueue.get(i);
			if(c.getRequestCode().equals(requestCode)) {
				callerQueue.remove(i);
				break;
			}
		}
	}
	
	public void hangup( Caller caller) {
		inSessionCallers.remove(caller.getRequestCode());
	}
	
	public boolean isSessionStart(String requestCode) {
		if(inSessionCallers.get(requestCode) != null) {
			return true;
		}
		return false;
	}
	
	public List<Caller> getCallerList() {		
		return callerQueue;
	}
	
	//Return the order of caller
	public int getOrder(String requestCode) {
		int order = 1;
		for(int i=0; i<callerQueue.size(); i++) {
			Caller c = callerQueue.get(i);
			if(c.getRequestCode().equals(requestCode)) {
				order = i;
				break;
			}
		}
			
		return order;
	}
	
	
	public boolean isCallerInQueue(String requestCode) {
	
		Iterator<Caller> its = callerQueue.iterator();	
		while(its.hasNext()) {
			Caller caller = its.next();
			if(caller.getRequestCode().equals(requestCode)) {
				return true;
			}
		}
		return false;
	}
}
