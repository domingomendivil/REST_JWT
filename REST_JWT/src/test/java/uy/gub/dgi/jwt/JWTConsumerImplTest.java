package uy.gub.dgi.jwt;

import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JWTConsumerImplTest {
	
	@InjectMocks
	private JWTConsumerImpl jwtConsumer;
	
	@Test(expected=JWTConsumerException.class)
	public void testValidateToken1() throws JWTConsumerException{
		jwtConsumer.validateJWT("");
	}
	
	@Test(expected=JWTConsumerException.class)
	public void testValidateToken2() throws JWTConsumerException{
		jwtConsumer.validateJWT(null);
	}
	
	
	//JWT válido pero vencido, debe dar excepción
	@Test(expected=JWTConsumerException.class)
	public void testValidateToken3() throws JWTConsumerException{
		String jwt="";
		jwtConsumer.validateJWT("Authorization "+jwt);
	}
	
	//JWT válido y no vencido
	public void testValidateToken4() throws JWTConsumerException{
		String jwt="";
		jwtConsumer.validateJWT("Authorization "+jwt);
	}
	
	//JWT inválido, debe dar una excepción
	
	
	
	//JWT válido, debe emitir un token para el usuario que se le pasa, y con el scope igual a los roles que se le pasa
	
	//
	
	@Test
	public void testGenerateRefreshToken() throws JWTConsumerException{
		ArrayList<String> roles = new ArrayList<String>();
		roles.add("Administrator");
		String jwt = jwtConsumer.generateRefreshToken("pepe", roles);
		System.out.println("JWT "+jwt);
		JWTData jwtData = jwtConsumer.validateJWT(jwt);
		assertEquals("pepe",jwtData.getUser());
	}
	
	
	@Test
	public void testGenerateAccessToken() throws JWTConsumerException{
		ArrayList<String> roles = new ArrayList<String>();
		roles.add("Administrator");
		String jwt = jwtConsumer.generateAccessToken("pepe", roles);
		JWTData jwtData = jwtConsumer.validateJWT(jwt);
		assertEquals("pepe",jwtData.getUser());
	}

	
	
	
	
	


}
