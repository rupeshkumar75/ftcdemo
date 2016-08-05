package com.citizant.fraudshield.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.citizant.fraudshield.bean.CustomerBean;
import com.citizant.fraudshield.bean.ScannedDocHolder;
import com.citizant.fraudshield.bean.ScannedIdentityBean;
import com.citizant.fraudshield.common.ActivityType;
import com.citizant.fraudshield.common.AppConstants;
import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.domain.TsoUser;
import com.citizant.fraudshield.exception.CustomerServiceException;
import com.citizant.fraudshield.service.ActivityLogService;
import com.citizant.fraudshield.service.CustomerService;
import com.citizant.fraudshield.service.DocumentService;
import com.citizant.fraudshield.util.IdCardGenerator;

@Controller
@RequestMapping("/customer")
public class CustomerEditController extends BaseController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	ActivityLogService activityLogService;

	@RequestMapping("/new")
	public ModelAndView addCustomer(HttpServletRequest request, Model model) {
		
		ModelAndView mav = new ModelAndView();
		if (!checkUserLogin(request) || ( !isTsoUser(request) && !isAdminUser(request))) {
			return new ModelAndView(getLoginRedirect());
		}
		
		mav.setViewName("tile.customerEditPage");
		CustomerBean frmBean = new CustomerBean();
		model.addAttribute("frmBean", frmBean);
		request.getSession().removeAttribute(AppConstants.DOC_HOLDER);
		model.addAttribute("showDocs", Boolean.TRUE);
		return mav;
	}

	@RequestMapping("/edit")
	public ModelAndView editCustomer(HttpServletRequest request,
			@RequestParam Long customerId, Model model) {
		ModelAndView mav = new ModelAndView();
		if (!checkUserLogin(request)  || !isAdminUser(request)) {
			return new ModelAndView(getLoginRedirect());
		}
		mav.setViewName("tile.customerEditPage");

		CustomerBean frmBean = customerService.getCustomerById(customerId);
		if (frmBean.getIdDocs() != null) {
			ScannedDocHolder holder = new ScannedDocHolder();
			holder.setDocs(frmBean.getIdDocs());
			request.getSession().setAttribute(AppConstants.DOC_HOLDER, holder);
		} else {
			request.getSession().removeAttribute(AppConstants.DOC_HOLDER);
		}
		model.addAttribute("frmBean", frmBean);
		model.addAttribute("showDocs", Boolean.TRUE);
		String tsoUser = this.getLoginUser(request).getUsername();
		activityLogService.log(tsoUser, ActivityType.VIEW_REGISTRATION, "Started editing customer", frmBean.getPersonId());
		return mav;
	}

	@RequestMapping("/save")
	public void saveCustomer(@ModelAttribute("formBean") CustomerBean frmBean,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!checkUserLogin(request)) {
			return;
		}
		JSONObject obj = new JSONObject();
		//Add server side validation
		if(frmBean.isValid()) {
			ScannedDocHolder holder = (ScannedDocHolder) request.getSession()
					.getAttribute(AppConstants.DOC_HOLDER);
			if (holder != null) {
				frmBean.setIdDocs(holder.getDocs());
			}
			
			String tsoUser = this.getLoginUser(request).getUsername();
			frmBean.setTsoUser(tsoUser);		
			try {			
				customerService.saveCustomer(frmBean);
				obj.put("status", "OK");
				obj.put("personId", frmBean.getPersonId());
				obj.put("fsId", frmBean.getFraudShieldID());
				obj.put("addressId", frmBean.getAddressId());
				obj.put("ssn", frmBean.getSsn());
				request.getSession().removeAttribute(AppConstants.DOC_HOLDER);
				String loginUser = this.getLoginUser(request).getUsername();
				activityLogService.log(loginUser, ActivityType.CREATE_REGISTRATION, "Customer save complete", frmBean.getPersonId());
				
			} catch (CustomerServiceException e) {
				obj.put("status", "FAIL");
				obj.put("errorMsg", e.getLongDesc());
			}
		} else {
			obj.put("status", "FAIL");
			obj.put("errorMsg", "Invalid customer data");
		}
					
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("/deletedoc")
	public String deleteDoc(HttpServletRequest request,
			@RequestParam Long docId, @RequestParam Integer count) {

		if (!checkUserLogin(request) || !isTsoUser(request)) {
			return  "content/ajaxSessionExpired";
		}
		if (docId != null && docId != 0) {
			customerService.deleteDocument(docId);
		}

		ScannedDocHolder holder = (ScannedDocHolder) request.getSession()
				.getAttribute(AppConstants.DOC_HOLDER);
		if (holder != null) {
			holder.removeDoc(count);
			request.getSession().setAttribute(AppConstants.DOC_HOLDER, holder);
		}
		return "content/ajaxSuccess";
	}

	@RequestMapping("/scanner")
	public String openScannerControl(HttpServletRequest request) {

		return "content/ajaxScannerController";
	}
	
	@RequestMapping("/picture")
	public String openWebcam(HttpServletRequest request) {

		return "content/webcamController";
	}

	@RequestMapping("/doclist")
	public String listDocument(HttpServletRequest request) {
		if (!checkUserLogin(request)) {
			return "content/ajaxSessionExpired";
		}
		// check if there is anything in application scope, if yes,
		// Transfer to session scope.
		ScannedDocHolder app_holder = (ScannedDocHolder) request.getSession()
				.getServletContext().getAttribute(request.getSession().getId());
		ScannedDocHolder holder = (ScannedDocHolder) request.getSession()
				.getAttribute(AppConstants.DOC_HOLDER);
		if (holder == null) {
			holder = new ScannedDocHolder();
		}
		if (app_holder != null) {
			for(ScannedIdentityBean dc : app_holder.getDocs()) {
				holder.addDoc(dc);
			}
			
			request.getSession().getServletContext()
					.removeAttribute(request.getSession().getId());
		}
		request.getSession().setAttribute(AppConstants.DOC_HOLDER, holder);

		return "content/ajaxDocList";
	}

	@RequestMapping("/image")
	public void getImage(HttpServletRequest request,
			HttpServletResponse response, @RequestParam Integer index)
			throws IOException {
		if (!checkUserLogin(request)) {
			return;
		}
		ScannedDocHolder holder = (ScannedDocHolder) request.getSession()
				.getAttribute(AppConstants.DOC_HOLDER);
		if (holder != null) {
			ScannedIdentityBean doc = holder.getDocs().get(index);
			String loginUser = this.getLoginUser(request).getUsername();
			response.setContentLength(doc.getImageData().length);
			response.setContentType("image/jpeg");
			response.setHeader("content-disposition",
					"inline; filename=\"docimage\"");
			response.getOutputStream().write(doc.getImageData());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
	@RequestMapping("/thumb")
	public void getImageThumb(HttpServletRequest request,
			HttpServletResponse response, @RequestParam Integer index)
			throws IOException {
		if (!checkUserLogin(request)) {
			return;
		}
		ScannedDocHolder holder = (ScannedDocHolder) request.getSession()
				.getAttribute(AppConstants.DOC_HOLDER);
		if (holder != null) {
			ScannedIdentityBean doc = holder.getDocs().get(index);
			response.setContentLength(doc.getThumbData().length);
			response.setContentType("image/jpeg");
			response.setHeader("content-disposition",
					"inline; filename=\"docimage\"");
			response.getOutputStream().write(doc.getThumbData());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
	
	
	@RequestMapping("/checkDuplicate")
	public void checkDuplicate (
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!checkUserLogin(request)) {
			return;
		}
		
		String ssn = request.getParameter("ssn");
	
		JSONObject obj = new JSONObject();
		Person p = customerService.isSSNExits(ssn);
		if(	p != null) {
			obj.put("status", "DUP");
			obj.put("personId", p.getId());
			obj.put("regDate", p.getCreatedDate().toString());
			obj.put("picture", customerService.getCustomerPictureById(p.getId()));
		} else {
			obj.put("status", "NO");
		}
	
		String jsonText = obj.toString();
		
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	@RequestMapping("/checkNewDocs")
	public void checkNewDocs (
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject obj = new JSONObject();
		ScannedDocHolder app_holder = (ScannedDocHolder) request.getSession()
				.getServletContext().getAttribute(request.getSession().getId());
		
		if (app_holder == null) {
			app_holder = new ScannedDocHolder();
		}
		if(app_holder.checkNewDocs()) {
			obj.put("status", "yes");
		} else {
			obj.put("status", "NO");
		}
	
		String jsonText = obj.toString();
		
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping("/back")
	public ModelAndView backToHome(HttpServletRequest request) {
		 ModelAndView mav = new ModelAndView();
		 TsoUser user = getLoginUser(request);
		 if(user!=null){			
			//Redirect user by roles
			if("TSO".equalsIgnoreCase(user.getUserRole())) {
				mav.setViewName("tile.tsoHomePage");			
			}
			
			if("CCTSO".equalsIgnoreCase(user.getUserRole())) {
				mav.setViewName("tile.cctsoPanelPage");
			}
			
			if("ADMIN".equalsIgnoreCase(user.getUserRole())) {
				mav.setViewName("tile.searchPage");
			}
			
	        return mav;
		 } else {
			 return new ModelAndView(getLoginRedirect());
		 }
	}
	
	@RequestMapping("/invalidate")
	public void invalidateReg (
			HttpServletRequest request, 
			@RequestParam Long personId,
			HttpServletResponse response)
			throws Exception {
		JSONObject obj = new JSONObject();
		customerService.setCustomerRecordInactive(personId);	
		String loginUser = this.getLoginUser(request).getUsername();
		activityLogService.log(loginUser, ActivityType.INVALID_REGISTRATION, "Invalidate registration ", personId);	

		obj.put("status", "OK");

		String jsonText = obj.toString();
		
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	
	@RequestMapping("/printcard")
	public void printIdCard (
			HttpServletRequest request, 
			@RequestParam Long personId,
			HttpServletResponse response)
			throws Exception {
		
		String loginUser = this.getLoginUser(request).getUsername();
		activityLogService.log(loginUser, ActivityType.PRINT_ID_CARD, "Print ID Card", personId);	

		SystemConfig.JASPER_TEMPLATE = request.getServletContext().getRealPath("/WEB-INF/FraudShiledID.jasper");
		
		Person customer = customerService.getPersonById(personId);
		
		byte[] pdf = IdCardGenerator.generateCard(customer, this.getLoginUser(request).getUsername());
		String filename = "fraudshieldId.pdf";
		
		response.setContentType("application/pdf");
		response.addHeader("content-disposition", "inline; filename=\"" + filename + "\"");
		response.setContentLength(pdf.length);
		try {
			OutputStream out = response.getOutputStream();
			out.write(pdf);
			out.flush();
			out.close();
		} catch (Exception e) {

		}
	}
	
	
}
