package uy.gub.dgi.rest;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import uy.gub.dgi.application.MyApplicationBinder;
import uy.gub.dgi.filters.AuthorizationFilter;


public class RestGoogleIT {
	
	static String AUTHORIZATION_HEADER = "Authorization";
	static String BASIC_HEADER = "Basic ";
	static String BEARER_HEADER = "Bearer ";

	
	static final String BASE_URI = "http://localhost:8080/REST_JWT/";
	static final String BASE_URI_PROXY = "http://localhost:8081/REST_JWT/";
	
	
	static String USER_WITHOUT_ORDERS = "Karina";
	static String EXISTING_USER = "domingo";
	static String EXISTING_USER_PASSWORD = "pep1to";
	static String NOT_EXISTING_USER = "Pepe";
	static Client client;
	static HttpServer server;

	static String REQUEST_GOOGLE_LOGIN = "security/google/login_page_url";
	static String REQUEST_GOOGLE_ACCESS_TOKEN = "/security/google/access_token";
	
	private WebTarget target = client.target(BASE_URI_PROXY);
	

	@BeforeClass
	public static void setUpClass() {
		loadTestingdata();
		server = startServer();
		System.out.println("server : " + server);
		client = ClientBuilder.newClient();
	}
	
	private static HttpServer startServer() {
		final ResourceConfig resourceConfig = new ResourceConfig().packages("uy.gub.dgi.rest")
				.register(RolesAllowedDynamicFeature.class).register(new MyApplicationBinder()).register(InvalidUserException.class)
		.register(new DynamicFeature() {
	        @Override
	        public void configure(ResourceInfo resourceInfo, FeatureContext context) {
	        	if (RestAPI.class.equals(resourceInfo.getResourceClass())){	                     
	                context.register(AuthorizationFilter.class);
	            }
	        }
	    });
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
	}

	
	private static void loadTestingdata() {
		// TODO Auto-generated method stub
		
	}


	@AfterClass
	public static void tearDownClass() {
		deleteTestingData();
		server.shutdown();
	}


	private static void deleteTestingData() {
		// TODO Auto-generated method stub
		
	}

	private String getGoogleAccessToken() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Tests that google login, returns the redirect URL page
	@Test
	public void testGoogleLogin(){
		String refreshToken = getGoogleAccessToken();
		assertGoogleLogin(AUTHORIZATION_HEADER,"Bearer "+refreshToken,200);
	}
	
	
	@Test
	public void testGoogleLogin1(){
		String refreshToken = "SDSDS";
		assertGoogleLogin(AUTHORIZATION_HEADER,"Bearer "+refreshToken,401);
	}
	
	
	@Test
	public void testGoogleLogin2(){
		assertGoogleLogin(AUTHORIZATION_HEADER,"",401);
	}
	
	@Test
	public void testGoogleLogin3(){
		String refreshToken = "SDSDS";
		assertGoogleLogin("","",401);
	}
	
	private void assertGoogleLogin(String authorization, String value, int i) {
		Response response= getGoogleLogin(authorization,value);
		assertEquals(i,response.getStatus());
		
	}


	private Response getGoogleLogin(String authorization, String value) {
		if (authorization==null){
			return target.path(REQUEST_GOOGLE_LOGIN).request().get();
		}else{
			return target.path(REQUEST_GOOGLE_LOGIN).request().header(authorization, value).get();	
		}
		
	}

}
