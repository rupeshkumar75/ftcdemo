package com.citizant.fraudshield.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;
import com.citizant.fraudshield.common.AccountLock;
import com.citizant.fraudshield.common.ActivityType;
import com.citizant.fraudshield.common.AppConstants;
import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.domain.TsoUser;
import com.citizant.fraudshield.service.ActivityLogService;
import com.citizant.fraudshield.service.ReferenceService;
import com.citizant.fraudshield.service.TsoUserService;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

	@Autowired
	TsoUserService tsoUserService;

	@Autowired
	ActivityLogService activityLogService;

	@Autowired
	private ReferenceService referenceService;
	
	@Override
	protected void initServletContext(ServletContext servletContext) {
		super.initServletContext(servletContext);
		if (servletContext.getAttribute("nameSuffixes") == null) {
			servletContext.setAttribute("nameSuffixes",
					referenceService.getNameSuffix());
			servletContext.setAttribute("namePrefixes",
					referenceService.getNamePrefix());
			servletContext.setAttribute("states", referenceService.getStates());
			servletContext.setAttribute("identityDocTypes",
					referenceService.getIdentityDocTypes());

			// Load System config
			String configFile = servletContext
					.getRealPath("/WEB-INF/system-config.xml");
			SystemConfig.loadConfig(configFile);
		}
	}

	@RequestMapping("/start")
	public String loginPage(HttpServletRequest request) {
		return  getLoginRedirect();
	}

	@RequestMapping("/lost")
	public ModelAndView lostPassword(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tile.lostPasswordPage");
		return mav;
	}

	@RequestMapping("/checkAccount")
	public void checkUserAccount(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
	
		String email = request.getParameter("email");
		TsoUser tuser = tsoUserService.getTsoUserByEmail(email);

		JSONObject obj = new JSONObject();
		try {
			if(tuser != null) {				
				if(tsoUserService.createPasswordRestCode(tuser)) {			
					activityLogService.log("SYSTEM", ActivityType.USER_REQUEST_PASSWORD_RESET, "Request password reset: " + tuser.getUsername(), tuser.getId());	
					obj.put("status", "OK");
				} else {
					obj.put("status", "FAIL");
					obj.put("code", "MAIL_ERROR");
				}
			} else {
				obj.put("status", "FAIL");	
				obj.put("code", "USER_NOT_FOUND");
			}
		} catch (Exception e){
			
		}
	
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("/passwordReset")
	public ModelAndView resetPassword(HttpServletRequest request, @RequestParam String resetCode) {
		ModelAndView mav = new ModelAndView();
		TsoUser tuser = tsoUserService.getTsoUserByResetCode(resetCode);
		if(tuser == null) {
			mav.setViewName("tile.errorPage");
			mav.addObject("errorMsg", "Invalid request.");
			return mav;
		}
		
		//Check if the code is expired
		Date now = new Date();
		if(now.getTime() - tuser.getResetDate().getTime() > 3 * 24 * 60 * 60 * 1000) {
			mav.setViewName("tile.errorPage");
			mav.addObject("errorMsg", "This password reset request is expired");
			return mav;
		}
		mav.addObject("resetCode", resetCode);
		mav.setViewName("tile.resetPasswordPage");
		return mav;
	}
	
	@RequestMapping("/changePassword")
	public void changePassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
	
		String resetCode = request.getParameter("resetCode");
		String password = request.getParameter("pass1");
		
		TsoUser tuser = tsoUserService.getTsoUserByResetCode(resetCode);
		activityLogService.log("SYSTEM", ActivityType.USER_PASSWORD_RESET, "Password reset: " + tuser.getUsername(), tuser.getId());	

		JSONObject obj = new JSONObject();
		try {
			if(tuser != null) {			
				if(tsoUserService.setUserPassword(tuser, password)) {			
					obj.put("status", "OK");						
				} else {
					obj.put("status", "FAIL");
				
				}
			}
		} catch (Exception e){
			
		}
	
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}

	
	
	@RequestMapping("/error")
	public ModelAndView errorPage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("tile.errorPage");
		return mav;
	}

	@RequestMapping("/logout")
	public String doLogout(HttpServletRequest request) {	
		request.getSession().removeAttribute(AppConstants.LOGIN_USER);
		request.getSession().invalidate();		
		String url = "redirect:" + SystemConfig.LOAD_BALANCER_ADDRESS;	
		return url;	
	}

	@RequestMapping("/login")
	public ModelAndView doLogin(HttpServletRequest request,
			@RequestParam String username, @RequestParam String password) {
		ModelAndView mav = new ModelAndView();
	
		HttpSession session = request.getSession();
		AccountLock lock = (AccountLock) session
				.getAttribute(AppConstants.LOGIN_ATTEMPTS);
		if (lock == null) {
			lock = new AccountLock();
			session.setAttribute(AppConstants.LOGIN_ATTEMPTS, lock);
		}
		if (lock.isUnderAttack()) {
			String errorMsg = "Too many failed attempts, login was disabled, please try later.";
			String url = getLoginRedirect() + "?errorMsg=" + errorMsg;
			return new ModelAndView(url);
		}
		activityLogService.log(username, ActivityType.USER_LOGIN, "User login attempt. " + username, 0L);
		
		TsoUser user = tsoUserService
				.getTsoUserByIdPassword(username, password);
		if (user != null) {
			if (user.isActive()) {

				request.getSession()
						.setAttribute(AppConstants.LOGIN_USER, user);

				// log user login
				

				// Redirect user by roles
				if ("TSO".equalsIgnoreCase(user.getUserRole())) {
					mav.setViewName("tile.tsoHomePage");
				}

				if ("CCTSO".equalsIgnoreCase(user.getUserRole())) {
					mav.setViewName("tile.cctsoPanelPage");
				}

				if ("ADMIN".equalsIgnoreCase(user.getUserRole())) {
					mav.setViewName("tile.searchPage");
				}

				session.removeAttribute(AppConstants.LOGIN_ATTEMPTS);
				return mav;
			} else {
				String errorMsg = "TSO account was locked, please contact your administrator at support@fraudshield.com.";
				String url = getLoginRedirect() + "?errorMsg=" + errorMsg;
				return new ModelAndView(url);
			}

		} else {
			activityLogService.log(username, ActivityType.USER_LOGIN_FAILED, "User login failed.  " + username, 0L);
			String errorMsg = "Invalid username or password.";
			lock.addAttempt(username, request.getRemoteAddr(), new Date());
			if (lock.isUnderAttack()) {
				if (lock.getAttackUsername() != null) {
					tsoUserService.lockUserAccount(lock.getAttackUsername());
					errorMsg = "TSO account was locked, please contact your administrator at support@fraudshield.com.";
					activityLogService.log("SYSTEM", ActivityType.LOCK_TSO_ACCOUNT, "User locked because of too many failed login " + username, 0L);
				} else {
					errorMsg = "Too many failed attempts, login was disabled, please try later.";
				}
			}
			String url = getLoginRedirect() + "?errorMsg=" + errorMsg;
			return new ModelAndView(url);	
		}

	}
	
}
