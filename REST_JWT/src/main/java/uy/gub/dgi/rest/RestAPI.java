package uy.gub.dgi.rest;

import java.security.Principal;
import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import uy.gub.dgi.api.API;
import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.dao.DAOException;
import uy.gub.dgi.domain.FlightBean;
import uy.gub.dgi.domain.OrderBean;
import uy.gub.dgi.domain.Profile;
import uy.gub.dgi.googleapi.GoogleApi;

/**
 * 
 * @author d0178 - Domingo Mendivil
 *
 */
@Path("/rest")
public class RestAPI {
	
	@Context
	private SecurityContext securityContext;
	
	@Inject
	private API api;
	
	
	@Inject
	private DAO dao;
	
	
	
	private GoogleApi googleApi;
	

	@GET
	@Path("/{user}/profile")
	@RolesAllowed({"Administrator","Supervisor"})
	public Profile getProfile(){
		Principal principal = securityContext.getUserPrincipal();
		System.out.println("USER PRINCIPAL "+principal);
		return api.getProfile(principal.getName());
	}


	@GET
	@Path("/orders")
	@RolesAllowed({"Administrator","Supervisor"})
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public ArrayList<OrderBean> getOrders() throws DAOException{
		System.out.println("Get Orders");
		return (ArrayList<OrderBean>)dao.getAll(OrderBean.class);
	}

	@GET
	@Path("/flights")
	@PermitAll
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public ArrayList<FlightBean> getFlights() throws DAOException{
		System.out.println("get Flights");
		ArrayList<FlightBean> lista = new ArrayList<>();
		FlightBean flight = new FlightBean();
		flight.setName("Hola");
		lista.add(flight);
		return lista;
	}

	
}
