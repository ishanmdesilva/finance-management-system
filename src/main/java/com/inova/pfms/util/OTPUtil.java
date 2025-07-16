package com.inova.pfms.util;

import java.security.SecureRandom;

public class OTPUtil {
    
    public static String generateOtp() {
    	SecureRandom secureRandom = new SecureRandom();
    	int otp = secureRandom.nextInt(1_000_000); // Generates a number from 0 to 999999
    	return String.format("%06d", otp);
    }
}
