package com.citizant.fraudshield.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import com.citizant.fraudshield.common.AppConstants;
import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.domain.TsoUser;


public class BaseController  extends AbstractController {
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
	
		return null;
	}
	
	public TsoUser getLoginUser(HttpServletRequest request){
		return (TsoUser)request.getSession().getAttribute(AppConstants.LOGIN_USER);
	}
	
	public boolean checkUserLogin(HttpServletRequest request){
		if(getLoginUser(request)!=null){
			return true;
		}
		return false;
	}
	
	public boolean isAdminUser(HttpServletRequest request) {
		TsoUser user = getLoginUser(request);
		if(user!=null) {
			if("ADMIN".equals(user.getUserRole())) {
				return true;
			}
		}		
		return false;
	}
	
	public boolean isTsoUser(HttpServletRequest request) {
		TsoUser user = getLoginUser(request);
		if(user!=null) {
			if("TSO".equals(user.getUserRole())) {
				return true;
			}
		}		
		return false;
	}
	
	public boolean isCcTsoUser(HttpServletRequest request) {
		TsoUser user = getLoginUser(request);
		if(user!=null) {
			if("CCTSO".equals(user.getUserRole())) {
				return true;
			}
		}		
		return false;
	}
	
	public String getLoginRedirect() {
		return "redirect:" + SystemConfig.LOAD_BALANCER_ADDRESS + "/login.jsp";
	}
}
