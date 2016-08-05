package com.citizant.fraudshield.controller;




import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.citizant.fraudshield.bean.CustomerBean;
import com.citizant.fraudshield.bean.FaceCompareResult;
import com.citizant.fraudshield.bean.ScannedDocHolder;
import com.citizant.fraudshield.bean.SearchBean;
import com.citizant.fraudshield.callcenter.CallCenterManager;
import com.citizant.fraudshield.callcenter.Caller;
import com.citizant.fraudshield.common.AppConstants;
import com.citizant.fraudshield.domain.Person;
import com.citizant.fraudshield.domain.RenewRequest;
import com.citizant.fraudshield.service.CustomerService;
import com.citizant.fraudshield.service.FaceRegService;
import com.citizant.fraudshield.service.RenewService;




@Controller
@RequestMapping("/renew")
public class RenewController extends BaseController {
	
	@Autowired
	RenewService renewService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	FaceRegService faceService;
	
	
	@RequestMapping("/startRequest")
	public ModelAndView startRequestRenew (HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tile.renewRequestPage");
		return mav;
	}

	@RequestMapping("/requestRenew")
	public void requestRenew (HttpServletRequest request, 
			HttpServletResponse response, Model model) throws IOException, JSONException {
		
		SearchBean sBean = new SearchBean();
		sBean.setSsn(request.getParameter("ssn"));
		sBean.setFirstName(request.getParameter("firstName"));
		sBean.setLastName(request.getParameter("lastName"));
		sBean.setFraudShiledId(request.getParameter("fraudShieldId"));
		
		JSONObject obj = new JSONObject();
		
		Person p = renewService.getRegistedPerson(sBean);
		
		if(p == null) {
			obj.put("status", "INVALID");
		} else {
		
			//Check if already in system
			RenewRequest renewReq = renewService.getRenewRequestByFid(p.getFraudShieldID());
			if(renewReq == null) {			
				//Create a Renew Process Record
				renewReq = renewService.createRenewRequest(p);
			}
			obj.put("status", "SUCCESS");
			obj.put("requestCode", renewReq.getRequestCode());
			//Send Email About Renew
		}
		
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	
	}
	
	@RequestMapping("/requestInterview")
	public String requestInterview (HttpServletRequest request, Model model) {	
		String requestCode = request.getParameter("requestCode");		
		//Validate request code
		RenewRequest req = renewService.getRenewRequestByCode(requestCode);
		
		if(req != null) {
			Date now = new Date();
			long TOW_DAYS = 48 * 3600 * 1000;
			if(now.getTime() - req.getRequestDate().getTime() > TOW_DAYS) {
				
				//Give some error message				
				//return null;
			}
		}
		model.addAttribute("requestCode", requestCode);
	
		return "content/waitInterview";
	}	
	
	
	//This is an Ajax method called by caller to see if
	//Their interview request was picked up by a CCTSO
	@RequestMapping("/checkInterviewRequest")
	public void checkInterview (HttpServletRequest request, HttpServletResponse response) throws Throwable {
	
		String requestCode = request.getParameter("requestCode");		
		
		CallCenterManager cmanager = CallCenterManager.getCallCenterManager();

		JSONObject obj = new JSONObject();

		if (cmanager.isSessionStart(requestCode)) {
			Caller caller = cmanager.callerPickup(requestCode);
			obj.put("sessionStart", "yes");
			obj.put("roomNumber", caller.getRoomNumber());
			obj.put("order", "0");

		} else {
			obj.put("sessionStart", "no");
			obj.put("order", "" + cmanager.getOrder(requestCode));
		}

		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
		
	}	
	
	
	
	//This is an Ajax method called by CCTSO to 
	//List callers
	@RequestMapping("/customerHangeup")
	public void customerHangup (HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestCode = request.getParameter("requestCode");		
		
		CallCenterManager cmanager = CallCenterManager.getCallCenterManager();
		
		cmanager.customerHangup(requestCode);
		
		response.setContentLength(4);
		response.setContentType("plain/text");
		response.getWriter().write("Done");
		response.getWriter().flush();
		response.getWriter().close();
	}	
	
	//This is an Ajax method called by CCTSO to 
	//List callers
	@RequestMapping("/getCallerList")
	public String getCallerList (HttpServletRequest request, Model model) {
		if (!checkUserLogin(request)) {
			return null;
		}			
		CallCenterManager cmanager = CallCenterManager.getCallCenterManager();
		
		model.addAttribute("callerList", cmanager.getCallerList());
	
		return "content/ajaxCallerList";
	}	
	
