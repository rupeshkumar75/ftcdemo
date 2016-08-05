package com.citizant.fraudshield.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.citizant.fraudshield.bean.FaceCompareResult;
import com.citizant.fraudshield.bean.ServiceRequestData;
import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.service.FaceRegService;
import com.citizant.fraudshield.util.ImageUtil;

public class FaceCompareServiceImpl implements FaceRegService {
	
	public FaceCompareResult compareFace(String encodedFaceImage, String encodedRefImage) {
		FaceCompareResult res = new FaceCompareResult();
		
		List<ServiceRequestData> reqData = new ArrayList<ServiceRequestData>();
		reqData.add(new ServiceRequestData("faceImage", encodedFaceImage));
		reqData.add(new ServiceRequestData("refImage", encodedRefImage));
		
		String response = getFaceServiceJSonResponse("compareFaceWithImage", reqData);
		
		try {
			JSONObject json = new JSONObject(response);			
			res.setEncodedFaceImage(json.getString("faceImage"));
			res.setEncodedRefImage(json.getString("refImage"));
			res.setSimilarity(json.getDouble("similarity"));
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return res;
	}
	
	
	public FaceCompareResult compareFace(BufferedImage faceImage, BufferedImage refImage) {	
		String encodedFace = ImageUtil.encodeImage(faceImage);
		String encodeRef = ImageUtil.encodeImage(refImage);
		
		return compareFace(encodedFace, encodeRef);
	}
	
	public FaceCompareResult compareFace(BufferedImage faceImage, String template) {
		FaceCompareResult res = new FaceCompareResult();
		
		String encodedFace = ImageUtil.encodeImage(faceImage);
		
		List<ServiceRequestData> reqData = new ArrayList<ServiceRequestData>();
		reqData.add(new ServiceRequestData("faceImage", encodedFace));
		reqData.add(new ServiceRequestData("refTemplate", template));
		
		String response = getFaceServiceJSonResponse("compareFaceWithTemplate", reqData);
		
		try {
			JSONObject json = new JSONObject(response);			
			res.setEncodedFaceImage(json.getString("faceImage"));
			res.setSimilarity(json.getDouble("similarity"));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return res;
	}
	
	public String getFaceTemplate(BufferedImage faceImage) {
		
		String encodedFace = ImageUtil.encodeImage(faceImage);
		
		List<ServiceRequestData> reqData = new ArrayList<ServiceRequestData>();
		reqData.add(new ServiceRequestData("faceImage", encodedFace));
		String response = getFaceServiceJSonResponse("getFaceTemplate", reqData);
		
		try {
			JSONObject json = new JSONObject(response);			
			return json.getString("template");
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private String getFaceServiceJSonResponse(String serviceType, List<ServiceRequestData> reqData) {
		
		String url = SystemConfig.FACE_SERVICE_URL + "/" + serviceType;

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", "Mozilla/5.0");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for(ServiceRequestData data : reqData) {
			urlParameters.add(new BasicNameValuePair(data.getName(), data.getValue()));
		}
		
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = client.execute(post);
			System.out.println("Response Code : " 
		                + response.getStatusLine().getStatusCode());
	
			BufferedReader rd = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			return result.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
