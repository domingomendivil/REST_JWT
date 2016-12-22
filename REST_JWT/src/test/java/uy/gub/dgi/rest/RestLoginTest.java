package uy.gub.dgi.rest;

import static org.mockito.Mockito.when;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uy.gub.dgi.api.API;
import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.filters.AuthorizationFilter;
import uy.gub.dgi.jwt.JWTConsumer;
import uy.gub.dgi.jwt.JWTConsumerException;
import uy.gub.dgi.jwt.JWTData;
import uy.gub.dgi.seguridad.PasswordAuthenticator;

public class RestLoginTest extends JerseyTest{

	@Mock
	private PasswordAuthenticator authenticator;
	
	@Mock
	private JWTConsumer jwtConsumer;
	
	
	@Mock
	private DAO dao;
	
	@Mock
	private API api;
	
	
	private String REQUEST_PASSWORD_LOGIN ="security/passwordlogin";
	
	 

	private RestAPI restExample;
	
	@Override
	protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
	    return new InMemoryTestContainerFactory();
	}


	@Override
	protected Application configure() {
		MockitoAnnotations.initMocks(this);
		RestLogin restLogin = new RestLogin();
		AbstractBinder binder = new AbstractBinder() {
			@Override
			protected void configure() {
				set("jersey.test.containerFactor","com.sun.jersey.test.framework.spi.container.inmemory.InMemoryTestContainerFactory");
				bind(authenticator).to(PasswordAuthenticator.class);
				bind(jwtConsumer).to(JWTConsumer.class);
				bind(dao).to(DAO.class);
				bind(api).to(API.class);
			//	new InMemoryTestContainerFactory()
				
			}
		};

		/**ResourceConfig res = new ResourceConfig();
		res= res.register(restLogin);
		res= res.packages(true, "uy.gub.dgi.rest").register(RolesAllowedDynamicFeature.class);
		res = res.register(binder).register(restExample).register(AuthorizationFilter.class);**/
				
		//		
		ResourceConfig res = new ResourceConfig().register(binder).register(restLogin).register(AuthorizationFilter.class).packages("uy.gub.dgi.rest").register(RolesAllowedDynamicFeature.class);
		
		
		
		System.out.println("init "+res);
		return res;
	}

	// Si el usuario es nulo  que retorne un 401 - Unauthorized
	@Test
	public void testPasswordLogin() {
//		when(authenticator.validUser("","")).thenReturn(false);
		Response res = target(REQUEST_PASSWORD_LOGIN).request().get();
		Assert.assertEquals(401,res.getStatus());
	}
	
	
	@Test
	public void testPasswordLogin2() {
		when(authenticator.validUser("","")).thenReturn(false);
		String authorization="";
		Response res = target(REQUEST_PASSWORD_LOGIN).request().header("Authorization",authorization).get();
		Assert.assertEquals(401,res.getStatus());
	}
	
	// Si obtiene el profile de un usuario no autenticado debe retornar 401
	@Test
	public void testProfile1() {
		when(authenticator.validUser("","")).thenReturn(false);
		Response res = target("security/passwordlogin").request().get();
		Assert.assertEquals(401,res.getStatus());
	}
	
	// Si obtiene el profile de un usuario con token valido, debe retornar el profile y un status de 200
	@Test
	public void testProfile2() throws JWTConsumerException {
		String authorization="asdf";
		when(authenticator.validUser("","")).thenReturn(false);
		JWTData jwtData = new JWTData();
		when(jwtConsumer.validateJWT(authorization)).thenReturn(jwtData);
		Response res = target("rest/profile").request().header("Authorization",authorization).get();
		Assert.assertEquals(200,res.getStatus());
	}

}
