package uy.gub.dgi.seguridad;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.dao.DAOException;
import uy.gub.dgi.domain.UserBean;
import uy.gub.dgi.hashing.HasherImpl;


@RunWith(MockitoJUnitRunner.class)
public class PasswordAuthenticatorDAOImplTest {

	
	@Mock
	private DAO dao;
	
	@Mock
	private HasherImpl hasher;
	
	
	
	@InjectMocks
	private PasswordAuthenticatorDAOImpl auth;
	
	private final static String SALT = "%_$P2·";
	
	//when the user exists but the hash password is not the same it must return false
	@Test
	public void test1() throws DAOException{
		String userName="25834109";
		String password ="";
		UserBean userBean = new UserBean();
		userBean.setUserName(userName);
		userBean.setPassword(password);
		when(dao.getById(userName,UserBean.class)).thenReturn(userBean);
		when(hasher.hash(userName, password,SALT)).thenReturn("another");
		boolean res = auth.validUser(userName,password);
		assertFalse(res);
	}
	
	
	//when the user does not exist it must return false
	@Test
	public void test2() throws DAOException{
		String userName="25834109";
		String password ="";
		UserBean userBean = new UserBean();
		userBean.setUserName(userName);
		userBean.setPassword(password);
		when(dao.getById(userName,UserBean.class)).thenReturn(null);
		boolean res = auth.validUser(userName,password);
		assertFalse(res);
	}
	
	
	//if there is an error accessing the database it must return a PasswordRuntimeException
	@Test(expected=PasswordRuntimeException.class)
	public void test3() throws DAOException{
		String userName="25834109";
		String password ="";
		UserBean userBean = new UserBean();
		userBean.setUserName(userName);
		userBean.setPassword(password);
		when(dao.getById(userName,UserBean.class)).thenThrow(DAOException.class);
		auth.validUser(userName,password);
	}
	
	//when the user exists but the hash password is the same it must return true
	@Test
	public void test4() throws DAOException{
		String userName="25834109";
		String password ="";
		UserBean userBean = new UserBean();
		userBean.setUserName(userName);
		userBean.setPassword(password);
		when(dao.getById(userName,UserBean.class)).thenReturn(userBean);
		when(hasher.hash(userName, password,SALT)).thenReturn(password);
		boolean res = auth.validUser(userName,password);
		assertTrue(res);
	}
	
	
	
	

}
