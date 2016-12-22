package uy.gub.dgi.seguridad;

import javax.inject.Inject;

import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.dao.DAOException;
import uy.gub.dgi.domain.UserBean;
import uy.gub.dgi.hashing.HasherImpl;

/**
 * 
 * @author d0178
 *
 */
public class PasswordAuthenticatorDAOImpl implements PasswordAuthenticator{

	@Inject
	private DAO dao;
	
	@Inject
	private HasherImpl hasher;
	
	private final static String SALT = "%_$P2·";
	


	@Override
	public boolean validUser(String user, String password) {
		System.out.println("PasswordAuthenticatorImpl.validUser "+user +" "+password);
		try {
			UserBean userBean = (UserBean)dao.getById(user,UserBean.class);
			System.out.println("userBean "+user);
			if (userBean!=null){
				String hashed = hash(user,password);
				System.out.println("hashed password "+hashed);
				System.out.println("current user password "+userBean.getPassword());
				if (hash(user,password).equals(userBean.getPassword())){
					return true;
				}
			}
			return false;
		} catch (DAOException e) {
			throw new PasswordRuntimeException(e);
		}
	}


	private String hash(String user, String password) {
		return hasher.hash(user,password,SALT);
	}

}
