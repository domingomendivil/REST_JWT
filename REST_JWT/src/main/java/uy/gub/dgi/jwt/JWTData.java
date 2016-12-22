package uy.gub.dgi.jwt;

import java.util.List;

public class JWTData {
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	private String user;
	private List<String> roles;

}
