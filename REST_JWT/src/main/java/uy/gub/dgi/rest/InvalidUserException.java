package uy.gub.dgi.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidUserException extends Exception implements ExceptionMapper<InvalidUserException>{

	public InvalidUserException(){
		super();
	}
	
	public InvalidUserException(Throwable e) {
		super(e);
	}

	public InvalidUserException(String msg) {
		super(msg);
		System.out.println(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4795106060824359997L;

	@Override
	public Response toResponse(InvalidUserException arg0) {
		return Response.status(401).build();
	}
}
