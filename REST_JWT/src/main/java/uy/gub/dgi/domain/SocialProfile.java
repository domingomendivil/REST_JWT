package uy.gub.dgi.domain;

import java.util.Map;

public class SocialProfile {
	private String type;
	private Map<String,String> properties;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	

}
