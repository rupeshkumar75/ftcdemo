package com.citizant.fraudshield.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.exception.DocumentServiceException;
import com.citizant.fraudshield.security.EncryptionUtil;
import com.citizant.fraudshield.service.DocumentService;

public class S3DocumentServiceImpl implements DocumentService {
	
	private AWSCredentials credentials = null;
	private AmazonS3 s3 = null;
	
	public S3DocumentServiceImpl() {
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. "
							+ "Please make sure that your credentials file is at the correct "
							+ "location (~/.aws/credentials), and is in valid format.", e);
		}

		s3 = new AmazonS3Client(credentials);
		Region usWest2 = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(usWest2);
		try {
			setupDocumentService();
		} catch (DocumentServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* 
	 * check if S3 bucket was created, if not, create one
	 */
	@Override
	public boolean setupDocumentService()  throws DocumentServiceException {
		boolean bucketExist = s3.doesBucketExist(SystemConfig.AWS_S3_BUCKET);
		if(!bucketExist) {
			s3.createBucket(SystemConfig.AWS_S3_BUCKET);
			try{
				Thread.sleep(2000);
			}catch(Exception e){
				
			}
			bucketExist = s3.doesBucketExist(SystemConfig.AWS_S3_BUCKET);
		}
		return bucketExist;
	}
	
	/*
	 * This function encrypt the file content and save into S3. Return 
	 * file key
	 */
	@Override
	public String saveDocument (byte[] contents) throws DocumentServiceException {
		try {
			String fileKey = SystemConfig.SERVER_ID + "-" + (new Date()).getTime();
			
			String fileData = EncryptionUtil.encryptImage(contents);
			InputStream inputStream = new ByteArrayInputStream(fileData.getBytes());
			Long contentLength = Long.valueOf(fileData.getBytes().length);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(contentLength);
			s3.putObject(new PutObjectRequest(SystemConfig.AWS_S3_BUCKET, fileKey, inputStream, metadata));
			return fileKey;
			
		}catch (AmazonServiceException ase) {
			DocumentServiceException docException = new DocumentServiceException(ase.toString(),
					"Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            throw docException;
            
        } catch (AmazonClientException ace) {
        	DocumentServiceException docException = new DocumentServiceException(ace.toString(),
        			"Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with S3, "
                            + "such as not being able to access the network.");
          
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            
            throw docException;
           
        }
	
	}
	
	
	@Override
	public byte[] getDocument (String documentId)  throws DocumentServiceException {
		try {
			S3Object object = s3.getObject(new GetObjectRequest(SystemConfig.AWS_S3_BUCKET, documentId));
			BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
			StringBuffer sbuffer = new StringBuffer();
			
			try {
		    	while (true) {
		            String line = reader.readLine();
		            if (line == null) break;
		            sbuffer.append(line);
		        }
			} catch (Exception e) {
				
			}
			
			byte[] imageData = EncryptionUtil.decryptImage(sbuffer.toString());
			
			return imageData;
		}catch (AmazonServiceException ase) {
			DocumentServiceException docException = new DocumentServiceException(ase.toString(),
					"Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            throw docException;
            
        } catch (AmazonClientException ace) {
        	DocumentServiceException docException = new DocumentServiceException(ace.toString(),
        			"Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with S3, "
                            + "such as not being able to access the network.");
          
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            throw docException;
           
        }

	}
	
}
