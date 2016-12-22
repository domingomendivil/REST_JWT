package uy.gub.dgi.rest;

import static org.junit.Assert.assertEquals;

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
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uy.gub.dgi.application.MyApplicationBinder;
import uy.gub.dgi.filters.AuthorizationFilter;
import uy.gub.dgi.hashing.HasherImpl;
import uy.gub.dgi.jwt.JWTConsumerImpl;

public class RestIT {

	static final String BASE_URI = "http://localhost:8080/REST_JWT/";
	static final String BASE_URI_PROXY = "http://localhost:8081/REST_JWT/";
	private static final String FLIGHTS_NO_RESULTS = "?from=2005-02-02&to=2005-02-01&origin=STA&destine=BSA";
	private static final String FLIGHTS_WITH_RESULTS = "?from=2005-02-02&to=2005-02-04&origin=MVD&destine=AUK";
	
	static HttpServer server;
	static Client client;
	private WebTarget target = client.target(BASE_URI_PROXY);

	static String REQUEST_PASSWORD_LOGIN = "security/passwordlogin";
	static String REQUEST_FLIGHTS = "rest/flights";
	static String REQUEST_ORDERS = "rest/orders";
	static String REQUEST_PROFILE ="rest/{user}/profile";
	static String REQUEST_ACCES_TOKEN = "security/accestoken";
	static String REQUEST_GOOGLE_VALIDATE_REFRESHTOKEN = "security/google/refreshtoken/validate";
	static String REQUEST_GOOGLE_LOGIN = "security/google/login";
	

	static String AUTHORIZATION_HEADER = "Authorization";
	static String BASIC_HEADER = "Basic ";
	static String BEARER_HEADER = "Bearer ";

	static String USER_WITHOUT_ORDERS = "Karina";
	static String EXISTING_USER = "domingo";
	static String EXISTING_USER_PASSWORD = "pep1to";
	static String NOT_EXISTING_USER = "Pepe";
	
	private HasherImpl hasher = new HasherImpl();

