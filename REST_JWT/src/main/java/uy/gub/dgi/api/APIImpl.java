package uy.gub.dgi.api;

import javax.inject.Inject;

import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.dao.DAOException;
import uy.gub.dgi.domain.Profile;
import uy.gub.dgi.domain.UserBean;

public class APIImpl implements API{
	
	
	@Inject
	private DAO dao;
	
	

	@Override
	public Profile getProfile(String userName) {
		Profile profile = new Profile();
		UserBean userBean;
		try {
			userBean = (UserBean)dao.getById(userName,UserBean.class);
			profile.setEmail(userBean.getEmail());
			return profile;
		} catch (DAOException e) {
			throw new APIException(e);
		}
		
	}

}
