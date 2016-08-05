package com.citizant.fraudshield.service;

import java.awt.image.BufferedImage;

import com.citizant.fraudshield.bean.FaceCompareResult;

public interface FaceRegService {
	
	public FaceCompareResult compareFace(String encodedFaceImage, String encodedRefImage);
	
	public FaceCompareResult compareFace(BufferedImage faceImage, BufferedImage refImage);
	
}