	//This is an Ajax method called by CCTSO to 
	//List callers
	@RequestMapping("/getRequestList")
	public String getRequestList (HttpServletRequest request, Model model) {
		if (!checkUserLogin(request)) {
			return null;
		}				
		model.addAttribute("requestList", renewService.getRequestList());
	
		return "content/ajaxRequestList";
	}	
	
	
	//This is an Ajax method called by CCTSO to 
	//Pickup a caller from queue, first come first serve
	//based
	@RequestMapping("/cctsopanel")
	public ModelAndView cctsoPanel ( HttpServletRequest request, Model model) {
		if (!checkUserLogin(request) || !isCcTsoUser(request)) {
			return null;
		}
		ModelAndView mav = new ModelAndView();
	
		mav.setViewName("tile.cctsoPanelPage");
		
		return mav;
	}	
	
	
	
	
	//This is an Ajax method called by CCTSO to 
	//Pickup a caller from queue, first come first serve
	//based
	@RequestMapping("/pickupCall")
	public ModelAndView pickupCall (HttpServletRequest request, Model model) {
		
		ModelAndView mav = new ModelAndView();
		
		CallCenterManager cmanager = CallCenterManager.getCallCenterManager();
		
		Caller caller = cmanager.pickupCaller();
		
		if(caller == null ) {
			//Return to
			return null;
		}
		
		RenewRequest req = renewService.getRenewRequestByCode(caller.getRequestCode());
		
		CustomerBean frmBean = customerService.getCustomerById(req.getCustomerId());
		
		if (frmBean.getIdDocs() != null) {
			ScannedDocHolder holder = new ScannedDocHolder();
			holder.setDocs(frmBean.getIdDocs());
			request.getSession().setAttribute(AppConstants.DOC_HOLDER, holder);
		} else {
			request.getSession().removeAttribute(AppConstants.DOC_HOLDER);
		}
		
		model.addAttribute("frmBean", frmBean);
		model.addAttribute("showDocs", Boolean.TRUE);
			
		mav.addObject("caller", caller);
		mav.addObject("renewRequest", renewService.getRenewRequestByCode(caller.getRequestCode()));
		
		mav.setViewName("tile.cctsoInterviewPage");
		
		return mav;
	}	
	
	//This is an Ajax method called by CCTSO to 
	//Pickup a caller from queue, first come first serve
	//based
	@RequestMapping("/approveRenew")
	public void  saveCustomer(@ModelAttribute("formBean") CustomerBean frmBean,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!checkUserLogin(request)) {
			return;
		}
	
		String requestCode = request.getParameter("requestCode");
		String tsoUser = this.getLoginUser(request).getUsername();
		frmBean.setTsoUser(tsoUser);
		request.getSession().removeAttribute(AppConstants.DOC_HOLDER);
		renewService.renewCustomer(requestCode, frmBean, tsoUser);
		
		
		JSONObject obj = new JSONObject();
		obj.put("personId", frmBean.getPersonId());
	
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	
	//This is an Ajax method called by CCTSO to 
	//Pickup a caller from queue, first come first serve
	//based
	@RequestMapping("/declineRenew")
	public void  declineCustomerRenew(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!checkUserLogin(request)) {
			return;
		}
		
		String requestCode = request.getParameter("requestCode");
		String comments = request.getParameter("declineComments");
		
		renewService.declineRenew(requestCode, comments, getLoginUser(request).getUsername());
		
