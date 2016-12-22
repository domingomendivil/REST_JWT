package uy.gub.dgi.domain;

import java.util.ArrayList;

public class UserBean {
	private String userName;
	private String password;
	private String email;
	
	private ArrayList<SocialProfile> socialProfiles;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<String> getRoles() {
		return roles;
	}
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	private ArrayList<String> roles;
	

}
