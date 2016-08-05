package com.citizant.fraudshield.bean;

public class FaceCompareResult {
	
	private String encodedFaceImage;
	
	private String encodedRefImage;
	
	private double similarity;
	
	public String getEncodedFaceImage() {
		return encodedFaceImage;
	}
	public void setEncodedFaceImage(String encodedFaceImage) {
		this.encodedFaceImage = encodedFaceImage;
	}
	public String getEncodedRefImage() {
		return encodedRefImage;
	}
	public void setEncodedRefImage(String encodedRefImage) {
		this.encodedRefImage = encodedRefImage;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	
}
