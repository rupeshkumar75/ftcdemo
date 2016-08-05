package com.citizant.fraudshield.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class StringUtil {
	
	public static String getServerURL(HttpServletRequest request){
		return "";
	}
	
	public static boolean isEmpty(String str){
		if (str == null || (str.trim().length() == 0))
		{
			return true;
		}
			
		return false;
	}
	
	/**
	 * This method is to get the picture expiration date in mm/dd/yyyy format,
	 * which is three years from the date of picture upload.
	 */
	public static String getPictureExpirationDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 12);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		System.out.println("Picture Expiration Date --- " + sdf.format(cal.getTime()));
		return sdf.format(cal.getTime());
	}
	
	public static String getStandardDate(Date d)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(d);
	}
	
	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, -1); // to get previous year add -1
		Date endDay = cal.getTime();
		System.out.println(today.toGMTString());
		System.out.println(endDay.toGMTString());
	}
}
