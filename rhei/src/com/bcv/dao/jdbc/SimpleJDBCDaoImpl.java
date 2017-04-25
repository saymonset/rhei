/**
 * 
 */
package com.bcv.dao.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import ve.org.bcv.rhei.util.Utilidades;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 02/07/2015 09:13:48
 * 2015
 * mail : oraclefedora@gmail.com
 */
public abstract class SimpleJDBCDaoImpl<E> implements SimpleJDBCDao<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	private static Logger log = Logger.getLogger(SimpleJDBCDaoImpl.class
			.getName());
	
 
//	public   Connection connection;
//	
	/**Lo primero que hace es iniciluizarce*/
	  {
		
//		try {
//			if (connection==null){
//				connection=manejadorDB.coneccionPool();				
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	// C CREATE ...
	@Override
	public E insert(E obj) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	public void insert(Collection<E> objs) throws Exception {
		for (E obj : objs) {
			insert(obj);
		}
	}

	@Override
	public void insert(E... objs) throws Exception {
		for (E obj : objs) {
			insert(obj);
		}
	}

	// R RETRIEVE ...
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public E findById(Serializable id) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<E> find() throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<E> find(Integer from, Integer quantity) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<E> find(String sql) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<E> find(String sql, Integer from, Integer quantity) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<E> find(String sql, Object... params) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<E> find(String sql, Integer from, Integer quantity,
			Object... params) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	// U UPDATE
	@Override
	public void update(E obj) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	public void update(Collection<E> objs) throws Exception{
		for (E obj : objs) {
			update(obj);
		}
	}

	@Override
	public void update(E... objs) throws Exception{
		for (E obj : objs) {
			update(obj);
		}
	}

	@Override
	public Integer update(String sql, Object... params) throws Exception{
		throw new Exception("Funcion no implementada aun");
	}

	// D DELETE
	@Override
	public void delete(E obj) throws Exception{
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	public void delete(Collection<E> objs) throws Exception{
		for (E obj : objs) {
			delete(obj);
		}
	}

	@Override
	public void delete(E... objs) throws Exception {
		for (E obj : objs) {
			delete(obj);
		}
	}

	@Override
	public Integer delete(String sql, Object... params) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	// ADVANCE SEARCH
	@Override
	public <T> List<T> search(String sql) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	public <T> List<T> search(String sql, Integer from, Integer quantity)
			throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	public <T> List<T> search(String sql, Object... params) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}

	@Override
	public <T> List<T> search(String sql, Integer from, Integer quantity,
			Object... params) throws Exception {
		throw new Exception("Funcion no implementada aun");
	}
	
	@Override
	public ResultSet query(String sql, Object... params) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ManejadorDB manejadorDB=new ManejadorDB();;
			conn =manejadorDB.coneccionPool();				
			if (conn != null) {
				ps = conn.prepareStatement(sql);
				
				if (params != null && params.length > 0) {
					for (int i = 0; i < params.length; i++) {
						Object object = params[i];
						ps.setObject(i+1, object);
					}
				}
				
				rs = ps.executeQuery();				
			}
		} catch (SQLException e) {
			log.info(e.getMessage());
			throw new Exception(e);
		}finally {
			liberarConexion(rs, ps, conn);
		}
		return rs;
	}

	public void liberarConexion(ResultSet rs, PreparedStatement ps, Connection connection) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.info(e.getMessage());
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				log.info(e.getMessage());
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.info(e.getMessage());
			}
		}
	}
	
	 
}
