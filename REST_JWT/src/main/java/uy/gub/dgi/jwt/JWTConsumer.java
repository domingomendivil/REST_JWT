package uy.gub.dgi.jwt;

import java.util.ArrayList;

/**
 * 
 * @author d0178 - Domingo Mendivil
 *
 */
public interface JWTConsumer {
	
	/**
	 * 
	 * @param jwt
	 * @return
	 * @throws JWTConsumerException
	 */
	public JWTData validateJWT(String jwt) throws JWTConsumerException;

	/**
	 * 
	 * @param user
	 * @param roles
	 * @return
	 */
	public String generateRefreshToken(String user,ArrayList<String> roles);
	
	
	/**
	 * 
	 * @param user
	 * @param roles
	 * @return
	 */
	public String generateAccessToken(String user,ArrayList<String> roles);
	
	
	

}
