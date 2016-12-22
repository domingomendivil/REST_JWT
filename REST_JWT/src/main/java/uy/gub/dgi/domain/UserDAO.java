package uy.gub.dgi.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uy.gub.dgi.dao.AbstractDAO;
import uy.gub.dgi.dao.DAOException;

public class UserDAO extends AbstractDAO{

	@Override
	public Object getById(Object id) throws DAOException {
		System.out.println("UserDAO.getById");
		if (id instanceof String){
			Connection connection = getConexion();
			try {
				PreparedStatement ps =connection.prepareStatement("SELECT * FROM USERS as u,USER_ROLES as ur,ROLEs as r where u.username=ur.username and ur.rolename=r.rolename and u.username=?");
				ps.setString(1,(String)id);
				ResultSet rs = ps.executeQuery();
				ArrayList<String> roles = new ArrayList<String>();
				String password="";
				if (rs.next()){
					System.out.println("tiene resultado ");
					boolean rolesExist=true;
					password = rs.getString("password");
					while (rolesExist){
						roles.add(rs.getString("ur.rolename"));
						rolesExist=rs.next();
					}
					UserBean userBean = new UserBean();
					userBean.setUserName((String)id);
					userBean.setRoles(roles);
					userBean.setPassword(password);
					System.out.println("user Bean rol "+userBean.getRoles().get(0));
					return userBean;
				}else{
					return null;
				}
			} catch (SQLException e) {
				throw new DAOException(e);
			}
		}else{
			return null;
		}
	}
	
	

}
