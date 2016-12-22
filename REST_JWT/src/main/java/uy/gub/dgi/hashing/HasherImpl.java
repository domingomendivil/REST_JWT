package uy.gub.dgi.hashing;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HasherImpl {
	
	

	
	public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
		 
	       try {
	           SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
	           PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
	           SecretKey key = skf.generateSecret( spec );
	           byte[] res = key.getEncoded( );
	           //for (int i=0;i<res.length;i++)
	        	 //  System.out.println(res[i]);
	           return res;
	 
	       } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
	           throw new RuntimeException( e );
	       }
	
	}

	
	public String hash(String user, String password,String salt) {
		byte[] res =hashPassword(password.toCharArray(),(user+salt).getBytes(), 2, 512);
		String s2 = Base64.getEncoder().encodeToString(res);
		return s2;
	}
	
	public static void main(String[] args) {
		System.out.println(new HasherImpl().hash("domingo", "pep1to", "%_$P2·"));
	}

}
