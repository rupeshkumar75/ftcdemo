package com.citizant.fraudshield.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.citizant.fraudshield.bean.SearchBean;
import com.citizant.fraudshield.common.ActivityType;
import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.service.ActivityLogService;
import com.citizant.fraudshield.service.CustomerService;



@Controller
@RequestMapping("/search")
public class CustomerSearchController extends BaseController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ActivityLogService activityLogService;
	
	@RequestMapping("/start")
	public ModelAndView searchPage(HttpServletRequest request) {
		
		if(!isAdminUser(request)) {		
			return new ModelAndView(getLoginRedirect());
		}
		
		SearchBean bean = (SearchBean)request.getSession().getAttribute("searchBean");
		if(bean==null){
			 bean =  new SearchBean();
			 request.getSession().setAttribute("searchBean", bean);
		}
		bean.resetCriteria();
		request.getSession().removeAttribute("results");
		ModelAndView mav = new ModelAndView();
		if (!checkUserLogin(request) || !isAdminUser(request)) {
			return new ModelAndView(getLoginRedirect());
		}
		mav.setViewName("tile.searchPage");
		return mav;
	 }
	
	@RequestMapping("/dosearch")
	public String doSearch(HttpServletRequest request) {
		if (!checkUserLogin(request) || !isAdminUser(request)) {
			return "content/ajaxSessionExpired";
		}
		//Perform search
		String fName = request.getParameter("firstName");
		String lName = request.getParameter("lastName");
		String ssn = request.getParameter("ssn");
		String address = request.getParameter("address");
		String docType = request.getParameter("idDocType");
		
		SearchBean bean = new SearchBean();
		
		bean.setFirstName(fName);
		bean.setLastName(lName);
		bean.setSsn(ssn);
		bean.setAddress(address);
		bean.setIdDocType(docType);
		request.getSession().setAttribute("searchBean", bean);
		try
		{
			List<Person> results = customerService.searchPerson(fName, lName, ssn, address, docType);
			request.getSession().setAttribute("results", results);
		}
		catch (Exception e)
		{
			request.setAttribute("errorMessage", "Error retrieving Customers.  Error: " + e.getMessage());
		}
		
		return "content/ajaxCustomerList";
	}
	
	@RequestMapping("/retrieveCustomer")
	public String doSearchCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!checkUserLogin(request) || !isTsoUser(request)) {
			return "content/ajaxSessionExpired";
		}
		//Perform search
		String fName = request.getParameter("firstName");
		String lName = request.getParameter("lastName");
		String ssn = request.getParameter("ssn");
		String dob = request.getParameter("dob");
		
		Person customer = null;
		String customerPic = null;
		try
		{
			customer = customerService.retrieveCustomer(fName, lName, ssn, dob);
			if(customer != null ) {
				String loginUser = this.getLoginUser(request).getUsername();
				activityLogService.log(loginUser, ActivityType.SEARCH_EXISTING_CUSTOMER, "Search existing customer", customer.getPersonId());				
				customerPic = customerService.getCustomerPictureById(customer.getId());		
			}
		}
		catch (Exception e)
		{
			
		}
		JSONObject obj = new JSONObject();
				
		if(customer != null) {
			obj.put("status", "OK");
			obj.put("personId", customer.getId());
			obj.put("regDate", customer.getCreatedDate().toString());
			obj.put("picture", customerPic);
		} else {
			obj.put("status", "FAIL");
		}
					
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
		
		
		return "content/ajaxCustomerList";
	}
	
	
	
	
	
	@RequestMapping("/reload")
	public String relodPage(HttpServletRequest request) {
		if (!checkUserLogin(request) || !isAdminUser(request)) {
			return "content/ajaxSessionExpired";
		}
		return "content/ajaxCustomerList";
	 }
}
