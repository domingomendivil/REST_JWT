package uy.gub.dgi.dao;

import uy.gub.dgi.domain.UserBean;
import uy.gub.dgi.domain.UserDAO;

public class UserDAOTest {
	
	public void test1() throws DAOException{
		UserBean user = new UserBean();
		String userName= "21900090011";
		UserBean userBean = (UserBean)new UserDAO().getById(userName);
	}

}
