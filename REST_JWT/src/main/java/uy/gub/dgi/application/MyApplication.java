package uy.gub.dgi.application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import uy.gub.dgi.rest.InvalidUserException;

public class MyApplication extends ResourceConfig {
	public MyApplication() {
        register(new MyApplicationBinder());
        packages(true, "uy.gub.dgi.rest").register(RolesAllowedDynamicFeature.class);
        register(InvalidUserException.class);
       // register(new InvalidUserException(msg))
    }
}
