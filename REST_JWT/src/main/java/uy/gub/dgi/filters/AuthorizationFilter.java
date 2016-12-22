package uy.gub.dgi.filters;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import uy.gub.dgi.jwt.JWTConsumer;
import uy.gub.dgi.jwt.JWTConsumerException;

@Provider
@PreMatching
public class AuthorizationFilter implements ContainerRequestFilter {

	@Inject
	private JWTConsumer jwtConsumer;

	@Override
	public void filter(ContainerRequestContext arg0) throws IOException {
		System.out.println("filtering request");
		if (arg0.getHeaders()!=null){
			System.out.println("filter:headers not null");
			List<String> auth = arg0.getHeaders().get("Authorization");
			System.out.println("filter:Authorization header not null");
			if (auth != null) {
				try {
					jwtConsumer.validateJWT(auth.get(0));
					SecurityContext sc = new RestSecurityContext();
					arg0.setSecurityContext(sc);
				} catch (JWTConsumerException e) {
					ResponseBuilder builder = null;
			        String response = "Unauthorized access";
					builder = Response.status(Response.Status.UNAUTHORIZED).entity(response);
			        throw new WebApplicationException(builder.build());
				}
			}
		}else{
			System.out.println("filter:headers null");
		}
	}
}
