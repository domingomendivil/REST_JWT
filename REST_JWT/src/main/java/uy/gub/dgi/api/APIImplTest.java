package uy.gub.dgi.api;

import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;



import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.dao.DAOException;
import uy.gub.dgi.domain.Profile;
import uy.gub.dgi.domain.UserBean;

@RunWith(MockitoJUnitRunner.class)
public class APIImplTest {
	
	
	@Mock
	private DAO dao;
	
	@InjectMocks
	private APIImpl api;
	
	
	@Test
	public void testGetProfile1() throws DAOException{
		String user = "d018";
		UserBean userBean = new UserBean();
		userBean.setPassword("12");
		when(dao.getById(user, UserBean.class)).thenReturn(userBean);
		Profile profile = api.getProfile(user);
		assertEquals(userBean.getEmail(),profile.getEmail());
	}
	
	@Test(expected=APIException.class)
	public void testGetProfile2() throws DAOException{
		String user = "d018";
		UserBean userBean = new UserBean();
		userBean.setPassword("12");
		when(dao.getById(user, UserBean.class)).thenThrow(DAOException.class);
		Profile profile = api.getProfile(user);
		assertEquals(userBean.getEmail(),profile.getEmail());
	}

}
