package com.citizant.fraudshield.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.citizant.fraudshield.common.SystemConfig;
import com.citizant.fraudshield.domain.Person;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class IdCardGenerator {
	
	
	
	public static byte[] generateCard(Person customer, String userId) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("txtInstruction", SystemConfig.CARD_INSTRUCTION);
			
			String name = customer.getFirstName();
			if(customer.getMiddleInitial()!=null && customer.getMiddleInitial().length()>0){
				name = name + " " + customer.getMiddleInitial() + ".";
			}
			name = name + " " + customer.getLastName();
			
			parameters.put("txtName", name);
			
			
			parameters.put("txtFid", customer.getFraudShieldID());
			parameters.put("txtRegDate", StringUtil.getStandardDate(customer.getCreatedDate()));
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(customer.getCreatedDate());;
			cal.add(Calendar.YEAR, 1); // to get next year add 1
			
			parameters.put("txtExDate", StringUtil.getStandardDate(cal.getTime()));

			JasperPrint print = JasperFillManager.fillReport(SystemConfig.JASPER_TEMPLATE, parameters, new JREmptyDataSource());

			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, o); 
			
			exporter.exportReport();
			
			return o.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		
		}
		
		return null;
	}
	
	
	
	
	public static void main(String[] args) {
		try {
			
			String reportName = "fraudshield_id_card";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("txtInstruction", "This is your Fraud Shield Registration\n Please call 777-777-1827 if you have any question");
			parameters.put("txtName", "Joe S. Smith");
			parameters.put("txtFid", "81271271772771");
			parameters.put("txtRegDate", "11/20/2015");
			parameters.put("txtExDate", "12/11/2016");
			// compiles jrxml
			JasperCompileManager.compileReportToFile("C:\\Projects\\FraudShield\\WebContent\\WEB-INF\\fraudshield_id_card.jrxml");
			// fills compiled report with parameters and a connection
			JasperPrint print = JasperFillManager.fillReport("C:\\Projects\\FraudShield\\WebContent\\WEB-INF\\FraudShiledID.jasper", parameters, new JREmptyDataSource());
			// exports report to pdf
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream("C:\\Projects\\FraudShield\\WebContent\\WEB-INF\\" + reportName + ".pdf")); 
			
			exporter.exportReport();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		
		}
	}
	
	
	
	
}
