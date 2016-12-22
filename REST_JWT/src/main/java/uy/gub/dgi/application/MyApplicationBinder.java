package uy.gub.dgi.application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import uy.gub.dgi.api.API;
import uy.gub.dgi.api.APIImpl;
import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.dao.DAOException;
import uy.gub.dgi.dao.DAOFactory;
import uy.gub.dgi.hashing.HasherImpl;
import uy.gub.dgi.jwt.JWTConsumer;
import uy.gub.dgi.jwt.JWTConsumerImpl;
import uy.gub.dgi.seguridad.PasswordAuthenticator;
import uy.gub.dgi.seguridad.PasswordAuthenticatorDAOImpl;

public class MyApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
    	System.out.println("******************");
    	System.out.println("CONFIGURING APPLICATION");
    	bind(JWTConsumerImpl.class).to(JWTConsumer.class);
        bind(PasswordAuthenticatorDAOImpl.class).to(PasswordAuthenticator.class);
        bind(HasherImpl.class).to(HasherImpl.class);
        DAO dao;
		try {
			dao = new DAOFactory("rest").getDAO();
	        bind(dao).to(DAO.class);
	        bind(APIImpl.class).to(API.class);			
		} catch (DAOException e) {
			e.printStackTrace();
		}

    }
}