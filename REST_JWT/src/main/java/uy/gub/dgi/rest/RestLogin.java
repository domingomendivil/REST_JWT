package uy.gub.dgi.rest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uy.gub.dgi.dao.DAO;
import uy.gub.dgi.dao.DAOException;
import uy.gub.dgi.domain.User;
import uy.gub.dgi.domain.UserBean;
import uy.gub.dgi.jwt.JWTConsumer;
import uy.gub.dgi.jwt.JWTConsumerException;
import uy.gub.dgi.seguridad.PasswordAuthenticator;

@Path("/security")
public class RestLogin {

	@Inject
	private PasswordAuthenticator passwordAuthenticator;

	@Inject
	private JWTConsumer jwtConsumer;

	@Inject
	private DAO dao;

	@GET
	@Path("/passwordlogin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response passwordLogin(@HeaderParam("Authorization") String authorization) throws InvalidUserException {
		System.out.println("invoking passwordlogin");
		if (authorization == null) {
			System.out.println("authorization null");
			return Response.status(401).build();
		}
		System.out.println("login");
		User aUser;
		aUser = getUserFromHeader(authorization);
		System.out.println("aUser " + aUser);
		String user = aUser.getUserName();
		String password = aUser.getPassword();
		if (passwordAuthenticator.validUser(user, password)) {
			System.out.println("true");
			System.out.println("*************REFRESH TOKEN "+getRefreshToken(user));
			return Response.status(200).entity(getRefreshToken(user)).build();
		} else {
			System.out.println("false");
			return Response.status(401).build();
		}
	}

	private User getUserFromHeader(String authorization) throws InvalidUserException {
		System.out.println("getUserFromHeader");
		if (authorization == null) {
			System.out.println("authorization is null");
			throw new InvalidUserException("User is empty");
		} else {
			System.out.println("authorization is not null");
			String[] res = authorization.split(" ");
			System.out.println("res "+res);
			if ((res.length>1)&&(res[0] != null) && (res[0].equals("Basic"))) {
				System.out.println("res[0] "+res[0]);
				System.out.println("res[1] "+res[1]);
				if (res[1] !=null){
					String userpassword;
					try {
						userpassword = decodeBase64(res[1]);
						System.out.println("decoded user password " + userpassword);
					} catch (UnsupportedEncodingException e) {
						throw new InvalidUserException(e);
					}
					if (userpassword.equals("")) {
						throw new InvalidUserException("User is empty");
					}
					String[] chain = userpassword.split(":");
					if ((chain!=null) && (chain.length==2)){
						User user = new User();
						user.setUserName(chain[0]);
						user.setPassword(chain[1]);
						return user;
					}else{
						throw new InvalidUserException("User is empty");
					}
				}else{
					return null;
				}
			} else {
				throw new InvalidUserException("User is empty");
			}
		}
	}

	@GET
	@Path("/accessToken")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAccessToken(@HeaderParam("Authorization") String authorization) throws InvalidUserException {
		if (validRefreshToken(authorization)) {
			String accessToken = generateAccessToken(authorization);
			return Response.status(200).entity(accessToken).build();
		} else {
			throw new InvalidUserException("Invalid refresh token");
		}
	}

	private String generateAccessToken(String authorization) {
		// TODO Auto-generated method stub
		return null;

	}

	private boolean validRefreshToken(String authorization) throws InvalidUserException {
		try {
			jwtConsumer.validateJWT(authorization);
			return true;
		} catch (JWTConsumerException e) {
			throw new InvalidUserException(e);
		}
	}

	private Object getRefreshToken(String user) {
		ArrayList<String> roles;
		try {
			roles = getRoles(user);
			return jwtConsumer.generateRefreshToken(user, roles);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
	}

	private String decodeBase64(String string) throws UnsupportedEncodingException, InvalidUserException {
		try{
			byte[] base64decodedBytes = Base64.getDecoder().decode(string);
			String res = new String(base64decodedBytes, "utf-8");
			System.out.println("original : " + res);
			return res;
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			throw new InvalidUserException(e);
		}
	}

	private ArrayList<String> getRoles(String user) throws DAOException {
		UserBean userBean = (UserBean) dao.getById(user, UserBean.class);
		return userBean.getRoles();
	}

}