		JSONObject obj = new JSONObject();
		obj.put("personId", "");
	
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	
	//This is an Ajax method called by CCTSO to 
	//Pickup a caller from queue, first come first serve
	//based
	@RequestMapping("/checkApprovalStatus")
	public void  checkApprovalStatus(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String requestCode = request.getParameter("requestCode");
	
		RenewRequest req = renewService.getRenewRequestByCode(requestCode);
		
		JSONObject obj = new JSONObject();
		obj.put("status", req.getStatus());
		
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	
	//This is an Ajax method called by CCTSO to 
	//Pickup a caller from queue, first come first serve
	//based
	@RequestMapping("/makePayment")
	public void  makePayment(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String requestCode = request.getParameter("requestCode");
	
		RenewRequest req = renewService.getRenewRequestByCode(requestCode);
		
		renewService.makePayment(requestCode);
		
		JSONObject obj = new JSONObject();
		obj.put("fid", req.getNewFid());
		
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	//This is an Ajax method called by CCTSO to 
	//Pickup a caller from queue, first come first serve
	//based
	@RequestMapping("/checkRequestStatus")
	public void  checkRequestStatus(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String requestCode = request.getParameter("requestCode");
	
		RenewRequest req = renewService.getRenewRequestByCode(requestCode);
		
		JSONObject obj = new JSONObject();
		
		if(req == null) {
			obj.put("status", "INVALID");
		} else {		
			CallCenterManager cmanager = CallCenterManager.getCallCenterManager();
			if("NEW".equals(req.getStatus())) {
				if(!cmanager.isCallerInQueue(requestCode)) {					
					//Add user to queue
					CustomerBean p = customerService.getCustomerById(req.getCustomerId());
					
					Caller c = new Caller();
					c.setRequestCode(requestCode);
					c.setFirstName(p.getFirstName());
					c.setLastName(p.getLastName());
					c.setCallTime(new Date());
					cmanager.addCaller(c);
				}
			}
			obj.put("status", req.getStatus());
			obj.put("fid", req.getNewFid());
		}
		
		String jsonText = obj.toString();
		response.setContentLength(jsonText.length());
		response.setContentType("plain/text");
		response.getWriter().write(jsonText);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	@RequestMapping("/cctsoConference")
	public ModelAndView startCctsoConference (HttpServletRequest request, Model model) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tile.cctsoConferencePage");
		return mav;
	}
	
	
	//This is an Ajax method called by CCTSO make 
	//facial comparing
	@RequestMapping("/faceCompare")
	public void  compareFace(HttpServletRequest request, HttpServletResponse response)
	 {
		String encodedNewFace = "";
		String encodedOrigFace = "";
		String sim  = "0";
		String requestCode = request.getParameter("requestCode");
		String imageData = request.getParameter("imgBase64");
		
		RenewRequest req = renewService.getRenewRequestByCode(requestCode);
		encodedOrigFace = req.getFaceImage();
		if(encodedOrigFace == null) {
			encodedOrigFace = customerService.getCustomerPictureById(req.getCustomerId());
		}
		
		/*
		try{

		
			BASE64Decoder bd = new BASE64Decoder();
			
			imageData = imageData.substring(imageData.indexOf(",") + 1);
			
			encodedNewFace = imageData;
			
			byte[] imgBytes = bd.decodeBuffer(imageData);
			
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imgBytes));
		
			//Retrieve Reference image from database
			FSDK.FSDK_FaceTemplate.ByReference refTemplate = null;
			
			RenewRequest req = renewService.getRenewRequestByCode(requestCode);
			
			if(req.getFaceTemplate()!=null && req.getFaceTemplate().length() > 0) {
				refTemplate = renewService.getFaceTamplate(req, null);
				encodedOrigFace = req.getFaceImage();
			} else {						
				CustomerBean customer = customerService.getCustomerById(req.getCustomerId());
				List<ScannedIdentityBean> docs = customer.getIdDocs();
				BufferedImage refImg = null;			
				for(ScannedIdentityBean doc : docs) {
					if("Picture".equalsIgnoreCase(doc.getDocType())) {
						refImg = ImageIO.read(new ByteArrayInputStream(doc.getImageData()));
						break;
					}
				}
				if(refImg != null) {
					refTemplate = renewService.getFaceTamplate(req, refImg);
					encodedOrigFace = req.getFaceImage();
				}
			}
			
			//Comapre the images
			PhotoCompare comp = new PhotoCompare();
			
			BufferedImage newFace = comp.getFaceImage(bi);
			if(newFace!=null) {
				encodedNewFace = ImageUtil.encodeImage(newFace);					
				if(refTemplate != null) {
					FSDK.FSDK_FaceTemplate.ByReference newRef = comp.getImageFaceTemplate(bi);
					if(newRef != null) {
						float fsim = comp.compareImageTemplate(refTemplate, newRef);		
						sim = "" + fsim * 100;
					}					
				}
							
			} else {
				encodedNewFace = ImageUtil.encodeImage (bi);	
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			sim = e.toString();
		}
		*/
		
		FaceCompareResult result = faceService.compareFace(encodedNewFace, encodedOrigFace);
		
		String jsonText = "";
		
		try {
			JSONObject obj = new JSONObject();
			
			obj.put("status", "" + result.getSimilarity());
			obj.put("orgFace", result.getEncodedRefImage());
			obj.put("newFace", result.getEncodedFaceImage());
			
			jsonText = obj.toString();
	
			response.setContentLength(jsonText.length());
			response.setContentType("plain/text");
			response.getWriter().write(jsonText);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
