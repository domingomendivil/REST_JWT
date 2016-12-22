package uy.gub.dgi.filters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uy.gub.dgi.jwt.JWTConsumer;
import uy.gub.dgi.jwt.JWTConsumerException;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationFilterTest {
	
	
	@Mock
	private JWTConsumer jwtConsumer;
	
	@InjectMocks
	private AuthorizationFilter filter;
	
	//Si los headers no tienen datos, debe dejar el securitycontext vacío
	@Test
	public void test() throws IOException{
		ContainerRequestContext req= mock(ContainerRequestContext.class);
		MultivaluedMap<String, String> header = mock(MultivaluedMap.class);
		when(header.get(0)).thenReturn(null);
		when(req.getHeaders()).thenReturn(header);
		filter.filter(req);
	}
	

	//Si los headers no tienen datos, debe dejar el securitycontext vacío
	@Test
	public void test2() throws IOException{
		ContainerRequestContext req= mock(ContainerRequestContext.class);
		when(req.getHeaders()).thenReturn(null);
		filter.filter(req);
	}

	
	//Si los headers retornan datos, y el token es válido deja los datos en el securityContext
	@Test
	public void test3() throws IOException{
		ContainerRequestContext req= mock(ContainerRequestContext.class);
		MultivaluedMap<String, String> header = mock(MultivaluedMap.class);
		List<String> lista = new ArrayList<String>();
		lista.add("AR");
		when(header.get(0)).thenReturn(lista);
		when(req.getHeaders()).thenReturn(header);
		filter.filter(req);
		
	}
	
	
	//Si los headers retornan datos, y el token es inválido, debe dejar el securitycontext vacío
	@Test
	public void test4() throws IOException, JWTConsumerException{
		ContainerRequestContext req= mock(ContainerRequestContext.class);
		MultivaluedMap<String, String> header = mock(MultivaluedMap.class);
		List<String> lista = new ArrayList<String>();
		String authorization="AR";
		lista.add(authorization);
		when(jwtConsumer.validateJWT(authorization)).thenThrow(JWTConsumerException.class);
		when(header.get(0)).thenReturn(lista);
		when(req.getHeaders()).thenReturn(header);
		filter.filter(req);
	}

	

}
