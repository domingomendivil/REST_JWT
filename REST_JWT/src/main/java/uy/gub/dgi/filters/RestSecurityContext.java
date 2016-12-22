package uy.gub.dgi.filters;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import uy.gub.dgi.seguridad.UserPrincipal;

public class RestSecurityContext implements SecurityContext {

	@Override
	public boolean isUserInRole(String arg0) {
		if (arg0.equals("Supervisor")){
			return true;
		}else if (arg0.equals("Administrator")){
			return false;	
		}else{
			return false;
		}
	}
	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		return new UserPrincipal("pepe");
		
	}

	@Override
	public String getAuthenticationScheme() {
		return "userPassword";
	}

}
