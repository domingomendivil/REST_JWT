package uy.gub.dgi.rest;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Base64;

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
import org.junit.BeforeClass;
import org.junit.Test;

import uy.gub.dgi.application.MyApplicationBinder;
import uy.gub.dgi.filters.AuthorizationFilter;

public class RestPasswordLoginIT {
	
	

	static final String BASE_URI = "http://localhost:8080/REST_JWT/";
	static final String BASE_URI_PROXY = "http://localhost:8081/REST_JWT/";
	
	static String REQUEST_PASSWORD_LOGIN = "security/passwordlogin";
	
	static String AUTHORIZATION_HEADER = "Authorization";
	static String BASIC_HEADER = "Basic ";
	static String BEARER_HEADER = "Bearer ";

	static String USER_WITHOUT_ORDERS = "Karina";
	static String EXISTING_USER = "domingo";
	static String EXISTING_USER_PASSWORD = "pep1to";
	static String NOT_EXISTING_USER = "Pepe";
	
	static HttpServer server;
	static Client client;
	private WebTarget target = client.target(BASE_URI_PROXY);
	
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

	@BeforeClass
	public static void setUpClass() {
		loadTestingdata();
		server = startServer();
		System.out.println("server : " + server);
		client = ClientBuilder.newClient();
	}

	
	private static void loadTestingdata() {
		// TODO Auto-generated method stub
		
	}

	private Response passwordLogin(String authorization, String value) {
		if (authorization == null) {
			return target.path(REQUEST_PASSWORD_LOGIN).request().get();
		} else {
			return target.path(REQUEST_PASSWORD_LOGIN).request().header(authorization, value).get();
		}
	}
	
	@Test
	public void testPasswordLogin() {
		Response response = passwordLogin(null, null);
		int res = response.getStatus();
		assertEquals(401, res);
		response.close();
	}

	// Testing Password Login, with wrong password, must return 401 error
	@Test
	public void testPasswordLogin2() {
		assertPasswordLogin(AUTHORIZATION_HEADER,"",401);
	}

	// Testing Password Login, with wrong password, must return 401 error
	@Test
	public void testPasswordLogin3() {
		assertPasswordLogin(AUTHORIZATION_HEADER, BASIC_HEADER,401);
	}

	// Testing Password Login, with wrong password, must return 401 error
	@Test
	public void testPasswordLogin4() {
		assertPasswordLogin(AUTHORIZATION_HEADER, BASIC_HEADER + "2",401);
	}

	// Testing Password Login, user exists, but with wrong password, must return
	// 401 error
	@Test
	public void testPasswordLogin5() {
		assertPasswordLogin(AUTHORIZATION_HEADER,"asd 2123123",401);
	}

	// Testing Password Login, user exists and correct password, must return 200
	// OK, and the JWT token
	@Test
	public void testPasswordLogin6() {
		String user = EXISTING_USER;
		String password = EXISTING_USER_PASSWORD;
		String encodedUserPassword = encodeUserPassword(user,password);
		System.out.println("test passwordlogin6");
		Response response = passwordLogin(AUTHORIZATION_HEADER, BASIC_HEADER + encodedUserPassword);
		int res = response.getStatus();
		String entity = (String)response.readEntity(String.class);
		assertNotNull(entity);
		assertEquals(200, res);
		response.close();
	}

	private String encodeUserPassword(String user, String password) {
		byte[] bytes = Base64.getEncoder().encode((user+":"+password).getBytes());
		String res = new String(bytes);
		System.out.println("encoded userpassword "+res);
		return res;
	}


	// Testing Password Login, user does not exist, must return 401 OK
	@Test
	public void testPasswordLogin7() {
		String encodedUserPassword = "asdfasdf";		
		assertPasswordLogin(AUTHORIZATION_HEADER,BASIC_HEADER + encodedUserPassword,401);
	}
	
	private void assertPasswordLogin(String authorization,String value,int assertion){
		Response response = passwordLogin(authorization,value);
		int res = response.getStatus();
		assertEquals(assertion,res);
		response.close();
	}

}