	private static HttpServer startServer() {
		final ResourceConfig resourceConfig = new ResourceConfig().packages("uy.gub.dgi.rest")
				.register(RolesAllowedDynamicFeature.class).register(new MyApplicationBinder()).register(InvalidUserException.class)
		.register(new DynamicFeature() {
	        @Override
	        public void configure(ResourceInfo resourceInfo, FeatureContext context) {
	        	if (RestAPI.class.equals(resourceInfo.getResourceClass())){
	        		System.out.println("*********REST API --------------");
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

	@AfterClass
	public static void tearDownClass() {
		deleteTestingData();
		server.shutdown();
	}

	private static void deleteTestingData() {
		// TODO Auto-generated method stub
		
	}

	private Response getFlights() {
		Response response = target.path(REQUEST_FLIGHTS).request().get();
		return response;
	}

	private Response getOrders(String authorization, String value) {
		if (authorization == null) {
			return target.path(REQUEST_ORDERS).request().get();
		} else {
			return target.path(REQUEST_ORDERS).request().header(authorization, value).get();
		}
	}

	private Response passwordLogin(String authorization, String value) {
		if (authorization == null) {
			return target.path(REQUEST_PASSWORD_LOGIN).request().get();
		} else {
			return target.path(REQUEST_PASSWORD_LOGIN).request().header(authorization, value).get();
		}
	}

	// Testing Get Flights without Authorization header,must return 200 OK, and
	// must return valid flights
	@Test
	public void testGetFlights() {
		Response response = getFlights();
		int res = response.getStatus();
		assertEquals(200, res);
		String content = "[{\"name\":\"Hola\"}]";
		assertEquals(content, response.readEntity(String.class));
		response.close();
	}

	// Testing Get Flights with Authorization header, must return 200 OK, and
	// must return valid flights
	@Test
	public void testGetFlights2() {
		assertGetFlights(200);	
	}

	// Testing Get Flights from another origin
	@Test
	public void testGetFlights3() {
		assertGetFlights(200);
	}

	// Testing Get Flights from a query without flights, must return 404, and no
	// content
	@Test
	public void testGetFlights4() {
		assertGetFlights(404);	
	}

	// Testing Get Orders without authorization header, must return 401
	@Test
	public void testGetOrders() {
		assertGetOrders(null,null,403);
	}

	// Testing Get Orders with authorization header, bad authorization header
	@Test
	public void testGetOrders1() {
		assertGetOrders(AUTHORIZATION_HEADER,"ASD",403);
	}

	// Testing Get Orders with authorization header, basic authorization header,
	// must return 403
	@Test
	public void testGetOrders2() {
		assertGetOrders(AUTHORIZATION_HEADER,BASIC_HEADER + "123123",403);
	}

	// Testing Get Orders with authorization header, authorization header is
	// bearer but wrong JSON Access Token, must return 403
	@Test
	public void testGetOrders3() {
		assertGetOrders(AUTHORIZATION_HEADER,"Bearer 234234",403);
	}

	// Testing Get Orders with authorization header, authorization header is
	// bearer but JSON Access Token has expired, must return 403
	@Test
	public void testGetOrders4() {
		String jwt = getJwt();
		assertGetOrders(AUTHORIZATION_HEADER,"Bearer " + jwt,403);
	}

	// Testing Get Orders with authorization header, authorization header is
	// bearer but JSON token is incorrect format, must return 403
	@Test
	public void testGetOrders5() {
      //assertGetOrders(authorization, value, assertion);
	}

	// Test Get Orders with valid authorization header, for a user without
	// orders, must return 404
	@Test
	public void testGetOrders6() {
		String user = USER_WITHOUT_ORDERS;
		String refreshToken  =new JWTConsumerImpl().generateAccessToken(USER_WITHOUT_ORDERS, null);
		String jwt = getAccessTokenJWT(user, refreshToken);
		assertGetOrders(AUTHORIZATION_HEADER, "Bearer " + jwt, 404);
	}

	// Test Get Orders with valid authorization header, for a user with orders,
	// must return 200 and the orders
	@Test
	public void testGetOrders7() {
		String user = USER_WITHOUT_ORDERS;
		String refreshToken = getRefreshToken(user);
		String jwt = getAccessTokenJWT(user, refreshToken);
		Response response = getOrders(AUTHORIZATION_HEADER, "Bearer " + jwt);
		int res = response.getStatus();
		assertEquals(200, res);
		String content = "[{nro:123,date:\"12/02/2016\"}]";
		assertEquals(content, response.readEntity(String.class));
		response.close();
	}
	
	
	//Test Get orders with a valid Google Access Token from a user with orders, must return 200 OK, with the valid orders 
	@Test
	public void testGetOrders8() {
		String accessToken = getGoogleAccessToken(); 
		assertGetOrders(AUTHORIZATION_HEADER,"Google " + accessToken,200);
	}
	
	private String getGoogleAccessToken() {
		// TODO Auto-generated method stub
		return null;
	}

	//Test Get orders with an invalid Google Access token , must return 403 
	@Test
	public void testGetOrders9() {
		assertGetOrders(AUTHORIZATION_HEADER,"Google XX",403);
	}
	
	//Test Get Orders with an expired Google Access token, must return 403 
	@Test
	public void testGetOrders10() {
		assertGetOrders(AUTHORIZATION_HEADER,"Google -",403);
	}

	//Test Get Orders with a Google Refresh token, must return 403 
	@Test
	public void testGetOrders11() {
		String refreshToken = getGoogleRefreshToken();
		assertGetOrders(AUTHORIZATION_HEADER,"Google "+refreshToken,403);
	}
	
	
	


	private String getGoogleRefreshToken() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAccessTokenJWT(String user, String refreshToken) {
		return new JWTConsumerImpl().generateAccessToken(user,null);
	}

	private String getRefreshToken(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getJwt() {
		String user = EXISTING_USER;
		String password = EXISTING_USER_PASSWORD;
		String encodedUserPassword = encodeUserPassword(user,password);
		Response response = passwordLogin(AUTHORIZATION_HEADER, BASIC_HEADER + encodedUserPassword);
		int res = response.getStatus();
		String entity = (String)response.readEntity(String.class);
		return entity;
	}
	
	private String encodeUserPassword(String user, String password) {
		byte[] bytes = Base64.getEncoder().encode((user+":"+password).getBytes());
		String res = new String(bytes);
		System.out.println("encoded userpassword "+res);
		return res;
	}

	
	//Get Access token, without a refresh token, must return 401
	@Test
	public void testGetAccessToken(){
		assertAccessToken(null,null,401);
	}

	private Response getAccessToken(String authorization,String value) {
		if (authorization==null){
			return target.path(REQUEST_ACCES_TOKEN).request().get();
		}else{
			return target.path(REQUEST_ACCES_TOKEN).request().header(authorization, value).get();	
		}
		
	}
	
	private void assertGetOrders(String authorization,String value,int assertion){
		Response response = getOrders(authorization,value);
		int res = response.getStatus();
		assertEquals(assertion,res);
		response.close();
	}
	
	private void assertPasswordLogin(String authorization,String value,int assertion){
		Response response = passwordLogin(authorization,value);
		int res = response.getStatus();
		assertEquals(assertion,res);
		response.close();
	}
	
	private void assertGetFlights(int assertion){
		Response response = getFlights();
		int res = response.getStatus();
		assertEquals(assertion,res);
		response.close();
	}


	
	//Get Access token, without an invalid refresh token
	@Test
	public void testGetAccessToken1(){
		assertAccessToken("","",401);
	}

	private void assertAccessToken(String authorization, String token, int i) {
		Response response= getAccessToken(authorization,token);
		assertEquals(i,response.getStatus());
		
	}
	

	//Test Access Token, with an invalid authorization header
	@Test
	public void testGetAccessToken2(){
		assertAccessToken(AUTHORIZATION_HEADER,"Basic 123123",401);
	}
	
	
	//Get Access token, without an invalid refresh token
	@Test
	public void testGetAccessToken3(){
		assertAccessToken(AUTHORIZATION_HEADER,"Bearer 123123",401);
	}

	//Get Access token with an invalid refresh token 
	@Test
	public void testGetAccessToken4(){
		String refreshToken = getRefreshToken("");
		assertAccessToken(AUTHORIZATION_HEADER,"Bearer "+refreshToken,401);
	}

	//Get Access token, with an expired refresh token, must return 401
	@Test
	public void testGetAccessToken5(){
		String refreshToken = "eyJraWQiOiJrMSIsImFsZyI6IlJTMjU2In0.eyJpc3MiOiJJc3N1ZXIiLCJhdWQiOiJyZWZyZXNoVG9rZW4iLCJleHAiOjE0ODE2NTU1NTQsImp0aSI6IjJ0YXRTMVZUa0NkTng0elNPeENMN0EiLCJpYXQiOjE0ODE2NTQ5NTQsIm5iZiI6MTQ4MTY1NDgzNCwic3ViIjoicGVwZSIsImVtYWlsIjoibWFpbEBleGFtcGxlLmNvbSIsInNjb3BlcyI6WyJhZG1pbmlzdHJhdG9yIiwicHJvdmlkZXIiXSwiZ3JvdXBzIjpbImdyb3VwLW9uZSIsIm90aGVyLWdyb3VwIiwiZ3JvdXAtdGhyZWUiXX0.DwQZOd0ahkvvc5LHZ3D4BKzOo1eyNUeGaOImmHIKs1ytLHo0SUJd-9XQydyYnBc7C7AZUGb_aMmD6rduDytYdClrlRfgONcROzK1uTnxOJHtfBMo3mnOdGe6ubwiF-kX-97U0uMsz2aj9tSXr3sxv0MLZqmq3SC_sfMEzqRYk2Q-nSn_yBQspW9-hEM5DYydR7MUfvjbray4Qr0UpOn8Xl8wZ_yVGHm8vhATH4QPdmKyBxfsMW75G7IQ0a6gC2gd6x_4OjoVLEbAEw0yyBlNmO-J3Xiu11UW1Gdibu49rbthoTZFj4re_Ks19XoaL4WqDWadHpd92e7Z17T6lJJ_3A";
		assertAccessToken(AUTHORIZATION_HEADER,"Bearer "+refreshToken,401);
	}
	
	//Get Access token, with an access Token, must return 401
	@Test
	public void testGetAccessToken6(){
		String accessToken = getAccessToken("domingo");
		assertAccessToken(AUTHORIZATION_HEADER,"Bearer "+accessToken,401);
	}

	private String getAccessToken(String string) {
		return new JWTConsumerImpl().generateAccessToken(string, null);
	}	
	

	//Get Profile without security headers, must return 401
	@Test
	public void testProfile1() {
		Response res = target.path("rest/"+EXISTING_USER+"/profile").request().get();
		Assert.assertEquals(403,res.getStatus());
	}
	
	//Get Profile with security headers that are wrong, must return 401
	
	@Test
	public void testProfile2() {
		Response res = target.path("rest/"+EXISTING_USER+"/profile").request().header("","").get();
		Assert.assertEquals(403,res.getStatus());
	}

	//Get Profile with security headers that are wrong, must return 401
	@Test
	public void testProfile3() {
		Response res = target.path("rest/"+EXISTING_USER+"/profile").request().header(AUTHORIZATION_HEADER,"").get();
		Assert.assertEquals(403,res.getStatus());
	}

	//Get Profile with security headers that are wrong, must return 401
	@Test
	public void testProfile4() {
		Response res = target.path("rest/"+EXISTING_USER+"/profile").request().header(AUTHORIZATION_HEADER,"Bearer").get();
		Assert.assertEquals(403,res.getStatus());
	}


	//Get Profile with valid security headers, but from a different user, must return 401
	@Test
	public void testProfile5() {
		String jwt = getJwt();
		Response res = target.path("rest/"+"Karina"+"/profile").request().header(AUTHORIZATION_HEADER,"Bearer "+jwt).get();
		Assert.assertEquals(403,res.getStatus());
	}

	

	
	//Get Profile with valid security headers, for the same user, must return 200, and a JSON token profile
	@Test
	public void testProfile6() {
		String jwt = getJwt();
		Response res = target.path("rest/"+EXISTING_USER+"/profile").request().header(AUTHORIZATION_HEADER,"Bearer "+jwt).get();
		Assert.assertEquals(403,res.getStatus());
	}
	

	
	
	
	
	
	







	
	
	

}
