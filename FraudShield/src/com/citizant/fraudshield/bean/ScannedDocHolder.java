/********************************************************************** 
 *
 *            Copyright (c) 2014 Citizant, Inc.
 * 
 **********************************************************************/
package com.citizant.fraudshield.bean;

import java.util.ArrayList;
import java.util.List;

public class ScannedDocHolder {
	private boolean newDocAdd = false; 
	private List<ScannedIdentityBean> docs = new ArrayList<ScannedIdentityBean>();
	
	public void addDoc(ScannedIdentityBean doc){
		int index = 0;
		if("Picture".equalsIgnoreCase(doc.getDocType().trim())) {
			for(ScannedIdentityBean dc : docs) {
				if("Picture".equalsIgnoreCase(dc.getDocType().trim())){
					docs.remove(index);
					break;
				}
				index++;
			}
		}
		docs.add(doc);
		newDocAdd = true;
	}
	
	public void removeDoc(int index){
		docs.remove(index);
	}
	
	public List<ScannedIdentityBean> getDocs() {
		return docs;
	}

	public void setDocs(List<ScannedIdentityBean> docs) {
		this.docs = docs;
	}
	
	public boolean checkNewDocs() {
		boolean res = newDocAdd;
		if(newDocAdd) {
			newDocAdd = false;
		}
		return res;
	}
	
}
