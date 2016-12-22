package uy.gub.dgi.jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

public class JWTConsumerImpl implements JWTConsumer {
	private RsaJsonWebKey rsaJsonWebKey;

	public JWTConsumerImpl() {
		try {
			rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
		} catch (JoseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public JWTData validateJWT(String jwt) throws JWTConsumerException {
		JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime() // the
																						// JWT
																						// must
																						// have
																						// an
																						// expiration
																						// time
				.setMaxFutureValidityInMinutes(300) // but the expiration time
													// can't be too crazy
				.setAllowedClockSkewInSeconds(30) // allow some leeway in
													// validating time based
													// claims to account for
													// clock skew
				.setRequireSubject() // the JWT must have a subject claim
				.setExpectedIssuer("Issuer") // whom the JWT needs to have been
												// issued by
				.setExpectedAudience("Audience") // to whom the JWT is intended
													// for
				.setVerificationKey(rsaJsonWebKey.getKey()) // verify the
															// signature with
															// the public key
				.build(); // create the JwtConsumer instance

		try {
			// Validate the JWT and process it to the Claims
			JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
			JWTData jwtData = new JWTData();
			jwtData.setUser(jwtClaims.getSubject());
			System.out.println("JWT validation succeeded! " + jwtClaims);
			System.out.println(jwtClaims.getExpirationTime());
			return jwtData ;
		} catch (InvalidJwtException e) {
			throw new JWTConsumerException(e);
		} catch (MalformedClaimException e) {
			new JWTConsumerException(e);
		}
		return null;
	}

	@Override
	public String generateRefreshToken(String user, ArrayList<String> roles) {
		try {
			rsaJsonWebKey.setKeyId("k1");
			// Create the Claims, which will be the content of the JWT
			JwtClaims claims = new JwtClaims();
			claims.setIssuer("Issuer"); // who creates the token and signs it
			claims.setAudience("refreshToken"); // to whom the token is intended to
											// be sent
			claims.setExpirationTimeMinutesInTheFuture(10); // time when the
															// token will expire
															// (10 minutes from
															// now)
			claims.setGeneratedJwtId(); // a unique identifier for the token
			claims.setIssuedAtToNow(); // when the token was issued/created
										// (now)
			claims.setNotBeforeMinutesInThePast(2); // time before which the
													// token is not yet valid (2
													// minutes ago)
			claims.setSubject(user); // the subject/principal is whom the
											// token is about
			claims.setClaim("email", "mail@example.com"); // additional
															// claims/attributes
															// about the subject
															// can be added
			List<String> scopes = Arrays.asList("administrator", "provider");
			claims.setClaim("scopes", scopes);
			List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
			claims.setStringListClaim("groups", groups); // multi-valued claims
															// work too and will
															// end up as a JSON
															// array
			JsonWebSignature jws = new JsonWebSignature();
			jws.setPayload(claims.toJson());
			jws.setKey(rsaJsonWebKey.getPrivateKey());
			jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
			jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
			String jwt = jws.getCompactSerialization();
			System.out.println("JWT: " + jwt);
			return jwt;
		} catch (JoseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String generateAccessToken(String user, ArrayList<String> roles) {
		try {
			rsaJsonWebKey.setKeyId("k1");
			// Create the Claims, which will be the content of the JWT
			JwtClaims claims = new JwtClaims();
			claims.setIssuer("Issuer"); // who creates the token and signs it
			claims.setAudience("accessToken"); // to whom the token is intended to
											// be sent
			claims.setExpirationTimeMinutesInTheFuture(10); // time when the
															// token will expire
															// (10 minutes from
															// now)
			claims.setGeneratedJwtId(); // a unique identifier for the token
			claims.setIssuedAtToNow(); // when the token was issued/created
										// (now)
			claims.setNotBeforeMinutesInThePast(2); // time before which the
													// token is not yet valid (2
													// minutes ago)
			claims.setSubject(user); // the subject/principal is whom the
											// token is about
			claims.setClaim("email", "mail@example.com"); // additional
															// claims/attributes
															// about the subject
															// can be added
			List<String> scopes = Arrays.asList("administrator", "provider");
			claims.setClaim("scopes", scopes);
			List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
			claims.setStringListClaim("groups", groups); // multi-valued claims
															// work too and will
															// end up as a JSON
															// array
			JsonWebSignature jws = new JsonWebSignature();
			jws.setPayload(claims.toJson());
			jws.setKey(rsaJsonWebKey.getPrivateKey());
			jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
			jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
			String jwt = jws.getCompactSerialization();
			System.out.println("JWT: " + jwt);
			return jwt;
		} catch (JoseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
