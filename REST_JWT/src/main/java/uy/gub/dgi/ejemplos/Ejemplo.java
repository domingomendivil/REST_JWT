package uy.gub.dgi.ejemplos;

import java.security.Key;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

public class Ejemplo {

	public static void main(String[] args) throws JoseException {
		Key key = new AesKey(ByteUtil.randomBytes(16));
		 JsonWebEncryption jwe = new JsonWebEncryption();
		 jwe.setPayload("Hello World!");
		 jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
		 jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
		 jwe.setKey(key);
		 String serializedJwe = jwe.getCompactSerialization();
		 System.out.println("Serialized Encrypted JWE: " + serializedJwe);
		 jwe = new JsonWebEncryption();
		 jwe.setKey(key);
		 jwe.setCompactSerialization(serializedJwe);
		 System.out.println("Payload: " + jwe.getPayload());
	}
}
