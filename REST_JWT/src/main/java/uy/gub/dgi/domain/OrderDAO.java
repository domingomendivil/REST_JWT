package uy.gub.dgi.domain;

import java.util.ArrayList;

import uy.gub.dgi.dao.AbstractDAO;
import uy.gub.dgi.dao.DAOException;

public class OrderDAO extends AbstractDAO	{

	@Override
	public ArrayList<? extends Object> getAll() throws DAOException {
		ArrayList<OrderBean> orders = new ArrayList<>();
		OrderBean order = new OrderBean();
		orders.add(order);
		return orders;
		
	}

	
	
}
