package com.shopping.jwtsecret;
import java.security.SecureRandom;
import java.util.Base64;
public class JwtSecretGenerator {	
	    public static void main(String[] args) {
	        // Generate a 256-bit secret (32 bytes for HS256)
	        SecureRandom secureRandom = new SecureRandom();
	        byte[] secretKey = new byte[32];  // 32 bytes = 256 bits
	        secureRandom.nextBytes(secretKey);

	        // Encode the key as a Base64 string
	        String jwtSecret = Base64.getEncoder().encodeToString(secretKey);

	        // Print the generated secret
	        System.out.println(jwtSecret);
	   
	}
}
