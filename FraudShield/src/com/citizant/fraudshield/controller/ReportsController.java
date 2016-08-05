package com.citizant.fraudshield.controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.citizant.fraudshield.bean.CustomerBean;
import com.citizant.fraudshield.common.ActivityType;
import com.citizant.fraudshield.service.ActivityLogService;
import com.citizant.fraudshield.service.CustomerService;
import com.citizant.fraudshield.service.TsoUserService;
import com.citizant.fraudshield.util.StringUtil;


@Controller
@RequestMapping("/report")
public class ReportsController extends BaseController {
	
	@Autowired
	private CustomerService customerService;
	 
	@Autowired
	ActivityLogService activityLogService;
	
	@Autowired
	TsoUserService tsoUserService;
	
	@RequestMapping("/start")
	public ModelAndView lostPassword(HttpServletRequest request) {
		if(!isAdminUser(request)) {		
			return new ModelAndView(getLoginRedirect());
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tile.reportHomePage");
		return mav;
	}
	
	@RequestMapping("/generateStatusReport")
	public void getTimeFrame(HttpServletRequest request,
			@RequestParam Date fromDate,
			@RequestParam Date toDate,
			HttpServletResponse response) throws Exception {
		String loginUser = this.getLoginUser(request).getUsername();
		activityLogService.log(loginUser, ActivityType.REPORT_STATUS, "Generating status Report", 0L);
		JSONObject obj = new JSONObject();
		toDate.setHours(24);
		int numOfReg = activityLogService.getNumOfActivities(ActivityType.CREATE_REGISTRATION, fromDate, toDate);
		int numOfExp = activityLogService.getNumOfActivities(ActivityType.EXPIRE_REGISTRATION, fromDate, toDate);
		numOfExp = numOfExp + activityLogService.getNumOfActivities(ActivityType.INVALID_REGISTRATION, fromDate, toDate);
		int numOfTso = tsoUserService.getNumOfUsers();
		int numOfLogin = activityLogService.getNumOfActivities(ActivityType.USER_LOGIN, fromDate, toDate);
		int numOfFail = activityLogService.getNumOfActivities(ActivityType.USER_LOGIN_FAILED, fromDate, toDate);
		int numOfSuccess = numOfLogin - numOfFail;
		
		int numOfPrint = activityLogService.getNumOfActivities(ActivityType.PRINT_ID_CARD, fromDate, toDate);
		int numOfLock = activityLogService.getNumOfActivities(ActivityType.LOCK_TSO_ACCOUNT, fromDate, toDate);
		
		obj.put("numOfReg", numOfReg);
		obj.put("numOfExp", numOfExp);
		obj.put("numOfRen", "0");
		obj.put("numOfTso", numOfTso);
		obj.put("numOfLogin", numOfLogin);
		obj.put("numOfSuccess", numOfSuccess);
		obj.put("numOfFail", numOfFail);
		obj.put("numOfPrint", numOfPrint);
		obj.put("numOfLock", numOfLock);
		
		
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	
	@RequestMapping("/getTimeframe")
	public void getTimeFrame(HttpServletRequest request,
			@RequestParam Integer frameId,
			HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		
		String fromDate = "";
		String toDate = "";
	
		if(frameId.intValue() == 0) {
			fromDate = "";
			toDate = "";
		} else {
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();			
			switch(frameId.intValue()) {
				
				case 1:
					cal.add(Calendar.DAY_OF_YEAR, -1); 
					break;
				case 2:
					cal.add(Calendar.DAY_OF_YEAR, -3); 
					break;
				case 3:
					cal.add(Calendar.DAY_OF_YEAR, -7); 
					break;
				case 4:
					cal.add(Calendar.DAY_OF_YEAR, -30); 
					break;			
				case 5:
					cal.add(Calendar.DAY_OF_YEAR, -60); 
					break;
				case 6:
					cal.add(Calendar.MONTH, -6); 
					break;	
				case 7:
					cal.add(Calendar.DAY_OF_YEAR, -1); 
					break;			
			}			
			toDate = StringUtil.getStandardDate(today);
			fromDate = StringUtil.getStandardDate(cal.getTime());;
		}
		obj.put("fromDate", fromDate);
		obj.put("toDate", toDate);
		
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	@RequestMapping("/generateCustomersReport")
	public ModelAndView generateCustomersReport(HttpServletRequest request) {
		if(!isAdminUser(request)) {		
			return new ModelAndView(getLoginRedirect());
		}
		ModelAndView mav = new ModelAndView("tile.customersReport");
		if (!checkUserLogin(request)) {
			return new ModelAndView(getLoginRedirect());
		}
		
		String loginUser = this.getLoginUser(request).getUsername();
		activityLogService.log(loginUser, ActivityType.REPORT_CUSTOMER, "Generating Customers Report", 0L);
		List<CustomerBean> customers = customerService.getAllCustomers(null);
		mav.addObject("customersList", customers);
		return mav;
	}
	
	@RequestMapping("/sortCustomersReport")
	public ModelAndView sortCustomersReport(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("tile.customersReport");
		if (!checkUserLogin(request)) {
			return new ModelAndView(getLoginRedirect());
		}
		
		String sortCol = request.getParameter("sort");
		String prevSortCol = request.getParameter("prevSort");
		String prevSortOrder = request.getParameter("prevSortOrder");
		String sortOrder = "asc";
		if (prevSortCol.equals(sortCol))
		{
			if (prevSortOrder.equals("asc"))
			{
				sortOrder = "desc";
			}
		}
		
		List<Order> sort = new ArrayList<>();
			
		if (sortCol.equals("Name")) {
			if (sortOrder.equals("asc"))
			{
				sort.add(Order.asc("firstName"));
				sort.add(Order.asc("middleInitial"));
				sort.add(Order.asc("lastName"));
			}
			else
			{
				sort.add(Order.desc("firstName"));
				sort.add(Order.desc("middleInitial"));
				sort.add(Order.desc("lastName"));
			}
		}
		else if (sortCol.equals("Zip")) {
			if (sortOrder.equals("asc"))
			{
				sort.add(Order.asc("personAddress.zip"));
				sort.add(Order.asc("personAddress.zipPlusFour"));
			}
			else
			{
				sort.add(Order.desc("personAddress.zip"));
				sort.add(Order.desc("personAddress.zipPlusFour"));
			}
		}
		else {
			if (sortOrder.equals("asc"))
			{
				sort.add(Order.asc(sortCol));
			}
			else
			{
				sort.add(Order.desc(sortCol));
			}
		}
		
		List<CustomerBean> customers = customerService.getAllCustomers(sort);
		mav.addObject("customersList", customers);
		mav.addObject("sortCol", sortCol);
		mav.addObject("sortOrder", sortOrder);
		return mav;
	}
}
