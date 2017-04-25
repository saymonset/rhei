package com.bcv.dao.jdbc;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

/**
 * @author Ing Simon Alberto Rodriguez Pacheco
 * 02/07/2015 09:05:06
 * 2015
 * mail : oraclefedora@gmail.com
 * @param <E> For external pojo
 * @param <I> For internal entity
 */
public abstract interface  SimpleJDBCDao<E> extends Serializable{
	// C CREATE...
		E insert(E obj) throws Exception;

		void insert(Collection<E> objs) throws Exception;

		void insert(E... objs) throws Exception;

		// R RETRIEVE...
		E findById(Serializable id) throws Exception;

		List<E> find() throws Exception;

		List<E> find(Integer from, Integer quantity) throws Exception;

		List<E> find(String sql) throws Exception;

		List<E> find(String sql, Integer from, Integer quantity) throws Exception;

		List<E> find(String sql, Object... params) throws Exception;

		List<E> find(String sql, Integer from, Integer quantity,
				Object... params) throws Exception;

		// U UPDATE...
		void update(E obj) throws Exception;

		void update(Collection<E> objs) throws Exception;

		void update(E... objs) throws Exception;

		Integer update(String sql, Object... params) throws Exception;

		// D DELETE...
		void delete(E obj) throws Exception;

		void delete(Collection<E> objs) throws Exception;

		void delete(E... objs) throws Exception;

		Integer delete(String sql, Object... params) throws Exception;
		
		//ADVANCE SEARCH
		<T> List<T> search(String sql) throws Exception;

		<T> List<T> search(String sql, Integer from, Integer quantity) throws Exception;

		<T> List<T> search(String sql, Object... params) throws Exception;

		<T> List<T> search(String sql, Integer from, Integer quantity,
				Object... params) throws Exception;

		ResultSet query(String sql, Object... params) throws Exception;
	}
