package uy.gub.dgi.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uy.gub.dgi.googleapi.GoogleApi;

/**
 * 
 * @author d0178 - Domingo Mendivil
 *
 */
public class RestGoogle {
	
	private GoogleApi googleApi;
	
	private String googleLoginURL;
	
	@GET
	@Path("/security/google/login_page_url")
	@Consumes(MediaType.APPLICATION_JSON)
	public String googleLogin() throws InvalidUserException{
		return googleApi.getLoginPage();
	}
	

	@GET
	@Path("/google/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response googleValidate(@HeaderParam("Authorization")String authorization) throws InvalidUserException{
		//TODO
		//Add Google Data to our database
		return null;
	}
	

	
}
