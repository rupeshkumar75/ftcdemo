package com.citizant.fraudshield.controller;


import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.citizant.fraudshield.common.ActivityType;
import com.citizant.fraudshield.domain.TsoUser;
import com.citizant.fraudshield.service.ActivityLogService;
import com.citizant.fraudshield.service.CustomerService;
import com.citizant.fraudshield.service.TsoUserService;


@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
	
	@Autowired
	TsoUserService tsoUserService;
	 
	@Autowired
	private CustomerService customerService;
	 
	@Autowired
	ActivityLogService activityLogService;
	
	@RequestMapping("/start")
	public ModelAndView searchPage(HttpServletRequest request) {
		if(!isAdminUser(request)) {		
			return new ModelAndView(getLoginRedirect());
		}
		ModelAndView mav = new ModelAndView("tile.adminPage");
		if (!checkUserLogin(request)) {
			return new ModelAndView(getLoginRedirect());			
		}
		if(!"ADMIN".equals(this.getLoginUser(request).getUserRole())){
			mav.setViewName("tile.accessDeniedPage");
		}
		return mav;
	}
	
	@RequestMapping("/dosearch")
	public String doSearch(HttpServletRequest request) {
		if (!checkUserLogin(request)) {
			return "content/ajaxSessionExpired";
		}
		if(!"ADMIN".equals(this.getLoginUser(request).getUserRole())){
			return "content/ajaxAccessDenied";
		}
		//Perform search
		String username = request.getParameter("username");
		
		TsoUser user = tsoUserService.getTsoUserByUsername(username);
		request.setAttribute("tsoUser", user);

		return "content/ajaxTsoUserDetail";
	}
	
	
	@RequestMapping("/doChangeAccount")
	public String doChangeAccount(HttpServletRequest request) {
		if (!checkUserLogin(request)) {
			return "content/ajaxSessionExpired";
		}
		if(!"ADMIN".equals(this.getLoginUser(request).getUserRole())){
			return "content/ajaxAccessDenied";
		}
		//Perform search
		String username = request.getParameter("userId");
		String lock = request.getParameter("lock");
		String loginUser = this.getLoginUser(request).getUsername();
		
		if("true".equalsIgnoreCase(lock)) {
			tsoUserService.lockUserAccount(username);
			activityLogService.log(loginUser, ActivityType.LOCK_TSO_ACCOUNT, "Lock user " + username, 0L);	
		} else {
			tsoUserService.unLockUserAccount(username);
			activityLogService.log(loginUser, ActivityType.UNLOCK_TSO_ACCOUNT, "Unlock user " + username, 0L);	
		}
	
		
		TsoUser user = tsoUserService.getTsoUserByUsername(username);
		request.setAttribute("tsoUser", user);

		return "content/ajaxTsoUserDetail";
	}
	
	
	
	@RequestMapping("/adduser")
	public String doAddUser(HttpServletRequest request) {
		if (!checkUserLogin(request)) {
			return "content/ajaxSessionExpired";
		}
		
		String loginUser = this.getLoginUser(request).getUsername();
		
		String username = request.getParameter("username1");
		String password = request.getParameter("password1");
		String firstName = request.getParameter("firstName");		
		String lastName = request.getParameter("lastName");
		String contactEmail = request.getParameter("email");
		String contactPhone = request.getParameter("phone");
		String role = request.getParameter("role");
		TsoUser user = tsoUserService.createTsoUser(firstName, lastName, contactPhone, 
				contactEmail, username, password, role);
		activityLogService.log(loginUser, ActivityType.CREATE_TSO_ACCOUNT, "New User created", user.getId());
		return "content/ajaxSuccess";
	}
}
