package com.citizant.fraudshield.security;


import java.util.HashMap;
import java.util.Random;
import java.util.Stack;


public class RandomUniqueNumberGenarator {
	private static Stack<String> tokens = null; 
	
	public static void main(String[] args) {
		generateRandomPin();
	}

	public static String getKey(){
		return tokens.pop();
	}
	
	public static synchronized String generateRequestCode() {		
			long START = 10000000000000L;
			long END = 99999999999999L;
			Random random = new Random();		
			String tt = "" + createRandomInteger(START, END, random);
			return tt;
	}
	
	public static synchronized void generateRandomPin() {
		if(tokens == null){
			tokens = new Stack<String>();
			long START = 10000000000000L;
			long END = 99999999999999L;
			HashMap<String, String> hs = new HashMap<String, String>();
			Random random = new Random();
			for (int idx = 1; idx <= 3000; ++idx) {
				String tt = "" + createRandomInteger(START, END, random);
				if(hs.get(tt)==null){
					tokens.push(tt);
					hs.put(tt, "");
				}
			}			
		}
	}

	private static long createRandomInteger(long aStart, long aEnd,
			Random aRandom) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		// get the range, casting to long to avoid overflow problems
		long range = aEnd - aStart + 1;

		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * aRandom.nextDouble());
		long randomNumber = fraction + aStart;
		return randomNumber;
	}

}
